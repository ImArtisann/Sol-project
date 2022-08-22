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

public class buttonInteractions extends ListenerAdapter {

    public mongo mongo = new mongo();

    public sol blockchain = new sol();

    public highorlow hl = new highorlow();

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        JDA jda = event.getJDA();
        if(event.getComponentId().equals("discordID")){
            event.reply(event.getUser().getId()).setEphemeral(true).queue();
        }else if(event.getComponentId().equals("Deposit")){
            if(userHasAccount(event.getUser().getId())){
                Document doc = (Document) mongo.mongoCo.find(new Document("userID", event.getUser().getId())).first();

                double bal = doc.getDouble("balance");

                Bson updatedvalue = new Document("balance", bal + 5.00);
                Bson updateOperation = new Document("$set", updatedvalue);
                mongo.mongoCo.updateOne(doc, updateOperation);
                event.reply("Added 5 to your balance").setEphemeral(true).queue();


            }else{
                Document doc = (Document) new Document("userID", event.getUser().getId());
                doc.append("balance", 10.00);
                mongo.mongoCo.insertOne(doc);
                event.reply("Added 10 to your balance").setEphemeral(true).queue();

            }
        }else if(event.getComponentId().equals("token-balance")){
            if(userHasAccount(event.getUser().getId())){
                Document doc = (Document) mongo.mongoCo.find(new Document("userID", event.getUser().getId())).first();

                Double bal = doc.getDouble("balance");

                event.reply("Your balance is: " + bal).setEphemeral(true).queue();

            }else{
                event.reply("Press deposit to get an account").setEphemeral(true).queue();

            }
        }else if(event.getComponentId().equals("withdrawl")){
            if(userHasAccount(event.getUser().getId())) {
                Document doc = (Document) mongo.mongoCo.find(new Document("userID", event.getUser().getId())).first();
                double balance = doc.getDouble("balance");
                if(balance>0) {
                    String wallet = doc.getString("wallet");
                    blockchain.run(wallet,balance);
                    setMongoToZero(event.getUser().getId());
                    event.reply("You have successfully withdrawn the tokens").setEphemeral(true).queue();
                }else{
                    event.reply("You dont have a balance to withdraw").setEphemeral(true).queue();
                }
            }else{
                event.reply("You dont have any balance to withdraw yet").setEphemeral(true).queue();
            }
        }else if(event.getComponentId().equals("Restart-cf")){
            MessageBuilder mess = new MessageBuilder();
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
        }else if(event.getComponentId().equalsIgnoreCase("high")){
            MessageBuilder mess = new MessageBuilder();
            mess = hl.processChoice("high",event.getUser().getId(), jda);
            event.reply(mess.build()).setEphemeral(true).queue();
        }else if(event.getComponentId().equalsIgnoreCase("low")){
            MessageBuilder mess = new MessageBuilder();
            mess = hl.processChoice("low",event.getUser().getId(), jda);
            event.reply(mess.build()).setEphemeral(true).queue();
        }else if(event.getComponentId().equalsIgnoreCase("restart-hl")){
            MessageBuilder mess = new MessageBuilder();
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
        }else if(event.getComponentId().equalsIgnoreCase("restart-spin")){
            MessageBuilder mess = new MessageBuilder();
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
    }

    public boolean userHasAccount(String userID){

        Document doc = (Document) mongo.mongoCo.find(new Document("userID", userID)).first();

        if (doc == null){
            return false;
        }else return true;

    }

    public void setMongoToZero(String userID){
        Document doc = (Document) mongo.mongoCo.find(new Document("userID", userID)).first();

        Bson updatedvalue = new Document("balance", 0.00);
        Bson updateOperation = new Document("$set", updatedvalue);
        mongo.mongoCo.updateOne(doc, updateOperation);
    }
}
