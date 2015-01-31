/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011, 2012 Peter Güttinger
 * 
 */

package uk.co.quartzcraft.skript.effects;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.lang.Effect;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Shear")
@Description("Shears or 'un-shears' a sheep. Please note that no wool is dropped, this only sets the 'sheared' state of the sheep.")
@Examples({"on rightclick on a sheep holding a sword:",
		"	shear the clicked sheep"})
@Since("2.0")
public class EffShear extends Effect {
	static {
		Skript.registerEffect(EffShear.class,
				"shear %livingentities%",
				"un[-]shear %livingentities%");
	}
	
	@SuppressWarnings("null")
	private Expression<LivingEntity> sheep;
	private boolean shear;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		sheep = (Expression<LivingEntity>) exprs[0];
		shear = matchedPattern == 0;
		return true;
	}
	
	@Override
	protected void execute(final Event e) {
		for (final LivingEntity en : sheep.getArray(e)) {
			if (en instanceof Sheep) {
				((Sheep) en).setSheared(shear);
			}
		}
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return (shear ? "" : "un") + "shear " + sheep.toString(e, debug);
	}
	
}
