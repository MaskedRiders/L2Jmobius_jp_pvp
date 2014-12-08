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

import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager;
import lineage2.gameserver.model.Player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestExCubeGameReadyAnswer extends L2GameClientPacket
{
	private static final Logger _log = LoggerFactory.getLogger(RequestExCubeGameReadyAnswer.class);
	private int _arena;
	private int _answer;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_arena = readD() + 1;
		_answer = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		Player player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		switch (_answer)
		{
			case 0:
				break;
			
			case 1:
				HandysBlockCheckerManager.getInstance().increaseArenaVotes(_arena);
				break;
			
			default:
				_log.warn("Unknown Cube Game Answer ID: " + _answer);
				break;
		}
	}
}
