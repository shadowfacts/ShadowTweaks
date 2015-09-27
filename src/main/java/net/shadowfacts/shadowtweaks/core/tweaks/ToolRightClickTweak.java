package net.shadowfacts.shadowtweaks.core.tweaks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.shadowfacts.shadowtweaks.MiscHandlers;
import net.shadowfacts.shadowtweaks.core.STPlugin;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

/**
 * @author shadowfacts
 */
public class ToolRightClickTweak {

	public static void transformItemTool(ClassNode classNode, boolean obfuscated) {
		STPlugin.log.info("Transforming net.minecraft.item.ItemTool");
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
		STPlugin.log.info("Finished transforming ItemTool");
	}

}
