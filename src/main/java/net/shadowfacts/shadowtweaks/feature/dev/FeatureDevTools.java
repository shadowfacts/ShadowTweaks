package net.shadowfacts.shadowtweaks.feature.dev;

import net.minecraft.command.server.CommandSummon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.shadowfacts.shadowtweaks.feature.Feature;

import java.util.Arrays;

/**
 * @author shadowfacts
 */
public class FeatureDevTools extends Feature {

	private boolean printEntityClass;
	private String[] removeEntities;

	@Override
	public void configure(Configuration config) {
		super.configure(config);

		printEntityClass = config.getBoolean("printEntityClass", getFeatureName(), false, "Print the class name of the entity on /summon\nCreated for use in conjunction with removeEntities.\n");
		removeEntities = config.getStringList("removeEntities", getFeatureName(), new String[0], "Remove entities with these names when they attempt to spawn.\nWARNING: This can be EXTREMELY dangerous.\nDO NOT USE THIS UNLESS YOUR ARE 100% SURE YOU KNOW WHAT YOU'RE DOING.\n");
	}

	@Override
	public boolean requiresServerSide() {
		return !Arrays.equals(removeEntities, new String[0]);
	}

	@Override
	public void preInit() {
		if (printEntityClass || !Arrays.equals(removeEntities, new String[0])) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	@Override
	protected boolean isEnabledByDefault() {
		return false;
	}

	@SubscribeEvent
	public void onCommand(CommandEvent event) {
		if (printEntityClass && event.getCommand() instanceof CommandSummon) {
			if (event.getParameters()[0] != null) {
				Class<? extends Entity> clazz = EntityList.NAME_TO_CLASS.get(event.getParameters()[0]);
				if (clazz != null) {
					System.out.println(clazz.getCanonicalName());
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void checkSpawn(EntityEvent.EntityConstructing event) {
		if (!Arrays.equals(removeEntities, new String[0])) {
			String className = event.getEntity().getClass().getCanonicalName();
			if (className != null) {
				for (String s : removeEntities) {
					if (s.equals(className)) {
						event.getEntity().setDead();
					}
				}
			}
		}
	}

}
