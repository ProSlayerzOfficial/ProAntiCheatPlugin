package _proping.proAntiCheatPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ProAntiCheatPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Register event listeners
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    // Check for flight without creative mode
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // If the player is flying but not in Creative Mode
        if (player.getAllowFlight() && !player.getGameMode().toString().equals("CREATIVE")) {
            // Flag the player as flying without creative mode
            flagCheat(player, "Flying without Creative Mode");
        }
    }

    // Detect PvP reach
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            // Calculate the distance between attacker and victim
            double distance = attacker.getLocation().distance(victim.getLocation());

            // Flag if reach is greater than 3.0 blocks (normal reach)
            if (distance > 3.0) {
                flagCheat(attacker, "Reach exceeded: " + distance + " blocks");
            }
        }
    }

    // Check for "perfect aim" (extreme precision)
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Check if the player is holding a weapon (for PvP)
        if (player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_SWORD) {
            // Logic to check perfect aim - if the player hits the same spot exactly every time
            // For simplicity, this is just a placeholder for your logic
            if (isPerfectAim(player)) {
                flagCheat(player, "Perfect Aim Detected");
            }
        }
    }

    // A simple method to flag a player for cheating
    private void flagCheat(Player player, String reason) {
        // Send a message to the player that they are suspected of cheating
        player.sendMessage("§cYou are suspected of cheating: " + reason);

        // Broadcast message to all players with the 'anticheat.see' permission
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission("anticheat.see")) {
                onlinePlayer.sendMessage("§e[AntiCheat] §7" + player.getName() + " flagged for " + reason);
            }
        }
    }

    // Placeholder method for detecting perfect aim
    private boolean isPerfectAim(Player player) {
        // For a more advanced detection, you'd calculate how consistent the player's hits are
        // compared to random chance, and flag if the player hits without missing.

        // This is a simplified version for demonstration purposes
        return Math.random() < 0.01; // For example, a very low random chance that simulates "perfect aim"
    }
}
