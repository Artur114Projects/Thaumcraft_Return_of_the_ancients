package com.artur.returnoftheancients.items;

import com.artur.returnoftheancients.handlers.MiscHandler;
import com.artur.returnoftheancients.init.InitDimensions;
import com.artur.returnoftheancients.main.MainR;
import com.artur.returnoftheancients.misc.TRAConfigs;
import com.artur.returnoftheancients.referense.Referense;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemSoulBinder extends BaseItem {
    public ItemSoulBinder(String name) {
        super(name);
        setMaxStackSize(1);
        setCreativeTab(MainR.RETURN_OF_ANCIENTS_TAB);
        addPropertyOverride(new ResourceLocation("full"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null && !stack.isOnItemFrame()) {
                    return 1;
                }
                return !stack.getOrCreateSubCompound(Referense.MODID).getBoolean("isFull") ? 1 : 0;
            }
        });
    }

    @Override
    public @NotNull EnumRarity getRarity(@NotNull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World worldIn, EntityPlayer playerIn, @NotNull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        NBTTagCompound nbt = stack.getOrCreateSubCompound(Referense.MODID);
        NBTTagCompound list = nbt.getCompoundTag("players");
        if (!playerIn.isSneaking()) {
            list.setUniqueId(playerIn.getName(), playerIn.getUniqueID());
        } else {
            list.removeTag(playerIn.getName() + "Most");
            list.removeTag(playerIn.getName() + "Least");
        }
        nbt.setTag("players", list);
        nbt.setBoolean("isFull", !list.hasNoTags());
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound nbt = stack.getOrCreateSubCompound(Referense.MODID);
        if (nbt.getBoolean("isFull")) {
            if(!GuiScreen.isShiftKeyDown()) {
                NBTTagCompound list = nbt.getCompoundTag("players");
                tooltip.add(TextFormatting.YELLOW + I18n.format("item.soul_binder.info.d.3"));
                for (String text : MiscHandler.uuidKeySetToList(list.getKeySet(), TextFormatting.AQUA)) {
                    tooltip.add(TextFormatting.WHITE + "[" + text + TextFormatting.RESET + "]");
                }
                tooltip.add("");
                tooltip.add(TextFormatting.YELLOW + I18n.format("returnoftheancients.info.p1")  + " " + TextFormatting.WHITE + "[Shift]" + TextFormatting.YELLOW  + " " + I18n.format("returnoftheancients.info.p2"));
            } else {
                addInfoTranslate(tooltip);
            }
        } else {
            addInfoTranslate(tooltip);
        }
    }

    private void addInfoTranslate(List<String> tooltip) {
        tooltip.add(TextFormatting.YELLOW + I18n.format("item.soul_binder.info.s.1"));
        tooltip.add(TextFormatting.YELLOW + I18n.format("item.soul_binder.info.s.2"));
        tooltip.add(TextFormatting.YELLOW + I18n.format("item.soul_binder.info.s.3"));
    }

    public static @Nullable ItemStack foundSoulBinder(EntityPlayerMP player) {
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.getItem() instanceof ItemSoulBinder) return stack;
        }
        return null;
    }

    public static boolean isSoulBinderFull(@Nullable ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemSoulBinder)) return false;
        return stack.getOrCreateSubCompound(Referense.MODID).getBoolean("isFull");
    }


    public static Tuple<List<EntityPlayerMP>, Map<UUID, String>> compileTeam(EntityPlayerMP player) {
        List<EntityPlayerMP> players = new ArrayList<>();
        Map<UUID, String> names = new HashMap<>();
        ItemStack stack = foundSoulBinder(player);
        MinecraftServer server = player.mcServer;

        names.put(player.getUniqueID(), player.getName());
        players.add(player);

        if (isSoulBinderFull(stack)) {
            NBTTagCompound nbt = stack.getOrCreateSubCompound(Referense.MODID);
            NBTTagCompound list = nbt.getCompoundTag("players");
            List<String> keys = MiscHandler.uuidKeySetToList(list.getKeySet());

            for (String key : keys) {
                UUID id = list.getUniqueId(key);
                if (id == null) {
                    continue;
                }
                Entity entity = server.getEntityFromUuid(id);
                if (entity instanceof EntityPlayerMP) {
                    if (entity.dimension != InitDimensions.ancient_world_dim_id) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) entity;
                        if (player.dimension == playerMP.dimension) {
                            List<String> uui = MiscHandler.isPlayerUseUnresolvedItems(playerMP);
                            if (uui.isEmpty() || !TRAConfigs.PortalSettings.checkItems) {
                                players.add(playerMP);
                                names.put(id, key);
                            } else {
                                names.put(id, key + "|returnoftheancients.team_state.has_unresolved_items");
                                playerMP.sendMessage(new TextComponentTranslation(Referense.MODID + ".portal.message"));
                                playerMP.sendMessage(new TextComponentString(uui.toString()));
                            }
                        } else {
                            names.put(id, key + "|returnoftheancients.team_state.in_other_world");
                        }
                    } else {
                        names.put(id, key + "|returnoftheancients.team_state.in_ancient_world");
                    }
                } else if (entity == null) {
                    names.put(id, key + "|returnoftheancients.team_state.out_of_game");
                } else {
                    names.put(id, key + "|returnoftheancients.team_state.not_player");
                }
            }
        }

        return new Tuple<>(players, names);
    }
}
