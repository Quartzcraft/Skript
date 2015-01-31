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

package uk.co.quartzcraft.skript.config.validate;

import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.config.EntryNode;
import uk.co.quartzcraft.skript.config.Node;
import uk.co.quartzcraft.skript.log.SkriptLogger;
import ch.njol.util.Setter;

/**
 * @author Peter Güttinger
 */
public class EntryValidator implements NodeValidator {
	
	@Nullable
	private final Setter<String> setter;
	
	public EntryValidator() {
		setter = null;
	}
	
	public EntryValidator(final Setter<String> setter) {
		this.setter = setter;
	}
	
	@Override
	public boolean validate(final Node node) {
		if (!(node instanceof EntryNode)) {
			notAnEntryError(node);
			return false;
		}
		if (setter != null)
			setter.set(((EntryNode) node).getValue());
		return true;
	}
	
	public final static void notAnEntryError(final Node node) {
		SkriptLogger.setNode(node);
		Skript.error("'" + node.getKey() + "' is not an entry (like 'name " + node.getConfig().getSeparator() + " value')");
	}
	
}