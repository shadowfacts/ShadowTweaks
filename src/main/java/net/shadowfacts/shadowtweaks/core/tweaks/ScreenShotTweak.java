package net.shadowfacts.shadowtweaks.core.tweaks;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IChatComponent;
import net.shadowfacts.shadowtweaks.core.STPlugin;
import net.shadowfacts.shadowtweaks.features.screenshot.ScreenShotHelper;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.util.Optional;

/**
 * @author shadowfacts
 */
public class ScreenShotTweak {

	public static void transformScreenShotHelper(ClassNode classNode, boolean obfuscated) {
		STPlugin.log.info("Transforming net.minecraft.util.ScreenShotHelper");

		final String SAVE_SCREENSHOT = obfuscated ? "func_148259_a" : "saveScreenshot";
//		final String SAVE_SCREENSHOT_DESC = obfuscated ? "(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;" : "(Ljava/io/File;Ljava/lang/String;IILnet/minecraft/client/shader/Framebuffer;)Lnet/minecraft/util/IChatComponent;";
		final String SAVE_SCREENSHOT_DESC = Type.getMethodDescriptor(Type.getType(IChatComponent.class),
				Type.getType(File.class), Type.getType(String.class), Type.getType(int.class), Type.getType(int.class), Type.getType(Framebuffer.class));

		Optional<MethodNode> optionalSaveScreenShot = classNode.methods.stream()
				.filter(method -> method.name.equals(SAVE_SCREENSHOT))
				.filter(method -> method.desc.equals(SAVE_SCREENSHOT_DESC))
				.findFirst();

		if (optionalSaveScreenShot.isPresent()) {

			MethodNode method = optionalSaveScreenShot.get();

			InsnList toInsert = new InsnList();

			toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInsert.add(new VarInsnNode(Opcodes.ALOAD, 1));
			toInsert.add(new VarInsnNode(Opcodes.ILOAD, 2));
			toInsert.add(new VarInsnNode(Opcodes.ILOAD, 3));
			toInsert.add(new VarInsnNode(Opcodes.ALOAD, 4));
			toInsert.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ScreenShotHelper.class), "saveScreenshot", SAVE_SCREENSHOT_DESC, false));
			toInsert.add(new InsnNode(Opcodes.ARETURN));


			method.instructions.insertBefore(method.instructions.getFirst(), toInsert);

		} else {
			STPlugin.log.error("The correct saveScreenshot method could not be found in ScreenShotHelper");
		}
	}

}
