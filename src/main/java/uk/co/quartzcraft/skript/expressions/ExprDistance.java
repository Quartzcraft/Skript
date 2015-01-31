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

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.ExpressionType;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import uk.co.quartzcraft.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Distance")
@Description("The distance between two points.")
@Examples({"distance between the player and {%player%.home} is smaller than 20:",
		"	message \"You're very close to your home!\""})
@Since("1.0")
public class ExprDistance extends SimpleExpression<Double> {
	static {
		Skript.registerExpression(ExprDistance.class, Double.class, ExpressionType.COMBINED, "[the] distance between %location% and %location%");
	}
	
	@SuppressWarnings("null")
	private Expression<Location> loc1, loc2;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] vars, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		loc1 = (Expression<Location>) vars[0];
		loc2 = (Expression<Location>) vars[1];
		return true;
	}
	
	@Override
	@Nullable
	protected Double[] get(final Event e) {
		final Location l1 = loc1.getSingle(e), l2 = loc2.getSingle(e);
		if (l1 == null || l2 == null || l1.getWorld() != l2.getWorld())
			return new Double[0];
		return new Double[] {l1.distance(l2)};
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "distance between " + loc1.toString(e, debug) + " and " + loc2.toString(e, debug);
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
	@Override
	public Class<? extends Double> getReturnType() {
		return Double.class;
	}
	
}
