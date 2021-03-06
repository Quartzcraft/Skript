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

package uk.co.quartzcraft.skript.expressions;

import java.lang.reflect.Array;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.Literal;
import uk.co.quartzcraft.skript.lang.Unit;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import uk.co.quartzcraft.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
public class ExprValue extends SimpleExpression<Unit> {
//	static { // REMIND add this (>2.0)
//		Skript.registerExpression(ExprValue.class, Unit.class, ExpressionType.PATTERN_MATCHES_EVERYTHING, "%~number% %*unit%");
//	}
	
	@SuppressWarnings("null")
	private Expression<Number> amount;
	@SuppressWarnings("null")
	private Unit unit;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		amount = (Expression<Number>) exprs[0];
		unit = ((Literal<Unit>) exprs[1]).getSingle();
		return true;
	}
	
	@Override
	@Nullable
	protected Unit[] get(final Event e) {
		final Number a = amount.getSingle(e);
		if (a == null)
			return null;
		final Unit u = unit.clone();
		u.setAmount(a.doubleValue());
		final Unit[] one = (Unit[]) Array.newInstance(unit.getClass(), 1);
		one[0] = u;
		return one;
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
	@SuppressWarnings("null")
	@Override
	public Class<? extends Unit> getReturnType() {
		return unit.getClass();
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return amount.toString(e, debug) + " " + unit.toString();
	}
	
}
