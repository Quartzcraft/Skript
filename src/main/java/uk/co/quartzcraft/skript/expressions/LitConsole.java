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

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
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
import uk.co.quartzcraft.skript.lang.util.SimpleLiteral;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Console")
@Description("Represents the server's console which can receive messages and execute commands")
@Examples({"execute console command \"/stop\"",
		"send \"message to console\" to the console"})
@Since("1.3.1")
public class LitConsole extends SimpleLiteral<ConsoleCommandSender> {
	static {
		Skript.registerExpression(LitConsole.class, ConsoleCommandSender.class, ExpressionType.SIMPLE, "[the] (console|server)");
	}
	
	@SuppressWarnings("null")
	private final static ConsoleCommandSender console = Bukkit.getConsoleSender();
	
	public LitConsole() {
		super(console, false);
	}
	
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		return true;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "the console";
	}
	
}
