package me.langyue.autotranslation;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
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


    public static void init() {
        Config.init();
        TranslatorManager.init();
        TranslatorHelper.init();
        ResourceManager.init();
        ScreenTranslationHelper.init();
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
