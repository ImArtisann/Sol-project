
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class highorlow {

    public static Map<String,Integer> valueStore = new HashMap<String,Integer>();
    public static Map<String, Double> betStore = new HashMap<String, Double>();

    public takefee fee = new takefee();

    public sol solcheck = new sol();



    public MessageBuilder play(double bet, String userID){
        MessageBuilder message = new MessageBuilder();
        Random rand = new Random();

        int number = rand.nextInt(14) + 1;
        message.append("Will the next number be high or low: " + number)
                .setActionRows(
                        ActionRow.of(Button.success("high", "High"),
                                     Button.success("low","low")
                                    )
                ).build();
        if(valueStore.containsKey(userID)){
            System.out.println("User didnt finish a game before");
            betStore.remove(userID);
            valueStore.remove(userID);
            valueStore.put(userID, number);
            betStore.put(userID, bet);
        }else {
            System.out.println("Storing values");
            valueStore.put(userID, number);
            betStore.put(userID, bet);
            System.out.println(betStore.size() + " " + valueStore.size());
        }

        return message;
    }

    public MessageBuilder processChoice(String choice, String userID, JDA jda){
        MessageBuilder message = new MessageBuilder();
        Random rand = new Random();

        int number = rand.nextInt(14) + 1;
        double bet = betStore.get(userID);
        if(solcheck.greaterThan20()) {
            if (choice.equalsIgnoreCase("high")) {
                if (number > valueStore.get(userID)) {
                    if (houseEdge(userID)) {
                        //they lost
                        if(choice.equalsIgnoreCase("high")) {
                            fee.postHouseEdge(jda, "High or Low", userID, bet);
                            fee.fees(true, bet, userID, "High or Low", jda);
                            message.append("You guessed wrong the next number was: " + 1);
                        }else{
                            fee.postHouseEdge(jda, "High or Low", userID, bet);
                            fee.fees(true, bet, userID, "High or Low", jda);
                            message.append("You guessed wrong the next number was: " + 14);
                        }
                    } else {
                        fee.fees(false, bet, userID,"High or Low", jda);
                        message.append("You won the next number was: " + number);
                    }
                } else {
                    // they lost
                    fee.fees(true, bet, userID,"High or Low", jda);
                    message.append("You guessed wrong the next number was: " + number);
                }
            } else {
                if (number > valueStore.get(userID)) {
                    // they lost
                    fee.fees(true, bet, userID,"High or Low", jda);
                    message.append("You guessed wrong the next number was: " + number);
                } else {
                    if (houseEdge(userID)) {
                        //they lost
                        fee.fees(true, bet, userID, "High or Low", jda);
                        fee.fees(true, bet, userID,"High or Low", jda);
                        message.append("You guessed wrong the next number was: " + 14);
                    } else {
                        fee.fees(false, bet, userID,"High or Low", jda);
                        message.append("You won the next number was: " + number);
                    }
                }
            }
        }else{

            if(valueStore.get(userID) == 1 || valueStore.get(userID) == 14){
                fee.fees(false, bet, userID,"High or Low", jda);
                message.append("You won the next number was: " + number);
            }else{
                fee.walletLowNoti(jda,"High or low",userID,bet);
                if(choice.equalsIgnoreCase("High")){
                    fee.fees(true, bet, userID,"High or Low", jda);
                    message.append("You guessed wrong the next number was: " + 1);
                }else{
                    fee.fees(true, bet, userID,"High or Low", jda);
                    message.append("You guessed wrong the next number was: " + 14);
                }
            }
        }
        message.setActionRows(ActionRow.of(Button.danger("restart-hl", "Restart"))).build();

        betStore.remove(userID);
        valueStore.remove(userID);
        return message;
    }


    public boolean houseEdge(String userID){
        Random rand = new Random();
        int choice = rand.nextInt(6) + 1;
        if(valueStore.get(userID) == 1 || valueStore.get(userID) == 14){
            return false;
        }else {
            if (choice == 3) {
                return true;
            } else {
                return false;
            }
        }


    }


}
