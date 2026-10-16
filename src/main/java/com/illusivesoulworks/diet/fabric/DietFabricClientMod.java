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
/*import com.illusivesoulworks.diet.client.DietClientEvents;
import com.illusivesoulworks.diet.client.DietKeys;
import com.illusivesoulworks.diet.client.screen.DynamicButton;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

public class DietFabricClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    KeyBindingHelper.registerKeyBinding(DietKeys.get());
    ClientTickEvents.END_CLIENT_TICK.register(DietClientEvents::tick);
    ItemTooltipCallback.EVENT.register((stack, context, type, lines) ->
        DietClientEvents.renderItemTooltip(Minecraft.getInstance().player, stack, lines));
    ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {

      if (screen instanceof InventoryScreen) {
        DynamicButton button = DietClientEvents.getButton(screen);

        if (button != null) {
          Screens.getButtons(screen).add(button);
        }
      }
      ScreenEvents.afterRender(screen).register((current, graphics, mouseX, mouseY, delta) ->
          DietClientEvents.renderEffectsTooltip(current, graphics));
    });
    DietFabricNetwork.HANDLERS.forEach(DietFabricClientMod::receive);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private static void receive(DietFabricNetwork.Handler handler) {
    ClientPlayNetworking.registerGlobalReceiver(handler.type(),
        (payload, context) -> context.client().execute(
            () -> handler.handler().accept(((DietFabricNetwork.Packet) payload).message())));
  }
}
*///?}
