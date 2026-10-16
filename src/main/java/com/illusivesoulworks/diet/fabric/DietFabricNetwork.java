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
/*import com.illusivesoulworks.diet.DietConstants;
import com.illusivesoulworks.diet.common.network.server.SPacketActivate;
import com.illusivesoulworks.diet.common.network.server.SPacketDiet;
import com.illusivesoulworks.diet.common.network.server.SPacketEaten;
import com.illusivesoulworks.diet.common.network.server.SPacketEffectsInfo;
import com.illusivesoulworks.diet.common.network.server.SPacketGroups;
import com.illusivesoulworks.diet.common.network.server.SPacketSuites;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public final class DietFabricNetwork {

  private static final Map<Class<?>, CustomPacketPayload.Type<Packet<?>>> TYPES = new HashMap<>();
  public static final List<Handler<?>> HANDLERS = new ArrayList<>();

  public record Packet<M>(CustomPacketPayload.Type<Packet<?>> type, M message)
      implements CustomPacketPayload {
  }

  public record Handler<M>(CustomPacketPayload.Type<Packet<?>> type, Consumer<M> handler) {
  }

  private DietFabricNetwork() {
  }

  public static void register() {
    register("diet", SPacketDiet.class, SPacketDiet::encode, SPacketDiet::decode, SPacketDiet::handle);
    register("effects_info", SPacketEffectsInfo.class, SPacketEffectsInfo::encode,
        SPacketEffectsInfo::decode, SPacketEffectsInfo::handle);
    register("activate", SPacketActivate.class, SPacketActivate::encode, SPacketActivate::decode,
        SPacketActivate::handle);
    register("eaten", SPacketEaten.class, SPacketEaten::encode, SPacketEaten::decode,
        SPacketEaten::handle);
    register("groups", SPacketGroups.class, SPacketGroups::encode, SPacketGroups::decode,
        SPacketGroups::handle);
    register("suites", SPacketSuites.class, SPacketSuites::encode, SPacketSuites::decode,
        SPacketSuites::handle);
  }

  @SuppressWarnings("unchecked")
  private static <M> void register(String name, Class<M> clazz, BiConsumer<M, FriendlyByteBuf> encoder,
                                   Function<FriendlyByteBuf, M> decoder, Consumer<M> handler) {
    CustomPacketPayload.Type<Packet<?>> type = new CustomPacketPayload.Type<>(
        ResourceLocation.fromNamespaceAndPath(DietConstants.MOD_ID, name));
    TYPES.put(clazz, type);
    StreamCodec<RegistryFriendlyByteBuf, Packet<?>> codec = StreamCodec.of(
        (buf, packet) -> encoder.accept((M) packet.message(), buf),
        buf -> new Packet<>(type, decoder.apply(buf)));
    PayloadTypeRegistry.playS2C().register(type, codec);
    HANDLERS.add(new Handler<>(type, handler));
  }

  public static void send(ServerPlayer player, Object message) {
    CustomPacketPayload.Type<Packet<?>> type = TYPES.get(message.getClass());

    if (type != null && ServerPlayNetworking.canSend(player, type)) {
      ServerPlayNetworking.send(player, new Packet<>(type, message));
    }
  }
}
*///?}
