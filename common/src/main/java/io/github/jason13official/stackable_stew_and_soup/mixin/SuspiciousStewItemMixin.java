package io.github.jason13official.stackable_stew_and_soup.mixin;

import io.github.jason13official.stackable_stew_and_soup.impl.common.ModConfig;
import io.github.jason13official.stackable_stew_and_soup.impl.common.util.FinishUsingItemLogic;
import io.github.jason13official.stackable_stew_and_soup.platform.Services;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SuspiciousStewItem.class)
public class SuspiciousStewItemMixin {

  static {
    if (!ModConfig.early) {

      ModConfig.load(Services.PLATFORM.getConfigDirectory());
      ModConfig.early = true;
    }
  }

  @Inject(at = @At("TAIL"), method = "finishUsingItem", cancellable = true)
  private void ssas$finishUsingItem(ItemStack stack, Level level, LivingEntity living, CallbackInfoReturnable<ItemStack> cir) {

    ItemStack heldStack = stack.copy();
    heldStack.shrink(1);
    ItemStack returned = cir.getReturnValue();
    if (!level.isClientSide() && !ItemStack.matches(heldStack, returned)) {
      FinishUsingItemLogic.apply(stack, living, cir);
    }
  }
}
