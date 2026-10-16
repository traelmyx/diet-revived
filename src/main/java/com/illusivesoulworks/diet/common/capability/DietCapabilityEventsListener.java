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


package com.illusivesoulworks.diet.common.capability;

//? if neoforge {
import com.illusivesoulworks.diet.DietConstants;
import com.illusivesoulworks.diet.api.type.IDietTracker;
import com.illusivesoulworks.diet.common.DietEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = DietConstants.MOD_ID)
public class DietCapabilityEventsListener {

  @SubscribeEvent
  public static void playerClone(final PlayerEvent.Clone evt) {

    if (evt.getEntity() instanceof ServerPlayer player) {
      DietCapability.get(player).ifPresent(tracker -> tracker.copy(evt.getOriginal(), evt.isWasDeath()));
    }
  }

  @SubscribeEvent
  public static void playerRespawned(final PlayerEvent.PlayerRespawnEvent evt) {

    if (evt.getEntity() instanceof ServerPlayer player) {
      DietCapability.get(player).ifPresent(IDietTracker::sync);
    }
  }

  @SubscribeEvent
  public static void playerLoggedIn(final PlayerEvent.PlayerLoggedInEvent evt) {

    if (evt.getEntity() instanceof ServerPlayer player) {
      // the datapack sync on join runs before the play connection exists, so send groups and suites again here
      DietEvents.syncDatapack(player);
      DietCapability.get(player).ifPresent(IDietTracker::sync);
    }
  }

  @SubscribeEvent
  public static void playerDimensionTravel(final PlayerEvent.PlayerChangedDimensionEvent evt) {

    if (evt.getEntity() instanceof ServerPlayer player) {
      DietCapability.get(player).ifPresent(IDietTracker::sync);
    }
  }

  @SubscribeEvent
  public static void playerTick(final PlayerTickEvent.Post evt) {

    if (evt.getEntity() instanceof ServerPlayer player) {
      DietCapability.get(player).ifPresent(IDietTracker::tick);
    }
  }

  @SubscribeEvent(priority = EventPriority.LOW)
  public static void finishItemUse(final LivingEntityUseItemEvent.Finish evt) {
    ItemStack stack = evt.getItem();
    LivingEntity livingEntity = evt.getEntity();

    if (!livingEntity.level().isClientSide() && livingEntity instanceof Player player) {
      FoodProperties food = stack.getFoodProperties(livingEntity);

      if (food != null) {
        DietCapability.get(player).ifPresent(diet -> diet.consume(stack));
      }
    }
  }
}
//?}
