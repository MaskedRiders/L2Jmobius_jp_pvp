/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.model.entity.events.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Dominion;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.IStaticPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Smo
 */
public class DominionSiegeRunnerEvent extends GlobalEvent
{
	private class BattlefieldChatTask extends RunnableImpl
	{
		public BattlefieldChatTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		@SuppressWarnings("synthetic-access")
		@Override
		public void runImpl()
		{
			setBattlefieldChatActive(false);
			setRegistrationOver(false);
			
			for (Dominion d : _registeredDominions)
			{
				DominionSiegeEvent siegeEvent = d.getSiegeEvent();
				
				siegeEvent.updateParticles(false);
				
				siegeEvent.broadcastTo(SystemMsg.THE_BATTLEFIELD_CHANNEL_HAS_BEEN_DEACTIVATED);
				
				siegeEvent.removeObjects(SiegeEvent.ATTACKERS);
				siegeEvent.removeObjects(SiegeEvent.DEFENDERS);
				siegeEvent.removeObjects(DominionSiegeEvent.ATTACKER_PLAYERS);
				siegeEvent.removeObjects(DominionSiegeEvent.DEFENDER_PLAYERS);
			}
			
			_battlefieldChatFuture = null;
		}
	}
	
	public static final String REGISTRATION = "registration";
	public static final String BATTLEFIELD = "battlefield";
	
	private boolean _battlefieldChatActive;
	private Future<?> _battlefieldChatFuture;
	private final BattlefieldChatTask _battlefieldChatTask = new BattlefieldChatTask();
	
	private Calendar _startTime = Calendar.getInstance();
	
	private boolean _isInProgress;
	private boolean _isRegistrationOver;
	
	private final Map<ClassId, Quest> _classQuests = new HashMap<>();
	private final List<Quest> _breakQuests = new ArrayList<>();
	
	private final List<Dominion> _registeredDominions = new ArrayList<>(9);
	
	public DominionSiegeRunnerEvent(MultiValueSet<String> set)
	{
		super(set);
		_startTime.setTimeInMillis(0);
	}
	
	@Override
	public void startEvent()
	{
		if (_startTime.getTimeInMillis() == 0)
		{
			clearActions();
			return;
		}
		
		super.startEvent();
		setInProgress(true);
		
		if (_battlefieldChatFuture != null)
		{
			_battlefieldChatFuture.cancel(false);
			_battlefieldChatFuture = null;
		}
		
		// проверка на 2х реги, и чиста ревардов от другой територии, если зареган был
		for (Dominion d : _registeredDominions)
		{
			List<SiegeClanObject> defenders = d.getSiegeEvent().getObjects(SiegeEvent.DEFENDERS);
			for (SiegeClanObject siegeClan : defenders)
			{
				// листаем мемберов от клана
				for (UnitMember member : siegeClan.getClan())
				{
					for (Dominion d2 : _registeredDominions)
					{
						DominionSiegeEvent siegeEvent2 = d2.getSiegeEvent();
						List<Integer> defenderPlayers2 = siegeEvent2.getObjects(DominionSiegeEvent.DEFENDER_PLAYERS);
						
						defenderPlayers2.remove(Integer.valueOf(member.getObjectId()));
						// с базы удалять ненужно, удалит познее
						// SiegePlayerDAO.getInstance().delete(d, 0, a);
						
						// если у игрока есть реварды от другой з територии - обнуляем
						if (d != d2)
						{
							siegeEvent2.clearReward(member.getObjectId());
						}
					}
				}
			}
			
			List<Integer> defenderPlayers = d.getSiegeEvent().getObjects(DominionSiegeEvent.DEFENDER_PLAYERS);
			for (int i : defenderPlayers)
			{
				for (Dominion d2 : _registeredDominions)
				{
					DominionSiegeEvent siegeEvent2 = d2.getSiegeEvent();
					
					// если у игрока есть реварды от другой з територии - обнуляем
					if (d != d2)
					{
						siegeEvent2.clearReward(i);
					}
				}
			}
		}
		
		// ненужно поднимать выше
		for (Dominion d : _registeredDominions)
		{
			d.getSiegeEvent().clearActions();
			d.getSiegeEvent().registerActions();
		}
		
		broadcastToWorld(SystemMsg.TERRITORY_WAR_HAS_BEGUN);
	}
	
	@Override
	public void stopEvent()
	{
		setInProgress(false);
		
		reCalcNextTime(false);
		
		for (Dominion d : _registeredDominions)
		{
			d.getSiegeDate().setTimeInMillis(_startTime.getTimeInMillis());
		}
		
		broadcastToWorld(SystemMsg.TERRITORY_WAR_HAS_ENDED);
		
		_battlefieldChatFuture = ThreadPoolManager.getInstance().schedule(_battlefieldChatTask, 600000L);
		
		super.stopEvent();
	}
	
	@Override
	public void announce(int val)
	{
		switch (val)
		{
			case -20:
				broadcastToWorld(SystemMsg.THE_TERRITORY_WAR_WILL_BEGIN_IN_20_MINUTES);
				break;
			case -10:
				broadcastToWorld(SystemMsg.THE_TERRITORY_WAR_BEGINS_IN_10_MINUTES);
				break;
			case -5:
				broadcastToWorld(SystemMsg.THE_TERRITORY_WAR_BEGINS_IN_5_MINUTES);
				break;
			case -1:
				broadcastToWorld(SystemMsg.THE_TERRITORY_WAR_BEGINS_IN_1_MINUTE);
				break;
			case 3600:
				broadcastToWorld(new SystemMessage2(SystemMsg.THE_TERRITORY_WAR_WILL_END_IN_S1HOURS).addInteger(val / 3600));
				break;
			case 600:
			case 300:
			case 60:
				broadcastToWorld(new SystemMessage2(SystemMsg.THE_TERRITORY_WAR_WILL_END_IN_S1MINUTES).addInteger(val / 60));
				break;
			case 10:
			case 5:
			case 4:
			case 3:
			case 2:
			case 1:
				broadcastToWorld(new SystemMessage2(SystemMsg.S1_SECONDS_TO_THE_END_OF_TERRITORY_WAR).addInteger(val));
				break;
		}
	}
	
	public Calendar getSiegeDate()
	{
		return _startTime;
	}
	
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		clearActions();
		
		if (onInit)
		{
			if (_startTime.getTimeInMillis() > 0)
			{
				registerActions();
			}
		}
		else if (_startTime.getTimeInMillis() > 0)
		{
			while (System.currentTimeMillis() > _startTime.getTimeInMillis())
			{
				_startTime.add(Calendar.WEEK_OF_MONTH, 2);
			}
			
			registerActions();
		}
	}
	
	@Override
	protected long startTimeMillis()
	{
		return _startTime.getTimeInMillis();
	}
	
	@Override
	protected void printInfo()
	{
		//
	}
	
	// ========================================================================================================================================================================
	// Broadcast
	// ========================================================================================================================================================================
	
	public void broadcastTo(IStaticPacket packet)
	{
		for (Dominion dominion : _registeredDominions)
		{
			dominion.getSiegeEvent().broadcastTo(packet);
		}
	}
	
	public void broadcastTo(L2GameServerPacket packet)
	{
		for (Dominion dominion : _registeredDominions)
		{
			dominion.getSiegeEvent().broadcastTo(packet);
		}
	}
	
	// ========================================================================================================================================================================
	// Getters/Setters
	// ========================================================================================================================================================================
	public boolean isBattlefieldChatActive()
	{
		return _battlefieldChatActive;
	}
	
	public void setBattlefieldChatActive(boolean battlefieldChatActive)
	{
		_battlefieldChatActive = battlefieldChatActive;
	}
	
	@Override
	public boolean isInProgress()
	{
		return _isInProgress;
	}
	
	public void setInProgress(boolean inProgress)
	{
		_isInProgress = inProgress;
	}
	
	public boolean isRegistrationOver()
	{
		return _isRegistrationOver;
	}
	
	public void setRegistrationOver(boolean registrationOver)
	{
		_isRegistrationOver = registrationOver;
		for (Dominion d : _registeredDominions)
		{
			d.getSiegeEvent().setRegistrationOver(registrationOver);
		}
		
		if (registrationOver)
		{
			broadcastToWorld(SystemMsg.THE_TERRITORY_WAR_REQUEST_PERIOD_HAS_ENDED);
		}
	}
	
	public void addClassQuest(ClassId c, Quest quest)
	{
		_classQuests.put(c, quest);
	}
	
	public Quest getClassQuest(ClassId c)
	{
		return _classQuests.get(c);
	}
	
	public void addBreakQuest(Quest q)
	{
		_breakQuests.add(q);
	}
	
	public List<Quest> getBreakQuests()
	{
		return _breakQuests;
	}
	
	// ========================================================================================================================================================================
	// Overrides GlobalEvent
	// ========================================================================================================================================================================
	@Override
	public void action(String name, boolean start)
	{
		if (name.equals(REGISTRATION))
		{
			setRegistrationOver(!start);
		}
		else if (name.equals(BATTLEFIELD))
		{
			setBattlefieldChatActive(start);
		}
		else
		{
			super.action(name, start);
		}
	}
	
	public synchronized void registerDominion(Dominion d)
	{
		if (_registeredDominions.contains(d))
		{
			return;
		}
		
		if (_registeredDominions.isEmpty())
		{
			Castle castle = d.getCastle();
			if (castle.getOwnDate().getTimeInMillis() == 0)
			{
				return;
			}
			
			_startTime = (Calendar) Config.CASTLE_VALIDATION_DATE.clone();
			_startTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			if (_startTime.before(Config.CASTLE_VALIDATION_DATE))
			{
				_startTime.add(Calendar.WEEK_OF_YEAR, 1);
			}
			_startTime.set(Calendar.HOUR_OF_DAY, 20);
			_startTime.set(Calendar.MINUTE, 0);
			_startTime.set(Calendar.SECOND, 0);
			_startTime.set(Calendar.MILLISECOND, 0);
			
			while (_startTime.getTimeInMillis() < System.currentTimeMillis())
			{
				_startTime.add(Calendar.WEEK_OF_YEAR, 2);
			}
			
			d.getSiegeDate().setTimeInMillis(_startTime.getTimeInMillis());
			
			reCalcNextTime(false);
		}
		else
		{
			d.getSiegeDate().setTimeInMillis(_startTime.getTimeInMillis());
		}
		
		d.getSiegeEvent().spawnAction(DominionSiegeEvent.TERRITORY_NPC, true);
		d.rewardSkills();
		
		_registeredDominions.add(d);
	}
	
	public synchronized void unRegisterDominion(Dominion d)
	{
		if (!_registeredDominions.contains(d))
		{
			return;
		}
		
		_registeredDominions.remove(d);
		
		d.getSiegeEvent().spawnAction(DominionSiegeEvent.TERRITORY_NPC, false);
		d.getSiegeDate().setTimeInMillis(0);
		
		if (_registeredDominions.isEmpty())
		{
			clearActions();
			
			_startTime.setTimeInMillis(0);
			
			reCalcNextTime(false);
		}
	}
	
	public List<Dominion> getRegisteredDominions()
	{
		return _registeredDominions;
	}
}
