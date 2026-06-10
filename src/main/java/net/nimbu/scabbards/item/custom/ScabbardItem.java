package net.nimbu.scabbards.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
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
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class ScabbardItem extends Item implements Equipable{

    public ScabbardItem(Properties properties) {
        super(properties);
    }


    //Right-clicking with item on another inventory item
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) { //if not an item of count one, clicked with right click
            return false;
        }

        StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM); //get the sword in the scabbard
        ItemStack target = slot.getItem(); //get the item in the targeted inventory slot

        if (storedItem == null) { //if no sword in scabbard

            if (!target.is(ItemTags.SWORDS)) {
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
        //TODO: sword swapping
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

        this.playRemoveOneSound(player);
        return true;
    }

    //Right-clicking with other inventory item on this
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {

        if(!other.is(ItemTags.SWORDS) || other.getCount() != 1 || action != ClickAction.SECONDARY || stack.get(ModDataComponents.STORED_ITEM)!=null){ //if not a sword, who cares
            return false;
        }

        stack.set(ModDataComponents.STORED_ITEM, new StoredItem(other.copy()));
        access.set(ItemStack.EMPTY);

        this.playInsertSound(player);
        return true;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        return this.swapWithEquipmentSlot(this, level, player, usedHand);
    }

    private static boolean dropContents(ItemStack stack, Player player) {
        StoredItem storedItem = stack.get(ModDataComponents.STORED_ITEM);
        if (storedItem != null && !storedItem.stack().isEmpty()) {
            ItemStack itemStack = storedItem.stack();
            stack.remove(ModDataComponents.STORED_ITEM);
            if (player instanceof ServerPlayer) {
                player.drop(itemStack, true);
            }
            return true;
        } else {
            stack.set(ModDataComponents.STORED_ITEM,
                    new StoredItem(Items.IRON_SWORD.getDefaultInstance()));
            return false;
        }
    }

    //Needs work
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP) ? Optional.ofNullable((BundleContents)stack.get(DataComponents.BUNDLE_CONTENTS)).map(BundleTooltip::new) : Optional.empty();
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
//        BundleContents bundlecontents = (BundleContents)stack.get(DataComponents.BUNDLE_CONTENTS);
//        if (bundlecontents != null) {
//            int i = Mth.mulAndTruncate(bundlecontents.weight(), 64);
//            tooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", new Object[]{i, 64}).withStyle(ChatFormatting.GRAY));
//        }
    }

    public void onDestroyed(ItemEntity itemEntity) {
//        BundleContents bundlecontents = (BundleContents)itemEntity.getItem().get(DataComponents.BUNDLE_CONTENTS);
//        if (bundlecontents != null) {
//            itemEntity.getItem().set(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
//            ItemUtils.onContainerDestroyed(itemEntity, bundlecontents.itemsCopy());
//        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.LEGS;
    }
}
