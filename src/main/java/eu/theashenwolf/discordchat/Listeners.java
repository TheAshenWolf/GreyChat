package eu.theashenwolf.discordchat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {

    @EventHandler()
    public void onChat(AsyncPlayerChatEvent event) {
        String originalMessage = event.getMessage();
        String changedMessage = DiscordBot.ReplaceMentionsFromId(originalMessage);
        String formattedMessage = DiscordBot.FormatMessage(changedMessage);
        String coloredMentions = DiscordBot.ColorMentionsToId(formattedMessage);

        event.setMessage(coloredMentions);

        String idMentions = DiscordBot.ReplaceMentionsToId(originalMessage);
        DiscordMessenger.OnMessageFromMinecraft(event.getPlayer().getName(), idMentions);
    }

    @EventHandler()
    public void OnPlayerJoin(PlayerJoinEvent event) {
        if (!DiscordBot.config.SEND_JOINLEAVE) return;
        DiscordMessenger.JoinLeaveMessage(":green_circle: " + event.getPlayer().getName() + " joined the game.");
    }

    @EventHandler()
    public void OnPlayerLeave(PlayerQuitEvent event) {
        if (!DiscordBot.config.SEND_JOINLEAVE) return;
        DiscordMessenger.JoinLeaveMessage(":red_circle: " + event.getPlayer().getName() + " left the game.");
    }

    @EventHandler()
    public void OnPlayerDied(PlayerDeathEvent event) {
        if (!DiscordBot.config.SEND_DEATHS) return;
        Player player = event.getEntity();
        if (player.isDead()) {
            if (player.getKiller() instanceof Player) {
                DiscordMessenger.OnDeathMessage(event.getDeathMessage(), true);
                return;
            }
        }
        DiscordMessenger.OnDeathMessage(event.getDeathMessage(), false);
    }

    @EventHandler
    public void OnAchievement(PlayerAdvancementDoneEvent event) {
        if (!DiscordBot.config.SEND_ADVANCEMENTS) return;
        String playerName = event.getPlayer().getName();
        String advancementKey = event.getAdvancement().getKey().getKey();
        if (event.message() == null) return;
        if (Advancements.Advancements.containsKey(advancementKey)) {
            DiscordMessenger.SendMessage(":tada: **" + playerName + "** achieved " + Advancements.Advancements.get(advancementKey));
        }
        else if (Advancements.Challenges.containsKey(advancementKey)) {
            DiscordMessenger.SendMessage(":trophy: **" + playerName + "** completed the " + Advancements.Challenges.get(advancementKey) + " challenge!");
        }

    }
}
