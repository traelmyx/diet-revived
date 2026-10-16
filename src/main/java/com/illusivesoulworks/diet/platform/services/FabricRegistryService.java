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

package com.illusivesoulworks.diet.platform.services;

//? if fabric {
/*import net.minecraft.core.Holder;
import com.illusivesoulworks.diet.DietCommonMod;
import com.illusivesoulworks.diet.fabric.ModIdArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import org.apache.commons.lang3.tuple.Triple;

public class FabricRegistryService implements IRegistryService {

  private static final TagKey<Item> INGREDIENTS =
      TagKey.create(Registries.ITEM, DietCommonMod.resource("ingredients"));
  private static final TagKey<Item> SPECIAL_FOOD =
      TagKey.create(Registries.ITEM, DietCommonMod.resource("special_food"));

  @Override
  public Optional<Item> getItem(ResourceLocation resourceLocation) {
    return BuiltInRegistries.ITEM.getOptional(resourceLocation);
  }

  @Override
  public ResourceLocation getItemKey(Item item) {
    return BuiltInRegistries.ITEM.getKey(item);
  }

  @Override
  public Optional<Holder<Attribute>> getAttribute(ResourceLocation resourceLocation) {
    return BuiltInRegistries.ATTRIBUTE.getHolder(resourceLocation).map(h -> h);
  }

  @Override
  public ResourceLocation getAttributeKey(Holder<Attribute> attribute) {
    return attribute.unwrapKey().map(k -> k.location()).orElse(null);
  }

  @Override
  public Optional<Holder<MobEffect>> getStatusEffect(ResourceLocation resourceLocation) {
    return BuiltInRegistries.MOB_EFFECT.getHolder(resourceLocation).map(h -> h);
  }

  @Override
  public ResourceLocation getStatusEffectKey(Holder<MobEffect> effect) {
    return effect.unwrapKey().map(k -> k.location()).orElse(null);
  }

  @Override
  public FoodProperties getFoodProperties(ItemStack stack, Player player) {
    return stack.get(net.minecraft.core.component.DataComponents.FOOD);
  }

  @Override
  public BiFunction<Player, ItemStack, Triple<List<ItemStack>, Integer, Float>> getOverride(
      Item item) {
    return null;
  }

  @Override
  public boolean isIngredient(ItemStack stack) {
    return stack.is(INGREDIENTS);
  }

  @Override
  public boolean isSpecialFood(ItemStack stack) {
    return stack.is(SPECIAL_FOOD);
  }

  @Override
  public Collection<Item> getItems() {
    return BuiltInRegistries.ITEM.stream().toList();
  }

  @Override
  public Collection<Item> getTagItems(TagKey<Item> tagKey) {
    return BuiltInRegistries.ITEM.getTag(tagKey).map(set -> set.stream().map(h -> h.value()).toList()).orElse(List.of());
  }

  @Override
  public ItemStack getPickStack(BlockState state, BlockHitResult result, Level world, BlockPos pos,
                                ServerPlayer player) {
    return state.getBlock().getCloneItemStack(world, pos, state);
  }

  @Override
  public ArgumentType<String> getModIdArgument() {
    return new ModIdArgument();
  }
}
*///?}
