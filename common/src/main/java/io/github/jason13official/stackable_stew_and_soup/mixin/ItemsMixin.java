package io.github.jason13official.stackable_stew_and_soup.mixin;

import io.github.jason13official.stackable_stew_and_soup.impl.common.ModConfig;
import io.github.jason13official.stackable_stew_and_soup.platform.Services;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public class ItemsMixin {

  static {
    if (!ModConfig.early) {

      ModConfig.load(Services.PLATFORM.getConfigDirectory());
      ModConfig.early = true;
    }
  }

  // redirects are targeting class initialization, also known as the static initializer

  @Redirect(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args= {"stringValue=rabbit_stew"}, ordinal = 0)), at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
  private static Item ssas$RedirectRabbitStew(Item.Properties properties) {
    return new Item((new Item.Properties()).stacksTo(ModConfig.STACKABLE_AMOUNT.getter().get()).food(Foods.RABBIT_STEW));
  }

  @Redirect(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args= {"stringValue=mushroom_stew"}, ordinal = 0)), at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
  private static Item ssas$RedirectMushroomStew(Item.Properties properties) {
    return new Item((new Item.Properties()).stacksTo(ModConfig.STACKABLE_AMOUNT.getter().get()).food(Foods.MUSHROOM_STEW));
  }

  @Redirect(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args= {"stringValue=beetroot_soup"}, ordinal = 0)), at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/Item;", ordinal = 0))
  private static Item ssas$RedirectBeetrootSoup(Item.Properties properties) {
    return new Item((new Item.Properties()).stacksTo(ModConfig.STACKABLE_AMOUNT.getter().get()).food(Foods.BEETROOT_SOUP));
  }

  @Redirect(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args= {"stringValue=suspicious_stew"}, ordinal = 0)), at = @At(value = "NEW", target = "(Lnet/minecraft/world/item/Item$Properties;)Lnet/minecraft/world/item/SuspiciousStewItem;", ordinal = 0))
  private static SuspiciousStewItem ssas$RedirectSuspiciousStew(Item.Properties properties) {
    return new SuspiciousStewItem((new Item.Properties()).stacksTo(ModConfig.STACKABLE_AMOUNT.getter().get()).food(Foods.SUSPICIOUS_STEW));
  }
}
