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
import com.illusivesoulworks.diet.common.data.group.DietGroups;
import com.illusivesoulworks.diet.common.data.suite.DietSuites;
import java.util.Collection;
import java.util.Collections;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.minecraft.resources.ResourceLocation;

public final class FabricReloadListeners {

  private FabricReloadListeners() {
  }

  public static class Groups extends DietGroups implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
      return DietCommonMod.resource("groups");
    }

    @Override
    public Collection<ResourceLocation> getFabricDependencies() {
      return Collections.singleton(ResourceReloadListenerKeys.TAGS);
    }
  }

  public static class Suites extends DietSuites implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
      return DietCommonMod.resource("suites");
    }

    @Override
    public Collection<ResourceLocation> getFabricDependencies() {
      return Collections.singleton(DietCommonMod.resource("groups"));
    }
  }
}
*///?}
