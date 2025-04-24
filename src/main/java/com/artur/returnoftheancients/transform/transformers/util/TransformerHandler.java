package com.artur.returnoftheancients.transform.transformers.util;

import com.artur.returnoftheancients.client.event.ClientEventsHandler;
import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitBiome;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.transform.api.base.ITransformer;
import com.artur.returnoftheancients.transform.api.base.MVBase;
import com.artur.returnoftheancients.util.EnumAssetLocation;
import com.chaosthedude.naturescompass.util.BiomeSearchWorker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.client.lib.events.RenderEventHandler;

import java.lang.reflect.Method;
import java.util.*;
public class TransformerHandler {

    /*--------------------------START TRANSFORMER METHODS--------------------------*/

    public static ResourceLocation getCustomPlayerArmTex() {
        return EnumAssetLocation.TEXTURES_MISC.getPngRL("player_arm");
    }

    public static int getPotionMaxStackSize() {
        return 32;
    }

    public static void addPatchedTooltip(Item item, ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn, String text) {
        tooltip.add(TextFormatting.GREEN + "Patched with the mod: " + TextFormatting.RESET + "[" + Referense.MODID + "]" + TextFormatting.GREEN + " Patch list:");
        String[] textArray = text.split("\n");
        Collections.addAll(tooltip, textArray);
        tooltip.add("");
    }

    public static boolean isEntityLivingBaseInTaintBiome(Item item, ItemStack stack, EntityLivingBase livingBase) {
        return MiscHandler.arrayContains(InitBiome.TAINT_BIOMES_ID, MiscHandler.getBiomeIdOnPos(livingBase.world, livingBase.getPosition()));
    }

    public static boolean isTaintLBiomeInPos(World world, BlockPos pos) {
        return MiscHandler.arrayContains(InitBiome.TAINT_BIOMES_L_ID, world.getChunkFromBlockCoords(pos).getBiomeArray()[(pos.getX() & 15) + (pos.getZ() & 15) * 16]);
    }

    public static boolean isClientPlayerInTaintBiome() {
        if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null) {
            return ClientEventsHandler.PLAYER_IN_BIOME_MANAGER.isPlayerInTaintBiome();
        } else {
            return false;
        }
    }

    public static float getSunBrightnessInTaintBiome() {
        return ClientEventsHandler.PLAYER_IN_BIOME_MANAGER.getSunBrightnessInTaintBiome();
    }

    public static boolean isTaintEdgeBiome(World world, BlockPos pos) {
        return MiscHandler.arrayContains(InitBiome.TAINT_BIOMES_EDGE_ID, world.getChunkFromBlockCoords(pos).getBiomeArray()[(pos.getX() & 15) + (pos.getZ() & 15) * 16]);
    }

    public static void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (world.isRemote) {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextDouble(), pos.getY() + rand.nextDouble(), pos.getZ() + rand.nextDouble(), 0, 0.1, 0);
        }
    }

    public static boolean isNotCanSearchBiome(BiomeSearchWorker bsw) {
        return !TRAConfigs.Any.debugMode && MiscHandler.arrayContains(InitBiome.TAINT_BIOMES_ID, (byte) Biome.getIdForBiome(bsw.biome));
    }

    public static void fixedFogDensityEvent(EntityViewRenderEvent.RenderFogEvent event) {
        if (RenderEventHandler.fogFiddled && RenderEventHandler.fogTarget > 0.0F) {
            GlStateManager.setFog(GlStateManager.FogMode.EXP);
            GlStateManager.setFogDensity(RenderEventHandler.fogTarget);
        }
    }

    /*--------------------------START METHOD VISITORS--------------------------*/

    public static class ReturnFloatVisitor extends MethodVisitor {

        private final float ret;

        public ReturnFloatVisitor(MethodVisitor methodVisitor, float value) {
            super(Opcodes.ASM5, methodVisitor);
            ret = value;
        }

        @Override
        public void visitCode() {
            super.visitCode();

            mv.visitLdcInsn(ret);
            mv.visitInsn(Opcodes.FRETURN);
        }
    }

    public static class PrimitiveOverrideVisitor extends MVBase { // TODO: 27.02.2025 Переделать
        private final String newMethodName;
        private final int loadParamsCount;

        public PrimitiveOverrideVisitor(MethodVisitor methodVisitor, int loadParamsCount, String newMethodName) {
            super(methodVisitor, newMethodName);
            this.loadParamsCount = loadParamsCount;
            this.newMethodName = newMethodName;
        }

        @Override
        public void visitCode() {
            super.visitCode();

            for (int i = 0; i != loadParamsCount; i++) {
                mv.visitVarInsn(Opcodes.ALOAD, i);
            }

            mv.visitMethodInsn(Opcodes.INVOKESTATIC, ITransformer.HANDLER_PATH, newMethodName, desc[0], false);
            mv.visitInsn(Opcodes.RETURN);
        }
    }

    /*--------------------------START UTILS--------------------------*/

    public static String createDescriptor(Class<?> returnValue, Class<?>... params) {
        StringBuilder builder = new StringBuilder("(");
        for (Class<?> clas : params) {
            builder.append(formatDescriptor(clas));
            builder.append(';');
        }
        builder.append(')');
        builder.append(formatDescriptor(returnValue));
        if (!isPrimitive(returnValue)) {
            builder.append(";");
        }
        return builder.toString();
    }

    public static String createDescriptor(Class<?> methodClass, String methodName, Class<?>... params) { // TODO: 01.03.2025 Сделать чтобы в конце парамеров метода ставилось [;] если последний парамер не примитивный.
        Method[] methods = findMethods(methodClass, methodName);
        if (methods.length == 0) {
            return "null";
        }

        Method findMethod = null;
        if (params.length == 0) {
            findMethod = methods[0];
        } else {
            for (Method method : methods) {
                if (Arrays.equals(method.getParameterTypes(), params)) {
                    findMethod = method;
                    break;
                }
            }
            if (findMethod == null) {
                return "null";
            }
        }

        StringBuilder builder = new StringBuilder("(");
        for (Class<?> clas : findMethod.getParameterTypes()) {
            builder.append(formatDescriptor(clas));
            builder.append(';');
        }
        builder.append(')');
        builder.append(formatDescriptor(findMethod.getReturnType()));
        if (!isPrimitive(findMethod.getReturnType())) {
            builder.append(";");
        }
        return builder.toString();
    }

    public static String formatDescriptor(Class<?> param) {
        if (param == boolean.class) {
            return "Z";
        } else if (param == byte.class) {
            return "B";
        } else if (param == char.class) {
            return "C";
        } else if (param == double.class) {
            return "D";
        } else if (param == float.class) {
            return "F";
        } else if (param == int.class) {
            return "I";
        } else if (param == long.class) {
            return "J";
        } else if (param == short.class) {
            return "S";
        } else if (param == void.class) {
            return "V";
        }

        String name = param.getName();
        if (!name.contains("[L")) {
            name = "L" + name;
        }
        name = name.replaceAll("\\.", "/");
        return name;
    }

    public static boolean isPrimitive(Class<?> param) {
        return formatDescriptor(param).length() == 1;
    }

    public static Method[] findMethods(Class<?> methodClass, String methodName) {
        ArrayList<Method> methods = new ArrayList<>();
        for (Method method : methodClass.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                methods.add(method);
            }
        }
        Class<?> superClass = methodClass.getSuperclass();
        while (superClass != null) {
            for (Method method : superClass.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    methods.add(method);
                }
            }
            superClass = superClass.getSuperclass();
        }
        return methods.toArray(new Method[0]);
    }
}
