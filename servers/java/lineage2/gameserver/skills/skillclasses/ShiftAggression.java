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

import lineage2.gameserver.model.AggroList.AggroInfo;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.StatsSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ShiftAggression extends Skill
{
	/**
	 * Constructor for ShiftAggression.
	 * @param set StatsSet
	 */
	public ShiftAggression(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method useSkill.
	 * @param activeChar Creature
	 * @param targets List<Creature>
	 */
	@Override
	public void useSkill(Creature activeChar, List<Creature> targets)
	{
		if (activeChar.getPlayer() == null)
		{
			return;
		}
		
		for (Creature target : targets)
		{
			if (target != null)
			{
				if (!target.isPlayer())
				{
					continue;
				}
				
				Player player = (Player) target;
				
				for (NpcInstance npc : World.getAroundNpc(activeChar, getSkillRadius(), getSkillRadius()))
				{
					AggroInfo ai = npc.getAggroList().get(activeChar);
					
					if (ai == null)
					{
						continue;
					}
					
					npc.getAggroList().addDamageHate(player, 0, ai.hate);
					npc.getAggroList().remove(activeChar, true);
				}
			}
		}
		
		if (isSSPossible())
		{
			activeChar.unChargeShots(isMagic());
		}
	}
}
