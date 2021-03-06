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
package lineage2.gameserver.skills.skillclasses;

import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.stats.Formulas;
import lineage2.gameserver.stats.Formulas.AttackInfo;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ChargeSoul extends Skill
{
	private final int _numSouls;
	
	/**
	 * Constructor for ChargeSoul.
	 * @param set StatsSet
	 */
	public ChargeSoul(StatsSet set)
	{
		super(set);
		_numSouls = set.getInteger("numSouls", getLevel());
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		boolean ss = activeChar.getChargedSoulShot() && isSSPossible();
		
		if (ss && (getTargetType() != SkillTargetType.TARGET_SELF))
		{
			activeChar.unChargeShots(false);
		}
		
		Creature realTarget;
		boolean reflected;
		
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (target.isDead())
				{
					continue;
				}
				
				reflected = (target != activeChar) && target.checkReflectSkill(activeChar, this);
				realTarget = reflected ? activeChar : target;
				
				if (getPower() > 0)
				{
					AttackInfo info = Formulas.calcPhysDam(activeChar, realTarget, this, false, false, ss, false);
					
					if (info.lethal_dmg > 0)
					{
						realTarget.reduceCurrentHp(info.lethal_dmg, info.reflectableDamage, activeChar, this, true, true, false, false, false, false, false);
					}
					
					realTarget.reduceCurrentHp(info.damage, info.reflectableDamage, activeChar, this, true, true, false, true, false, false, true);
					
					if (!reflected)
					{
						realTarget.doCounterAttack(this, activeChar, false);
					}
				}
				
				if (realTarget.isPlayable() || realTarget.isMonster())
				{
					activeChar.setConsumedSouls(activeChar.getConsumedSouls() + _numSouls, null);
				}
				
				getEffects(activeChar, target, getActivateRate() > 0, false, reflected);
			}
		}
		
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}
