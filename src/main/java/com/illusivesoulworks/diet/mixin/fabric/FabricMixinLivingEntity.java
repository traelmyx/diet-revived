/*
 * Copyright (C) 2021-2023 Illusive Soulworks
 *
 * Diet is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Diet is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Diet.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.diet.mixin.fabric;

//? if fabric {
/*import com.illusivesoulworks.diet.fabric.FabricDietData;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// counts food once the player has finished eating it (NeoForge uses LivingEntityUseItemEvent.Finish)
@Mixin(LivingEntity.class)
public class FabricMixinLivingEntity {

  @Shadow
  protected ItemStack useItem;

  @Unique
  private ItemStack diet$eaten = ItemStack.EMPTY;

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"), method = "completeUsingItem")
  private void diet$beforeFinish(CallbackInfo ci) {
    this.diet$eaten = this.useItem.copy();
  }

  @Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/ItemStack;finishUsingItem(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/item/ItemStack;"), method = "completeUsingItem")
  private void diet$afterFinish(CallbackInfo ci) {
    LivingEntity self = (LivingEntity) (Object) this;

    if (!self.level().isClientSide() && self instanceof Player player && this.diet$eaten.has(DataComponents.FOOD)) {
      FabricDietData.get(player).consume(this.diet$eaten);
    }
    this.diet$eaten = ItemStack.EMPTY;
  }
}
*///?}
