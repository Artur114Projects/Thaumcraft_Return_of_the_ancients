package com.artur.returnoftheancients.items;


import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.util.ITRAStructure;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.TRAStructureEBS;
import com.artur.returnoftheancients.client.particle.RotateParticleFlame;
import com.artur.returnoftheancients.client.particle.TrapParticleFlame;
import com.artur.returnoftheancients.gui.CoolLoadingGui;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemGavno extends BaseItem {

	private ITRAStructure structure = null;

	public ItemGavno(String name) {
		super(name);
		setMaxStackSize(1);
		setContainerItem(this);
	}

	@Override
	public @NotNull EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player instanceof EntityPlayerMP) {
			if (structure == null) {
				structure = new TRAStructureEBS("ancient_crossroads", null);
			}
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
		if (worldIn.isRemote) {
			Vec3d vec3d = player.getLook(1.0F);
			for (int i = 0; i != 10; i++) {
				spawnCustomParticle(worldIn, player.posX, player.posY + 1, player.posZ, vec3d.x + (i / 20D) / vec3d.x, vec3d.y + (i / 20D) / vec3d.y, vec3d.z + (i / 20D) / vec3d.z);
//				spawnCustomParticleTM(worldIn, pos.getX() + 1.5, pos.getY() + 1.5, pos.getZ() + 0.6);
//				spawnCustomParticleTM(worldIn, pos.getX(), pos.getY(), pos.getZ(), 1);
			}
//			spawnCustomParticle(worldIn, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0.1, 0);
		}
		if (!worldIn.isRemote) {
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
		if (!worldIn.isRemote) {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("sendMessage", "gavno");
			MainR.NETWORK.sendToAll(new ClientPacketMisc(nbt));
		}
		System.out.println(worldIn.playerEntities);
		if (worldIn.isRemote) {
			Minecraft.getMinecraft().displayGuiScreen(new CoolLoadingGui(true));
			CoolLoadingGui.instance.updatePlayersList(new String[] {
				"Artur114",
				"Meow227",
				"wesfrgtfhjgh125"
			});
		}
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
		playerIn.setActiveHand(handIn);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@SideOnly(Side.CLIENT)
	public static void spawnCustomParticleTM(World world, double x, double y, double z, double radius) {
		Minecraft.getMinecraft().effectRenderer.addEffect(new RotateParticleFlame(world, x, y + 1.5, z, radius, 1.0 + (world.rand.nextDouble() / 5)));
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
		tooltip.add("Don t click");
	}
}
