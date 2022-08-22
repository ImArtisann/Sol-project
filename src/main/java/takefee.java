import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.*;

public class takefee {

    public double fee = .05;

    public mongo mongo = new mongo();



    public void fees(Boolean lose, double bet, String userID, String game, JDA jda){



        double house = calculateFee(bet);
        double win = (bet*2)-house;
        Document doc = (Document) mongo.mongoCo.find(new Document("userID", userID)).first();

        Double bal = doc.getDouble("balance");

        if(lose){
            System.out.println("House won the game");
            TextChannel channel = jda.getGuildById("1008058181928370266").getTextChannelById("1008191399017066526");
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("New lost");
            eb.setColor(Color.RED);
            eb.addField(game + " Result", "<@" + userID + "> has lost their game betting: " + bet,true);
            channel.sendMessageEmbeds(eb.build()).queue();
        }else{
            TextChannel channel = jda.getGuildById("1008058181928370266").getTextChannelById("1008191399017066526");
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("New Win");
            eb.setColor(Color.green);
            eb.addField(game + " Result", "<@" + userID + "> has won their bet winning: " + win,true);
            channel.sendMessageEmbeds(eb.build()).queue();
            Bson updatedvalue = new Document("balance", bal + win);
            Bson updateOperation = new Document("$set", updatedvalue);
            mongo.mongoCo.updateOne(doc, updateOperation);
        }

    }

    public void postHouseEdge(JDA jda, String game, String userID, double bet){
        TextChannel channel = jda.getGuildById("1008058181928370266").getTextChannelById("1008599123827638323");
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("New House Edge Event");
        eb.setColor(Color.green);
        eb.addField(game + " Result", "<@" + userID + "> just had their bet house edged: " + bet,true);
        channel.sendMessageEmbeds(eb.build()).queue();
    }

    public void walletLowNoti(JDA jda, String game, String userID, double bet){
        TextChannel channel = jda.getGuildById("1008058181928370266").getTextChannelById("1008599123827638323");
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("New Wallet low event 20%");
        eb.setColor(Color.red);
        eb.addField(game + " Result", "<@" + userID + "> lost their bet because wallet is 20% low" + bet,true);
        channel.sendMessageEmbeds(eb.build()).queue();
    }

    public Double calculateFee(double bet){

        if(bet == .05){
            return .0025;
        }else if(bet == .1){
            return .005;
        }else if(bet == .25){
            return .0125;
        }else if(bet == .5){
            return 0.025;
        }else if(bet == 1){
            return .05;
        }else{
            return .1;
        }

    }
}
