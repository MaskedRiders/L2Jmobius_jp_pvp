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
package lineage2.gameserver.listener.actor.ai;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.listener.AiListener;
import lineage2.gameserver.model.Creature;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public interface OnAiEventListener extends AiListener
{
	/**
	 * Method onAiEvent.
	 * @param actor Creature
	 * @param evt CtrlEvent
	 * @param args Object[]
	 */
	public void onAiEvent(Creature actor, CtrlEvent evt, Object[] args);
}
