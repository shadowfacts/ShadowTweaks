package net.shadowfacts.shadowtweaks.features.screenshot.services;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.function.Consumer;

/**
 * @author shadowfacts
 */
public interface Service extends Consumer<File> {

	String getName();

	void configure(Configuration config, String category);

}
