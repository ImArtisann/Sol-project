import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Random;

public class coinflip {

    public String HorT;

    public takefee fee = new takefee();

    public sol solcheck = new sol();

    public MessageBuilder play(String choice, double bet, String userID, JDA jda){
        MessageBuilder message = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();
        String headLost = "https://i.imgur.com/rCtieV0.gif";
        String headsWon = "https://imgur.com/Pay0vAA.gif";
        String tailsLost = "https://imgur.com/jAHRxOW.gif";
        String tailsWon = "https://imgur.com/BjFJfbv.gif";
        String winner  = flipcoin();
        Boolean winorlose = game(choice,winner);
        if(solcheck.greaterThan20()) {
            if (winorlose == null) {
                if (choice.equalsIgnoreCase("heads")) {
                    fee.postHouseEdge(jda,"Coin flip", userID, bet);
                    fee.fees(true, bet, userID,"Coin Flip", jda);
                    eb.setImage(tailsLost);
                    message.setEmbeds(eb.build());
                    message.append("You lost the game the coin flip was: Tails");
                } else {
                    fee.postHouseEdge(jda,"Coin flip", userID, bet);
                    fee.fees(true, bet, userID,"Coin Flip", jda);
                    eb.setImage(headLost);
                    message.setEmbeds(eb.build());
                    message.append("You lost the game the coin flip was: Heads");
                }
            } else {
                if (winorlose) {
                    fee.fees(false, bet, userID,"Coin Flip", jda);
                    if(choice.equalsIgnoreCase("Heads")){
                        eb.setImage(headsWon);
                    }else{
                        eb.setImage(tailsWon);
                    }

                    message.setEmbeds(eb.build());
                    message.append("You won the coin flip! Winning: " + bet);
                } else {
                    fee.fees(true, bet, userID,"Coin Flip", jda);
                    if(choice.equalsIgnoreCase("Heads")){
                        eb.setImage(tailsLost);
                    }else{
                        eb.setImage(headLost);
                    }
                    message.setEmbeds(eb.build());
                    message.append("You lost the game the coin flip was: " + winner);
                }
            }
        }else{
            fee.walletLowNoti(jda,"Coin flip",userID,bet);
            if (choice.equalsIgnoreCase("Heads")) {
                fee.fees(true, bet, userID,"Coin Flip", jda);
                eb.setImage(headLost);
                message.setEmbeds(eb.build());
                message.append("You lost the game the coin flip was: Tails");
            } else {
                fee.fees(true, bet, userID,"Coin Flip", jda);
                eb.setImage(tailsLost);
                message.setEmbeds(eb.build());
                message.append("You lost the game the coin flip was: Heads");
            }
        }
        message.setActionRows(
                ActionRow.of(Button.danger("Restart-cf","Restart"))
        );
        return message;
    }

    public Boolean game(String choice, String winner){
        //Check if they got the right choice
        System.out.println(winner + " " + choice);
        if(choice.trim().equalsIgnoreCase(winner.trim())){
            if(houseEdge()){
                //house edge got them
                System.out.println("House edge");
                return null;
            }else{
                //they won
                System.out.println("They won");
                return true;
            }
        }else{
            //they lost
            System.out.println("Lost " + choice.trim().equalsIgnoreCase(winner.trim()));
            return false;

        }


    }

    public String flipcoin(){
        Random rand = new Random();
        int winner = rand.nextInt(4) +1;

        System.out.println(winner);
        if(winner <=2){
            return "Heads";
        }else{
            return "Tails";
        }
    }

    public boolean houseEdge(){
        Random rand = new Random();
        int choice = rand.nextInt(6) + 1;

        if(choice == 3){
            return true;
        }else{
            return false;
        }


    }



}
