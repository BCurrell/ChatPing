package com.notoriousdev.chatping;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        String message = event.getMessage();
        if (Permissions.PING.isAuthorised(player))
        {
            for (final Player oPlayer : getServer().getOnlinePlayers())
            {
                if (oPlayer != player && (message.toLowerCase().contains(oPlayer.getName().toLowerCase()) || message.toLowerCase().contains(oPlayer.getDisplayName().toLowerCase())) && !Permissions.PING_EXEMPT.isAuthorised(oPlayer))
                {
                    oPlayer.playSound(oPlayer.getLocation(), Sound.ORB_PICKUP, 20, 1);
                    getServer().getScheduler().runTaskLater(this, new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            oPlayer.playSound(oPlayer.getLocation(), Sound.ORB_PICKUP, 20, 1);
                        }
                    }, 5L);
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