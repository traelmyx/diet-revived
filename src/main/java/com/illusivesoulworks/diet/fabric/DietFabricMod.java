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

package com.illusivesoulworks.diet.fabric;

//? if fabric {
/*import com.illusivesoulworks.diet.DietCommonMod;
import com.illusivesoulworks.diet.DietConstants;
import com.illusivesoulworks.diet.common.DietApiImpl;
import com.illusivesoulworks.diet.common.DietEvents;
import com.illusivesoulworks.diet.common.command.DietCommand;
import com.illusivesoulworks.diet.common.command.DietGroupArgument;
import com.illusivesoulworks.diet.common.config.DietConfig;
import com.illusivesoulworks.diet.common.data.group.DietGroups;
import com.illusivesoulworks.diet.common.data.suite.DietSuites;
import com.illusivesoulworks.diet.common.integration.IntegrationManager;
import com.illusivesoulworks.diet.common.util.DietValueGenerator;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeModConfigEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.fml.config.ModConfig;

public class DietFabricMod implements ModInitializer {

  @Override
  public void onInitialize() {
    naturalRegeneration();
    NeoForgeConfigRegistry.INSTANCE.register(DietConstants.MOD_ID, ModConfig.Type.CLIENT, DietConfig.CLIENT_SPEC);
    NeoForgeConfigRegistry.INSTANCE.register(DietConstants.MOD_ID, ModConfig.Type.SERVER, DietConfig.SERVER_SPEC);
    NeoForgeModConfigEvents.loading(DietConstants.MOD_ID).register(config -> resetOverrides(config));
    NeoForgeModConfigEvents.reloading(DietConstants.MOD_ID).register(config -> resetOverrides(config));
    FabricDietData.init();
    DietFabricNetwork.register();
    IntegrationManager.setup();

    ResourceManagerHelper resources = ResourceManagerHelper.get(PackType.SERVER_DATA);
    resources.registerReloadListener((IdentifiableResourceReloadListener) DietGroups.SERVER);
    resources.registerReloadListener((IdentifiableResourceReloadListener) DietSuites.SERVER);
    ArgumentTypeRegistry.registerArgumentType(DietCommonMod.resource("modid"), ModIdArgument.class,
        SingletonArgumentInfo.contextFree(ModIdArgument::new));
    ArgumentTypeRegistry.registerArgumentType(DietCommonMod.resource("group"),
        DietGroupArgument.class, SingletonArgumentInfo.contextFree(DietGroupArgument::group));
    CommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess, environment) -> DietCommand.register(dispatcher));

    ServerLifecycleEvents.SERVER_STARTED.register(DietValueGenerator::reload);
    ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(
        (server, resourceManager, success) -> DietEvents.syncDatapack(server));
    ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register(
        (player, joined) -> DietEvents.syncDatapack(player));
    ServerPlayConnectionEvents.JOIN.register(
        (handler, sender, server) -> FabricDietData.get(handler.player).sync());
    ServerPlayerEvents.COPY_FROM.register(
        (oldPlayer, newPlayer, alive) -> FabricDietData.get(newPlayer).copy(oldPlayer, !alive));
    ServerPlayerEvents.AFTER_RESPAWN.register(
        (oldPlayer, newPlayer, alive) -> FabricDietData.get(newPlayer).sync());
    ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(
        (player, origin, destination) -> FabricDietData.get(player).sync());
  }

  // the player's attributes can be built before this mod initializes, so register on first use
  public static synchronized Holder<Attribute> naturalRegeneration() {

    if (DietApiImpl.naturalRegeneration == null) {
      DietApiImpl.naturalRegeneration = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE,
          DietCommonMod.resource("natural_regeneration"),
          new RangedAttribute("diet.naturalRegeneration", 1.0d, 0.0d, 2.0d).setSyncable(true));
    }
    return DietApiImpl.naturalRegeneration;
  }

  private static void resetOverrides(ModConfig config) {

    if (config.getSpec() == DietConfig.SERVER_SPEC) {
      DietConfig.SERVER.initializedFoodOverrides = false;
    }
  }
}
*///?}
