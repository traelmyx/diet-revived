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


package com.illusivesoulworks.diet;

import com.illusivesoulworks.diet.client.DietKeys;
import com.illusivesoulworks.diet.common.DietEvents;
import com.illusivesoulworks.diet.common.capability.DietCapability;
import com.illusivesoulworks.diet.common.command.DietCommand;
import com.illusivesoulworks.diet.common.command.DietGroupArgument;
import com.illusivesoulworks.diet.common.config.DietConfigLoader;
import com.illusivesoulworks.diet.common.data.group.DietGroups;
import com.illusivesoulworks.diet.common.data.suite.DietSuites;
import com.illusivesoulworks.diet.common.integration.IntegrationManager;
import com.illusivesoulworks.diet.common.network.DietForgeNetwork;
import com.illusivesoulworks.diet.common.util.DietOverride;
import com.illusivesoulworks.diet.common.util.DietValueGenerator;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(DietConstants.MOD_ID)
public class DietForgeMod {

  private static final DeferredRegister<Attribute> ATTRIBUTES =
      DeferredRegister.create(Registries.ATTRIBUTE, DietConstants.MOD_ID);
  private static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES =
      DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, DietConstants.MOD_ID);

  public static final DeferredHolder<Attribute, Attribute> NATURAL_REGEN =
      ATTRIBUTES.register("natural_regeneration",
          () -> new RangedAttribute("diet.naturalRegeneration", 1.0d, 0.0d, 2.0d).setSyncable(true));

  static {
    ARGUMENT_TYPES.register("group", () -> ArgumentTypeInfos.registerByClass(DietGroupArgument.class,
        SingletonArgumentInfo.contextFree(DietGroupArgument::group)));
  }

  public DietForgeMod(IEventBus eventBus, ModContainer container) {
    eventBus.addListener(this::setup);
    eventBus.addListener(this::process);
    eventBus.addListener(this::modifyAttributes);
    eventBus.addListener(DietForgeNetwork::register);
    DietConfigLoader.setup(container, eventBus);
    ATTRIBUTES.register(eventBus);
    ARGUMENT_TYPES.register(eventBus);
    DietCapability.ATTACHMENTS.register(eventBus);
    NeoForge.EVENT_BUS.addListener(this::serverStarting);
    NeoForge.EVENT_BUS.addListener(this::onDatapackSync);
    NeoForge.EVENT_BUS.addListener(this::setupCommands);
    NeoForge.EVENT_BUS.addListener(this::addReloaders);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    IntegrationManager.setup();
  }

  private void addReloaders(final AddReloadListenerEvent evt) {
    evt.addListener(DietGroups.SERVER);
    evt.addListener(DietSuites.SERVER);
  }

  private void process(final InterModProcessEvent evt) {
    DietOverride.process(evt.getIMCStream());
  }

  private void modifyAttributes(final EntityAttributeModificationEvent evt) {
    evt.add(EntityType.PLAYER, NATURAL_REGEN);
  }

  private void serverStarting(final ServerStartingEvent evt) {
    DietValueGenerator.reload(evt.getServer());
  }

  private void onDatapackSync(final OnDatapackSyncEvent evt) {

    if (evt.getPlayer() == null) {
      DietEvents.syncDatapack(evt.getPlayerList().getServer());
    } else {
      DietEvents.syncDatapack(evt.getPlayer());
    }
  }

  private void setupCommands(final RegisterCommandsEvent evt) {
    DietCommand.register(evt.getDispatcher());
  }

  @EventBusSubscriber(modid = DietConstants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
  public static class ClientModEvents {

    @SubscribeEvent
    public static void registerKeys(final RegisterKeyMappingsEvent evt) {
      evt.register(DietKeys.get());
    }
  }
}
