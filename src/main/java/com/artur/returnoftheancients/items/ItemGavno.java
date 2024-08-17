package com.artur.returnoftheancients.items;


import com.artur.returnoftheancients.ancientworldgeneration.main.AncientWorld;
import com.artur.returnoftheancients.ancientworldgeneration.structurebuilder.CustomGenStructure;
import com.artur.returnoftheancients.generation.generators.GenStructure;
import com.artur.returnoftheancients.gui.SkalaGui;
import com.artur.returnoftheancients.handlers.HandlerR;
import com.artur.returnoftheancients.init.InitSounds;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
//			nbt.setString("playSound", InitSounds.RUI_DEAD.NAME);
//			MainR.NETWORK.sendTo(new ClientPacketMisc(nbt),(EntityPlayerMP)  player);
			if (player.isSneaking()) {
				AncientWorld.reload();
			} else {
				AncientWorld.unload();
				player.sendMessage(new TextComponentString("UNLOAD"));
			}
			HandlerR.playSound((EntityPlayerMP) player, InitSounds.FIRE_TRAP_SOUND);
		}
		for (int i = 0; i != 20; i++) {
			for (int j = 0; j != 1; j++) {
				worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + (1 + (j / 100D)), pos.getZ() + 0.5, 0, 0.1 + i / 40D, 0);
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
