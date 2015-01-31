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

package uk.co.quartzcraft.skript.conditions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.lang.Condition;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Chance")
@Description({"A condition that randomly succeeds or fails.",
		"Valid values are between 0% and 100%, or if the percent sign is omitted between 0 and 1."})
@Examples({"chance of 50%:",
		"	drop a diamond",
		"chance of {var}% # {var} between 0 and 100",
		"chance of {var} # {var} between 0 and 1"})
@Since("1.0")
public class CondChance extends Condition {
	static {
		Skript.registerCondition(CondChance.class, "chance of %number%(1¦\\%|)");
	}
	
	@SuppressWarnings("null")
	private Expression<Double> chance;
	boolean percent;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		chance = (Expression<Double>) exprs[0];
		percent = parser.mark == 1;
		return true;
	}
	
	@Override
	public boolean check(final Event e) {
		final Number n = chance.getSingle(e);
		if (n == null)
			return false;
		return Math.random() < (percent ? n.doubleValue() / 100 : n.doubleValue());
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "chance of " + chance.toString(e, debug) + (percent ? "%" : "");
	}
	
}