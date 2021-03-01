package io.github.stampede2011.psdailyrewards.commands;

import io.github.stampede2011.psdailyrewards.utils.Utilities;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Help implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        List<Text> texts = new ArrayList<>();

        if (src.hasPermission("psdailyrewards.command.base")) {
            texts.add(Utilities.toText("&a/dr &8- &fOpens the daily rewards menu"));
        }

        texts.add(Utilities.toText("&a/dr help &8- &fDisplays this help message"));

        if (src.hasPermission("psdailyrewards.command.claim.base")) {
            texts.add(Utilities.toText("&a/dr claim &8- &fClaims any available rewards"));
        }

        if (src.hasPermission("psdailyrewards.command.setday.base")) {
            texts.add(Utilities.toText("&a/dr setday <player> <day> <t/f> &8- &fSets the specified day to claimed/unclaimed"));
        }

        if (src.hasPermission("psdailyrewards.command.clear.base")) {
            texts.add(Utilities.toText("&a/dr clear <player> &8- &fClears specified player's data"));
        }

        if (src.hasPermission("psdailyrewards.command.reload.base")) {
            texts.add(Utilities.toText("&a/dr reload &8- &fReloads PSDailyRewards"));
        }

        PaginationList.builder()
                .title(Utilities.toText("&2&lDaily Rewards"))
                .padding(Utilities.toText("&8&m-&r"))
                .contents(texts)
                .linesPerPage(10)
                .sendTo(src);

        return CommandResult.success();
    }

    public static CommandSpec build() {
        return CommandSpec.builder()
                .permission("psdailyrewards.command.help.base")
                .executor(new Help())
                .build();
    }
}
