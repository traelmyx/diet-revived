package com.illusivesoulworks.diet.test.neoforge;

//? if neoforge {
import com.illusivesoulworks.diet.test.ClientSmoke;
import com.illusivesoulworks.diet.test.DietTests;
import net.minecraft.client.Minecraft;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(NeoForgeGameTests.MOD_ID)
@PrefixGameTestTemplate(false)
@Mod(NeoForgeGameTests.MOD_ID)
public class NeoForgeGameTests {
    public static final String MOD_ID = "diet_gametest";

    public NeoForgeGameTests(Dist dist) {
        if (dist.isClient() && ClientSmoke.start()) {
            NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post e) -> ClientSmoke.tick(Minecraft.getInstance()));
        }
    }

    @GameTest(template = "empty")
    public static void defaultGroupsLoaded(GameTestHelper helper) throws Exception {
        DietTests.defaultGroupsLoaded(helper);
    }

    @GameTest(template = "empty")
    public static void generatedFromRecipes(GameTestHelper helper) throws Exception {
        DietTests.generatedFromRecipes(helper);
    }

    @GameTest(template = "empty")
    public static void eatingRaisesGroup(GameTestHelper helper) throws Exception {
        DietTests.eatingRaisesGroup(helper);
    }

    @GameTest(template = "empty")
    public static void eatingCakeBlockCounts(GameTestHelper helper) throws Exception {
        DietTests.eatingCakeBlockCounts(helper);
    }

    @GameTest(template = "empty")
    public static void hungerDecaysGroups(GameTestHelper helper) throws Exception {
        DietTests.hungerDecaysGroups(helper);
    }

    @GameTest(template = "empty")
    public static void balancedDietGivesBonus(GameTestHelper helper) throws Exception {
        DietTests.balancedDietGivesBonus(helper);
    }

    @GameTest(template = "empty")
    public static void saveAndLoad(GameTestHelper helper) throws Exception {
        DietTests.saveAndLoad(helper);
    }

    @GameTest(template = "empty")
    public static void deathPenalty(GameTestHelper helper) throws Exception {
        DietTests.deathPenalty(helper);
    }
}
//?}
