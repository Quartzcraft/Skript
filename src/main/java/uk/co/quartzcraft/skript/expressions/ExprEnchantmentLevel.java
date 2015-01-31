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

import java.util.Map;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.aliases.ItemType;
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

/**
 * @author Peter Güttinger
 */
@Name("Enchantment Level")
@Description("The level of a particular <a href='../classes/#enchantment'>enchantment</a> on an item")
@Examples({"player' tool is a sword of sharpness:",
		"	message \"You have a sword of sharpness %level of sharpness of the player's tool% equipped\""})
@Since("2.0")
public class ExprEnchantmentLevel extends PropertyExpression<ItemType, Integer> {
	static {
		Skript.registerExpression(ExprEnchantmentLevel.class, Integer.class, ExpressionType.PROPERTY,
				"[the] (%-enchantment% level|level of [[the] enchant[ment]] %-enchantment%) o(f|n) %itemtypes%",
				"%itemtypes%'[s] (%-enchantment% level|level of [[the] enchant[ment]] %-enchantment%)");
	}
	
	@SuppressWarnings("null")
	private Expression<Enchantment> enchantment;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parseResult) {
		setExpr((Expression<? extends ItemType>) exprs[2 - 2 * matchedPattern]);
		enchantment = (Expression<Enchantment>) (exprs[matchedPattern] == null ? exprs[matchedPattern + 1] : exprs[matchedPattern]);
		return true;
	}
	
	@Override
	protected Integer[] get(final Event e, final ItemType[] source) {
		final Enchantment ench = enchantment.getSingle(e);
		if (ench == null)
			return new Integer[0];
		return get(source, new Converter<ItemType, Integer>() {
			@Override
			@Nullable
			public Integer convert(final ItemType i) {
				final Map<Enchantment, Integer> enchs = i.getEnchantments();
				if (enchs == null)
					return Integer.valueOf(0);
				final Integer l = enchs.get(ench);
				return l == null ? Integer.valueOf(0) : l;
			}
		});
	}
	
	@Override
	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "the level of " + enchantment.toString(e, debug) + " of " + getExpr().toString(e, debug);
	}
	
}
