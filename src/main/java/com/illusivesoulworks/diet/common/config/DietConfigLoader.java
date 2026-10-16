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


package com.illusivesoulworks.diet.common.config;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

public class DietConfigLoader {

  public static void setup(ModContainer container, IEventBus eventBus) {
    container.registerConfig(ModConfig.Type.CLIENT, DietConfig.CLIENT_SPEC);
    container.registerConfig(ModConfig.Type.SERVER, DietConfig.SERVER_SPEC);
    eventBus.addListener((ModConfigEvent evt) -> {

      if (evt.getConfig().getSpec() == DietConfig.SERVER_SPEC) {
        DietConfig.SERVER.initializedFoodOverrides = false;
      }
    });
  }
}
