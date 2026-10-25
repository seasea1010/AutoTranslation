package me.langyue.autotranslation;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import me.langyue.autotranslation.command.AutoTranslationCommands;
import me.langyue.autotranslation.config.Config;
import me.langyue.autotranslation.resource.ResourceManager;
import me.langyue.autotranslation.translate.TranslatorManager;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class AutoTranslation {
    public static final Logger LOGGER = LoggerFactory.getLogger("AutoTranslation");
    public static final String MOD_ID = "autotranslation";

    public static final Path ROOT = Platform.getGameFolder().resolve("AutoTranslation");
    public static Config CONFIG = null;

    public static final KeyMapping SCREEN_TRANSLATE_KEYMAPPING = new KeyMapping(
            "key.autotranslation.screen_translate",
            InputConstants.KEY_M,
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"))
    );


    public static void init() {
        Config.init();
        TranslatorManager.init();
        TranslatorHelper.init();
        ResourceManager.init();
        ScreenTranslationHelper.init();
        KeyMappingRegistry.register(SCREEN_TRANSLATE_KEYMAPPING);
        ClientTickEvent.CLIENT_POST.register(client -> {
            while (SCREEN_TRANSLATE_KEYMAPPING.consumeClick()) {
                ScreenTranslationHelper.toggleScreenStatus(ScreenTranslationHelper.getCurrentScreen());
            }
        });
        CommandRegistrationEvent.EVENT.register((dispatcher, dedicated, ignored) -> AutoTranslationCommands.register(dispatcher));
    }

    public static void stop() {
        ScreenTranslationHelper.saveConfig();
        ResourceManager.save();
    }

    public static String getLanguage() {
        try {
            return Minecraft.getInstance().options.languageCode;
        } catch (Throwable e) {
            return Language.DEFAULT;
        }
    }

    public static void debug(String var1, Object... var2) {
        if (CONFIG.debug) {
            LOGGER.info(var1, var2);
        }
    }
}
