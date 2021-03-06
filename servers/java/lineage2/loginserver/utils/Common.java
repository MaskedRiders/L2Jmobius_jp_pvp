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
package lineage2.loginserver.utils;

public class Common
{
	/**
	 * Method to generate byte array from hex string.
	 * @param s
	 * @return
	 */
	public static byte[] hexStringToByteArray(String s)
	{
		String fs = s.replaceAll("\\s+", "");
		int len = fs.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
		{
			data[i / 2] = (byte) ((Character.digit(fs.charAt(i), 16) << 4) + Character.digit(fs.charAt(i + 1), 16));
		}
		return data;
	}
}
