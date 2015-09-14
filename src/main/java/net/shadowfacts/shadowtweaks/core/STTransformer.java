package net.shadowfacts.shadowtweaks.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.world.World;
import net.shadowfacts.shadowtweaks.MiscHandlers;
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

	private static final List<String> classes = Arrays.asList("net.minecraft.item.ItemTool");

	private static final Logger log = LogManager.getLogger("STTransformer");

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
					transformItemTool(classNode, obfuscated);
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

	private static void transformItemTool(ClassNode classNode, boolean obfuscated) {
		log.info("Transforming class: ItemTool");
		final String ON_ITEM_USE = obfuscated ? "func_77648_a" : "onItemUse";
		Type intType = Type.getType(int.class);
		Type floatType = Type.getType(float.class);
		final String ON_ITEM_USE_DESC = Type.getMethodDescriptor(Type.getType(boolean.class),
				Type.getType(ItemStack.class), Type.getType(EntityPlayer.class), Type.getType(World.class),
				intType, intType, intType, intType, floatType, floatType, floatType);
		MethodNode onItemUse = new MethodNode(Opcodes.ACC_PUBLIC, ON_ITEM_USE, ON_ITEM_USE_DESC, null, null);
		LabelNode L0 = new LabelNode(new Label());

		onItemUse.instructions.add(L0);
		onItemUse.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
		onItemUse.instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
		onItemUse.instructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
		onItemUse.instructions.add(new VarInsnNode(Opcodes.ILOAD, 4));
		onItemUse.instructions.add(new VarInsnNode(Opcodes.ILOAD, 5));
		onItemUse.instructions.add(new VarInsnNode(Opcodes.ILOAD, 6));
		onItemUse.instructions.add(new VarInsnNode(Opcodes.ILOAD, 7));
		onItemUse.instructions.add(new VarInsnNode(Opcodes.FLOAD, 8));
		onItemUse.instructions.add(new VarInsnNode(Opcodes.FLOAD, 9));
		onItemUse.instructions.add(new VarInsnNode(Opcodes.FLOAD, 10));
		onItemUse.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MiscHandlers.class), "onToolUse", ON_ITEM_USE_DESC, false));
		onItemUse.instructions.add(new InsnNode(Opcodes.IRETURN));

		classNode.methods.add(onItemUse);
		log.info("Finished transforming ItemTool");
	}

}
