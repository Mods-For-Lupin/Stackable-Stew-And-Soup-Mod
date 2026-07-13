package io.github.jason13official.stackable_stew_and_soup.impl.common.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class FinishUsingItemLogic {

  public static void apply(ItemStack heldItemStack, LivingEntity itemHolder, CallbackInfoReturnable<ItemStack> cir) {

    // if we would return a single bowl from a heldItemStack that is Suspicious Stew with more than one stew (vanilla behavior)
    ItemStack returned = cir.getReturnValue();
    if ((returned.is(Items.BOWL) && returned.getCount() == 1) && (heldItemStack.getMaxStackSize() > 1 && heldItemStack.getCount() > 0)) {

      // try to give an empty bowl to the player
      boolean givenToPlayer = false;
      if (itemHolder instanceof ServerPlayer player) {

        // either add directly to inventory, or drop at player location
        if (!player.getInventory().add(returned)) {
          player.drop(returned, false);
        }

        givenToPlayer = true;
      }

      // if we couldn't give it to the player, drop an empty bowl from the entity that used the item
      if (!givenToPlayer) {
        vulcan$drop(itemHolder, returned, false);
      }

      // shrink the held item stack (bc it was used), and set the actual return value to that
      // heldItemStack.shrink(1);
      cir.setReturnValue(heldItemStack);
    }
  }

  @SuppressWarnings("all") // return value is never used, except for advancements (unrelated to this)
  public static ItemEntity vulcan$drop(LivingEntity living, ItemStack itemStack, boolean includeThrowerName) {
    return vulcan$drop(living, itemStack, false, includeThrowerName);
  }

  /// copying vanilla player logic
  public static ItemEntity vulcan$drop(LivingEntity living, ItemStack droppedItem, boolean dropAround, boolean includeThrowerName) {
    if (droppedItem.isEmpty()) {
      return null;
    } else {
      if (living.level().isClientSide) {
        living.swing(InteractionHand.MAIN_HAND);
      }

      double d0 = living.getEyeY() - (double)0.3F;
      ItemEntity itementity = new ItemEntity(living.level(), living.getX(), d0, living.getZ(), droppedItem);
      itementity.setPickUpDelay(40);
      if (includeThrowerName) {
        itementity.setThrower(living.getUUID());
      }

      if (dropAround) {
        float f = living.getRandom().nextFloat() * 0.5F;
        float f1 = living.getRandom().nextFloat() * ((float)Math.PI * 2F);
        itementity.setDeltaMovement((double)(-Mth.sin(f1) * f), (double)0.2F, (double)(Mth.cos(f1) * f));
      } else {
        float f7 = 0.3F;
        float f8 = Mth.sin(living.getXRot() * ((float)Math.PI / 180F));
        float f2 = Mth.cos(living.getXRot() * ((float)Math.PI / 180F));
        float f3 = Mth.sin(living.getYRot() * ((float)Math.PI / 180F));
        float f4 = Mth.cos(living.getYRot() * ((float)Math.PI / 180F));
        float f5 = living.getRandom().nextFloat() * ((float)Math.PI * 2F);
        float f6 = 0.02F * living.getRandom().nextFloat();
        itementity.setDeltaMovement((double)(-f3 * f2 * 0.3F) + Math.cos((double)f5) * (double)f6, (double)(-f8 * 0.3F + 0.1F + (living.getRandom().nextFloat() - living.getRandom().nextFloat()) * 0.1F), (double)(f4 * f2 * 0.3F) + Math.sin((double)f5) * (double)f6);
      }

      return itementity;
    }
  }
}
