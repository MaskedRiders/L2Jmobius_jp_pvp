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
package lineage2.gameserver.data.xml.holder;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.templates.DoorTemplate;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class DoorHolder extends AbstractHolder
{
	private static final DoorHolder _instance = new DoorHolder();
	private final IntObjectMap<DoorTemplate> _doors = new HashIntObjectMap<>();
	
	/**
	 * Method getInstance.
	 * @return DoorHolder
	 */
	public static DoorHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addTemplate.
	 * @param door DoorTemplate
	 */
	public void addTemplate(DoorTemplate door)
	{
		_doors.put(door.getId(), door);
	}
	
	/**
	 * Method getTemplate.
	 * @param doorId int
	 * @return DoorTemplate
	 */
	public DoorTemplate getTemplate(int doorId)
	{
		return _doors.get(doorId);
	}
	
	/**
	 * Method getDoors.
	 * @return IntObjectMap<DoorTemplate>
	 */
	public IntObjectMap<DoorTemplate> getDoors()
	{
		return _doors;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _doors.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_doors.clear();
	}
}
