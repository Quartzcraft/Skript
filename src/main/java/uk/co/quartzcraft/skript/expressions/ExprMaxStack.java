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

import org.bukkit.inventory.ItemStack;

import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.expressions.base.SimplePropertyExpression;

/**
 * @author joeuguce99
 */
@Name("Maximum Stack Size")
@Description("The maximum stack size of the specified material, e.g. 64 for torches, 16 for buckets, and 1 for swords.")
@Examples("send \"You can only pick up %max stack size of player's tool% of %type of (player's tool)%\" to player")
@Since("2.1")
public class ExprMaxStack extends SimplePropertyExpression<ItemStack, Integer> {
	static {
		register(ExprMaxStack.class, Integer.class, "max[imum] stack[[ ]size]", "itemstack");
	}
	
	@Override
	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}
	
	@Override
	protected String getPropertyName() {
		return "maximum stack size";
	}
	
	@SuppressWarnings("null")
	@Override
	public Integer convert(final ItemStack i) {
		return Integer.valueOf(i.getMaxStackSize());
	}
}
