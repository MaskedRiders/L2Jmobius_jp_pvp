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

/**
 * @author kick
 **/
public class ExPutShapeShiftingExtractionItemResult extends L2GameServerPacket
{
	public static final L2GameServerPacket FAIL = new ExPutShapeShiftingExtractionItemResult(0x00);
	public static final L2GameServerPacket SUCCESS = new ExPutShapeShiftingExtractionItemResult(0x01);
	private final int _result;
	
	private ExPutShapeShiftingExtractionItemResult(int result)
	{
		_result = result;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x12B);
		writeD(_result); // Result
	}
}