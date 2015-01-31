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

package uk.co.quartzcraft.skript.hooks.regions.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.expressions.base.EventValueExpression;
import uk.co.quartzcraft.skript.hooks.regions.classes.Region;
import uk.co.quartzcraft.skript.lang.ExpressionType;

/**
 * @author Peter Güttinger
 */
@Name("Region")
@Description({"The <a href='../classes/#region'>region</a> involved in an event.",
		"This expression requires a supported regions plugin to be installed."})
@Examples({"on region enter:",
		"	region is {forbidden region}",
		"	cancel the event"})
@Since("2.1")
public class ExprRegion extends EventValueExpression<Region> {
	static {
		Skript.registerExpression(ExprRegion.class, Region.class, ExpressionType.SIMPLE, "[the] [event-]region");
	}
	
	public ExprRegion() {
		super(Region.class);
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "the region";
	}
	
}
