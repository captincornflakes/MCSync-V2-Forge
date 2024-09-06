package com.mcsync.mod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


// The value here should match the modid in your mods.toml file
@Mod("mcsync")
public class main {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public main() {
        // Register the setup method for mod loading
        LOGGER.info("Hello from Your Mod!");
    }

    // You can subscribe to events if needed
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Server is starting...");
    }

    // Subscribe to the PlayerLoggedInEvent
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // Check if the player is on a server (ServerPlayerEntity)
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
            
            // Send a message to the player when they join the server
            player.sendMessage(new StringTextComponent("Welcome to the server, " + player.getName().getString() + "!"), player.getUUID());

            // Log a message to the console
            LOGGER.info("Player " + player.getName().getString() + " has joined the game.");
        }
    }
}