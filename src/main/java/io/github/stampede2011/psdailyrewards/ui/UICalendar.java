package io.github.stampede2011.psdailyrewards.ui;

import dev.flashlabs.flashlibs.inventory.Element;
import dev.flashlabs.flashlibs.inventory.Layout;
import dev.flashlabs.flashlibs.inventory.View;
import io.github.stampede2011.psdailyrewards.PSDailyRewards;
import io.github.stampede2011.psdailyrewards.config.MainConfig;
import io.github.stampede2011.psdailyrewards.config.Storage;
import io.github.stampede2011.psdailyrewards.utils.RandomCollection;
import io.github.stampede2011.psdailyrewards.utils.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.item.inventory.property.InventoryCapacity;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.common.util.Constants;

import java.util.*;

public class UICalendar {

    private static PSDailyRewards plugin = PSDailyRewards.getInstance();

    private Player player;

    public UICalendar(Player player) {
        this.player = player;
    }

    public void displayGUI() {

        if (plugin.storage.get().playerData.containsKey(player.getUniqueId().toString())) {
            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.MONTH) != plugin.storage.get().playerData.get(player.getUniqueId().toString()).month
                    || cal.get(Calendar.YEAR) != plugin.storage.get().playerData.get(player.getUniqueId().toString()).year) {
                plugin.storage.get().playerData.get(player.getUniqueId().toString()).month = cal.get(Calendar.MONTH);
                plugin.storage.get().playerData.get(player.getUniqueId().toString()).year = cal.get(Calendar.YEAR);
                plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.clear();
                plugin.storage.save();
            }
        }

        Element blackGlass = Element.of(ItemStack.builder()
                .itemType(ItemTypes.STAINED_GLASS_PANE)
                .add(Keys.DYE_COLOR, DyeColors.BLACK)
                .add(Keys.DISPLAY_NAME, Text.of(""))
                .build());

        Layout.Builder layout = Layout.builder(6, 9)
                .set(blackGlass, new int[]{7, 16, 25, 34, 43});

        View view = View.builder(InventoryArchetype.builder().property(InventoryCapacity.of(45)).build("minecraft:slot", "Slot"))
                .title(Utilities.toText(plugin.mainConfig.get().settings.ui.title))
                .build(PSDailyRewards.getContainer());

        List<Integer> daySlots = Arrays.asList(
                     0,  1,  2,  3,  4,  5,  6,
                     9, 10, 11, 12, 13, 14, 15,
                    18, 19, 20, 21, 22, 23, 24,
                    27, 28, 29, 30, 31, 32, 33,
                    36, 37, 38, 39, 40, 41, 42
                );

        List<Integer> weekSlots = Arrays.asList(
                8,
                17,
                26,
                35,
                44
        );

        Calendar c = Calendar.getInstance();
        int monthDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);


        for (int i = 1; i <= monthDays; i++) {
            ItemStack item;

            if (plugin.storage.get().playerData.containsKey(player.getUniqueId().toString()) && plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.contains(i)) {
                item = plugin.mainConfig.get().settings.ui.claimedItem.createSnapshot().createStack();
            } else if (c.get(Calendar.DAY_OF_MONTH) == i) {
                item = plugin.mainConfig.get().settings.ui.claimableItem.createSnapshot().createStack();
            } else if (c.get(Calendar.DAY_OF_MONTH) > i) {
                item = plugin.mainConfig.get().settings.ui.missedItem.createSnapshot().createStack();
            } else {
                item = plugin.mainConfig.get().settings.ui.lockedItem.createSnapshot().createStack();
            }

            if (item.get(Keys.DISPLAY_NAME).isPresent()) {
                item.offer(Keys.DISPLAY_NAME, Utilities.toText(Utilities.fromText(item.get(Keys.DISPLAY_NAME).get()).replace("%day%", String.valueOf(i))));
            }

            List<Text> loreList = new ArrayList<>();
            if (item.get(Keys.ITEM_LORE).isPresent()) {
                for (Text line : item.get(Keys.ITEM_LORE).get()) {
                    String lineStr = Utilities.fromText(line);
                    loreList.add(Utilities.toText(lineStr.replace("%day%", String.valueOf(i))));
                }
            }
            item.offer(Keys.ITEM_LORE, loreList);

            int finalI = i;

            Element slot = Element.of(item, pl -> {
                if (c.get(Calendar.DAY_OF_MONTH) == finalI) {
                    if (!plugin.storage.get().playerData.containsKey(player.getUniqueId().toString()) ) {
                        Storage.PlayerData pd = new Storage.PlayerData();
                        pd.month = c.get(Calendar.MONTH);
                        pd.year = c.get(Calendar.YEAR);
                        pd.claimedDays.add(finalI);

                        plugin.storage.get().playerData.put(player.getUniqueId().toString(), pd);
                        plugin.storage.save();

                        pl.getPlayer().closeInventory();
                        giveRewards(pl.getPlayer(), finalI);
                    } if (!plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.contains(finalI)) {
                        plugin.storage.get().playerData.get(player.getUniqueId().toString()).claimedDays.add(finalI);
                        plugin.storage.save();

                        pl.getPlayer().closeInventory();
                        giveRewards(pl.getPlayer(), finalI);
                    }
                }
            });

            layout.set(slot, daySlots.get(i-1));
        }

        for (int i = 1; i <= 5; i++) {
            ItemStack item = ItemStack.builder()
                    .itemType(Sponge.getRegistry().getType(ItemType.class, "pixelmon:custom_icon").orElse(ItemTypes.SIGN))
                    .add(Keys.DISPLAY_NAME, Utilities.toText(plugin.mainConfig.get().settings.ui.weekItem.displayName.replace("%week%", String.valueOf(i))))
                    .build();

            DataContainer itemContainer = item.toContainer();

            itemContainer.set(DataQuery.of("UnsafeData", "SpriteName"), "pixelmon:customicon/button_no" + i);

            if (itemContainer.contains(Constants.Sponge.UNSAFE_NBT))
                item.setRawData(itemContainer);

            List<Text> loreList = new ArrayList<>();

            for (String loreLine : plugin.mainConfig.get().settings.ui.weekItem.lore) {
                loreList.add(Utilities.toText(loreLine
                        .replace("%week%", String.valueOf(i))
                ));
            }

            item.offer(Keys.ITEM_LORE, loreList);

            Element slot = Element.of(item);
            layout.set(slot, weekSlots.get(i-1));
        }

        Layout lay = layout.build();

        view.define(lay);
        view.open(this.player);
    }

    public static void giveRewards(Player player, int day) {
        MainConfig.Reward reward = plugin.getReward();

        for (int i = 0; i < reward.commands.size(); i++) {
            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), Utilities.replacePH(reward.commands.get(i), player));
        }

        Title title = Title.builder()
                .title(Utilities.toText(plugin.mainConfig.get().lang.REWARD_CLAIM_TITLE
                    .replace("%tier%", getTierName(reward.tier))
                    .replace("%item%", reward.displayName)
                ))
                .subtitle(Utilities.toText(plugin.mainConfig.get().lang.REWARD_CLAIM_SUBTITLE
                        .replace("%tier%", getTierName(reward.tier))
                        .replace("%item%", reward.displayName)
                ))
                .stay(30)
                .build();

        player.sendMessage(Utilities.getMessage(plugin.mainConfig.get().lang.REWARD_CLAIM
                .replace("%day%", String.valueOf(day))
                .replace("%tier%", getTierName(reward.tier))
                .replace("%item%", reward.displayName)
            , true));

        player.sendTitle(title);

    }

    public static String getTierName(String tier) {
        return plugin.mainConfig.get().settings.tierNames.getOrDefault(tier, "");
    }


}