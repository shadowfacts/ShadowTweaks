package net.shadowfacts.shadowtweaks.core.tweaks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.shadowfacts.shadowtweaks.STConfig;
import net.shadowfacts.shadowtweaks.core.STPlugin;
import net.shadowfacts.shadowtweaks.core.STTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author shadowfacts
 */
public class SleepTweak {

	private static String SLEEP_IN_BED_AT;
	private static String WORLD_OBJ;
	private static String PROVIDER;
	private static String IS_SURFACE_WORLD;
	private static String IS_RAINING;
	private static String OK;
	private static String ON_UPDATE;
	private static String IS_DAYTIME;
	private static String SLEEP_TIMER;
	private static String SET_RAIN_STRENGTH;

	public static void transformEntityPlayer(ClassNode classNode, boolean obfuscated) {
		if (STConfig.sleepWhenRaining) {
			STPlugin.log.info("Transforming net.minecraft.entity.player.EntityPlayer");
			SLEEP_IN_BED_AT = obfuscated ? "" : "sleepInBedAt";
			WORLD_OBJ = obfuscated ? "" : "worldObj";
			PROVIDER = obfuscated ? "" : "provider";
			IS_SURFACE_WORLD = obfuscated ? "" : "isSurfaceWorld";
			IS_RAINING = obfuscated ? "" : "isRaining";
			OK = obfuscated ? "" : "OK";
			ON_UPDATE = obfuscated ? "" : "onUpdate";
			IS_DAYTIME = obfuscated ? "" : "isDaytime";
			SLEEP_TIMER = obfuscated ? "" : "sleepTimer";
			SET_RAIN_STRENGTH = obfuscated ? "" : "setRainStrength";

//		sleepInBedAt
			Optional<MethodNode> optional = classNode.methods.stream().filter(method -> method.name.equals(SLEEP_IN_BED_AT)).findFirst();
			if (optional.isPresent()) {
				transformSleepInBedAt(classNode, optional.get());
			} else {
				STPlugin.log.error("The method sleepInBedAt could not be found in EntityPlayer");
			}

//		onUpdate
			Optional<MethodNode> optionalOnUpdate = classNode.methods.stream().filter(method -> method.name.equals(ON_UPDATE)).findFirst();
			if (optionalOnUpdate.isPresent()) {
				transformOnUpdate(classNode, optionalOnUpdate.get());
			} else {
				STPlugin.log.error("The method onUpdate could not be found in EntityPlayer");
			}

			STPlugin.log.info("Finished transforming EntityPlayer");
		}
	}

	private static void transformSleepInBedAt(ClassNode classNode, MethodNode method) {

		AbstractInsnNode target = null;

		for (AbstractInsnNode instruction : method.instructions.toArray()) {
			if (instruction.getOpcode() == Opcodes.GETFIELD && instruction instanceof FieldInsnNode) {
				FieldInsnNode fieldInsnNode = (FieldInsnNode)instruction;
				if (fieldInsnNode.owner.equals(classNode.name) &&
						fieldInsnNode.name.equals(WORLD_OBJ)) {
					AbstractInsnNode next = instruction.getNext();
					if (next.getOpcode() == Opcodes.GETFIELD && next instanceof FieldInsnNode) {
						FieldInsnNode fieldInsnNodeNext = (FieldInsnNode)next;
						if (fieldInsnNodeNext.owner.equals(Type.getInternalName(World.class)) &&
								fieldInsnNodeNext.name.equals(PROVIDER)) {
							AbstractInsnNode nextNext = next.getNext();
							if (nextNext.getOpcode() == Opcodes.INVOKEVIRTUAL && nextNext instanceof MethodInsnNode) {
								MethodInsnNode methodInsnNode = (MethodInsnNode)nextNext;
								if (methodInsnNode.owner.equals(Type.getInternalName(WorldProvider.class)) &&
										methodInsnNode.name.equals(IS_SURFACE_WORLD)) {
									target = instruction;
								}
							}
						}
					}
				}
			}
		}

		if (target != null) {

			LabelNode L46 = new LabelNode(new Label());
			LabelNode L47 = new LabelNode(new Label());


			InsnList toInsert = new InsnList();

			toInsert.add(L46);
			toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
			toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, WORLD_OBJ, "L" + Type.getInternalName(World.class) + ";"));
			toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Type.getInternalName(World.class), IS_RAINING, "()Z", false));
			toInsert.add(new JumpInsnNode(Opcodes.IFEQ, L47));

			toInsert.add(L47);
			toInsert.add(new FieldInsnNode(Opcodes.GETSTATIC, Type.getInternalName(EntityPlayer.EnumStatus.class), OK, "L" + Type.getInternalName(EntityPlayer.EnumStatus.class) + ";"));
			toInsert.add(new InsnNode(Opcodes.ARETURN));

			method.instructions.insertBefore(target, toInsert);

		} else {
			STPlugin.log.error("Could not find the target node in EntityPlayer.sleepInBedAt");
		}

	}

	private static void transformOnUpdate(ClassNode classNode, MethodNode method) {
		List<AbstractInsnNode> toRemove = new ArrayList<>();

		for (AbstractInsnNode instruction : method.instructions.toArray()) {
			if (instruction.getOpcode() == Opcodes.ALOAD && instruction instanceof VarInsnNode &&
					((VarInsnNode)instruction).var == 0) {
				AbstractInsnNode next = instruction.getNext();
				if (next.getOpcode() == Opcodes.GETFIELD && next instanceof FieldInsnNode) {
					FieldInsnNode fieldInsnNode = (FieldInsnNode)next;
					if (fieldInsnNode.owner.equals(classNode.name) && fieldInsnNode.name.equals(WORLD_OBJ)) {
						AbstractInsnNode nextNext = next.getNext();
						if (nextNext.getOpcode() == Opcodes.INVOKEVIRTUAL && nextNext instanceof MethodInsnNode) {
							MethodInsnNode methodInsnNode = (MethodInsnNode)nextNext;
							if (methodInsnNode.owner.equals(Type.getInternalName(World.class)) && methodInsnNode.name.equals(IS_DAYTIME) && methodInsnNode.desc.equals("()Z")) {
								if (methodInsnNode.getNext().getOpcode() == Opcodes.IFEQ && methodInsnNode.getNext() instanceof JumpInsnNode) {
									toRemove.add(instruction);
									toRemove.add(next);
									toRemove.add(nextNext);
									toRemove.add(nextNext.getNext());
								}
							}
						}
					}
				} else if (next.getOpcode() == Opcodes.ICONST_0) {
					AbstractInsnNode nextNext = next.getNext();
					AbstractInsnNode nextNextNext = nextNext.getNext();
					if (nextNext.getOpcode() == Opcodes.ICONST_1 && nextNextNext.getOpcode() == Opcodes.ICONST_1) {
						AbstractInsnNode maybeInvokeVirtual = nextNextNext.getNext();
						if (maybeInvokeVirtual.getOpcode() == Opcodes.INVOKEVIRTUAL && maybeInvokeVirtual instanceof MethodInsnNode) {
							MethodInsnNode invokeVirtual = (MethodInsnNode)maybeInvokeVirtual;
							if (invokeVirtual.owner.equals(classNode.name) && invokeVirtual.name.equals(WORLD_OBJ)) {
								AbstractInsnNode maybeIfeq = invokeVirtual.getNext();
								if (maybeIfeq.getOpcode() == Opcodes.IFEQ) {
									toRemove.add(instruction);
									toRemove.add(next);
									toRemove.add(nextNext);
									toRemove.add(nextNextNext);
									toRemove.add(invokeVirtual);
									toRemove.add(maybeIfeq);
								}
							}
						}
					}
				}
			}
		}

		AbstractInsnNode target = toRemove.get(0);

		LabelNode L64 = new LabelNode(new Label());
		LabelNode L65 = new LabelNode(new Label());
		LabelNode L66 = new LabelNode(new Label());

		InsnList toInsert = new InsnList();

		toInsert.add(L64);
		toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, SLEEP_TIMER, "I"));
		toInsert.add(new IntInsnNode(Opcodes.BIPUSH, 100));
		toInsert.add(new JumpInsnNode(Opcodes.IF_ICMPLT, L66));
		toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, WORLD_OBJ, "L" + Type.getInternalName(World.class) + ";"));
		toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Type.getInternalName(World.class), IS_RAINING, "()Z", false));
		toInsert.add(new JumpInsnNode(Opcodes.IFEQ, L66));

		toInsert.add(L65);
		toInsert.add(new VarInsnNode(Opcodes.ALOAD, 0));
		toInsert.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name, WORLD_OBJ,"L" + Type.getInternalName(World.class) + ";"));
		toInsert.add(new InsnNode(Opcodes.FCONST_0));
		toInsert.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, Type.getInternalName(World.class), SET_RAIN_STRENGTH, "()Z", false));

		toInsert.add(L66);
		toInsert.add(new InsnNode(Opcodes.RETURN));

		method.instructions.insert(target, toInsert);

		toRemove.stream().forEach(method.instructions::remove);
		System.out.println("test");
	}

}
