package net.nimbu.scabbards.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.Level;
import net.nimbu.scabbards.component.ModDataComponents;
import net.nimbu.scabbards.component.StoredItem;
import net.nimbu.scabbards.config.ScabbardItemCache;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.Optional;

public class ScabbardItem extends Item implements ICurioItem {

    private final Class<? extends Item> item_type;

    public ScabbardItem(Properties properties, Class<? extends Item> item_type) {
        super(properties);
        this.item_type=item_type;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM);

        if (storedItem==null) {return;}

        ItemStack storedItemStack = storedItem.stack().copy();
        storedItemStack.getItem().inventoryTick(storedItemStack, level, entity, slotId, isSelected); //also tick the stored item... this might be a bad idea
        stack.set(ModDataComponents.STORED_ITEM, new StoredItem(storedItemStack));
    }

    public static boolean matches(ItemStack stack, Class<? extends Item> type) {
        if (stack == null || stack.isEmpty() || type == null) {
            return false;
        }

        boolean defaultItem = type.isInstance(stack.getItem());
        if (type == SwordItem.class){
            return defaultItem || ScabbardItemCache.isScabbardExtra(stack.getItem());
        }

        return defaultItem || ScabbardItemCache.isWeaponHolsterExtra(stack.getItem());
    }

    public void drawOrSheathSword(ServerPlayer player, ItemStack scabbardItem){
        Inventory playerInventory = player.getInventory();
        int selectedSlot=playerInventory.selected;
        ItemStack heldItem = playerInventory.getItem(selectedSlot).copy(); //store outside of inventory to stop things breaking

        if (scabbardItem.get(ModDataComponents.STORED_ITEM)!=null){ //if sword in scabbard:

            //Remove item from current slot, placing it into inventory
            playerInventory.setItem(selectedSlot, scabbardItem.get(ModDataComponents.STORED_ITEM).stack());
            playerInventory.add(heldItem);
            player.drop(heldItem, false);
            scabbardItem.remove(ModDataComponents.STORED_ITEM);

            player.level().playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BUNDLE_REMOVE_ONE,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

        }
        else //if the scabbard is empty, we add any held sword
        {
            if(matches(heldItem, item_type)){  //.is(this.item_type)){
                scabbardItem.set(ModDataComponents.STORED_ITEM, new StoredItem(heldItem.copy()));
                playerInventory.setItem(selectedSlot, ItemStack.EMPTY);

                player.level().playSound(
                        null,
                        player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BUNDLE_INSERT,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                );
            }
        }
    }

    //Right-clicking with item on another inventory item
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) { //if not an item of count one, clicked with right click
            return false;
        }

        StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM); //get the sword in the scabbard
        ItemStack target = slot.getItem(); //get the item in the targeted inventory slot

        if (storedItem == null) { //if no sword in scabbard

            if (!(matches(target, item_type))) {
                return false;
            }
            ItemStack taken = slot.safeTake(1, 1, player);
            if (!taken.isEmpty()) {
                stack.set(ModDataComponents.STORED_ITEM, new StoredItem(taken.copy()));
                this.playInsertSound(player);
            }
            return true;
        }

        //Otherwise, if there is an item in the scabbard
        ItemStack storedItemStack = storedItem.stack();

        if (storedItemStack.isEmpty() || !target.isEmpty()) {
            return false;
        }

        ItemStack remainder = slot.safeInsert(storedItemStack.copy());
        if (remainder.isEmpty()) {
            stack.remove(ModDataComponents.STORED_ITEM);
        } else {
            stack.set(ModDataComponents.STORED_ITEM, new StoredItem(remainder.copy()));
        }

        this.playRemoveSound(player);
        return true;
    }

    //Right-clicking with other inventory item on this
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

        if(!(matches(other, item_type)) || other.getCount() != 1 || action != ClickAction.SECONDARY || stack.get(ModDataComponents.STORED_ITEM)!=null){ //if not a sword, who cares
            return false;
        }

        stack.set(ModDataComponents.STORED_ITEM, new StoredItem(other.copy()));
        access.set(ItemStack.EMPTY);

        this.playInsertSound(player);
        return true;
    }

    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return true;
    }

//    private static boolean dropContents(ItemStack stack, Player player) {
//        StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM);
//        if (storedItem != null && !storedItem.stack().isEmpty()) {
//            ItemStack itemStack = storedItem.stack();
//            stack.remove(ModDataComponents.STORED_ITEM);
//            if (player instanceof ServerPlayer) {
//                player.drop(itemStack, true);
//            }
//            return true;
//        } else {
//            stack.set(ModDataComponents.STORED_ITEM,
//                    new StoredItem(Items.IRON_SWORD.getDefaultInstance()));
//            return false;
//        }
//    }

    //Needs work
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP) ? Optional.ofNullable((BundleContents)stack.get(DataComponents.BUNDLE_CONTENTS)).map(BundleTooltip::new) : Optional.empty();
    }

    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {


        StoredItem storedItem;
        if ((storedItem = stack.get(ModDataComponents.STORED_ITEM))!=null) {
            ItemStack storedItemStack = storedItem.stack();
            List<Component> storedItemTooltip = storedItemStack.getTooltipLines(context, null, tooltipFlag);
            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.scabbards.stored_sword").withStyle(ChatFormatting.GOLD));
            for (Component component : storedItemTooltip) {
                tooltipComponents.add(Component.literal("  ")
                        .append(component));
            }
        }
    }

//    public void onDestroyed(ItemEntity itemEntity) {
//        BundleContents bundlecontents = (BundleContents)itemEntity.getItem().get(DataComponents.BUNDLE_CONTENTS);
//        if (bundlecontents != null) {
//            itemEntity.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
//            ItemUtils.onContainerDestroyed(itemEntity, bundlecontents.itemsCopy());
//        }
//    }

    private void playRemoveSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

//    private void playDropContentsSound(Entity entity) {
//        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
//    }
}
