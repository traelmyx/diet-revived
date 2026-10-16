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
/*import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.fabric.FabricDietData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class FabricMixinPlayer {

  @Inject(at = @At("RETURN"), method = "createAttributes")
  private static void diet$createAttributes(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
    cir.getReturnValue().add(DietApi.getInstance().getNaturalRegeneration());
  }

  @Inject(at = @At("TAIL"), method = "tick")
  private void diet$tick(CallbackInfo ci) {
    FabricDietData.get((Player) (Object) this).tick();
  }
}
*///?}
