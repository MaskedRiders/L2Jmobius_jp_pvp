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
package lineage2.gameserver.skills.skillclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExtractStone extends Skill
{
	private final static int ExtractScrollSkill = 2630;
	private final static int ExtractedCoarseRedStarStone = 13858;
	private final static int ExtractedCoarseBlueStarStone = 13859;
	private final static int ExtractedCoarseGreenStarStone = 13860;
	private final static int ExtractedRedStarStone = 14009;
	private final static int ExtractedBlueStarStone = 14010;
	private final static int ExtractedGreenStarStone = 14011;
	private final static int RedStarStone1 = 18684;
	private final static int RedStarStone2 = 18685;
	private final static int RedStarStone3 = 18686;
	private final static int BlueStarStone1 = 18687;
	private final static int BlueStarStone2 = 18688;
	private final static int BlueStarStone3 = 18689;
	private final static int GreenStarStone1 = 18690;
	private final static int GreenStarStone2 = 18691;
	private final static int GreenStarStone3 = 18692;
	private final static int FireEnergyCompressionStone = 14015;
	private final static int WaterEnergyCompressionStone = 14016;
	private final static int WindEnergyCompressionStone = 14017;
	private final static int EarthEnergyCompressionStone = 14018;
	private final static int DarknessEnergyCompressionStone = 14019;
	private final static int SacredEnergyCompressionStone = 14020;
	private final static int SeedFire = 18679;
	private final static int SeedWater = 18678;
	private final static int SeedWind = 18680;
	private final static int SeedEarth = 18681;
	private final static int SeedDarkness = 18683;
	private final static int SeedDivinity = 18682;
	private final List<Integer> _npcIds = new ArrayList<>();
	
	/**
	 * Constructor for ExtractStone.
	 * @param set StatsSet
	 */
	public ExtractStone(StatsSet set)
	{
		super(set);
		StringTokenizer st = new StringTokenizer(set.getString("npcIds", ""), ";");
		
		while (st.hasMoreTokens())
		{
			_npcIds.add(Integer.valueOf(st.nextToken()));
		}
	}
	
	/**
	 * Method checkCondition.
	 * @param activeChar Creature
	 * @param target Creature
	 * @param forceUse boolean
	 * @param dontMove boolean
	 * @param first boolean
	 * @return boolean
	 */
	@Override
	public boolean checkCondition(Creature activeChar, Creature target, boolean forceUse, boolean dontMove, boolean first)
	{
		if ((target == null) || !target.isNpc() || (getItemId(target.getId()) == 0))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return false;
		}
		
		if (!_npcIds.isEmpty() && !_npcIds.contains(new Integer(target.getId())))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.INVALID_TARGET));
			return false;
		}
		
		return super.checkCondition(activeChar, target, forceUse, dontMove, first);
	}
	
	/**
	 * Method getItemId.
	 * @param npcId int
	 * @return int
	 */
	private int getItemId(int npcId)
	{
		switch (npcId)
		{
			case RedStarStone1:
			case RedStarStone2:
			case RedStarStone3:
				if (_id == ExtractScrollSkill)
				{
					return ExtractedCoarseRedStarStone;
				}
				
				return ExtractedRedStarStone;
				
			case BlueStarStone1:
			case BlueStarStone2:
			case BlueStarStone3:
				if (_id == ExtractScrollSkill)
				{
					return ExtractedCoarseBlueStarStone;
				}
				
				return ExtractedBlueStarStone;
				
			case GreenStarStone1:
			case GreenStarStone2:
			case GreenStarStone3:
				if (_id == ExtractScrollSkill)
				{
					return ExtractedCoarseGreenStarStone;
				}
				
				return ExtractedGreenStarStone;
				
			case SeedFire:
				return FireEnergyCompressionStone;
				
			case SeedWater:
				return WaterEnergyCompressionStone;
				
			case SeedWind:
				return WindEnergyCompressionStone;
				
			case SeedEarth:
				return EarthEnergyCompressionStone;
				
			case SeedDarkness:
				return DarknessEnergyCompressionStone;
				
			case SeedDivinity:
				return SacredEnergyCompressionStone;
				
			default:
				return 0;
		}
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		Player player = activeChar.getPlayer();
		
		if (player == null)
		{
			return;
		}
		
		for (Creature target : targets)
		{
			if ((target != null) && (getItemId(target.getId()) != 0))
			{
				double rate = Config.RATE_QUESTS_DROP * player.getBonus().getQuestDropRate();
				long count = _id == ExtractScrollSkill ? 1 : Math.min(10, Rnd.get((int) ((getLevel() * rate) + 1)));
				int itemId = getItemId(target.getId());
				
				if (count > 0)
				{
					player.getInventory().addItem(itemId, count);
					player.sendPacket(new PlaySound(Quest.SOUND_ITEMGET));
					player.sendPacket(SystemMessage2.obtainItems(itemId, count, 0));
					player.sendChanges();
				}
				else
				{
					player.sendPacket(new SystemMessage(SystemMessage.THE_COLLECTION_HAS_FAILED));
				}
				
				target.doDie(player);
			}
		}
		
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}
