import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class onjoin extends ListenerAdapter {


    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        //Create the text channel on join
        event.getGuild().createTextChannel("Degen Games").queue();



    }
}
