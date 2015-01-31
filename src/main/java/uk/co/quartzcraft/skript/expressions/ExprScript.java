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

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.ScriptLoader;
import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.config.Config;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Events;
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
@Name("Script Name")
@Description("Holds the current script's name (the file name without '.sk').")
@Examples({"on script load:",
		"	set {running.%script%} to true",
		"on script unload:",
		"	set {running.%script%} to false"})
@Since("2.0")
@Events("Script Load/Unload")
public class ExprScript extends SimpleExpression<String> {
	static {
		Skript.registerExpression(ExprScript.class, String.class, ExpressionType.SIMPLE, "[the] script[['s] name]");
	}
	
	@SuppressWarnings("null")
	private String name;
	
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		final Config script = ScriptLoader.currentScript;
		if (script == null) {
			assert false;
			return false;
		}
		String name = script.getFileName();
		if (name.contains("."))
			name = "" + name.substring(0, name.lastIndexOf('.'));
		this.name = name;
		return true;
	}
	
	@Override
	protected String[] get(final Event e) {
		return new String[] {name};
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "the script's name";
	}
	
}
