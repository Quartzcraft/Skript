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

package uk.co.quartzcraft.skript.effects;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.bukkitutil.HealthUtils;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;
import uk.co.quartzcraft.skript.lang.Effect;
import uk.co.quartzcraft.skript.lang.Expression;
import uk.co.quartzcraft.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

/**
 * @author Peter Güttinger
 */
@Name("Kill")
@Description({"Kills an entity.",
		"Note: This effect does not set the entitie's health to 0 (which causes issues), but damages the entity by 100 times its maximum health."})
@Examples({"kill the player",
		"kill all creepers in the player's world",
		"kill all endermen, witches and bats"})
@Since("1.0")
public class EffKill extends Effect {
	static {
		Skript.registerEffect(EffKill.class, "kill %entities%");
	}
	
	@SuppressWarnings("null")
	private Expression<Entity> entities;
	
	@SuppressWarnings({"unchecked", "null"})
	@Override
	public boolean init(final Expression<?>[] vars, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		entities = (Expression<Entity>) vars[0];
		return true;
	}
	
	@Override
	protected void execute(final Event e) {
		for (final Entity entity : entities.getArray(e)) {
			if (entity instanceof LivingEntity) {
				final boolean creative = entity instanceof Player && ((Player) entity).getGameMode() == GameMode.CREATIVE;
				if (creative)
					((Player) entity).setGameMode(GameMode.SURVIVAL);
				HealthUtils.damage((LivingEntity) entity, HealthUtils.getMaxHealth((LivingEntity) entity) * 100); // just to make sure that it really dies >:)
				if (creative)
					((Player) entity).setGameMode(GameMode.CREATIVE);
			}
		}
	}
	
	@Override
	public String toString(final @Nullable Event e, final boolean debug) {
		return "kill " + entities.toString(e, debug);
	}
	
}
