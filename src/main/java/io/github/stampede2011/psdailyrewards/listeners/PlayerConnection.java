package io.github.stampede2011.psdailyrewards.listeners;

import io.github.stampede2011.psdailyrewards.PSDailyRewards;
import io.github.stampede2011.psdailyrewards.ui.UICalendar;
import io.github.stampede2011.psdailyrewards.utils.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.network.ClientConnectionEvent;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class PlayerConnection {

    private static PSDailyRewards plugin = PSDailyRewards.getInstance();

    @Listener
    public void onPlayerConnect(ClientConnectionEvent.Join event, @First Player player) {
        if (plugin.storage.get().playerData.containsKey(player.getUniqueId().toString())) {
            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.MONTH) != plugin.storage.get().playerData.get(player.getUniqueId().toString()).month
                    || cal.get(Calendar.YEAR) != plugin.storage.get().playerData.get(player.getUniqueId().toString()).year) {

                plugin.storage.get().playerData.get(player.getUniqueId().toString()).month = cal.get(Calendar.MONTH);
                plugin.storage.get().playerData.get(player.getUniqueId().toString()).year = cal.get(Calendar.YEAR);
                plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.clear();

            } else if (plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.contains(cal.get(Calendar.DAY_OF_MONTH))) {
                return;
            }
        }

        if (plugin.mainConfig.get().settings.showUI) {
            Sponge.getScheduler().createTaskBuilder()
                    .execute(t -> {
                        player.sendMessage(Utilities.getMessage(plugin.mainConfig.get().lang.REWARD_AVAILABLE, true));
                        UICalendar ui = new UICalendar(player);
                        ui.displayGUI();

                        t.cancel();
                    })
                    .delay(plugin.mainConfig.get().settings.loginDelay, TimeUnit.SECONDS)
                    .submit(PSDailyRewards.getInstance());
        }
    }

}
