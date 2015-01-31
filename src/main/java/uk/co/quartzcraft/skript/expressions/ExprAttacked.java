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
 * Copyright 2011-2014 Peter Güttinger
 * 
 */

package uk.co.quartzcraft.skript.expressions;

import java.lang.reflect.Array;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.ScriptLoader;
import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Events;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.entity.EntityData;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.ExpressionType;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import uk.co.quartzcraft.skript.lang.util.SimpleExpression;
import uk.co.quartzcraft.skript.log.ErrorQuality;
import uk.co.quartzcraft.skript.registrations.Classes;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Attacked")
@Description("The victim of a damage event, e.g. when a player attacks a zombie this expression represents the zombie.")
@Examples({"on damage:",
		"	victim is a creeper",
		"	damage the attacked by 1 heart"})
@Since("1.3")
@Events({"damage", "death"})
public class ExprAttacked extends SimpleExpression<Entity> {
	static {
		Skript.registerExpression(ExprAttacked.class, Entity.class, ExpressionType.SIMPLE, "[the] (attacked|damaged|victim) [<(.+)>]");
	}
	
	@SuppressWarnings("null")
	private EntityData<?> type;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(final Expression<?>[] vars, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		if (!ScriptLoader.isCurrentEvent(EntityDamageEvent.class, EntityDeathEvent.class)) {
			Skript.error("The expression 'victim' can only be used in a damage or death event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		final String type = parser.regexes.size() == 0 ? null : parser.regexes.get(0).group();
		if (type == null) {
			this.type = EntityData.fromClass(Entity.class);
		} else {
			final EntityData<?> t = EntityData.parse(type);
			if (t == null) {
				Skript.error("'" + type + "' is not an entity type", ErrorQuality.NOT_AN_EXPRESSION);
				return false;
			}
			this.type = t;
		}
		return true;
	}
	
	@Override
	@Nullable
	protected Entity[] get(final Event e) {
		final Entity[] one = (Entity[]) Array.newInstance(type.getType(), 1);
		final Entity entity = ((EntityEvent) e).getEntity();
		if (type.isInstance(entity)) {
			one[0] = entity;
			return one;
		}
		return null;
	}
	
	@Override
	public Class<? extends Entity> getReturnType() {
		return type.getType();
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		if (e == null)
			return "the attacked " + type;
		return Classes.getDebugMessage(getSingle(e));
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
}
