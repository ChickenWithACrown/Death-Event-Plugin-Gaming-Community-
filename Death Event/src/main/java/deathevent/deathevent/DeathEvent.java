package deathevent.deathevent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DeathEvent extends JavaPlugin implements Listener {

    private final Map<UUID, TaskData> taskDataMap = new HashMap<>();
    private BukkitRunnable taskTimer;
    private final Map<Player, BossBar> bossBarsMap = new HashMap<>();

    private static final String[] TASKS = {
            "kill another player",
            "grow a tree",
            "kill 50 cows",
            "pop 2 totems",
            "kill 10 zombies",
            "collect 5 diamonds",
            "craft a diamond sword",
            "craft a diamond pickaxe",
            "Respawn & kill the ender Dragon"
             "kill 10 creepers",
            "kill 5 skeletons",
            "kill 5 spiders",
            "Go to the End",
            "Go to the Nether",
            "Craft a golden apple",
            "Find a Enchanted Golden Apple",
            "Craft a enchantment table",
            "Kill 5 players",
            "Kill 5 witches",
            "Kill 5 enderman",
            "Kill 5 blazes",
            "Kill 5 ghasts",
            "Kill 5 piglins",
            "Go to the end city",
            "Go to the bastion",
            "Go to the fortress",
            "Go to the village",
            "Go to the jungle",
            "Go to the desert",
            "Go to the ocean",
            "Go to the mountains",
            "Go to the forest",
            "Go to the plains",
            "Go to the swamp",
            "Go to the taiga",
            "Go to the savanna",
            "Go to the snowy tundra",
            "Get a ender dragon egg",
            "Get a nether star",
            "Get a wither skull",
            "Get a totem of undying",
            "Get a elytra",
            "Get a trident",
            "Get a netherite ingot",
            "Get a netherite sword",
            "Get a dragon head",
            "Get a beacon",
            "Get a shulker shell"
    };

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Clean up
        taskDataMap.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("DeathEventStart")) {
            startTaskTimerForAllPlayers();
            sender.sendMessage("Task timer started.");
            return true;
        } else if (command.getName().equalsIgnoreCase("DeathEventStop")) {
            stopTaskTimerForAllPlayers();
            sender.sendMessage("Task timer stopped.");
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (taskTimer != null) {
            startTaskTimer(player, taskDataMap.get(player.getUniqueId()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        stopTaskTimer(player);
        removeBossBar(player);
    }

    private void startTaskTimerForAllPlayers() {
        if (taskTimer != null) {
            taskTimer.cancel();
        }
        taskTimer = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!taskDataMap.containsKey(player.getUniqueId())) {
                        String task = getRandomTask();
                        int delay = generateRandomDelay(); // Generate a random delay in minutes
                        TaskData taskData = new TaskData(task, delay);
                        taskDataMap.put(player.getUniqueId(), taskData);
                        startTaskTimer(player, taskData);
                    }
                }
            }
        };
        taskTimer.runTaskTimer(this, 0, 20 * 60); // Run task timer every minute (20 ticks * 60 seconds)
    }

    private void stopTaskTimerForAllPlayers() {
        if (taskTimer != null) {
            taskTimer.cancel();
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            stopTaskTimer(player);
            removeBossBar(player);
        }
    }

    private void startTaskTimer(Player player, TaskData taskData) {
        BossBar bossBar = Bukkit.createBossBar("Death Timer: " + taskData.getRemainingTimeFormatted() + " - Task: " + taskData.getTask(), BarColor.RED, BarStyle.SOLID);
        bossBar.addPlayer(player);
        bossBarsMap.put(player, bossBar); // Store the bossBar instance for later reference
        new BukkitRunnable() {
            @Override
            public void run() {
                int timeLeft = taskData.getRemainingTime();
                if (timeLeft > 0) {
                    taskData.decreaseTime();
                    bossBar.setTitle("Death Timer: " + taskData.getRemainingTimeFormatted() + " - Task: " + taskData.getTask());
                } else {
                    // Task failure handling
                    player.sendMessage("You failed to complete the task in time!");
                    // If the player dies due to the plugin, cancel the task and remove boss bar
                    if (player.isDead()) {
                        removeBossBar(player);
                        cancel();
                    }
                }
                // Check if task is completed
                if (timeLeft == 0) {
                    // Task completion handling
                    // If the player is alive and the task time runs out, remove boss bar and cancel task
                    if (!player.isDead()) {
                        player.sendTitle("You Survived", "", 10, 70, 20); // Display title "You Survived" in green text
                        removeBossBar(player);
                        cancel();
                    }
                }
            }
        }.runTaskTimer(this, 0, 20 * 60); // Run task every minute (20 ticks * 60 seconds)
    }

    private void stopTaskTimer(Player player) {
        taskDataMap.remove(player.getUniqueId());
    }

    private void removeBossBar(Player player) {
        BossBar bossBar = bossBarsMap.remove(player);
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    private String getRandomTask() {
        Random random = new Random();
        return TASKS[random.nextInt(TASKS.length)];
    }

    private int generateRandomDelay() {
        Random random = new Random();
        return random.nextInt(24 * 60) + 1; // Generate random delay between 1 minute and 24 hours
    }

    private static class TaskData {
        private final String task;
        private int delay;

        public TaskData(String task, int delay) {
            this.task = task;
            this.delay = delay;
        }

        public String getTask() {
            return task;
        }

        public int getRemainingTime() {
            return delay;
        }

        public String getRemainingTimeFormatted() {
            int minutes = delay % 60;
            int hours = delay / 60;
            return String.format("%02d:%02d", hours, minutes); // Correct usage of String.format()
        }

        public void decreaseTime() {
            if (delay > 0) {
                delay--;
            }
        }
    }
}
