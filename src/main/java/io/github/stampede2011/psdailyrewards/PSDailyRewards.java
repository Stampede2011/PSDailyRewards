package io.github.stampede2011.psdailyrewards;

import com.google.inject.Inject;
import io.github.eufranio.config.Config;
import io.github.stampede2011.psdailyrewards.commands.Base;
import io.github.stampede2011.psdailyrewards.config.MainConfig;
import io.github.stampede2011.psdailyrewards.config.Storage;
import io.github.stampede2011.psdailyrewards.listeners.PlayerConnection;
import io.github.stampede2011.psdailyrewards.ui.UICalendar;
import io.github.stampede2011.psdailyrewards.utils.RandomCollection;
import me.rojo8399.placeholderapi.PlaceholderService;
import net.minecraftforge.common.MinecraftForge;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Plugin(id = PSDailyRewards.ID,
        name = PSDailyRewards.NAME,
        authors = PSDailyRewards.AUTHORS,
        description = PSDailyRewards.DESCRIPTION,
        version = PSDailyRewards.VERSION,
        dependencies = {@Dependency(id = "placeholderapi", optional = true), @Dependency(id = "flashlibs")}
)
public class PSDailyRewards {

    public static final String ID = "psdailyrewards";
    public static final String NAME = "PSDailyRewards";
    public static final String AUTHORS = "Stampede2011";
    public static final String DESCRIPTION = "Daily rewards plugin for the PokeSkies Network!";
    public static final String VERSION = "1.12.2-1.0.0";

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    public File dir;

    public Config<MainConfig> mainConfig;
    public Config<Storage> storage;

    @Inject
    private PluginContainer container;

    private static PSDailyRewards instance;
    private PlaceholderService placeholder;

    private RandomCollection<MainConfig.Reward> rewardsRC = new RandomCollection<>();

    @Listener
    public void onGameInit(GameInitializationEvent e) {
        instance = this;

        Sponge.getEventManager().registerListeners(this, new PlayerConnection());

        this.placeholder = (PlaceholderService)Sponge.getServiceManager().provideUnchecked(PlaceholderService.class);

        logger.info("PSDailyRewards has been enabled!");
    }
    @Listener
    public void onReload(GameReloadEvent e) {
        this.mainConfig.reload();
        this.storage.reload();

        rewardsRC = new RandomCollection<>();
        for (MainConfig.Reward reward : mainConfig.get().rewards) {
            rewardsRC.add(reward.weight, reward);
        }
    }

    @Listener
    public void onServerStarted(GameStartedServerEvent e) {
        this.mainConfig = new Config<>(MainConfig.class, "Config.conf", dir);
        this.storage = new Config<>(Storage.class, "Storage.conf", dir);

        refreshRewards();

        Sponge.getCommandManager().register(instance, Base.build(), "psdailyrewards", "psdr", "dailyrewards", "dr", "daily");
    }

    public static PSDailyRewards getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return instance.logger;
    }

    public static PluginContainer getContainer() {
        return instance.container;
    }

    public static PlaceholderService getPH() { return instance.placeholder; }

    public MainConfig.Reward getReward() {
        return rewardsRC.next();
    }

    public void refreshRewards() {
        for (MainConfig.Reward reward : mainConfig.get().rewards) {
            rewardsRC.add(reward.weight, reward);
        }
    }

}
