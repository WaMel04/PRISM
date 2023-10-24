package io.github.wamel04.prism.bukkit.receiver.prism_player;

import com.google.gson.Gson;
import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.prism_object.ingame.boss.PrismBossBar;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PP_SendBossBarReceiver extends Subscriber {

    public static HashMap<UUID, PlayerBarData> playerBarMap = new HashMap<>();

    public PP_SendBossBarReceiver() {
        super("pp_send_bossbar_receiver", "pp_send_bossbar_request", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                String[] split = message.split("\\|\\|\\|");

                if (split[0].equals(BukkitInitializer.getPrismServer().getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(split[1]));

                    if (player == null)
                        return;

                    Gson gson = new Gson();
                    String data = split[2];

                    PrismBossBar prismBossBar = gson.fromJson(data, PrismBossBar.class);

                    if (!playerBarMap.containsKey(player.getUniqueId()))
                        playerBarMap.put(player.getUniqueId(), new PlayerBarData(player.getUniqueId()));

                    PlayerBarData barData = playerBarMap.get(player.getUniqueId());

                    if (barData.exist(prismBossBar.getId()))
                        return;

                    BossBar bossBar = Bukkit.createBossBar(prismBossBar.getTitle().replace("%remaining_time%", String.valueOf(prismBossBar.getDuration())), BarColor.valueOf(prismBossBar.getColor().name()), BarStyle.valueOf(prismBossBar.getStyle().name()));
                    bossBar.setProgress(1D);
                    bossBar.addPlayer(player);

                    barData.add(prismBossBar.getId(), bossBar);
                    playerBarMap.put(player.getUniqueId(), barData);

                    if (prismBossBar.getDuration() != -1) {
                        new BukkitRunnable() {
                            int duration = prismBossBar.getDuration();
                            int remainTime = prismBossBar.getDuration();
                            @Override
                            public void run() {
                                if (!playerBarMap.containsKey(player.getUniqueId())) {
                                    bossBar.removeAll();
                                    super.cancel();
                                    return;
                                }

                                PlayerBarData runnableBarData = playerBarMap.get(player.getUniqueId());

                                if (!runnableBarData.exist(prismBossBar.getId()) || remainTime <= 0) {
                                    bossBar.removeAll();
                                    super.cancel();
                                    return;
                                }

                                double progress = duration * 1D / remainTime;
                                bossBar.setProgress(progress);
                                bossBar.setTitle(prismBossBar.getTitle().replace("%remaining_time%", String.valueOf(remainTime)));

                                remainTime -= 1;
                            }
                        }.runTaskTimerAsynchronously(BukkitInitializer.getInstance(), 0, 20);
                    }
                }
            }
        });
    }

    public static class PlayerBarData {
        UUID uuid;
        HashMap<String, BossBar> barMap = new HashMap<>();

        private PlayerBarData(UUID uuid) {
            this.uuid = uuid;
        }

        public UUID getUuid() {
            return uuid;
        }

        public void add(String id, BossBar bar) {
            barMap.put(id, bar);
        }

        public void remove(String id) {
            if (!barMap.containsKey(id))
                return;

            BossBar bar = barMap.get(id);
            bar.removeAll();
            barMap.remove(id);
        }

        public boolean exist(String id) {
            return barMap.containsKey(id);
        }

        public HashMap<String, BossBar> getBarMap() {
            return barMap;
        }
    }

}
