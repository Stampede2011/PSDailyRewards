package io.github.stampede2011.psdailyrewards.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class Storage {

    @Setting(value="player-data")
    public Map<String, PlayerData> playerData = Maps.newHashMap();

    @ConfigSerializable
    public static class PlayerData {

        @Setting(value="month")
        public int month;

        @Setting(value="year")
        public int year;

        @Setting(value="claimed-days", comment="")
        public List<Integer> claimedDays = new ArrayList<>();

    }

}