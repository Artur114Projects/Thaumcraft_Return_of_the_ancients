package com.artur.returnoftheancients.items;


import com.artur.returnoftheancients.gui.SkalaGui;
import com.artur.returnoftheancients.handlers.EventsHandler;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.network.ClientPacketMisc;
import com.artur.returnoftheancients.referense.Referense;
import com.artur.returnoftheancients.init.InitSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.misc.PacketMiscEvent;

import java.io.IOException;
import java.io.InputStream;
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
			PacketHandler.INSTANCE.sendTo(new PacketMiscEvent((byte) 2), (EntityPlayerMP) player);
		}
		Minecraft.getMinecraft().player.playSound(InitSounds.RUI_DEAD, 1, 1);
		player.setHealth(player.getHealth() - 1);
		player.performHurtAnimation();
		System.out.println(Minecraft.getMinecraft().player.rotationYaw);
		if (!worldIn.isRemote) {
			player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 30, -1));
			ResourceLocation location = new ResourceLocation(Referense.MODID, "ancient_kusok_portal");
			String s = location.getResourceDomain();
			String s1 = location.getResourcePath();
			InputStream inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + s + "/structures/" + s1 + ".nbt");
			try {
				assert inputstream != null;
				NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(inputstream);
				System.out.println(nbttagcompound);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
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
