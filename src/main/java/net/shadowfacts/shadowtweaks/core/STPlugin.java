package net.shadowfacts.shadowtweaks.core;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * @author shadowfacts
 */

@MCVersion("1.7.10")
@TransformerExclusions("net.shadowfacts.shadowtweaks")
public class STPlugin implements IFMLLoadingPlugin {

	public static final Logger log = LogManager.getLogger("ShadowTweaks|Core");

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{"net.shadowfacts.shadowtweaks.core.STTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
