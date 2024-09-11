package com.mcsync.mod;

import com.google.gson.*;
import net.minecraft.server.level.ServerPlayer; // For the player object
import net.minecraftforge.event.entity.player.PlayerEvent; // Player event handling
import net.minecraftforge.event.server.ServerStartingEvent; // For server start events
import net.minecraftforge.eventbus.api.SubscribeEvent; // To subscribe to Forge events
import net.minecraftforge.fml.common.Mod; // Mod annotation and EventBusSubscriber
import org.apache.logging.log4j.LogManager; // Logging system
import org.apache.logging.log4j.Logger; // Logger class

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mcsync")
public class main {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("mcsync_config.json");
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static JsonObject configJson = new JsonObject();

    // Default config values
    public static String token = "set token here from MCSync";

    public main() {
        LOGGER.info("Thank you for choosing MCSync for your verification system!");
        // Load the config at startup
        loadConfig();
    }

    // Loads config from the JSON file
    public static void loadConfig() {
        if (Files.exists(CONFIG_PATH)) {
            try (FileReader reader = new FileReader(CONFIG_PATH.toFile())) {
                configJson = gson.fromJson(reader, JsonObject.class);
                if (configJson.has("token")) {
                    token = configJson.get("token").getAsString();
                }
                LOGGER.info("Config loaded: " + configJson.toString());
            } catch (IOException e) {
                LOGGER.error("Failed to load config", e);
            }
        } else {
            generateConfig();
        }
    }

    // Generates a new config file with default values
    public static void generateConfig() {
        configJson.addProperty("token", token);
        try (FileWriter writer = new FileWriter(CONFIG_PATH.toFile())) {
            gson.toJson(configJson, writer);
            LOGGER.info("Config saved: " + configJson.toString());
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
        }
    }

    // Updates a value in the config and saves it
    public static void updateConfigValue(String key, String value) {
        configJson.addProperty(key, value);
        generateConfig();  // Save the updated config
        LOGGER.info("Updated config: " + key + " = " + value);
    }

    @Mod.EventBusSubscriber(modid = "mcsync")
    public static class EventHandlers {
        // Server starting event
        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            LOGGER.info("MCSync verifying config existence");
            generateConfig();
        }

        // Player login event
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getPlayer() instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer) event.getPlayer();
                player.sendSystemMessage(new TextComponent("Welcome to the server, " + player.getName().getString() + "!"));
                LOGGER.info("Player " + player.getName().getString() + " has joined the game.");
            }
        }
    }
}
