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

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.classes.Converter;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.expressions.base.PropertyExpression;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.ExpressionType;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.Math2;

/**
 * @author Peter Güttinger
 */
@Name("Rounding")
@Description("Rounds numbers normally, up (ceiling) or down (floor) respectively")
@Examples({"set {var} to rounded health of player",
		"set line 1 of the block to round(1.5 * player's level)",
		"set {_x} to floor({_y}) - ceil({_x})",
		"add rounded down argument to the player's health"})
@Since("2.0")
public class ExprRound extends PropertyExpression<Number, Long> {
	static {
		Skript.registerExpression(ExprRound.class, Long.class, ExpressionType.PROPERTY,
				"(a|the|) round[ed] down %number%",
				"(a|the|) round[ed] %number%",
				"(a|the|) round[ed] up %number%");
	}
	
	int action;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		setExpr((Expression<? extends Number>) exprs[0]);
		action = matchedPattern - 1;
		return true;
	}
	
	@Override
	protected Long[] get(final Event e, final Number[] source) {
		return get(source, new Converter<Number, Long>() {
			@SuppressWarnings("null")
			@Override
			public Long convert(final Number n) {
				if (n instanceof Integer)
					return Long.valueOf(n.longValue());
				else if (n instanceof Long)
					return (Long) n;
				return Long.valueOf(action == -1 ? Math2.floor(n.doubleValue()) : action == 0 ? Math2.round(n.doubleValue()) : Math2.ceil(n.doubleValue()));
			}
		});
	}
	
	@Override
	public Class<? extends Long> getReturnType() {
		return Long.class;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return (action == -1 ? "floor" : action == 0 ? "round" : "ceil") + "(" + getExpr().toString(e, debug) + ")";
	}
	
}
