import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class selectinteractions extends ListenerAdapter {

    public coinflip cf = new coinflip();

    public spin spingame = new spin();

    public mongo mongo = new mongo();

    public highorlow hl = new highorlow();

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        JDA jda = event.getJDA();
        MessageBuilder mess = new MessageBuilder();
        if(event.getComponentId().equals("choose-Game")) {
            if (event.getValues().get(0).equals("Coinflip")) {
                mess.append("Heads or tails?")
                        .setActionRows(
                                ActionRow.of(SelectMenu.create("place-Hbet")
                                        .addOptions(SelectOption.of("Heads .05", ".05"),
                                                SelectOption.of("Heads .1", ".1"),
                                                SelectOption.of("Heads .25",".25"),
                                                SelectOption.of("Heads .5",".5"),
                                                SelectOption.of("Heads 1","1"),
                                                SelectOption.of("Heads 2", "2")).build()),
                                ActionRow.of(SelectMenu.create("place-Tbet")
                                        .addOptions(SelectOption.of("Tails .05", ".05"),
                                                SelectOption.of("Tails .1", ".1"),
                                                SelectOption.of("Tails .25",".25"),
                                                SelectOption.of("Tails .5",".5"),
                                                SelectOption.of("Tails 1","1"),
                                                SelectOption.of("Tails 2", "2")).build()),
                                ActionRow.of(Button.danger("Restart-cf","Restart"))
                        );
                event.reply(mess.build()).setEphemeral(true).queue();
            } else if (event.getValues().get(0).equals("HighLow")) {
                mess.append("To Start the game place how much you want to bet")
                        .setActionRows(
                                ActionRow.of(SelectMenu.create("place-highlow")
                                        .addOptions(SelectOption.of(".05", ".05"),
                                                SelectOption.of(".1", ".1"),
                                                SelectOption.of(".25",".25"),
                                                SelectOption.of(".5",".5"),
                                                SelectOption.of("1","1"),
                                                SelectOption.of("2", "2")).build()));

                event.reply(mess.build()).setEphemeral(true).queue();
            } else if (event.getValues().get(0).equals("Spin")) {
                mess.append("Place your bet for the spin")
                        .setActionRows(
                                ActionRow.of(SelectMenu.create("place-Spin")
                                        .addOptions(SelectOption.of(".05", ".05"),
                                                SelectOption.of(".1", ".1"),
                                                SelectOption.of(".25",".25"),
                                                SelectOption.of(".5",".5"),
                                                SelectOption.of("1","1"),
                                                SelectOption.of("2", "2")).build()));
                event.reply(mess.build()).setEphemeral(true).queue();
            }
        }else if(event.getComponentId().equals("place-Hbet")){
            if(userhaveaccount(event.getUser().getId())) {
                Document doc = (Document) mongo.mongoCo.find(new Document("userID", event.getUser().getId())).first();
                double bet = Double.parseDouble(event.getValues().get(0));
                double bal = doc.getDouble("balance");
                if (bet > bal) {
                    event.reply("You dont have enough to bet that amount").setEphemeral(true).queue();
                } else {
                    Bson updatedvalue = new Document("balance", bal - bet);
                    Bson updateOperation = new Document("$set", updatedvalue);
                    mongo.mongoCo.updateOne(doc, updateOperation);

                    MessageBuilder message = new MessageBuilder();
                    message = cf.play("Heads", bet, event.getUser().getId(), jda);
                    event.reply(message.build()).setEphemeral(true).queue();
                }
            }else{
                event.reply("Please make a deposit or wait a min or two for your deposit to complete").setEphemeral(true).queue();
            }

        }else if(event.getComponentId().equals("place-Tbet")){
            if(userhaveaccount(event.getUser().getId())) {
                Document doc = (Document) mongo.mongoCo.find(new Document("userID", event.getUser().getId())).first();
                double bet = Double.parseDouble(event.getValues().get(0));
                double bal = doc.getDouble("balance");
                if (bet > bal) {
                    event.reply("You dont have enough to bet that amount").setEphemeral(true).queue();
                } else {

                    Bson updatedvalue = new Document("balance", bal - bet);
                    Bson updateOperation = new Document("$set", updatedvalue);
                    mongo.mongoCo.updateOne(doc, updateOperation);

                    MessageBuilder message = new MessageBuilder();
                    message = cf.play("Tails", bet, event.getUser().getId(), jda);
                    event.reply(message.build()).setEphemeral(true).queue();
                }
            }else {
                event.reply("Please make a deposit or wait a min or two for your deposit to complete").setEphemeral(true).queue();
            }

        }else if(event.getComponentId().equalsIgnoreCase("place-Spin")){
            if(userhaveaccount(event.getUser().getId())) {
                Document doc = (Document) mongo.mongoCo.find(new Document("userID", event.getUser().getId())).first();
                double bet = Double.parseDouble(event.getValues().get(0));
                double bal = doc.getDouble("balance");
                if (bet > bal) {
                    event.reply("You dont have enough to bet that amount").setEphemeral(true).queue();
                } else {

                    Bson updatedvalue = new Document("balance", bal - bet);
                    Bson updateOperation = new Document("$set", updatedvalue);
                    mongo.mongoCo.updateOne(doc, updateOperation);

                    MessageBuilder message = new MessageBuilder();
                    message = spingame.play(bet, event.getUser().getId(), jda);
                    event.reply(message.build()).setEphemeral(true).queue();
                }
            }else {
                event.reply("Please make a deposit or wait a min or two for your deposit to complete").setEphemeral(true).queue();
            }

        }else if(event.getComponentId().equalsIgnoreCase("place-highlow")){
            if(userhaveaccount(event.getUser().getId())) {
                Document doc = (Document) mongo.mongoCo.find(new Document("userID", event.getUser().getId())).first();
                double bet = Double.parseDouble(event.getValues().get(0));
                double bal = doc.getDouble("balance");
                if (bet > bal) {
                    event.reply("You dont have enough to bet that amount").setEphemeral(true).queue();
                } else {

                    Bson updatedvalue = new Document("balance", bal - bet);
                    Bson updateOperation = new Document("$set", updatedvalue);
                    mongo.mongoCo.updateOne(doc, updateOperation);

                    MessageBuilder message = new MessageBuilder();
                    message = hl.play(bet, event.getUser().getId());
                    event.reply(message.build()).setEphemeral(true).queue();
                }
            }else {
                event.reply("Please make a deposit or wait a min or two for your deposit to complete").setEphemeral(true).queue();
            }
        }
    }


    public boolean userhaveaccount(String userID){

        Document doc = (Document) mongo.mongoCo.find(new Document("userID", userID)).first();

        if (doc == null){
            return false;
        }else return true;

    }
}
