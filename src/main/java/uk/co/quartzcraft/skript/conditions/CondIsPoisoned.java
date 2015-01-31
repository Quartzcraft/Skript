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

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

import uk.co.quartzcraft.skript.conditions.base.PropertyCondition;
import uk.co.quartzcraft.skript.doc.Description;
import uk.co.quartzcraft.skript.doc.Examples;
import uk.co.quartzcraft.skript.doc.Name;
import uk.co.quartzcraft.skript.doc.Since;

/**
 * @author Peter Güttinger
 */
@Name("Is Poisoned")
@Description("Checks whether an entity is poisoned.")
@Examples({"player is poisoned:",
		"	cure the player from posion",
		"	message \"You have been cured!\""})
@Since("1.4.4")
public class CondIsPoisoned extends PropertyCondition<LivingEntity> {
	
	static {
		register(CondIsPoisoned.class, "poisoned", "livingentities");
	}
	
	@Override
	public boolean check(final LivingEntity e) {
		return e.hasPotionEffect(PotionEffectType.POISON);
	}
	
	@Override
	protected String getPropertyName() {
		return "poisoned";
	}
	
}
