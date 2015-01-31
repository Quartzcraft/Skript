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

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.effects.Delay;
import uk.co.quartzcraft.skript.expressions.base.PropertyExpression;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.ExpressionType;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import uk.co.quartzcraft.skript.registrations.Classes;
import uk.co.quartzcraft.skript.util.EquipmentSlot;
import uk.co.quartzcraft.skript.util.Getter;
import uk.co.quartzcraft.skript.util.InventorySlot;
import uk.co.quartzcraft.skript.util.Slot;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Tool")
@Description("The item a player is holding.")
@Examples({"player is holding a pickaxe",
		"# is the same as",
		"player's tool is a pickaxe"})
@Since("1.0")
public class ExprTool extends PropertyExpression<LivingEntity, Slot> {
	static {
		Skript.registerExpression(ExprTool.class, Slot.class, ExpressionType.PROPERTY, "[the] (tool|held item|weapon) [of %livingentities%]", "%livingentities%'[s] (tool|held item|weapon)");
	}
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] exprs, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		setExpr((Expression<Player>) exprs[0]);
		return true;
	}
	
	@Override
	protected Slot[] get(final Event e, final LivingEntity[] source) {
		final boolean delayed = Delay.isDelayed(e);
		return get(source, new Getter<Slot, LivingEntity>() {
			@Override
			@Nullable
			public Slot get(final LivingEntity p) {
				if (!delayed) {
					if (e instanceof PlayerItemHeldEvent && ((PlayerItemHeldEvent) e).getPlayer() == p) {
						final PlayerInventory i = ((PlayerItemHeldEvent) e).getPlayer().getInventory();
						assert i != null;
						return new InventorySlot(i, getTime() >= 0 ? ((PlayerItemHeldEvent) e).getNewSlot() : ((PlayerItemHeldEvent) e).getPreviousSlot());
					} else if (e instanceof PlayerBucketEvent && ((PlayerBucketEvent) e).getPlayer() == p) {
						final PlayerInventory i = ((PlayerBucketEvent) e).getPlayer().getInventory();
						assert i != null;
						return new InventorySlot(i, ((PlayerBucketEvent) e).getPlayer().getInventory().getHeldItemSlot()) {
							@Override
							@Nullable
							public ItemStack getItem() {
								return getTime() <= 0 ? super.getItem() : ((PlayerBucketEvent) e).getItemStack();
							}
							
							@Override
							public void setItem(final @Nullable ItemStack item) {
								if (getTime() >= 0) {
									((PlayerBucketEvent) e).setItemStack(item);
								} else {
									super.setItem(item);
								}
							}
						};
					}
				}
				final EntityEquipment e = p.getEquipment();
				if (e == null)
					return null;
				return new EquipmentSlot(e, EquipmentSlot.EquipSlot.TOOL) {
					@Override
					public String toString_i() {
						return "the " + (getTime() == 1 ? "future " : getTime() == -1 ? "former " : "") + super.toString_i();
					}
				};
			}
		});
	}
	
	@Override
	public Class<Slot> getReturnType() {
		return Slot.class;
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		if (e == null)
			return "the " + (getTime() == 1 ? "future " : getTime() == -1 ? "former " : "") + "tool of " + getExpr().toString(e, debug);
		return Classes.getDebugMessage(getSingle(e));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean setTime(final int time) {
		return super.setTime(time, getExpr(), PlayerItemHeldEvent.class, PlayerBucketFillEvent.class, PlayerBucketEmptyEvent.class);
	}
}
