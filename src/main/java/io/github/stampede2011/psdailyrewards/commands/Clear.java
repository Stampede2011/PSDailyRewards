package io.github.stampede2011.psdailyrewards.commands;

import io.github.stampede2011.psdailyrewards.PSDailyRewards;
import io.github.stampede2011.psdailyrewards.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Calendar;

public class Clear implements CommandExecutor {

    private static PSDailyRewards plugin = PSDailyRewards.getInstance();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (args.<Player>getOne(Text.of("player")).isPresent()) {
            Player player = args.<Player>getOne(Text.of("player")).get();
            if (plugin.storage.get().playerData.containsKey(player.getUniqueId().toString())) {
                Calendar cal = Calendar.getInstance();

                plugin.storage.get().playerData.get(player.getUniqueId().toString()).year = cal.get(Calendar.YEAR);
                plugin.storage.get().playerData.get(player.getUniqueId().toString()).month = cal.get(Calendar.MONTH);

                plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.clear();

                plugin.storage.save();

                src.sendMessage(Utilities.getMessage(plugin.mainConfig.get().lang.CLEAR_SUCCESS
                        .replace("%player%", player.getName()), true));
            } else {
                src.sendMessage(Utilities.getMessage(plugin.mainConfig.get().lang.PLAYER_NO_DATA
                        .replace("%player%", player.getName()), true));
            }
        } else {
            src.sendMessage(Utilities.getMessage(plugin.mainConfig.get().lang.COMMAND_SYNTAX_ERROR.replace("%usage%", "/dailyreward clear <player>"), true));
        }

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("psdailyrewards.command.clear.base")
                .arguments(new CommandElement[] { GenericArguments.seq(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))
                )})
                .executor(new Clear())
                .build();
    }
}
