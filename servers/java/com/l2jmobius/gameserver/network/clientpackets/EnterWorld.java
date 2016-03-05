/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jmobius.gameserver.network.clientpackets;

import com.l2jmobius.Config;
import com.l2jmobius.gameserver.LoginServerThread;
import com.l2jmobius.gameserver.cache.HtmCache;
import com.l2jmobius.gameserver.data.sql.impl.AnnouncementsTable;
import com.l2jmobius.gameserver.data.sql.impl.OfflineTradersTable;
import com.l2jmobius.gameserver.data.xml.impl.AdminData;
import com.l2jmobius.gameserver.data.xml.impl.BeautyShopData;
import com.l2jmobius.gameserver.data.xml.impl.SkillTreesData;
import com.l2jmobius.gameserver.enums.Race;
import com.l2jmobius.gameserver.enums.SubclassInfoType;
import com.l2jmobius.gameserver.instancemanager.CHSiegeManager;
import com.l2jmobius.gameserver.instancemanager.CastleManager;
import com.l2jmobius.gameserver.instancemanager.ClanHallManager;
import com.l2jmobius.gameserver.instancemanager.CoupleManager;
import com.l2jmobius.gameserver.instancemanager.CursedWeaponsManager;
import com.l2jmobius.gameserver.instancemanager.FortManager;
import com.l2jmobius.gameserver.instancemanager.FortSiegeManager;
import com.l2jmobius.gameserver.instancemanager.InstanceManager;
import com.l2jmobius.gameserver.instancemanager.MailManager;
import com.l2jmobius.gameserver.instancemanager.PetitionManager;
import com.l2jmobius.gameserver.instancemanager.QuestManager;
import com.l2jmobius.gameserver.instancemanager.SiegeManager;
import com.l2jmobius.gameserver.model.L2Clan;
import com.l2jmobius.gameserver.model.L2Object;
import com.l2jmobius.gameserver.model.L2World;
import com.l2jmobius.gameserver.model.PcCondOverride;
import com.l2jmobius.gameserver.model.TeleportWhereType;
import com.l2jmobius.gameserver.model.actor.instance.L2ClassMasterInstance;
import com.l2jmobius.gameserver.model.actor.instance.L2PcInstance;
import com.l2jmobius.gameserver.model.entity.Castle;
import com.l2jmobius.gameserver.model.entity.Couple;
import com.l2jmobius.gameserver.model.entity.Fort;
import com.l2jmobius.gameserver.model.entity.FortSiege;
import com.l2jmobius.gameserver.model.entity.L2Event;
import com.l2jmobius.gameserver.model.entity.Siege;
import com.l2jmobius.gameserver.model.entity.TvTEvent;
import com.l2jmobius.gameserver.model.entity.clanhall.AuctionableHall;
import com.l2jmobius.gameserver.model.entity.clanhall.SiegableHall;
import com.l2jmobius.gameserver.model.items.instance.L2ItemInstance;
import com.l2jmobius.gameserver.model.quest.Quest;
import com.l2jmobius.gameserver.model.quest.QuestState;
import com.l2jmobius.gameserver.model.quest.State;
import com.l2jmobius.gameserver.model.skills.AbnormalVisualEffect;
import com.l2jmobius.gameserver.model.zone.ZoneId;
import com.l2jmobius.gameserver.network.SystemMessageId;
import com.l2jmobius.gameserver.network.serverpackets.AcquireSkillList;
import com.l2jmobius.gameserver.network.serverpackets.ActionFailed;
import com.l2jmobius.gameserver.network.serverpackets.AllyCrest;
import com.l2jmobius.gameserver.network.serverpackets.Die;
import com.l2jmobius.gameserver.network.serverpackets.EtcStatusUpdate;
import com.l2jmobius.gameserver.network.serverpackets.ExAcquireAPSkillList;
import com.l2jmobius.gameserver.network.serverpackets.ExAdenaInvenCount;
import com.l2jmobius.gameserver.network.serverpackets.ExAutoSoulShot;
import com.l2jmobius.gameserver.network.serverpackets.ExBasicActionList;
import com.l2jmobius.gameserver.network.serverpackets.ExBeautyItemList;
import com.l2jmobius.gameserver.network.serverpackets.ExCastleState;
import com.l2jmobius.gameserver.network.serverpackets.ExGetBookMarkInfoPacket;
import com.l2jmobius.gameserver.network.serverpackets.ExNewSkillToLearnByLevelUp;
import com.l2jmobius.gameserver.network.serverpackets.ExNoticePostArrived;
import com.l2jmobius.gameserver.network.serverpackets.ExNotifyPremiumItem;
import com.l2jmobius.gameserver.network.serverpackets.ExPCCafePointInfo;
import com.l2jmobius.gameserver.network.serverpackets.ExPledgeCount;
import com.l2jmobius.gameserver.network.serverpackets.ExPledgeWaitingListAlarm;
import com.l2jmobius.gameserver.network.serverpackets.ExRotation;
import com.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jmobius.gameserver.network.serverpackets.ExShowUsm;
import com.l2jmobius.gameserver.network.serverpackets.ExStorageMaxCount;
import com.l2jmobius.gameserver.network.serverpackets.ExSubjobInfo;
import com.l2jmobius.gameserver.network.serverpackets.ExUnReadMailCount;
import com.l2jmobius.gameserver.network.serverpackets.ExUserInfoEquipSlot;
import com.l2jmobius.gameserver.network.serverpackets.ExUserInfoInvenWeight;
import com.l2jmobius.gameserver.network.serverpackets.ExVitalityEffectInfo;
import com.l2jmobius.gameserver.network.serverpackets.ExVoteSystemInfo;
import com.l2jmobius.gameserver.network.serverpackets.ExWorldChatCnt;
import com.l2jmobius.gameserver.network.serverpackets.HennaInfo;
import com.l2jmobius.gameserver.network.serverpackets.ItemList;
import com.l2jmobius.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jmobius.gameserver.network.serverpackets.PledgeCrest;
import com.l2jmobius.gameserver.network.serverpackets.PledgeShowMemberListAll;
import com.l2jmobius.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import com.l2jmobius.gameserver.network.serverpackets.PledgeSkillList;
import com.l2jmobius.gameserver.network.serverpackets.QuestList;
import com.l2jmobius.gameserver.network.serverpackets.ShortCutInit;
import com.l2jmobius.gameserver.network.serverpackets.SkillCoolTime;
import com.l2jmobius.gameserver.network.serverpackets.SkillList;
import com.l2jmobius.gameserver.network.serverpackets.SystemMessage;
import com.l2jmobius.gameserver.network.serverpackets.dailymission.ExConnectedTimeAndGetTableReward;
import com.l2jmobius.gameserver.network.serverpackets.friend.L2FriendList;

/**
 * Enter World Packet Handler
 */
public class EnterWorld extends L2GameClientPacket
{
	private final static String _C__11_ENTERWORLD = "[C] 11 EnterWorld";
	private final int[][] tracert = new int[5][4];
	private final static double MIN_HP = 0.5;
	private static final int COMBAT_FLAG = 9819;
	private final static int ERTHEIA_INTRO_FOR_ERTHEIA_USM_ID = 147;
	private final static int ERTHEIA_INTRO_FOR_OTHERS_USM_ID = 148;
	
	private final static int ERTHEIA_INTRO_FOR_ERTHEIA_USM_ID = 147;
	private final static int ERTHEIA_INTRO_FOR_OTHERS_USM_ID = 148;
	
	@Override
	protected void readImpl()
	{
		readB(new byte[32]); // Unknown Byte Array
		readD(); // Unknown Value
		readD(); // Unknown Value
		readD(); // Unknown Value
		readD(); // Unknown Value
		readB(new byte[32]); // Unknown Byte Array
		readD(); // Unknown Value
		for (int i = 0; i < 5; i++)
		{
			for (int o = 0; o < 4; o++)
			{
				tracert[i][o] = readC();
			}
		}
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getActiveChar();
		if (activeChar == null)
		{
			_log.warning("EnterWorld failed! activeChar returned 'null'.");
			getClient().closeNow();
			return;
		}
		
		final String[] address = new String[5];
		for (int i = 0; i < 5; i++)
		{
			address[i] = tracert[i][0] + "." + tracert[i][1] + "." + tracert[i][2] + "." + tracert[i][3];
		}
		
		LoginServerThread.getInstance().sendClientTracert(activeChar.getAccountName(), address);
		
		getClient().setClientTracert(tracert);
		
		// Restore to instanced area if enabled
		if (Config.RESTORE_PLAYER_INSTANCE)
		{
			activeChar.setInstanceId(InstanceManager.getInstance().getPlayerInstance(activeChar.getObjectId()));
		}
		else
		{
			final int instanceId = InstanceManager.getInstance().getPlayerInstance(activeChar.getObjectId());
			if (instanceId > 0)
			{
				InstanceManager.getInstance().getInstance(instanceId).removePlayer(activeChar.getObjectId());
			}
		}
		
		if (Config.DEBUG)
		{
			if (L2World.getInstance().findObject(activeChar.getObjectId()) != null)
			{
				_log.warning("User already exists in Object ID map! User " + activeChar.getName() + " is a character clone.");
			}
		}
		
		// Apply special GM properties to the GM when entering
		if (activeChar.isGM())
		{
			if (Config.GM_STARTUP_INVULNERABLE && AdminData.getInstance().hasAccess("admin_invul", activeChar.getAccessLevel()))
			{
				activeChar.setIsInvul(true);
			}
			
			if (Config.GM_STARTUP_INVISIBLE && AdminData.getInstance().hasAccess("admin_invisible", activeChar.getAccessLevel()))
			{
				activeChar.setInvisible(true);
				activeChar.startAbnormalVisualEffect(AbnormalVisualEffect.STEALTH);
			}
			
			if (Config.GM_STARTUP_SILENCE && AdminData.getInstance().hasAccess("admin_silence", activeChar.getAccessLevel()))
			{
				activeChar.setSilenceMode(true);
			}
			
			if (Config.GM_STARTUP_DIET_MODE && AdminData.getInstance().hasAccess("admin_diet", activeChar.getAccessLevel()))
			{
				activeChar.setDietMode(true);
				activeChar.refreshOverloaded();
			}
			
			if (Config.GM_STARTUP_AUTO_LIST && AdminData.getInstance().hasAccess("admin_gmliston", activeChar.getAccessLevel()))
			{
				AdminData.getInstance().addGm(activeChar, false);
			}
			else
			{
				AdminData.getInstance().addGm(activeChar, true);
			}
			
			if (Config.GM_GIVE_SPECIAL_SKILLS)
			{
				SkillTreesData.getInstance().addSkills(activeChar, false);
			}
			
			if (Config.GM_GIVE_SPECIAL_AURA_SKILLS)
			{
				SkillTreesData.getInstance().addSkills(activeChar, true);
			}
		}
		
		// Set dead status if applies
		if (activeChar.getCurrentHp() < MIN_HP)
		{
			activeChar.setIsDead(true);
		}
		
		if (Config.ENABLE_VITALITY)
		{
			activeChar.sendPacket(new ExVitalityEffectInfo(activeChar));
		}
		
		// Send Macro List
		activeChar.getMacros().sendAllMacros();
		
		// Send Teleport Bookmark List
		sendPacket(new ExGetBookMarkInfoPacket(activeChar));
		
		// Send Item List
		sendPacket(new ItemList(activeChar, false));
		
		// Send Shortcuts
		sendPacket(new ShortCutInit(activeChar));
		
		// Send Action list
		activeChar.sendPacket(ExBasicActionList.STATIC_PACKET);
		
		// Send blank skill list
		activeChar.sendPacket(new SkillList());
		
		// Send castle state.
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			activeChar.sendPacket(new ExCastleState(castle));
		}
		
		// Send GG check
		activeChar.queryGameGuard();
		
		// Send Dye Information
		activeChar.sendPacket(new HennaInfo(activeChar));
		
		// Send Skill list
		activeChar.sendSkillList();
		
		// Send acquirable skill list
		activeChar.sendPacket(new AcquireSkillList(activeChar));
		
		// Send EtcStatusUpdate
		activeChar.sendPacket(new EtcStatusUpdate(activeChar));
		
		// Clan packets
		if (activeChar.getClan() != null)
		{
			final L2Clan clan = activeChar.getClan();
			activeChar.sendPacket(new PledgeShowMemberListUpdate(activeChar));
			sendPacket(new PledgeShowMemberListAll(clan));
			activeChar.sendPacket(new PledgeSkillList(clan));
		}
		else
		{
			activeChar.sendPacket(ExPledgeWaitingListAlarm.STATIC_PACKET);
		}
		
		// Used as a tempfix for Clan crests.
		activeChar.broadcastUserInfo();
		
		// Send SubClass Info
		activeChar.sendPacket(new ExSubjobInfo(activeChar, SubclassInfoType.NO_CHANGES));
		
		// Send Unread Mail Count
		if (MailManager.getInstance().hasUnreadPost(activeChar))
		{
			activeChar.sendPacket(new ExUnReadMailCount(activeChar));
		}
		
		// Faction System
		if (Config.FACTION_SYSTEM_ENABLED)
		{
			if (activeChar.isGood())
			{
				activeChar.getAppearance().setNameColor(Config.FACTION_GOOD_NAME_COLOR);
				activeChar.getAppearance().setTitleColor(Config.FACTION_GOOD_NAME_COLOR);
				activeChar.sendMessage("Welcome " + activeChar.getName() + ", you are fighting for the " + Config.FACTION_GOOD_TEAM_NAME + " faction.");
				activeChar.sendPacket(new ExShowScreenMessage("Welcome " + activeChar.getName() + ", you are fighting for the " + Config.FACTION_GOOD_TEAM_NAME + " faction.", 10000));
			}
			else if (activeChar.isEvil())
			{
				activeChar.getAppearance().setNameColor(Config.FACTION_EVIL_NAME_COLOR);
				activeChar.getAppearance().setTitleColor(Config.FACTION_EVIL_NAME_COLOR);
				activeChar.sendMessage("Welcome " + activeChar.getName() + ", you are fighting for the " + Config.FACTION_EVIL_TEAM_NAME + " faction.");
				activeChar.sendPacket(new ExShowScreenMessage("Welcome " + activeChar.getName() + ", you are fighting for the " + Config.FACTION_EVIL_TEAM_NAME + " faction.", 10000));
			}
		}
		
		Quest.playerEnter(activeChar);
		
		// Send Quest List
		activeChar.sendPacket(new QuestList());
		
		boolean showClanNotice = false;
		
		// Clan related checks are here
		final L2Clan clan = activeChar.getClan();
		if (clan != null)
		{
			notifyClanMembers(activeChar);
			
			notifySponsorOrApprentice(activeChar);
			
			final AuctionableHall clanHall = ClanHallManager.getInstance().getClanHallByOwner(clan);
			if (clanHall != null)
			{
				if (!clanHall.getPaid())
				{
					activeChar.sendPacket(SystemMessageId.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW);
				}
			}
			
			for (Siege siege : SiegeManager.getInstance().getSieges())
			{
				if (!siege.isInProgress())
				{
					continue;
				}
				
				if (siege.checkIsAttacker(clan))
				{
					activeChar.setSiegeState((byte) 1);
					activeChar.setSiegeSide(siege.getCastle().getResidenceId());
				}
				else if (siege.checkIsDefender(clan))
				{
					activeChar.setSiegeState((byte) 2);
					activeChar.setSiegeSide(siege.getCastle().getResidenceId());
				}
			}
			
			for (FortSiege siege : FortSiegeManager.getInstance().getSieges())
			{
				if (!siege.isInProgress())
				{
					continue;
				}
				
				if (siege.checkIsAttacker(clan))
				{
					activeChar.setSiegeState((byte) 1);
					activeChar.setSiegeSide(siege.getFort().getResidenceId());
				}
				else if (siege.checkIsDefender(clan))
				{
					activeChar.setSiegeState((byte) 2);
					activeChar.setSiegeSide(siege.getFort().getResidenceId());
				}
			}
			
			for (SiegableHall hall : CHSiegeManager.getInstance().getConquerableHalls().values())
			{
				if (!hall.isInSiege())
				{
					continue;
				}
				
				if (hall.isRegistered(clan))
				{
					activeChar.setSiegeState((byte) 1);
					activeChar.setSiegeSide(hall.getId());
					activeChar.setIsInHideoutSiege(true);
				}
			}
			
			// Residential skills support
			if (clan.getCastleId() > 0)
			{
				CastleManager.getInstance().getCastleByOwner(clan).giveResidentialSkills(activeChar);
			}
			
			if (clan.getFortId() > 0)
			{
				FortManager.getInstance().getFortByOwner(clan).giveResidentialSkills(activeChar);
			}
			
			showClanNotice = clan.isNoticeEnabled();
			
			// Show clan crest
			if (clan.getCrestId() > 0)
			{
				sendPacket(new PledgeCrest(clan.getCrestId()));
			}
			// Show ally crest
			if (clan.getAllyCrestId() > 0)
			{
				sendPacket(new AllyCrest(clan.getAllyCrestId()));
			}
		}
		
		if (!Config.DISABLE_TUTORIAL)
		{
			loadTutorial(activeChar);
		}
		
		if (Config.PLAYER_SPAWN_PROTECTION > 0)
		{
			activeChar.setProtection(true);
		}
		
		activeChar.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
		activeChar.sendPacket(new ExRotation(activeChar.getObjectId(), activeChar.getHeading()));
		
		activeChar.getInventory().applyItemSkills();
		
		if (L2Event.isParticipant(activeChar))
		{
			L2Event.restorePlayerEventStatus(activeChar);
		}
		
		// Wedding Checks
		if (Config.L2JMOD_ALLOW_WEDDING)
		{
			engage(activeChar);
			notifyPartner(activeChar, activeChar.getPartnerId());
		}
		
		if (activeChar.isCursedWeaponEquipped())
		{
			CursedWeaponsManager.getInstance().getCursedWeapon(activeChar.getCursedWeaponEquippedId()).cursedOnLogin();
		}
		
		if (Config.PC_BANG_ENABLED)
		{
			if (activeChar.getPcBangPoints() > 0)
			{
				activeChar.sendPacket(new ExPCCafePointInfo(activeChar.getPcBangPoints(), 0, 1));
			}
			else
			{
				activeChar.sendPacket(new ExPCCafePointInfo());
			}
		}
		
		activeChar.updateEffectIcons();
		
		// Expand Skill
		activeChar.sendPacket(new ExStorageMaxCount(activeChar));
		
		// Friend list
		sendPacket(new L2FriendList(activeChar));
		
		if (Config.SHOW_GOD_VIDEO_INTRO && activeChar.getVariables().getBoolean("intro_god_video", false))
		{
			activeChar.getVariables().remove("intro_god_video");
			if (activeChar.getRace() == Race.ERTHEIA)
			{
				activeChar.sendPacket(new ExShowUsm(ERTHEIA_INTRO_FOR_ERTHEIA_USM_ID));
			}
			else
			{
				activeChar.sendPacket(new ExShowUsm(ERTHEIA_INTRO_FOR_OTHERS_USM_ID));
			}
		}
		
		final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOUR_FRIEND_S1_JUST_LOGGED_IN);
		sm.addCharName(activeChar);
		for (int id : activeChar.getFriendList().keySet())
		{
			final L2Object obj = L2World.getInstance().findObject(id);
			if (obj != null)
			{
				obj.sendPacket(sm);
			}
		}
		
		activeChar.broadcastUserInfo();
		// Send Inventory Info
		activeChar.sendPacket(new ExUserInfoInvenWeight(activeChar));
		// Send Adena / Inventory Count Info
		activeChar.sendPacket(new ExAdenaInvenCount(activeChar));
		// Send Equipped Items
		activeChar.sendPacket(new ExUserInfoEquipSlot(activeChar));
		
		activeChar.sendPacket(SystemMessageId.WELCOME_TO_THE_WORLD_OF_LINEAGE_II);
		
		AnnouncementsTable.getInstance().showAnnouncements(activeChar);
		
		if (showClanNotice)
		{
			final NpcHtmlMessage notice = new NpcHtmlMessage();
			notice.setFile(activeChar.getHtmlPrefix(), "html/clanNotice.htm");
			notice.replace("%clan_name%", activeChar.getClan().getName());
			notice.replace("%notice_text%", activeChar.getClan().getNotice());
			notice.disableValidation();
			sendPacket(notice);
		}
		else if (Config.SERVER_NEWS)
		{
			final String serverNews = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "html/servnews.htm");
			if (serverNews != null)
			{
				sendPacket(new NpcHtmlMessage(serverNews));
			}
		}
		
		if (Config.PETITIONING_ALLOWED)
		{
			PetitionManager.getInstance().checkPetitionMessages(activeChar);
		}
		
		if (activeChar.isAlikeDead()) // dead or fake dead
		{
			// no broadcast needed since the player will already spawn dead to others
			sendPacket(new Die(activeChar));
		}
		
		activeChar.onPlayerEnter();
		
		sendPacket(new SkillCoolTime(activeChar));
		sendPacket(new ExVoteSystemInfo(activeChar));
		
		for (L2ItemInstance item : activeChar.getInventory().getItems())
		{
			if (item.isTimeLimitedItem())
			{
				item.scheduleLifeTimeTask();
			}
			if (item.isShadowItem() && item.isEquipped())
			{
				item.decreaseMana(false);
			}
		}
		
		for (L2ItemInstance whItem : activeChar.getWarehouse().getItems())
		{
			if (whItem.isTimeLimitedItem())
			{
				whItem.scheduleLifeTimeTask();
			}
		}
		
		if (activeChar.getClanJoinExpiryTime() > System.currentTimeMillis())
		{
			activeChar.sendPacket(SystemMessageId.YOU_HAVE_RECENTLY_BEEN_DISMISSED_FROM_A_CLAN_YOU_ARE_NOT_ALLOWED_TO_JOIN_ANOTHER_CLAN_FOR_24_HOURS);
		}
		
		// remove combat flag before teleporting
		final L2ItemInstance combatFlag = activeChar.getInventory().getItemByItemId(COMBAT_FLAG);
		if (combatFlag != null)
		{
			final Fort fort = FortManager.getInstance().getFort(activeChar);
			if (fort != null)
			{
				FortSiegeManager.getInstance().dropCombatFlag(activeChar, fort.getResidenceId());
			}
			else
			{
				final int slot = activeChar.getInventory().getSlotFromItem(combatFlag);
				activeChar.getInventory().unEquipItemInBodySlot(slot);
				activeChar.destroyItem("CombatFlag", combatFlag, null, true);
			}
		}
		
		// Attacker or spectator logging in to a siege zone.
		// Actually should be checked for inside castle only?
		if (!activeChar.canOverrideCond(PcCondOverride.ZONE_CONDITIONS) && activeChar.isInsideZone(ZoneId.SIEGE) && (!activeChar.isInSiege() || (activeChar.getSiegeState() < 2)))
		{
			activeChar.teleToLocation(TeleportWhereType.TOWN);
		}
		
		// Remove demonic weapon if character is not cursed weapon equipped
		if ((activeChar.getInventory().getItemByItemId(8190) != null) && !activeChar.isCursedWeaponEquipped())
		{
			activeChar.destroyItem("Zariche", activeChar.getInventory().getItemByItemId(8190), null, true);
		}
		if ((activeChar.getInventory().getItemByItemId(8689) != null) && !activeChar.isCursedWeaponEquipped())
		{
			activeChar.destroyItem("Akamanah", activeChar.getInventory().getItemByItemId(8689), null, true);
		}
		
		if (Config.ALLOW_MAIL)
		{
			if (MailManager.getInstance().hasUnreadPost(activeChar))
			{
				sendPacket(ExNoticePostArrived.valueOf(false));
			}
		}
		
		TvTEvent.onLogin(activeChar);
		
		if (Config.WELCOME_MESSAGE_ENABLED)
		{
			activeChar.sendPacket(new ExShowScreenMessage(Config.WELCOME_MESSAGE_TEXT, Config.WELCOME_MESSAGE_TIME));
		}
		
		L2ClassMasterInstance.showQuestionMark(activeChar);
		
		final int birthday = activeChar.checkBirthDay();
		if (birthday == 0)
		{
			activeChar.sendPacket(SystemMessageId.HAPPY_BIRTHDAY_ALEGRIA_HAS_SENT_YOU_A_BIRTHDAY_GIFT);
			// activeChar.sendPacket(new ExBirthdayPopup()); Removed in H5?
		}
		else if (birthday != -1)
		{
			final SystemMessage sm1 = SystemMessage.getSystemMessage(SystemMessageId.THERE_ARE_S1_DAYS_REMAINING_UNTIL_YOUR_BIRTHDAY_ON_YOUR_BIRTHDAY_YOU_WILL_RECEIVE_A_GIFT_THAT_ALEGRIA_HAS_CAREFULLY_PREPARED);
			sm1.addInt(birthday);
			activeChar.sendPacket(sm1);
		}
		
		if (!activeChar.getPremiumItemList().isEmpty())
		{
			activeChar.sendPacket(ExNotifyPremiumItem.STATIC_PACKET);
		}
		
		if ((Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) && Config.STORE_OFFLINE_TRADE_IN_REALTIME)
		{
			OfflineTradersTable.onTransaction(activeChar, true, false);
		}
		
		if (BeautyShopData.getInstance().hasBeautyData(activeChar.getRace(), activeChar.getAppearance().getSexType()))
		{
			activeChar.sendPacket(new ExBeautyItemList(activeChar));
		}
		
		if (SkillTreesData.getInstance().hasAvailableSkills(activeChar, activeChar.getClassId()))
		{
			activeChar.sendPacket(ExNewSkillToLearnByLevelUp.STATIC_PACKET);
		}
		
		activeChar.sendPacket(new ExAcquireAPSkillList(activeChar));
		activeChar.sendPacket(new ExWorldChatCnt(activeChar));
		
		// Underground auto shots system.
		activeChar.sendPacket(new ExAutoSoulShot(0, 0, 0));
		activeChar.sendPacket(new ExAutoSoulShot(0, 0, 1));
		activeChar.sendPacket(new ExAutoSoulShot(0, 0, 2));
		activeChar.sendPacket(new ExAutoSoulShot(0, 0, 3));
		
		// Underground daily rewards system.
		activeChar.sendPacket(ExConnectedTimeAndGetTableReward.STATIC_PACKET);
		
		// Unstuck players that had client open when server crashed.
		activeChar.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	private void engage(L2PcInstance cha)
	{
		final int chaId = cha.getObjectId();
		for (Couple cl : CoupleManager.getInstance().getCouples())
		{
			if ((cl.getPlayer1Id() == chaId) || (cl.getPlayer2Id() == chaId))
			{
				if (cl.getMaried())
				{
					cha.setMarried(true);
				}
				
				cha.setCoupleId(cl.getId());
				
				if (cl.getPlayer1Id() == chaId)
				{
					cha.setPartnerId(cl.getPlayer2Id());
				}
				else
				{
					cha.setPartnerId(cl.getPlayer1Id());
				}
			}
		}
	}
	
	/**
	 * @param cha
	 * @param partnerId
	 */
	private void notifyPartner(L2PcInstance cha, int partnerId)
	{
		final L2PcInstance partner = L2World.getInstance().getPlayer(cha.getPartnerId());
		if (partner != null)
		{
			partner.sendMessage("Your Partner has logged in.");
		}
	}
	
	/**
	 * @param activeChar
	 */
	private void notifyClanMembers(L2PcInstance activeChar)
	{
		final L2Clan clan = activeChar.getClan();
		if (clan != null)
		{
			clan.getClanMember(activeChar.getObjectId()).setPlayerInstance(activeChar);
			
			final SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_HAS_LOGGED_INTO_GAME);
			msg.addString(activeChar.getName());
			clan.broadcastToOtherOnlineMembers(msg, activeChar);
			clan.broadcastToOtherOnlineMembers(new PledgeShowMemberListUpdate(activeChar), activeChar);
			clan.broadcastToOnlineMembers(new ExPledgeCount(clan));
		}
	}
	
	/**
	 * @param activeChar
	 */
	private void notifySponsorOrApprentice(L2PcInstance activeChar)
	{
		if (activeChar.getSponsor() != 0)
		{
			final L2PcInstance sponsor = L2World.getInstance().getPlayer(activeChar.getSponsor());
			if (sponsor != null)
			{
				final SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.YOUR_APPRENTICE_S1_HAS_LOGGED_IN);
				msg.addString(activeChar.getName());
				sponsor.sendPacket(msg);
			}
		}
		else if (activeChar.getApprentice() != 0)
		{
			final L2PcInstance apprentice = L2World.getInstance().getPlayer(activeChar.getApprentice());
			if (apprentice != null)
			{
				final SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.YOUR_SPONSOR_C1_HAS_LOGGED_IN);
				msg.addString(activeChar.getName());
				apprentice.sendPacket(msg);
			}
		}
	}
	
	private void loadTutorial(L2PcInstance player)
	{
		QuestState qs = player.getQuestState("Q00255_Tutorial");
		if (qs == null)
		{
			qs = QuestManager.getInstance().getQuest("Q00255_Tutorial").newQuestState(player);
			qs.setState(State.STARTED);
		}
		qs.getQuest().notifyEvent("user_connected", null, player);
	}
	
	@Override
	public String getType()
	{
		return _C__11_ENTERWORLD;
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
}