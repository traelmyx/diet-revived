package com.illusivesoulworks.diet.test.fabric;

//? if fabric {
/*import com.illusivesoulworks.diet.test.DietTests;
import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;

public class FabricGameTests implements FabricGameTest {
    private static final String EMPTY = "diet_gametest:empty";

    @GameTest(template = EMPTY)
    public void defaultGroupsLoaded(GameTestHelper helper) throws Exception {
        DietTests.defaultGroupsLoaded(helper);
    }

    @GameTest(template = EMPTY)
    public void generatedFromRecipes(GameTestHelper helper) throws Exception {
        DietTests.generatedFromRecipes(helper);
    }

    @GameTest(template = EMPTY)
    public void eatingRaisesGroup(GameTestHelper helper) throws Exception {
        DietTests.eatingRaisesGroup(helper);
    }

    @GameTest(template = EMPTY)
    public void eatingCakeBlockCounts(GameTestHelper helper) throws Exception {
        DietTests.eatingCakeBlockCounts(helper);
    }

    @GameTest(template = EMPTY)
    public void hungerDecaysGroups(GameTestHelper helper) throws Exception {
        DietTests.hungerDecaysGroups(helper);
    }

    @GameTest(template = EMPTY)
    public void balancedDietGivesBonus(GameTestHelper helper) throws Exception {
        DietTests.balancedDietGivesBonus(helper);
    }

    @GameTest(template = EMPTY)
    public void saveAndLoad(GameTestHelper helper) throws Exception {
        DietTests.saveAndLoad(helper);
    }

    @GameTest(template = EMPTY)
    public void deathPenalty(GameTestHelper helper) throws Exception {
        DietTests.deathPenalty(helper);
    }
}
*///?}
