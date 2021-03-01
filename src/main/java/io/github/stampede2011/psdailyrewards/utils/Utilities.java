package io.github.stampede2011.psdailyrewards.utils;

import io.github.stampede2011.psdailyrewards.PSDailyRewards;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Optional;
import java.util.UUID;

public class Utilities {

    public static Text toText(String msg) {
        return TextSerializers.FORMATTING_CODE.deserialize(msg);
    }

    public static String fromText(Text msg) {
        return TextSerializers.FORMATTING_CODE.serialize(msg);
    }

    public static Text getMessage(String msg, boolean prefix) {
        if (prefix)
            return toText(PSDailyRewards.getInstance().mainConfig.get().lang.PREFIX + msg);

        return toText(msg);
    }

    public static Text getMessageP(String msg, String prefix) {
        return toText(prefix + msg);
    }

    public static void broadcast(Text text) {

        Sponge.getServer().getBroadcastChannel().send(Text.of(text));

    }

    public static void broadcastSound(SoundType soundType) {
        for (Player player : Sponge.getServer().getOnlinePlayers()) {
            player.playSound(soundType, player.getPosition(), 0.5);
        }
    }

    public static Text of(String s, Player src) { return PSDailyRewards.getPH().replaceSourcePlaceholders(of(s), src); }

    public static Text of(String s) { return TextSerializers.FORMATTING_CODE.deserialize(s); }

    public static String replacePH(String s, Player player) { return of(s, player).toPlain(); }

    // From https://forums.spongepowered.org/t/get-a-player-from-uuid/17638/6
    public static Optional<User> getUserForUuid(UUID uuid){
        return Sponge.getServiceManager().provideUnchecked(UserStorageService.class).get(uuid);
    }

    public static String getNameForUuid(UUID uuid) {
        Optional<User> user = getUserForUuid(uuid);

        if (user.isPresent()) {
            return user.get().getName();
        } else {
            return "Unknown User";
        }

    }

}
