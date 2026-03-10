package me.lajzy.utilAPI.Utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownBuilder {
    private final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    public void setCooldown(Player player, String key, long seconds) {
        cooldowns.computeIfAbsent(key, k -> new HashMap<>())
                .put(player.getUniqueId(), System.currentTimeMillis() + (seconds * 1000));
    }

    public void removeCooldown(Player player, String key) {
        if(!cooldowns.containsKey(key)) return;
        cooldowns.get(key).remove(player.getUniqueId());
    }

    public boolean isOnCooldown(Player player, String key) {
        if (!cooldowns.containsKey(key)) return false;
        Map<UUID, Long> map = cooldowns.get(key);
        Long expiry = map.get(player.getUniqueId());
        if (expiry == null) return false;

        if (System.currentTimeMillis() > expiry) {
            map.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    public long remainingCooldown(Player player, String key) {
        if(!cooldowns.containsKey(key)) return 0;
        Long expiry = cooldowns.get(key).get(player.getUniqueId());
        if(expiry == null) return 0;

        long remaining = expiry - System.currentTimeMillis();
        return remaining > 0 ? remaining / 1000 : 0;
    }

    public void clearPlayerCooldowns(Player player) {
        UUID uuid = player.getUniqueId();
        for(Map<UUID, Long> map : cooldowns.values()) {
            map.remove(uuid);
        }
    }

}
