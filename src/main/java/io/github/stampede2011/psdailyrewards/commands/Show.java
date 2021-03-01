package io.github.stampede2011.psdailyrewards.commands;

import io.github.stampede2011.psdailyrewards.PSDailyRewards;
import io.github.stampede2011.psdailyrewards.ui.UICalendar;
import io.github.stampede2011.psdailyrewards.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;

public class Show implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        if (src instanceof Player) {
            UICalendar ui = new UICalendar((Player) src);
            ui.displayGUI();
        } else {
            src.sendMessage(Utilities.getMessage(PSDailyRewards.getInstance().mainConfig.get().lang.NOT_A_PLAYER_ERROR, true));
        }

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("psdailyrewards.command.show.base")
                .executor(new Show())
                .build();
    }
}
