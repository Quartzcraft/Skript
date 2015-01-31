package uk.co.quartzcraft.skript.util;

import java.io.IOException;

import org.eclipse.jdt.annotation.Nullable;

import uk.co.quartzcraft.skript.Skript;
import uk.co.quartzcraft.skript.localization.Language;

public abstract class ExceptionUtils {
	private ExceptionUtils() {}
	
	private final static String IO_NODE = "io exceptions";
	
	@Nullable
	public final static String toString(final IOException e) {
		if (Language.keyExists(IO_NODE + "." + e.getClass().getSimpleName())) {
			return Language.format(IO_NODE + "." + e.getClass().getSimpleName(), e.getLocalizedMessage());
		}
		if (Skript.testing())
			e.printStackTrace();
		return e.getLocalizedMessage();
	}
	
}
