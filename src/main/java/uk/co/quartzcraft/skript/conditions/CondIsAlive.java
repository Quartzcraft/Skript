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

package uk.co.quartzcraft.skript.conditions;

import org.bukkit.entity.LivingEntity;

import uk.co.quartzcraft.skript.conditions.base.PropertyCondition;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Is Alive")
@Description("Checks whetehr an entity is alive. This is mostly useful to check whether an entity stored in a variable does still exist")
@Examples({"{villagerbuddy.%player%} is dead"})
@Since("2.0")
public class CondIsAlive extends PropertyCondition<LivingEntity> {
	static {
		register(CondIsAlive.class, "(1¦alive|0¦dead)", "livingentities");
	}
	
	private boolean alive;
	
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		alive = parseResult.mark == 1;
		return super.init(exprs, matchedPattern, isDelayed, parseResult);
	}
	
	@Override
	public boolean check(final LivingEntity e) {
		return alive != e.isDead();
	}
	
	@Override
	protected String getPropertyName() {
		return alive ? "alive" : "dead";
	}
	
}
