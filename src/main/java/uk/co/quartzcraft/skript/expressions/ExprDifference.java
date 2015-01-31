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

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.classes.Arithmetic;
import uk.co.quartzcraft.skript.classes.ClassInfo;
import uk.co.quartzcraft.skript.conditions.CondCompare;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.ExpressionType;
import uk.co.quartzcraft.skript.lang.Literal;
import uk.co.quartzcraft.skript.lang.Variable;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import uk.co.quartzcraft.skript.lang.util.SimpleExpression;
import uk.co.quartzcraft.skript.log.ErrorQuality;
import uk.co.quartzcraft.skript.registrations.Classes;
import uk.co.quartzcraft.skript.util.Utils;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Difference")
@Description("The difference between two values, e.g. <a href='../classes/#number'>numbers</a>, <a href='../classes/#date'>dates</a> or <a href='../classes/#time'>times</a>.")
@Examples({"difference between {command.%player%.lastuse} and now is smaller than a minute:",
		"  message \"You have to wait a minute before using this command again!\"",
		"  stop"})
@Since("1.4")
public class ExprDifference extends SimpleExpression<Object> {
	
	static {
		Skript.registerExpression(ExprDifference.class, Object.class, ExpressionType.COMBINED, "difference (between|of) %object% and %object%");
	}
	
	@SuppressWarnings("null")
	private Expression<?> first, second;
	
	@SuppressWarnings({"null", "rawtypes"})
	private Arithmetic math;
	@SuppressWarnings("null")
	private Class<?> relativeType;
	
	@SuppressWarnings({"unchecked", "null", "unused"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		first = exprs[0];
		second = exprs[1];
		final ClassInfo<?> ci;
		if (first instanceof Variable && second instanceof Variable) {
			ci = Classes.getExactClassInfo(Double.class);
			first = first.getConvertedExpression(Double.class);
			second = second.getConvertedExpression(Double.class);
		} else if (first instanceof Literal<?> && second instanceof Literal<?>) {
			first = first.getConvertedExpression(Object.class);
			second = second.getConvertedExpression(Object.class);
			if (first == null || second == null)
				return false;
			ci = Classes.getSuperClassInfo(Utils.getSuperType(first.getReturnType(), second.getReturnType()));
		} else {
			if (first instanceof Literal<?>) {
				first = first.getConvertedExpression(second.getReturnType());
				if (first == null)
					return false;
			} else if (second instanceof Literal<?>) {
				second = second.getConvertedExpression(first.getReturnType());
				if (second == null)
					return false;
			}
			if (first instanceof Variable) {
				first = first.getConvertedExpression(second.getReturnType());
			} else if (second instanceof Variable) {
				second = second.getConvertedExpression(first.getReturnType());
			}
			assert first != null && second != null;
			ci = Classes.getSuperClassInfo(Utils.getSuperType(first.getReturnType(), second.getReturnType()));
		}
		assert ci != null;
		if (ci.getMath() == null) {
			Skript.error("Can't get the difference of " + CondCompare.f(first) + " and " + CondCompare.f(second), ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		math = ci.getMath();
		relativeType = ci.getMathRelativeType();
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Nullable
	protected Object[] get(final Event e) {
		final Object f = first.getSingle(e), s = second.getSingle(e);
		if (f == null || s == null)
			return null;
		final Object[] one = (Object[]) Array.newInstance(relativeType, 1);
		one[0] = math.difference(f, s);
		return one;
	}
	
	@Override
	public Class<? extends Object> getReturnType() {
		return relativeType;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "difference between " + first.toString(e, debug) + " and " + second.toString(e, debug);
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
}
