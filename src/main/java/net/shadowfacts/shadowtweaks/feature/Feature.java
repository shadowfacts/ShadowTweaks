package net.shadowfacts.shadowtweaks.feature;

import lombok.Getter;
import net.minecraftforge.common.config.Configuration;

/**
 * @author shadowfacts
 */
public abstract class Feature {

	@Getter
	protected boolean enabled;

	public void configure(Configuration config) {
		enabled = config.getBoolean(getFeatureName(), "_features", isEnabledByDefault(), "Enable the " + getFeatureName() + " feature");
	}

	public abstract boolean requiresServerSide();

	public void preInit() {

	}

	public void init() {

	}

	public void postInit() {

	}

	protected String getFeatureName() {
		String name = getClass().getSimpleName().replaceAll("Feature", "");
		name = name.substring(0, 2).toLowerCase() + name.substring(2);
		return name;
	}

	protected boolean isEnabledByDefault() {
		return true;
	}

}
