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
package quests.Q10384_AnAudienceWithTauti;

import com.l2jmobius.gameserver.enums.QuestType;
import com.l2jmobius.gameserver.model.actor.L2Npc;
import com.l2jmobius.gameserver.model.actor.instance.L2PcInstance;
import com.l2jmobius.gameserver.model.quest.Quest;
import com.l2jmobius.gameserver.model.quest.QuestState;

import quests.Q10383_FergasonsOffer.Q10383_FergasonsOffer;

/**
 * @hlwrave
 */
public class Q10384_AnAudienceWithTauti extends Quest
{
	// NPCs
	private static final int FERGASON = 33681;
	private static final int AKU = 33671;
	// Monsters
	private static final int TAUTI = 29237;
	// Items
	private static final int TAUTIS_FRAGMENT = 34960;
	private static final int BOTTLE_OF_TAUTIS_SOUL = 35295;
	// Misc
	private static final int MIN_LEVEL = 97;
	
	public Q10384_AnAudienceWithTauti()
	{
		super(10384, Q10384_AnAudienceWithTauti.class.getSimpleName(), "An Audience With Tauti");
		addStartNpc(FERGASON);
		addTalkId(FERGASON, AKU);
		addKillId(TAUTI);
		registerQuestItems(TAUTIS_FRAGMENT);
		addCondMinLevel(MIN_LEVEL, "maestro_ferguson_q10384_05.htm");
		addCondCompletedQuest(Q10383_FergasonsOffer.class.getSimpleName(), "maestro_ferguson_q10384_06.htm");
		
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		final QuestState qs = getQuestState(player, false);
		
		if (qs == null)
		{
			return getNoQuestMsg(player);
		}
		
		switch (event)
		{
			case "maestro_ferguson_q10384_04.htm":
			{
				qs.startQuest();
				break;
			}
			case "sofa_aku_q10384_02.htm":
			{
				qs.setCond(2);
				break;
			}
			case "maestro_ferguson_q10384_11.htm":
			{
				qs.addExpAndSp(951127800, 435041400);
				qs.giveItems(57, 3256740);
				qs.giveItems(BOTTLE_OF_TAUTIS_SOUL, 1);
				qs.exitQuest(QuestType.ONE_TIME, true);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		
		switch (npc.getId())
		{
			case FERGASON:
			{
				if (qs.isCreated())
				{
					htmltext = "maestro_ferguson_q10384_01.htm";
				}
				else if (qs.isStarted())
				{
					if (qs.isCond(1) || qs.isCond(2))
					{
						htmltext = "maestro_ferguson_q10384_08.htm";
					}
					else if (qs.isCond(3) && qs.hasQuestItems(TAUTIS_FRAGMENT))
					{
						htmltext = "maestro_ferguson_q10384_09.htm";
					}
					
				}
				else if (qs.isCompleted())
				{
					htmltext = "maestro_ferguson_q10384_07.htm";
				}
				break;
			}
			case AKU:
			{
				if (qs.isStarted())
				{
					htmltext = "sofa_aku_q10384_01.htm";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		final QuestState qs = getQuestState(killer, false);
		if ((qs != null) && qs.isCond(2))
		{
			qs.setCond(3);
			qs.giveItems(TAUTIS_FRAGMENT, 1);
		}
		return super.onKill(npc, killer, isSummon);
	}
}