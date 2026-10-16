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


package com.illusivesoulworks.diet.common.network;

//? if neoforge {
import com.illusivesoulworks.diet.DietConstants;
import com.illusivesoulworks.diet.common.network.server.SPacketActivate;
import com.illusivesoulworks.diet.common.network.server.SPacketDiet;
import com.illusivesoulworks.diet.common.network.server.SPacketEaten;
import com.illusivesoulworks.diet.common.network.server.SPacketEffectsInfo;
import com.illusivesoulworks.diet.common.network.server.SPacketGroups;
import com.illusivesoulworks.diet.common.network.server.SPacketSuites;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class DietForgeNetwork {

  private static final String PTC_VERSION = "1";
  private static final Map<Class<?>, CustomPacketPayload.Type<Packet<?>>> TYPES = new HashMap<>();

  public record Packet<M>(CustomPacketPayload.Type<Packet<?>> type, M message)
      implements CustomPacketPayload {
  }

  public static void register(final RegisterPayloadHandlersEvent evt) {
    PayloadRegistrar registrar = evt.registrar(DietConstants.MOD_ID).versioned(PTC_VERSION).optional();
    register(registrar, "diet", SPacketDiet.class, SPacketDiet::encode, SPacketDiet::decode,
        SPacketDiet::handle);
    register(registrar, "effects_info", SPacketEffectsInfo.class, SPacketEffectsInfo::encode,
        SPacketEffectsInfo::decode, SPacketEffectsInfo::handle);
    register(registrar, "activate", SPacketActivate.class, SPacketActivate::encode,
        SPacketActivate::decode, SPacketActivate::handle);
    register(registrar, "eaten", SPacketEaten.class, SPacketEaten::encode, SPacketEaten::decode,
        SPacketEaten::handle);
    register(registrar, "groups", SPacketGroups.class, SPacketGroups::encode,
        SPacketGroups::decode, SPacketGroups::handle);
    register(registrar, "suites", SPacketSuites.class, SPacketSuites::encode,
        SPacketSuites::decode, SPacketSuites::handle);
  }

  @SuppressWarnings("unchecked")
  private static <M> void register(PayloadRegistrar registrar, String name, Class<M> clazz,
                                   BiConsumer<M, FriendlyByteBuf> encoder,
                                   Function<FriendlyByteBuf, M> decoder, Consumer<M> handler) {
    CustomPacketPayload.Type<Packet<?>> type = new CustomPacketPayload.Type<>(
        ResourceLocation.fromNamespaceAndPath(DietConstants.MOD_ID, name));
    TYPES.put(clazz, type);
    StreamCodec<RegistryFriendlyByteBuf, Packet<?>> codec = StreamCodec.of(
        (buf, packet) -> encoder.accept((M) packet.message(), buf),
        buf -> new Packet<>(type, decoder.apply(buf)));
    registrar.playToClient(type, codec,
        (packet, context) -> context.enqueueWork(() -> handler.accept((M) packet.message())));
  }

  public static void send(ServerPlayer player, Object message) {
    CustomPacketPayload.Type<Packet<?>> type = TYPES.get(message.getClass());

    if (type != null && player.connection != null && player.connection.hasChannel(type)) {
      PacketDistributor.sendToPlayer(player, new Packet<>(type, message));
    }
  }
}
//?}
