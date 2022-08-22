import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class onready extends ListenerAdapter {



    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent event) {
        if(event.getChannel().getName().equalsIgnoreCase("degen-games")){
            TextChannel channel = event.getChannel().asTextChannel();

            MessageBuilder message = new MessageBuilder();

            message.append("Welcome to the house of degens below are your options to interact with the bot")
                    .setActionRows(
                            ActionRow.of(Button.danger("discordID","Copy Discord ID"),
                                         Button.link("https://dev.hel.io/pay/aef56b65-67ee-4f33-95f5-e5c19e52e711","Deposit"),
                                         Button.success("token-balance", "Check token balance"),
                                         Button.success("withdrawl","Withdraw tokens")),
                            ActionRow.of(SelectMenu.create("choose-Game")
                                    .addOptions(SelectOption.of("Coin Flip", "Coinflip"),
                                                SelectOption.of("High or Low", "HighLow"),
                                                SelectOption.of("Spin 2 Win","Spin")).build())
                    );

            channel.sendMessage(message.build()).queue();
        }
    }
}
