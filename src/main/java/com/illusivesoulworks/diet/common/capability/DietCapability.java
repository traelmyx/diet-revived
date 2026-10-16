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

import com.illusivesoulworks.diet.DietConstants;
import com.illusivesoulworks.diet.api.type.IDietTracker;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class DietCapability {

  public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DietConstants.MOD_ID);

  public static final Supplier<AttachmentType<PlayerDietTracker>> DIET_TRACKER =
      ATTACHMENTS.register("diet_tracker", () -> AttachmentType.builder(
              holder -> new PlayerDietTracker((Player) holder))
          .serialize(new IAttachmentSerializer<CompoundTag, PlayerDietTracker>() {
            @Override
            public PlayerDietTracker read(IAttachmentHolder holder, CompoundTag tag,
                                          HolderLookup.Provider provider) {
              PlayerDietTracker tracker = new PlayerDietTracker((Player) holder);
              tracker.load(tag);
              return tracker;
            }

            @Override
            public CompoundTag write(PlayerDietTracker tracker, HolderLookup.Provider provider) {
              CompoundTag tag = new CompoundTag();
              tracker.save(tag);
              return tag;
            }
          }).build());

  public static Optional<IDietTracker> get(final Player player) {
    return Optional.of(player.getData(DIET_TRACKER));
  }
}
