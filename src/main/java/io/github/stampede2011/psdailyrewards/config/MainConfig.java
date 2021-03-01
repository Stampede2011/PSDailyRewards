package io.github.stampede2011.psdailyrewards.config;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.github.stampede2011.psdailyrewards.utils.Utilities;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.*;

@ConfigSerializable
public class MainConfig {

    @Setting
    public Language lang = new Language();

    @ConfigSerializable
    public static class Language {

        @Setting
        public String PREFIX = "&8(&2&lDaily Rewards&8) ";

        @Setting
        public String RELOAD_SUCCESS = "&aPSDailyRewards has been successfully reloaded!";

        @Setting
        public String COMMAND_SYNTAX_ERROR = "&cIncorrect command syntax! Correct usage: &l%usage%";

        @Setting
        public String NOT_A_PLAYER_ERROR = "&cYou are not a player!";

        @Setting
        public String REWARD_CLAIM = "&aYou claimed the reward for &lDay #%day%&a! You received a &l%tier% Reward&a: %item%";

        @Setting
        public String REWARD_CLAIM_TITLE = "%tier%";

        @Setting
        public String REWARD_CLAIM_SUBTITLE = "%item%";

        @Setting
        public String REWARD_AVAILABLE = "&aYou have an unclaimed daily reward for today! Use &l/dailyreward &ato claim it";

        @Setting
        public String REWARD_ALREADY_CLAIMED = "&cYou already claimed the reward for today&c! Please try again tomorrow...";

        @Setting
        public String PLAYER_NO_DATA = "&cThere is no existing data for &l%player%&c!";

        @Setting
        public String CLEAR_SUCCESS = "&aSuccessfully cleared the data for &l%player%&a!";

        @Setting
        public String SETDAY_SUCCESS = "&aSuccessfully set &lDay #%day% &afor &l%player% &ato &l%status%&a!";

    }

    @Setting
    public Settings settings = new Settings();

    @ConfigSerializable
    public static class Settings {

        @Setting(value="login-delay", comment="Delay in seconds after the player logs in to display the rewards UI")
        public int loginDelay = 1;

        @Setting(value="ui-on-join", comment="Should the rewards UI be shown when the player logs in?")
        public boolean showUI = true;

        @Setting(value="tier-names")
        public Map<String, String> tierNames = ImmutableMap.<String, String>builder()
                .put("COMMON", "&aCommon")
                .put("UNCOMMON", "&eUncommon")
                .put("RARE", "&dRare")
                .put("LEGENDARY", "&6Legendary")
                .build();

        @Setting
        public UI ui = new UI();

        @ConfigSerializable
        public static class UI {

            @Setting(value="title")
            public String title = "&8Daily Rewards";

            @Setting(value="missed")
            public ItemStack missedItem = ItemStack.builder()
                    .itemType(ItemTypes.TNT_MINECART)
                    .add(Keys.DISPLAY_NAME, Utilities.toText("&7&lDay #%day%"))
                    .add(Keys.ITEM_LORE, Arrays.asList(Utilities.toText("&7&oYou missed this day!")))
                    .build();

            @Setting(value="claimed")
            public ItemStack claimedItem = ItemStack.builder()
                    .itemType(ItemTypes.MINECART)
                    .add(Keys.DISPLAY_NAME, Utilities.toText("&e&lDay #%day%"))
                    .add(Keys.ITEM_LORE, Arrays.asList(Utilities.toText("&7&oThis has been claimed, thanks for logging in!")))
                    .build();

            @Setting(value="claimable")
            public ItemStack claimableItem = ItemStack.builder()
                    .itemType(ItemTypes.CHEST_MINECART)
                    .add(Keys.DISPLAY_NAME, Utilities.toText("&a&lDay #%day%"))
                    .add(Keys.ITEM_LORE, Arrays.asList(Utilities.toText("&7&oClick to claim today's reward!")))
                    .build();

            @Setting(value="locked")
            public ItemStack lockedItem = ItemStack.builder()
                    .itemType(ItemTypes.IRON_BARS)
                    .add(Keys.DISPLAY_NAME, Utilities.toText("&c&lDay %day%"))
                    .add(Keys.ITEM_LORE, Arrays.asList(Utilities.toText("&7&oPlease check back later!")))
                    .build();

            @Setting(value="week")
            public WeekItem weekItem = new WeekItem("&e&lWeek %week%", new ArrayList<>());

        }

    }

    @Setting(comment="All possible rewards. Placeholders: %player%")
    public List<Reward> rewards = Collections.emptyList();

    @ConfigSerializable
    public static class Reward {

        @Setting(value="tier")
        public String tier = "";

        @Setting(value="weight")
        public int weight = 0;

        @Setting(value="display-name")
        public String displayName = "";

        @Setting(value="commands")
        public List<String> commands = Lists.newArrayList();

        Reward(String tier, int weight, String displayName, List<String> commands) {
            this.tier = tier;
            this.weight = weight;
            this.displayName = displayName;
            this.commands = commands;
        }

        Reward() {

        }

    }

    @ConfigSerializable
    public static class WeekItem {

        @Setting(value="display-name", comment="Placeholders: %week%")
        public String displayName = "";

        @Setting(comment="Placeholders: %week%")
        public List<String> lore = Lists.newArrayList();

        WeekItem(String displayName, List<String> lore) {
            this.displayName = displayName;
            this.lore = lore;
        }

        WeekItem() {
        }

    }


}

