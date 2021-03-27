package com.notoriousdev.chatping;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatPing extends JavaPlugin implements Listener
{

    @Override
    public void onDisable()
    {
    }

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncChatEvent event)
    {
        getLogger().info("Chat event");
        Player player = event.getPlayer();
        String message = event.message().toString();
        if (Permissions.PING.isAuthorised(player))
        {
            for (final Player oPlayer : getServer().getOnlinePlayers())
            {
                // oPlayer != player &&
                if ((message.toLowerCase().contains(oPlayer.getName().toLowerCase()) || message.toLowerCase().contains(oPlayer.displayName().toString().toLowerCase())) && !Permissions.PING_EXEMPT.isAuthorised(oPlayer))
                {
                    this.getLogger().info("Playing sound for " + oPlayer.getName());
                    oPlayer.playSound(oPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1);
                    getServer().getScheduler().runTaskLater(this, () -> oPlayer.playSound(oPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20, 1), 5L);
                }
            }
        }
    }

    public enum Permissions
    {

        PING,
        PING_EXEMPT;

        public boolean isAuthorised(CommandSender sender)
        {
            return sender.hasPermission(this.toString());
        }

        @Override
        public String toString()
        {
            return "chatping." + this.name().toLowerCase().replace("_", ".");
        }
    }
}