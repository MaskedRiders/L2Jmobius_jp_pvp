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
package lineage2.gameserver.network.serverpackets;

public class ExNavitAdventTimeChange extends L2GameServerPacket
{
	private final int _active;
	private final int _time;
	
	public ExNavitAdventTimeChange(boolean active, int time)
	{
		_active = active ? 1 : 0;
		_time = 14400 - time;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeEx(0xE5);
		writeC(_active); // state 0 - pause 1 - started
		writeD(_time); // left time in minutes and state is automatically changed to quit
	}
}
