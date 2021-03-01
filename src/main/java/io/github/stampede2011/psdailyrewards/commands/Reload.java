package io.github.stampede2011.psdailyrewards.commands;

import io.github.stampede2011.psdailyrewards.PSDailyRewards;
import io.github.stampede2011.psdailyrewards.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

public class Reload implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        PSDailyRewards.getInstance().mainConfig.reload();
        PSDailyRewards.getInstance().storage.reload();

        PSDailyRewards.getInstance().refreshRewards();

        PSDailyRewards.getLogger().info("PSDailyRewards has been successfully reloaded!");
        src.sendMessage(Utilities.getMessage(PSDailyRewards.getInstance().mainConfig.get().lang.RELOAD_SUCCESS, true));

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("psdailyrewards.command.reload.base")
                .executor(new Reload())
                .build();
    }
}
