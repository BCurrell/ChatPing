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

    // TODO: Turn these into configurable options
    private static final Sound PING_SOUND = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
    private static final int PING_VOLUME = 20;
    private static final int PING_PITCH = 1;
    private static final long PING_DELAY = 5;
    private static final int PING_COUNT = 2;

    @Override
    public void onDisable()
    {
    }

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event)
    {
        Player player = event.getPlayer();
        String message = event.message().toString();

        if (Permissions.PING.isAuthorised(player))
        {
            for (final Player oPlayer : getServer().getOnlinePlayers())
            {
                if (oPlayer == player || Permissions.PING_EXEMPT.isAuthorised(oPlayer))
                {
                    continue;
                }

                if (this.containsIgnoreCase(message, oPlayer.getName(), oPlayer.displayName().toString()))
                {
                    this.playSound(iPlayer, PING_SOUND, PING_VOLUME, PING_PITCH, PING_DELAY, PING_COUNT);
                }
            }
        }
    }

    private boolean containsIgnoreCase(String source, String... targets)
    {
        source = source.toLowerCase();

        for (String target : targets)
        {
            if (source.contains(target.toLowerCase()))
            {
                return true;
            }
        }

        return false;
    }

    private void playSound(Player player, Sound sound, float volume, float pitch, long delay, int count)
    {
        player.playSound(player.getLocation(), sound, volume, pitch);

        while (--count > 0)
        {
            this.getServer().getScheduler().runTaskLater(this, () -> {
                player.playSound(player.getLocation(), sound, volume, pitch);
            }, delay);
        }
    }

    private void playSound(Player player, Sound sound, float volume, float pitch, long delay)
    {
        this.playSound(player, sound, volume, pitch, delay, PING_COUNT);
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
