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
package lineage2.gameserver.templates.item;

import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ArmorTemplate extends ItemTemplate
{
	public static final double EMPTY_RING = 5;
	public static final double EMPTY_EARRING = 9;
	public static final double EMPTY_NECKLACE = 13;
	public static final double EMPTY_HELMET = 12;
	public static final double EMPTY_BODY_FIGHTER = 31;
	public static final double EMPTY_LEGS_FIGHTER = 18;
	public static final double EMPTY_BODY_MYSTIC = 15;
	public static final double EMPTY_LEGS_MYSTIC = 8;
	public static final double EMPTY_GLOVES = 8;
	public static final double EMPTY_BOOTS = 7;
	
	/**
	 * @author Mobius
	 */
	public enum ArmorType implements ItemType
	{
		NONE(1, "None"),
		LIGHT(2, "Light"),
		HEAVY(3, "Heavy"),
		MAGIC(4, "Magic"),
		PET(5, "Pet"),
		SIGIL(6, "Sigil");
		public final static ArmorType[] VALUES = values();
		private final long _mask;
		private final String _name;
		
		/**
		 * Constructor for ArmorType.
		 * @param id int
		 * @param name String
		 */
		ArmorType(int id, String name)
		{
			_mask = 1L << (id + WeaponType.VALUES.length);
			_name = name;
		}
		
		/**
		 * Method mask.
		 * @return long
		 * @see lineage2.gameserver.templates.item.ItemType#mask()
		 */
		@Override
		public long mask()
		{
			return _mask;
		}
		
		/**
		 * Method toString.
		 * @return String
		 */
		@Override
		public String toString()
		{
			return _name;
		}
	}
	
	/**
	 * Constructor for ArmorTemplate.
	 * @param set StatsSet
	 */
	public ArmorTemplate(StatsSet set)
	{
		super(set);
		type = set.getEnum("type", ArmorType.class);
		
		if ((_bodyPart == SLOT_NECK) || ((_bodyPart & SLOT_L_EAR) != 0) || ((_bodyPart & SLOT_L_FINGER) != 0))
		{
			_type1 = TYPE1_WEAPON_RING_EARRING_NECKLACE;
			_type2 = TYPE2_ACCESSORY;
		}
		else if ((_bodyPart == SLOT_HAIR) || (_bodyPart == SLOT_DHAIR) || (_bodyPart == SLOT_HAIRALL))
		{
			_type1 = TYPE1_OTHER;
			_type2 = ItemTemplate.TYPE2_OTHER;
		}
		else
		{
			_type1 = TYPE1_SHIELD_ARMOR;
			_type2 = TYPE2_SHIELD_ARMOR;
		}
		
		if (getItemType() == ArmorType.PET)
		{
			_type1 = TYPE1_SHIELD_ARMOR;
			
			switch (_bodyPart)
			{
				case SLOT_WOLF:
					_type2 = TYPE2_PET_WOLF;
					_bodyPart = SLOT_CHEST;
					break;
				
				case SLOT_GWOLF:
					_type2 = TYPE2_PET_GWOLF;
					_bodyPart = SLOT_CHEST;
					break;
				
				case SLOT_HATCHLING:
					_type2 = TYPE2_PET_HATCHLING;
					_bodyPart = SLOT_CHEST;
					break;
				
				case SLOT_PENDANT:
					_type2 = TYPE2_PENDANT;
					_bodyPart = SLOT_NECK;
					break;
				
				case SLOT_BABYPET:
					_type2 = TYPE2_PET_BABY;
					_bodyPart = SLOT_CHEST;
					break;
				
				default:
					_type2 = TYPE2_PET_STRIDER;
					_bodyPart = SLOT_CHEST;
					break;
			}
		}
	}
	
	/**
	 * Method getItemType.
	 * @return ArmorType
	 */
	@Override
	public ArmorType getItemType()
	{
		return (ArmorType) super.type;
	}
	
	/**
	 * Method getItemMask.
	 * @return long
	 */
	@Override
	public final long getItemMask()
	{
		return getItemType().mask();
	}
}
