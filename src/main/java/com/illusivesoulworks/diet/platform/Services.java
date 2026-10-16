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

package com.illusivesoulworks.diet.platform;

import com.illusivesoulworks.diet.platform.services.ICapabilityService;
import com.illusivesoulworks.diet.platform.services.IEventService;
import com.illusivesoulworks.diet.platform.services.INetworkService;
import com.illusivesoulworks.diet.platform.services.IPlatformService;
import com.illusivesoulworks.diet.platform.services.IRegistryService;
import com.illusivesoulworks.diet.platform.services.*;

public class Services {

  //? if neoforge {
  public static final IPlatformService PLATFORM = new ForgePlatformService();
  public static final ICapabilityService CAPABILITY = new ForgeCapabilityService();
  public static final IRegistryService REGISTRY = new ForgeRegistryService();
  public static final IEventService EVENT = new ForgeEventService();
  public static final INetworkService NETWORK = new ForgeNetworkService();
  //?} else {
  /*public static final IPlatformService PLATFORM = new FabricPlatformService();
  public static final ICapabilityService CAPABILITY = new FabricCapabilityService();
  public static final IRegistryService REGISTRY = new FabricRegistryService();
  public static final IEventService EVENT = new FabricEventService();
  public static final INetworkService NETWORK = new FabricNetworkService();
  *///?}
}
