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
 * Copyright 2011-2013 Peter Güttinger
 * 
 */

package uk.co.quartzcraft.skript.effects;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.classes.ClassInfo;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.lang.Effect;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.TriggerItem;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import uk.co.quartzcraft.skript.lang.function.FunctionEvent;
import uk.co.quartzcraft.skript.lang.function.Functions;
import uk.co.quartzcraft.skript.lang.function.ScriptFunction;
import uk.co.quartzcraft.skript.log.RetainingLogHandler;
import uk.co.quartzcraft.skript.log.SkriptLogger;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Return")
@Description("Makes a function return a value")
@Examples({"function double(i: number) :: number:",
		"	return 2 * {_i}"})
@Since("2.2")
public class EffReturn extends Effect {
	static {
		Skript.registerEffect(EffReturn.class, "return %objects%");
	}
	
	@SuppressWarnings("null")
	private ScriptFunction<?> function;
	
	@SuppressWarnings("null")
	private Expression<?> value;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		final ScriptFunction<?> f = Functions.currentFunction;
		if (f == null) {
			Skript.error("The return statement can only be used in a function");
			return false;
		}
		if (!isDelayed.isFalse()) {
			Skript.error("A return statement after a delay is useless, as the calling trigger will resume when the delay starts (and won't get any returned value)");
			return false;
		}
		function = f;
		final ClassInfo<?> rt = function.getReturnType();
		if (rt == null) {
			Skript.error("This function doesn't return any value. Please use 'stop' or 'exit' if you want to stop the function.");
			return false;
		}
		final RetainingLogHandler log = SkriptLogger.startRetainingLog();
		final Expression<?> v;
		try {
			v = exprs[0].getConvertedExpression(rt.getC());
			if (v == null) {
				log.printErrors("This function is declared to return " + rt.getName().withIndefiniteArticle() + ", but " + exprs[0].toString(null, false) + " is not of that type.");
				return false;
			}
			log.printLog();
		} finally {
			log.stop();
		}
		if (f.isSingle() && !v.isSingle()) {
			Skript.error("This function is defined to only return a single " + rt.toString() + ", but this return statement can return multiple values.");
			return false;
		}
		value = v;
		return true;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	@Nullable
	protected TriggerItem walk(final Event e) {
		debug(e, false);
		if (e instanceof FunctionEvent)
			((ScriptFunction) function).setReturnValue((FunctionEvent) e, value.getArray(e));
		else
			assert false : e;
		return null;
	}
	
	@Override
	protected void execute(final Event e) {
		assert false;
	}
	
	@Override
	public String toString(@Nullable final Event e, final boolean debug) {
		return "return " + value.toString(e, debug);
	}
	
}
