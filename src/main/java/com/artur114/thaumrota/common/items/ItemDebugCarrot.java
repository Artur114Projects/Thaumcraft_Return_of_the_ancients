package com.artur114.thaumrota.common.items;


import com.artur114.bananalib.mc.base.BItemBase;
import com.artur114.bananalib.mc.math.m3d.vec.PosMc3IM;
import com.artur114.thaumrota.common.blocks.BlockLightningStoneTC;
import com.artur114.thaumrota.common.tileentity.interf.ITileBurner;
import com.artur114.thaumrota.common.util.DevScriptsShell;
import com.artur114.thaumrota.common.util.math.UltraMutableBlockPos;
import com.artur114.thaumrota.main.ThaumRotA;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;
import java.util.Random;

import static net.minecraft.item.ItemStack.DECIMALFORMAT;

public class ItemDebugCarrot extends BItemBase {
	private final Random rand = new Random();

	public ItemDebugCarrot(String name) {
		super(name);
		this.setMaxStackSize(1);
		this.setContainerItem(this);
		this.setCreativeTab(ThaumRotA.CREATIVE_TAB);
	}

	@Override
	public @NotNull EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		if (player.isSneaking()) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof ITileBurner) {
				if (((ITileBurner) tile).isActive()) {
					((ITileBurner) tile).deactivate();
				} else {
					((ITileBurner) tile).activate();
				}
			}
			if (!worldIn.isRemote && tile instanceof TileEntityStructure) {
				this.loadLightMapToFile((TileEntityStructure) tile, (EntityPlayerMP) player, worldIn);
				return EnumActionResult.SUCCESS;
			}
		}

		if (state.getBlock() instanceof BlockLightningStoneTC) {
			int value = state.getValue(BlockLightningStoneTC.LIGHT) + 1 - (player.isSneaking() ? 2 : 0); value = value > 15 ? 0 : value; value = value < 0 ? 15 : value;
			worldIn.setBlockState(pos, state.withProperty(BlockLightningStoneTC.LIGHT, value));
			player.sendStatusMessage(new TextComponentString("Lightning: " + (15 - value)), true);
			return EnumActionResult.SUCCESS;
		}

        if (DevScriptsShell.isDev()) {
            ThaumRotA.DEV_SHELL.evaluate(
                "carrot_use_on_block.groovy",
                new String[]{"player", "world", "pos", "hand", "facing", "hitX", "hitY", "hitZ"},
                new Object[]{player, worldIn, pos, hand, facing, hitX, hitY, hitZ}
            );
        }

        return EnumActionResult.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (DevScriptsShell.isDev()) {
            ThaumRotA.DEV_SHELL.evaluate(
                "carrot_use.groovy",
                new String[]{"world", "player", "hand"},
                new Object[]{worldIn, playerIn, handIn}
            );
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public IRarity getForgeRarity(ItemStack stack) {
		return EnumRarity.EPIC;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		target.onKillCommand();
		if (attacker.isSneaking() && target.getHealth() > 0.0F) {
			target.setHealth(0.0F);
			target.setDead();
		}
		return true;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!player.world.isRemote && player.isSneaking() && !entity.attackEntityFrom(DamageSource.causePlayerDamage(player), 1.0F)) {
			entity.onKillCommand();
			if (!entity.isDead && entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHealth() > 0.0F){
				entity.setDead();
				((EntityLivingBase) entity).setHealth(0.0F);
			}
			if (!entity.isDead) {
				player.world.removeEntity(entity);
			}
		}
		entity.hurtResistantTime = 0;
		return super.onLeftClickEntity(stack, player, entity);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("");
		tooltip.add(I18n.format("item.modifiers.mainhand"));
		tooltip.add(" " + net.minecraft.util.text.translation.I18n.translateToLocalFormatted("attribute.modifier.equals.0", DECIMALFORMAT.format(3.0D), net.minecraft.util.text.translation.I18n.translateToLocal("attribute.name.generic.attackSpeed")));
		tooltip.add(" " + I18n.format("item.debug_carrot.info.i") + " " + I18n.format("attribute.name.generic.attackDamage"));
	}

	private void loadLightMapToFile(TileEntityStructure tile, EntityPlayerMP player, World world) {
		MinecraftServer server = player.mcServer;
		File structuresPath = server.getActiveAnvilConverter().getFile(server.getFolderName(), "structures");
		if (!structuresPath.exists()) return;

		NBTTagCompound data = tile.writeToNBT(new NBTTagCompound());
		if (!data.getString("mode").equals("SAVE")) return;
		BlockPos startPos = tile.getPos().add(data.getInteger("posX"), data.getInteger("posY"), data.getInteger("posZ"));
		BlockPos endPos = startPos.add(data.getInteger("sizeX"), data.getInteger("sizeY"), data.getInteger("sizeZ"));
		File file = new File(structuresPath, data.getString("name") + ".nbt");

		InputStream inputStream = null;
		NBTTagCompound fileNBT;

		try {
			inputStream = new FileInputStream(file);
			fileNBT = CompressedStreamTools.readCompressed(inputStream);
		} catch (FileNotFoundException e) {
			player.sendMessage(new TextComponentString("File \"" + data.getString("name") + ".nbt\"" + " not found!"));
			return;
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return;
        } finally {
			IOUtils.closeQuietly(inputStream);
		}

		PosMc3IM blockPos = PosMc3IM.obtain();
		BlockPos minPos = new BlockPos(Math.min(startPos.getX(), endPos.getX()), Math.min(startPos.getY(), endPos.getY()), Math.min(startPos.getZ(), endPos.getZ()));
		BlockPos maxPos = new BlockPos(Math.max(startPos.getX(), endPos.getX()), Math.max(startPos.getY(), endPos.getY()), Math.max(startPos.getZ(), endPos.getZ()));
		NBTTagList lightData = new NBTTagList();

		for (BlockPos.MutableBlockPos iteratePos : BlockPos.getAllInBoxMutable(minPos, maxPos)) {
			blockPos.set(iteratePos).subtract(minPos);
			int light = world.getLightFor(EnumSkyBlock.BLOCK, iteratePos);
			if (light == 0) continue;
			NBTTagCompound d = new NBTTagCompound();
			NBTTagList pos = new NBTTagList();
			pos.appendTag(new NBTTagInt(blockPos.getX()));
			pos.appendTag(new NBTTagInt(blockPos.getY()));
			pos.appendTag(new NBTTagInt(blockPos.getZ()));
			d.setTag("pos", pos);
			d.setInteger("value", light);
			lightData.appendTag(d);
		}

        PosMc3IM.release(blockPos);

		fileNBT.setTag("light", lightData);

		OutputStream outputStream = null;

		try {
			outputStream = new FileOutputStream(file);
			CompressedStreamTools.writeCompressed(fileNBT, outputStream);
		} catch (FileNotFoundException e) {
			player.sendMessage(new TextComponentString("File \"" + data.getString("name") + ".nbt\"" + " not found!"));
		} catch (IOException e) {
			e.printStackTrace(System.err);
        } finally {
			IOUtils.closeQuietly(outputStream);
		}

		player.sendMessage(new TextComponentString("Light map successfully load to file \"" + data.getString("name") + ".nbt\""));
	}
}
