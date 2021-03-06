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
package lineage2.gameserver.model.instances;

import java.util.StringTokenizer;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.WarehouseFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class NpcFriendInstance extends MerchantInstance
{
	
	/**
	 * Constructor for NpcFriendInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public NpcFriendInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onInteract(final Player player)
	{
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (player.isChaotic()) && !player.isGM())
		{
			return;
		}
		
		if (hasRandomAnimation())
		{
			onRandomAnimation();
		}
		
		String filename = "";
		
		if (((getId() >= 31370) && (getId() <= 31376) && (player.getVarka() > 0)) || ((getId() >= 31377) && (getId() < 31384) && (player.getKetra() > 0)))
		{
			filename = "npc_friend/" + getId() + "-nofriend.htm";
			showChatWindow(player, filename);
			return;
		}
		
		switch (getId())
		{
			case 31370:
			case 31371:
			case 31373:
			case 31377:
			case 31378:
			case 31380:
			case 31553:
			case 31554:
				filename = "npc_friend/" + getId() + ".htm";
				break;
			
			case 31372:
				if (player.getKetra() > 2)
				{
					filename = "npc_friend/" + getId() + "-bufflist.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
			
			case 31379:
				if (player.getVarka() > 2)
				{
					filename = "npc_friend/" + getId() + "-bufflist.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
			
			case 31374:
				if (player.getKetra() > 1)
				{
					filename = "npc_friend/" + getId() + "-warehouse.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
			
			case 31381:
				if (player.getVarka() > 1)
				{
					filename = "npc_friend/" + getId() + "-warehouse.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
			
			case 31375:
				if ((player.getKetra() == 3) || (player.getKetra() == 4))
				{
					filename = "npc_friend/" + getId() + "-special1.htm";
				}
				else if (player.getKetra() == 5)
				{
					filename = "npc_friend/" + getId() + "-special2.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
			
			case 31382:
				if ((player.getVarka() == 3) || (player.getVarka() == 4))
				{
					filename = "npc_friend/" + getId() + "-special1.htm";
				}
				else if (player.getVarka() == 5)
				{
					filename = "npc_friend/" + getId() + "-special2.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
			
			case 31376:
				if (player.getKetra() == 4)
				{
					filename = "npc_friend/" + getId() + "-normal.htm";
				}
				else if (player.getKetra() == 5)
				{
					filename = "npc_friend/" + getId() + "-special.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
			
			case 31383:
				if (player.getVarka() == 4)
				{
					filename = "npc_friend/" + getId() + "-normal.htm";
				}
				else if (player.getVarka() == 5)
				{
					filename = "npc_friend/" + getId() + "-special.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
			
			case 31555:
				if (player.getRam() == 1)
				{
					filename = "npc_friend/" + getId() + "-special1.htm";
				}
				else if (player.getRam() == 2)
				{
					filename = "npc_friend/" + getId() + "-special2.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
			
			case 31556:
				if (player.getRam() == 2)
				{
					filename = "npc_friend/" + getId() + "-bufflist.htm";
				}
				else
				{
					filename = "npc_friend/" + getId() + ".htm";
				}
				break;
		}
		
		showChatWindow(player, filename);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		final StringTokenizer st = new StringTokenizer(command, " ");
		final String actualCommand = st.nextToken();
		
		if (actualCommand.equals("Buff"))
		{
			if (st.countTokens() < 1)
			{
				return;
			}
			
			final int val = Integer.parseInt(st.nextToken());
			int item = 0;
			
			switch (getId())
			{
				case 31372:
					item = 7186;
					break;
				
				case 31379:
					item = 7187;
					break;
				
				case 31556:
					item = 7251;
					break;
			}
			
			int skill = 0;
			int level = 0;
			long count = 0;
			
			switch (val)
			{
				case 1:
					skill = 4359;
					level = 2;
					count = 2;
					break;
				
				case 2:
					skill = 4360;
					level = 2;
					count = 2;
					break;
				
				case 3:
					skill = 4345;
					level = 3;
					count = 3;
					break;
				
				case 4:
					skill = 4355;
					level = 2;
					count = 3;
					break;
				
				case 5:
					skill = 4352;
					level = 1;
					count = 3;
					break;
				
				case 6:
					skill = 4354;
					level = 3;
					count = 3;
					break;
				
				case 7:
					skill = 4356;
					level = 1;
					count = 6;
					break;
				
				case 8:
					skill = 4357;
					level = 2;
					count = 6;
					break;
			}
			
			if ((skill != 0) && player.getInventory().destroyItemByItemId(item, count))
			{
				player.doCast(SkillTable.getInstance().getInfo(skill, level), player, true);
			}
			else
			{
				showChatWindow(player, "npc_friend/" + getId() + "-havenotitems.htm");
			}
		}
		else if (command.startsWith("Chat"))
		{
			showChatWindow(player, "npc_friend/" + getId() + "-" + Integer.parseInt(command.substring(5)) + ".htm");
		}
		else if (command.startsWith("Buy"))
		{
			showShopWindow(player, Integer.parseInt(command.substring(4)), false);
		}
		else if (actualCommand.equals("Sell"))
		{
			showShopWindow(player);
		}
		else if (command.startsWith("WithdrawP"))
		{
			final int val = Integer.parseInt(command.substring(10));
			
			if (val == 99)
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("npc-friend/personal.htm");
				html.replace("%npcname%", getName());
				player.sendPacket(html);
			}
			else
			{
				WarehouseFunctions.showRetrieveWindow(player, val);
			}
		}
		else if (command.equals("DepositP"))
		{
			WarehouseFunctions.showDepositWindow(player);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
