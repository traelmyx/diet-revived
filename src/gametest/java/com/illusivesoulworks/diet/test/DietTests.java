package com.illusivesoulworks.diet.test;

import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.illusivesoulworks.diet.api.type.IDietTracker;
import com.illusivesoulworks.diet.platform.Services;
import com.illusivesoulworks.diet.common.capability.PlayerDietTracker;
import com.illusivesoulworks.diet.common.data.group.DietGroups;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.gametest.framework.GameTestHelper;

import java.util.Set;
import java.util.stream.Collectors;

public final class DietTests {

    private static IDietTracker fresh(ServerPlayer player) {
        IDietTracker diet = Services.CAPABILITY.get(player).orElseThrow();
        diet.initSuite();
        for (String group : diet.getValues().keySet()) {
            diet.setValue(group, 0.0f);
        }
        return diet;
    }

    public static void defaultGroupsLoaded(GameTestHelper helper) {
        Set<String> names = DietGroups.getGroups(helper.getLevel()).stream().map(IDietGroup::getName).collect(Collectors.toSet());
        helper.assertTrue(names.containsAll(Set.of("fruits", "grains", "proteins", "sugars", "vegetables")), "default groups missing: " + names);
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        Set<String> apple = DietApi.getInstance().getGroups(player, new ItemStack(Items.APPLE)).stream().map(IDietGroup::getName).collect(Collectors.toSet());
        helper.assertTrue(apple.equals(Set.of("fruits")), "apple should be fruits only, got " + apple);
        Set<String> bread = DietApi.getInstance().getGroups(player, new ItemStack(Items.BREAD)).stream().map(IDietGroup::getName).collect(Collectors.toSet());
        helper.assertTrue(bread.contains("grains"), "bread should be grains, got " + bread);
        helper.succeed();
    }

    public static void generatedFromRecipes(GameTestHelper helper) {
        // pumpkin pie has no tag of its own; Diet works out its groups from the recipe
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        Set<String> pie = DietApi.getInstance().getGroups(player, new ItemStack(Items.PUMPKIN_PIE)).stream().map(IDietGroup::getName).collect(Collectors.toSet());
        helper.assertTrue(pie.contains("sugars") && pie.contains("vegetables"), "pumpkin pie groups from recipe: " + pie);
        helper.succeed();
    }

    public static void eatingRaisesGroup(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        IDietTracker diet = fresh(player);
        ItemStack apple = new ItemStack(Items.APPLE);
        // eat it for real: hold it, start using it and let the use timer run out
        player.setItemInHand(InteractionHand.MAIN_HAND, apple.copy());
        player.startUsingItem(InteractionHand.MAIN_HAND);
        for (int i = 0; i < 40 && player.isUsingItem(); i++) {
            player.doTick();
        }
        float fruits = diet.getValue("fruits");
        helper.assertTrue(fruits > 0.0f, "fruits did not go up after an apple: " + fruits);
        helper.assertTrue(diet.getValue("proteins") == 0.0f, "proteins went up after an apple");
        helper.assertTrue(diet.getEaten().contains(Items.APPLE), "apple not recorded as eaten");
        helper.succeed();
    }

    public static void eatingCakeBlockCounts(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        player.setGameMode(GameType.SURVIVAL);
        player.getFoodData().setFoodLevel(10);
        IDietTracker diet = fresh(player);
        BlockPos cake = helper.absolutePos(new BlockPos(1, 1, 1));
        helper.getLevel().setBlockAndUpdate(cake, Blocks.CAKE.defaultBlockState());
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(cake), Direction.UP, cake, false);
        player.gameMode.useItemOn(player, helper.getLevel(), ItemStack.EMPTY, InteractionHand.MAIN_HAND, hit);
        helper.assertTrue(player.getFoodData().getFoodLevel() > 10, "cake was not eaten");
        helper.assertTrue(diet.getValue("sugars") > 0.0f, "eating cake from the block did not count: sugars " + diet.getValue("sugars"));
        helper.succeed();
    }

    public static void hungerDecaysGroups(GameTestHelper helper) throws Exception {
        // mock players always report creative, which skips decay in tick(), so call it directly
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        IDietTracker diet = fresh(player);
        diet.setValue("fruits", 0.5f);
        var decay = PlayerDietTracker.class.getDeclaredMethod("decay", int.class);
        decay.setAccessible(true);
        decay.invoke(diet, 6);
        float fruits = diet.getValue("fruits");
        helper.assertTrue(fruits < 0.5f && fruits > 0.3f, "fruits after losing 6 hunger: " + fruits);
        helper.assertTrue(diet.getValue("grains") == 0.0f, "empty group went negative or changed");
        helper.succeed();
    }

    public static void balancedDietGivesBonus(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        IDietTracker diet = fresh(player);
        double base = player.getAttributeValue(Attributes.MAX_HEALTH);
        for (String group : new String[] {"proteins", "fruits", "vegetables", "grains"}) {
            diet.setValue(group, 0.9f);
        }
        // effects are re-applied every 80 ticks
        player.tickCount = 80;
        diet.tick();
        double now = player.getAttributeValue(Attributes.MAX_HEALTH);
        helper.assertTrue(now > base, "max health bonus not applied: " + base + " -> " + now);
        for (String group : new String[] {"proteins", "fruits", "vegetables", "grains"}) {
            diet.setValue(group, 0.1f);
        }
        player.tickCount = 160;
        diet.tick();
        double after = player.getAttributeValue(Attributes.MAX_HEALTH);
        helper.assertTrue(after == base, "bonus not removed: " + after + " vs " + base);
        helper.succeed();
    }

    public static void saveAndLoad(GameTestHelper helper) {
        ServerPlayer player = helper.makeMockServerPlayerInLevel();
        IDietTracker diet = fresh(player);
        diet.setValue("grains", 0.42f);
        diet.addEaten(Items.BREAD);
        diet.setActive(false);
        CompoundTag tag = new CompoundTag();
        diet.save(tag);
        PlayerDietTracker loaded = new PlayerDietTracker(player);
        loaded.load(tag);
        helper.assertTrue(Math.abs(loaded.getValue("grains") - 0.42f) < 1e-4, "grains not restored: " + loaded.getValue("grains"));
        helper.assertTrue(loaded.getEaten().contains(Items.BREAD), "eaten list not restored");
        helper.assertFalse(loaded.isActive(), "active flag not restored");
        helper.succeed();
    }

    public static void deathPenalty(GameTestHelper helper) {
        ServerPlayer original = helper.makeMockServerPlayerInLevel();
        fresh(original).setValue("fruits", 0.8f);
        ServerPlayer respawned = helper.makeMockServerPlayerInLevel();
        IDietTracker diet = fresh(respawned);
        diet.copy(original, true);
        // default config: lose everything on death, down to a minimum of 0
        helper.assertTrue(diet.getValue("fruits") < 0.8f, "death did not reduce fruits: " + diet.getValue("fruits"));
        IDietTracker changedDim = fresh(respawned);
        changedDim.copy(original, false);
        helper.assertTrue(Math.abs(changedDim.getValue("fruits") - 0.8f) < 1e-4, "non-death copy lost values");
        helper.succeed();
    }
}
