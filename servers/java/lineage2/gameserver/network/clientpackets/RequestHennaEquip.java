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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.data.xml.holder.HennaHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.Henna;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestHennaEquip extends L2GameClientPacket
{
	private int _symbolId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_symbolId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		Henna temp = HennaHolder.getInstance().getHenna(_symbolId);
		
		if ((temp == null) || !temp.isForThisClass(player))
		{
			player.sendPacket(new SystemMessage(SystemMessage.THE_SYMBOL_CANNOT_BE_DRAWN));
			return;
		}
		
		long adena = player.getAdena();
		long countDye = player.getInventory().getCountOf(temp.getDyeId());
		
		if ((countDye >= temp.getDrawCount()) && (adena >= temp.getPrice()))
		{
			if (player.consumeItem(temp.getDyeId(), temp.getDrawCount()) && player.reduceAdena(temp.getPrice()))
			{
				player.sendPacket(SystemMsg.THE_SYMBOL_HAS_BEEN_ADDED);
				player.addHenna(temp);
			}
		}
		else
		{
			player.sendPacket(SystemMsg.THE_SYMBOL_CANNOT_BE_DRAWN);
		}
	}
}
