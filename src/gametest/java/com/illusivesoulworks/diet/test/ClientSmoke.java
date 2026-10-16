package com.illusivesoulworks.diet.test;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;

import com.illusivesoulworks.diet.common.capability.DietCapability;
import com.illusivesoulworks.diet.common.data.group.DietGroups;
import com.illusivesoulworks.diet.client.screen.DietScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.nio.file.Files;

// Run with -Dsmoketest=true: creates a flat world, takes a screenshot and quits. Exit code 1 on failure.
public final class ClientSmoke {

    public static final boolean ENABLED = Boolean.getBoolean("smoketest");
    private static final long TIMEOUT_MS = 5 * 60 * 1000;

    private static int stage;
    private static int ticks;

    private ClientSmoke() {
    }

    public static boolean start() {
        if (!ENABLED) {
            return false;
        }
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(TIMEOUT_MS);
                fail("timed out");
            } catch (InterruptedException ignored) {
            }
        }, "smoketest-watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
        return true;
    }

    public static void tick(Minecraft mc) {
        try {
            switch (stage) {
                case 0 -> {
                    Screen screen = mc.screen;
                    if (screen != null && mc.getOverlay() == null && ++ticks >= 20) {
                        if (screen.getClass().getSimpleName().equals("LoadingErrorScreen")) {
                            fail("mod loading errors");
                        }
                        ticks = 0;
                        createWorld(mc);
                        stage = 1;
                    }
                }
                case 1 -> {
                    if (mc.level != null && mc.player != null && mc.screen == null) {
                        stage = 2;
                    }
                }
                case 2 -> {
                    ticks++;
                    if (ticks == 40) {
                        feed(mc);
                    }
                    if (ticks == 80) {
                        mc.setScreen(new InventoryScreen(mc.player));
                    }
                    if (ticks == 110) {
                        Screenshot.grab(mc.gameDirectory, "inventory.png", mc.getMainRenderTarget(), msg -> {});
                        mc.setScreen(new DietScreen(true));
                    }
                    if (ticks == 140) {
                        Screenshot.grab(mc.gameDirectory, "diet-screen.png", mc.getMainRenderTarget(), msg -> {});
                    }
                    if (ticks >= 160) {
                        if (!(mc.screen instanceof DietScreen)) {
                            fail("diet screen did not stay open");
                        }
                        float fruits = DietCapability.get(mc.player).orElseThrow().getValue("fruits");
                        int groups = DietGroups.getGroups(mc.level).size();
                        var tracker = DietCapability.get(mc.player).orElseThrow();
                        System.out.println("[clientsmoke] groups=" + groups + " suite=" + tracker.getSuite()
                            + " suites=" + com.illusivesoulworks.diet.common.data.suite.DietSuites.getSuite(mc.level, tracker.getSuite()).isPresent()
                            + " values=" + tracker.getValues());
                        if (groups < 5) {
                            fail("client has " + groups + " food groups, expected 5");
                        }
                        if (fruits <= 0) {
                            fail("client never received the diet sync (fruits = " + fruits + ")");
                        }
                        mc.setScreen(null);
                        Files.writeString(mc.gameDirectory.toPath().resolve("smoke-ok"), "ok\n");
                        stage = 3;
                        mc.stop();
                    }
                }
                default -> {
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            fail(t.toString());
        }
    }

    private static void createWorld(Minecraft mc) {
        LevelSettings settings = new LevelSettings("smoke", GameType.SURVIVAL, false, Difficulty.PEACEFUL, true, new GameRules(), WorldDataConfiguration.DEFAULT);
        mc.createWorldOpenFlows().createFreshLevel("smoke", settings, new WorldOptions(0L, false, false),
            registries -> registries.registryOrThrow(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions(),
            mc.screen);
    }

    // eat an apple, bread and a steak on the server so the screen has values to show
    private static void feed(Minecraft mc) {
        var server = mc.getSingleplayerServer();
        server.execute(() -> {
            var player = server.getPlayerList().getPlayers().get(0);
            var diet = DietCapability.get(player).orElseThrow();
            diet.consume(new ItemStack(Items.APPLE));
            diet.consume(new ItemStack(Items.BREAD));
            diet.consume(new ItemStack(Items.COOKED_BEEF));
            diet.sync();
        });
    }

    public static void fail(String why) {
        System.err.println("Smoke test failed: " + why);
        System.out.println("[clientsmoke] FAILED: " + why);
        System.err.flush();
        Runtime.getRuntime().halt(1);
    }
}
