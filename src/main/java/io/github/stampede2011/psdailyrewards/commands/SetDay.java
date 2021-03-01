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

public class SetDay implements CommandExecutor {

    private static PSDailyRewards plugin = PSDailyRewards.getInstance();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (args.<Player>getOne(Text.of("player")).isPresent() && args.<Integer>getOne(Text.of("day")).isPresent() && args.<Boolean>getOne(Text.of("claimed")).isPresent()) {
            Player player = args.<Player>getOne(Text.of("player")).get();
            int day = args.<Integer>getOne(Text.of("day")).get();
            boolean claimed = args.<Boolean>getOne(Text.of("claimed")).get();

            if (plugin.storage.get().playerData.containsKey(player.getUniqueId().toString())) {
                Calendar cal = Calendar.getInstance();

                if (claimed) {
                    plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.add(day);
                } else {
                    plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.remove(Integer.valueOf(day));
                }

                plugin.storage.save();

                src.sendMessage(Utilities.getMessage(plugin.mainConfig.get().lang.SETDAY_SUCCESS
                        .replace("%day%", String.valueOf(day))
                        .replace("%player%", player.getName())
                        .replace("%status%", (claimed) ? "claimed" : "unclaimed"), true));
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
                .permission("psdailyrewards.command.setday.base")
                .arguments(new CommandElement[] { GenericArguments.seq(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("day"))),
                        GenericArguments.onlyOne(GenericArguments.bool(Text.of("claimed")))
                )})
                .executor(new SetDay())
                .build();
    }
}
