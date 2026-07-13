package io.github.jason13official.stackable_stew_and_soup.impl.common;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.stackable_stew_and_soup.Constants;
import io.github.jason13official.stackable_stew_and_soup.platform.Services;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModConfig {

  private static final String FILENAME = Constants.MOD_ID + "-server.toml";

  private static int STACKABLE_AMOUNT_VALUE = 8;

  public static ConfigGetterSetter<Integer> STACKABLE_AMOUNT =
      new ConfigGetterSetter<>("stackable_amount", () -> STACKABLE_AMOUNT_VALUE, i -> STACKABLE_AMOUNT_VALUE = i);

  public static void load(Path configDir) {

    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      System.out.println("Loading config, possibly before mixins are applied?");
    }

    File configDirectory = new File(configDir.toUri());
    if (!configDirectory.isDirectory() && !configDirectory.mkdirs()) {
      Constants.LOG.info("Failed to get or create config directory {}", configDirectory.getAbsolutePath());
      return;
    }

    Config.setInsertionOrderPreserved(true);
    ModConfig.loadConfig(configDir, FILENAME);
  }

  private static void loadConfig(Path configDir, String filename) {

    Path configFilepath = configDir.resolve(filename);
    File configFile = new File(configFilepath.toUri());

    try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).build()) {

      if (Files.exists(configFilepath)) {
        config.load();
      }

      // getters (getting from config) to load our runtime config values

      STACKABLE_AMOUNT.setter().accept(config.getIntOrElse(STACKABLE_AMOUNT.key(), STACKABLE_AMOUNT.getter().get()));

      // setters (setting config values/comments for writing the file) to save our runtime config values

      config.setComment(STACKABLE_AMOUNT.key(), " The amount that stew and soup may stack to. Default: 8");
      config.set(STACKABLE_AMOUNT.key(), STACKABLE_AMOUNT.getter().get());

      config.save();
    } catch (Exception e) {

      Constants.LOG.info("Failed to get or create config file {}", configFile.getAbsolutePath());
      e.printStackTrace();
    }
  }

  public record ConfigGetterSetter<T>(String key, Supplier<T> getter, Consumer<T> setter) {}
}
