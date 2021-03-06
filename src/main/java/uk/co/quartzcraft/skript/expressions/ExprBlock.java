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
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.classes.Converter;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.expressions.base.EventValueExpression;
import uk.co.quartzcraft.skript.expressions.base.WrapperExpression;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.ExpressionType;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import uk.co.quartzcraft.skript.lang.util.ConvertedExpression;
import uk.co.quartzcraft.skript.util.Direction;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Block")
@Description({"The block involved in the event, e.g. the clicked block or the placed block.",
		"Can optionally include a direction as well, e.g. 'block above' or 'block in front of the player'."})
@Examples({"block is ore",
		"set block below to air",
		"spawn a creeper above the block",
		"loop blocks in radius 4:",
		"	loop-block is obsidian",
		"	set loop-block to water",
		"block is a chest:",
		"	clear the inventory of the block"})
@Since("1.0")
public class ExprBlock extends WrapperExpression<Block> {
	static {
		Skript.registerExpression(ExprBlock.class, Block.class, ExpressionType.SIMPLE, "[the] [event-]block");
		Skript.registerExpression(ExprBlock.class, Block.class, ExpressionType.COMBINED, "[the] block %direction% [%location%]");
	}
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		if (exprs.length > 0) {
			setExpr(new ConvertedExpression<Location, Block>(Direction.combine((Expression<? extends Direction>) exprs[0], (Expression<? extends Location>) exprs[1]), Block.class, new Converter<Location, Block>() {
				@Override
				public Block convert(final Location l) {
					return l.getBlock();
				}
			}));
			return true;
		} else {
			setExpr(new EventValueExpression<Block>(Block.class));
			return ((EventValueExpression<Block>) getExpr()).init();
		}
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return getExpr() instanceof EventValueExpression ? "the block" : "the block " + getExpr().toString(e, debug);
	}
	
}
