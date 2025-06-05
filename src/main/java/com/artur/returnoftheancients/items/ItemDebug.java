package com.artur.returnoftheancients.items;


import com.artur.returnoftheancients.ancientworld.map.build.AncientLayer1Builder;
import com.artur.returnoftheancients.ancientworld.map.gen.GenPhase;
import com.artur.returnoftheancients.client.fx.particle.RotateParticleSmokeInPlayer;
import com.artur.returnoftheancients.client.fx.particle.TrapParticleFlame;
import com.artur.returnoftheancients.events.ServerEventsHandler;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.generation.portal.base.client.ClientAncientPortal;
import com.artur.returnoftheancients.structurebuilder.StructureBuildersManager;
import com.artur.returnoftheancients.util.math.UltraMutableBlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.client.fx.FXDispatcher;

import java.util.List;
import java.util.Random;

public class ItemDebug extends BaseItem {

	public ItemDebug(String name) {
		super(name);
		setMaxStackSize(1);
		setContainerItem(this);
		setMaxDamage(2);
	}

	@Override
	public @NotNull EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player instanceof EntityPlayerMP) {
//			if (structure == null) {
//				structure = new TRAStructureEBS("ancient_crossroads", null);
//			}
//			PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((byte) 2), (EntityPlayerMP) player);
//			NBTTagCompound nbt = new NBTTagCompound();
//			nbt.setString("playSound", InitSounds.RUI_DEAD.NAM);
//			MainR.NETWORK.sendTo(new ClientPacketMisc(nbt),(EntityPlayerMP)  player);
//			if (player.isSneaking()) {
//				Team.clear();
//				AncientWorld.reload();
//			} else {
//				AncientWorld.unload();
//				player.sendMessage(new TextComponentString("UNLOAD"));
//				Team.clear();
//			}
//			IPlayerTimerCapability timer = TRACapabilities.getTimer(player);
//			if (timer.hasTimer("recovery")) {
//				player.sendMessage(new TextComponentString("time:" + timer.getTime("recovery")));
//			}
//			GenStructure.generateStructure(worldIn, pos.getX() - 3, pos.getY() - 30, pos.getZ() - 2, "ancient_exit");
//			System.out.println(worldIn.getBlockState(pos));
//			EntityLightningBolt lightningBolt = new EntityLightningBolt(worldIn, pos.getX(), pos.getY(), pos.getZ(), false);
//			worldIn.addWeatherEffect(lightningBolt);
		}
//		if (worldIn.isRemote) {
//			Vec3d vec3d = player.getLook(1.0F);
//			for (int i = 0; i != 10; i++) {
//				spawnCustomParticle(worldIn, player.posX, player.posY + 1, player.posZ, vec3d.x + (i / 20D) / vec3d.x, vec3d.y + (i / 20D) / vec3d.y, vec3d.z + (i / 20D) / vec3d.z);
////				spawnCustomParticleTM(worldIn, pos.getX() + 1.5, pos.getY() + 1.5, pos.getZ() + 0.6);
////				spawnCustomParticleTM(worldIn, pos.getX(), pos.getY(), pos.getZ(), 1);
//			}
////			spawnCustomParticle(worldIn, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1, 0);
//		}
		if (worldIn.isRemote) {

			for (int i = 0; i != 10; i++) {
				FXDispatcher.INSTANCE.drawVentParticles(pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5, 0.0, 0.001, 0.0, 0x999999, 1.0F);
			}

//			ClientEventsHandler.CAMERA_FX_MANAGER.startShake(20 * 20);

//			if (player.isSneaking()) {
//				NBTTagCompound data = new NBTTagCompound();
//				data.setInteger("dimension", worldIn.provider.getDimension());
//				data.setInteger("chunkX", pos.getX() >> 4);
//				data.setInteger("chunkZ", pos.getZ() >> 4);
//				data.setInteger("posY", pos.getY());
//				data.setInteger("id", 0);
//				portal = new ClientAncientPortal(data);
//				ClientAncientPortalsProcessor.PORTALS.put(0, portal);
//			} else {
//				if (portal != null) {
//					System.out.println(portal.isCollide(pos));
//				}
//			}
//			if (sound == null) {
//				sound = new RepeatingSound(InitSounds.HEARTBEAT.SOUND.getSoundName(), SoundCategory.AMBIENT, 10, 1, true, 1, ISound.AttenuationType.LINEAR, 0, 0, 0);
//			}
//
//			if (player.isSneaking()) {
//				sound.stop();
//			} else {
//				Minecraft.getMinecraft().getSoundHandler().playSound(sound);
//			}
//			if (player.isSneaking()) {
//				ClientEventsHandler.FOG_MANAGER.setFogParams(new FogManager.FogParams(100, 80, 20, 60));
//			} else {
//				ClientEventsHandler.FOG_MANAGER.setFogParams(new FogManager.FogParams(100, 20, 100, 20));
//			}
		}
		worldIn.getChunkFromBlockCoords(pos).checkLight();
		if (!worldIn.isRemote) {
			if (player.isSneaking()) {
				GenPhase phase = GenPhase.initAllGenPhases();
				AncientLayer1Builder builder = new AncientLayer1Builder(phase.getMap(System.currentTimeMillis(), 17).toInteractive(worldIn), worldIn, new Random(), worldIn.getChunkFromBlockCoords(pos).getPos());
				ServerEventsHandler.SLOW_BUILD_MANAGER.newBuilder(builder);
			}


//			if (player.isSneaking()) {
//				BlockProtectHandler.protect(worldIn, pos);
//			} else {
//				BlockProtectHandler.unProtect(worldIn, pos);
//			}

//			Class<?> clas = AncientPortalNaturalGeneration.class.getDeclaredClasses()[1];
//			player.sendMessage(new TextComponentString(clas.getName()));
//			player.sendMessage(new TextComponentString(AncientPortalNaturalGeneration.class.getDeclaredClasses().length + ""));
//			System.out.println((new WorldGenTaintBigTree(false)).generate(worldIn, new Random(), pos));`
//			if (player.isSneaking()) {
//				blockPos = pos;
//			} else {
//				if (blockPos != null) {
//					new GenAncientArch().generate(worldIn, pos, blockPos, EnumFacing.AxisDirection.POSITIVE);
//				}
//			}
//			TerrainAnalyzer analyzer = new TerrainAnalyzer(worldIn);
//
//			analyzer.startAnalyzing(worldIn.getChunkFromBlockCoords(pos).getPos());
//
//			player.sendMessage(new TextComponentString("Flow:" + analyzer.getTerrainFlow()));
//			player.sendMessage(new TextComponentString("Height variation:" + analyzer.getHeightVariation()));
//			player.sendMessage(new TextComponentString("Average Height:" + analyzer.getAverageHeight()));
//			player.sendMessage(new TextComponentString("Height (0, 0):" + analyzer.getHeight(0, 0, false)));
//			player.sendMessage(new TextComponentString("Height not liquids (0, 0):" + analyzer.getHeight(0, 0, true)));
//			player.sendMessage(new TextComponentString("The second time!"));
//			player.sendMessage(new TextComponentString("Flow:" + analyzer.getTerrainFlow()));
//			player.sendMessage(new TextComponentString("Height variation:" + analyzer.getHeightVariation()));
//			player.sendMessage(new TextComponentString("Average Height:" + analyzer.getAverageHeight()));

//			BlockPos[] sanctuaryPillars = new BlockPos[] {
//					new BlockPos(2, 0, 4),
//					new BlockPos(4, 0, 2),
//
//					new BlockPos(10, 0, 2),
//					new BlockPos(12, 0, 4),
//
//					new BlockPos(2, 0, 10),
//					new BlockPos(4, 0, 12),
//
//					new BlockPos(10, 0, 12),
//					new BlockPos(12, 0, 10),
//			};
//
//			UltraMutableBlockPos blockPos = new UltraMutableBlockPos(worldIn.getChunkFromBlockCoords(pos).getPos());
//
//			blockPos.setY(analyzer.getMaxHeight());
//
//			CustomGenStructure.gen(worldIn, blockPos.addY(16), "ancient_sanctuary");
//
//			blockPos.addY(-1);
//
//			for (BlockPos offset : sanctuaryPillars) {
//				blockPos.pushPos();
//
//				for (blockPos.add(offset); worldIn.isAirBlock(blockPos); blockPos.down()) {
//					worldIn.setBlockState(blockPos, BlocksTC.stoneEldritchTile.getDefaultState());
//				}
//
//				blockPos.popPos();
//			}

			player.sendMessage(new TextComponentString("[" + (pos.getX() & 15) + ", " + (pos.getZ() & 15) + "]"));

//			new GenAncientSpire().generate(worldIn, worldIn.getChunkFromBlockCoords(pos).getPos(), 255);

//			BlockPos playerPos = player.getPosition();
//			long time1 = System.currentTimeMillis();
//			GenStructure.generateStructure(player.world, playerPos.getX() + 20, playerPos.getY(), playerPos.getZ(), "ancient_turn");
//			long time1Finish = System.currentTimeMillis() -  time1;
//			player.sendMessage(new TextComponentString("template is took:" + time1Finish + "ms"));
//			long time = System.currentTimeMillis();
//			CustomGenStructure.registerOrGen(player.world, playerPos.getX(), playerPos.getY(), playerPos.getZ(), "ancient_turn");
//			long timeFinish = System.currentTimeMillis() -  time;
//			player.sendMessage(new TextComponentString("my gen is took:" + timeFinish + "ms"));
		}
//		if (!worldIn.isRemote) {
//			NBTTagCompound nbt = new NBTTagCompound();
//			nbt.setString("sendMessage", "gavno");
//			MainR.NETWORK.sendToAll(new ClientPacketMisc(nbt));
//
//			new AncientPortalNaturalGeneration(worldIn.getMinecraftServer(), worldIn.provider.getDimension(), pos.getX() >> 4, pos.getZ() >> 4, HandlerR.calculateGenerationHeight(worldIn, pos.getX(), pos.getZ()));
//
////			worldIn.createExplosion(player, pos.getX(), pos.getY(), pos.getZ(), 12, true);
//		}
//		System.out.println(worldIn.playerEntities);
//		if (worldIn.isRemote) {
//			for (int i = 0; i != 32; i++) {
//				spawnCustomParticleTM(worldIn, 1, 0.4 + (i / 100.0D), player, i / 10.0D);
//			}
//		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (worldIn.isRemote) {
			Vec3d vec3d = playerIn.getLook(1.0F);

			double xOff = -Math.sin(Math.toRadians(playerIn.rotationYaw)) * 2;
			double zOff =  Math.cos(Math.toRadians(playerIn.rotationYaw)) * 2;

			for (int i = 0; i != 32; i++) {
				spawnCustomParticle(worldIn, playerIn.posX + xOff, playerIn.posY + 1, playerIn.posZ + zOff, vec3d.x * ((i / 10.0D) + 1), vec3d.y * ((i / 10.0D) + 1), vec3d.z * ((i / 10.0D) + 1));
			}
		}

		playerIn.getHeldItem(handIn).setItemDamage(1);

		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@SideOnly(Side.CLIENT)
	public static void spawnCustomParticleTM(World world, double radius, double angularVelocity, EntityPlayer player, double yOffset) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new RotateParticleSmokeInPlayer(world, radius, angularVelocity, player, yOffset));
	}

	@SideOnly(Side.CLIENT)
	public static void spawnCustomParticle(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new TrapParticleFlame(world, x, y, z, xSpeed, ySpeed, zSpeed));
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.UNDERLINE + "M" + TextFormatting.RESET + TextFormatting.OBFUSCATED + "e" + TextFormatting.RESET  + TextFormatting.BOLD + "o" + TextFormatting.RESET  + TextFormatting.STRIKETHROUGH + "w");
	}

}
