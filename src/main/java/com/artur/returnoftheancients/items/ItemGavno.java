package com.artur.returnoftheancients.items;


import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.ITRAStructure;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.TRAStructureEBS;
import com.artur.returnoftheancients.ancientworldgeneration.util.Team;
import com.artur.returnoftheancients.client.CameraShake;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.gui.SkalaGui;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.init.InitTileEntity;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;

import java.util.List;

public class ItemGavno extends BaseItem{

	ITRAStructure structure = null;

	public ItemGavno(String name) {
		super(name);
		setMaxStackSize(1);
		setContainerItem(this);
	}

	@Override
	public @NotNull EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player instanceof EntityPlayerMP) {
//			PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((byte) 2), (EntityPlayerMP) player);
//			NBTTagCompound nbt = new NBTTagCompound();
//			nbt.setString("playSound", InitSounds.RUI_DEAD.NAM);
//			MainR.NETWORK.sendTo(new ClientPacketMisc(nbt),(EntityPlayerMP)  player);
			if (player.isSneaking()) {
				Team.clear();
				AncientWorld.reload();
			} else {
				AncientWorld.unload();
				player.sendMessage(new TextComponentString("UNLOAD"));
				Team.clear();
			}
		}
		if (worldIn.isRemote) {
			CameraShake.startShake(4);
			for (int i = 0; i != 20; i++) {
				for (int j = 0; j != 1; j++) {
					ParticleManager particleManager = Minecraft.getMinecraft().effectRenderer;
					particleManager.spawnEffectParticle(
							EnumParticleTypes.BLOCK_CRACK.getParticleID(),
							pos.getX() + worldIn.rand.nextDouble(),
							pos.getY() + worldIn.rand.nextDouble(),
							pos.getZ() + worldIn.rand.nextDouble(),
							0.0D, 0.1D, 0.0D,
							Block.getStateId(worldIn.getBlockState(pos))
					);
				}
			}
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
		return EnumActionResult.SUCCESS;
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
