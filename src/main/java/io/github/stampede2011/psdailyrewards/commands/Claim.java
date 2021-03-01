package io.github.stampede2011.psdailyrewards.commands;

import io.github.stampede2011.psdailyrewards.PSDailyRewards;
import io.github.stampede2011.psdailyrewards.config.Storage;
import io.github.stampede2011.psdailyrewards.ui.UICalendar;
import io.github.stampede2011.psdailyrewards.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Calendar;

public class Claim implements CommandExecutor {

    private static PSDailyRewards plugin = PSDailyRewards.getInstance();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            Player player = (Player) src;
            Calendar cal = Calendar.getInstance();

            // If the player does NOT have data, setup new data
            if (!plugin.storage.get().playerData.containsKey(player.getUniqueId().toString()) ) {
                Storage.PlayerData pd = new Storage.PlayerData();
                pd.month = cal.get(Calendar.MONTH);
                pd.year = cal.get(Calendar.YEAR);
                pd.claimedDays.add(cal.get(Calendar.DAY_OF_MONTH));
                plugin.storage.get().playerData.put(player.getUniqueId().toString(), pd);
                plugin.storage.save();

                UICalendar.giveRewards(player, cal.get(Calendar.DAY_OF_MONTH));
            // If the player does have data
            } else {
                // If the player's data contains a incorrect month or year
                if (cal.get(Calendar.MONTH) != plugin.storage.get().playerData.get(player.getUniqueId().toString()).month
                        || cal.get(Calendar.YEAR) != plugin.storage.get().playerData.get(player.getUniqueId().toString()).year) {
                    plugin.storage.get().playerData.get(player.getUniqueId().toString()).month = cal.get(Calendar.MONTH);
                    plugin.storage.get().playerData.get(player.getUniqueId().toString()).year = cal.get(Calendar.YEAR);
                    plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.clear();
                    plugin.storage.save();
                }

                // If the player's data does not contain today's date
                if (!plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.contains(cal.get(Calendar.DAY_OF_MONTH))) {
                    plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.add(cal.get(Calendar.DAY_OF_MONTH));
                    plugin.storage.save();
                    UICalendar.giveRewards(player, cal.get(Calendar.DAY_OF_MONTH));
                // If the player's data does contain today's date
                } else {
                    player.sendMessage(Utilities.getMessage(plugin.mainConfig.get().lang.REWARD_ALREADY_CLAIMED, true));
                }
            }
        } else {
            src.sendMessage(Utilities.getMessage(plugin.mainConfig.get().lang.NOT_A_PLAYER_ERROR, true));
        }


        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("psdailyrewards.command.claim.base")
                .executor(new Claim())
                .build();
    }
}
