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
import com.illusivesoulworks.diet.common.capability.PlayerDietTracker;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

// Player diet data as a Fabric data attachment. The tracker needs its player, so the saved tag is
// kept until the first access and loaded then.
public final class FabricDietData {

  public static final AttachmentType<FabricDietData> TYPE = AttachmentRegistry.<FabricDietData>builder()
      .persistent(CompoundTag.CODEC.xmap(FabricDietData::new, FabricDietData::save))
      .initializer(FabricDietData::new)
      .buildAndRegister(DietCommonMod.resource("diet_tracker"));

  private CompoundTag pending;
  private PlayerDietTracker tracker;

  private FabricDietData() {
  }

  private FabricDietData(CompoundTag tag) {
    this.pending = tag;
  }

  private CompoundTag save() {
    if (this.tracker != null) {
      CompoundTag tag = new CompoundTag();
      this.tracker.save(tag);
      return tag;
    }
    return this.pending != null ? this.pending : new CompoundTag();
  }

  public static PlayerDietTracker get(Player player) {
    FabricDietData data = player.getAttachedOrCreate(TYPE);

    if (data.tracker == null) {
      data.tracker = new PlayerDietTracker(player);

      if (data.pending != null) {
        data.tracker.load(data.pending);
        data.pending = null;
      }
    }
    return data.tracker;
  }

  public static void init() {
    // loads the class so the attachment type is registered
  }
}
*///?}
