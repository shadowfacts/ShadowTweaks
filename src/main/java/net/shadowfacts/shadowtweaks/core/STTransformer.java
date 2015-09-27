package net.shadowfacts.shadowtweaks.core;

import net.minecraft.launchwrapper.IClassTransformer;
import net.shadowfacts.shadowtweaks.core.tweaks.ScreenShotTweak;
import net.shadowfacts.shadowtweaks.core.tweaks.SleepTweak;
import net.shadowfacts.shadowtweaks.core.tweaks.ToolRightClickTweak;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author shadowfacts
 */
public class STTransformer implements IClassTransformer {

	private static final List<String> classes = Arrays.asList(
			"net.minecraft.item.ItemTool",
			"net.minecraft.entity.player.EntityPlayer",
			"net.minecraft.util.ScreenShotHelper");

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		boolean obfuscated = !name.equals(transformedName);
		int index = classes.indexOf(transformedName);
		return index != -1 ? transform(index, bytes, obfuscated) : bytes;
	}

	private static byte[] transform(int index, byte[] bytes, boolean obfuscated) {
		try {

			ClassNode classNode = new ClassNode();
			ClassReader classReader = new ClassReader(bytes);
			classReader.accept(classNode, 0);

			switch (index) {
				case 0:
					ToolRightClickTweak.transformItemTool(classNode, obfuscated);
					break;
				case 1:
					SleepTweak.transformEntityPlayer(classNode, obfuscated);
					break;
				case 2:
					ScreenShotTweak.transformScreenShotHelper(classNode, obfuscated);
					break;
			}

			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			classNode.accept(classWriter);
			return classWriter.toByteArray();



		} catch (Exception e) {
			STPlugin.log.error("There was a problem transforming " + classes.get(index));
			e.printStackTrace();
		}

		return bytes;
	}



}
