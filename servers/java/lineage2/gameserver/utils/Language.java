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
package lineage2.gameserver.utils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum Language
{
	ENGLISH("en");
	public static final Language[] VALUES = Language.values();
	private String _shortName;
	
	/**
	 * Constructor for Language.
	 * @param shortName String
	 */
	Language(String shortName)
	{
		_shortName = shortName;
	}
	
	/**
	 * Method getShortName.
	 * @return String
	 */
	public String getShortName()
	{
		return _shortName;
	}
}
