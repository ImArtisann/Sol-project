import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Random;

public class spin {

    public takefee fee = new takefee();

    public sol solchecker = new sol();

    public MessageBuilder play(double bet, String userID, JDA jda){
        MessageBuilder message = new MessageBuilder();
        int number = spinWheel();

        if(solchecker.greaterThan20()) {
            if (game(number) == null) {
                fee.fees(false, (bet * 2), userID,"Spin", jda);
                message.append("You just hit the jackpot and 4x!");
            } else {
                if (game(number)) {
                    if (houseEdge()) {
                        fee.postHouseEdge(jda,"Spin", userID, bet);
                        fee.fees(true, bet, userID,"Spin", jda);
                        message.append("You lost the spin try again next time!");
                    } else {
                        fee.fees(false, bet, userID,"Spin", jda);
                        message.append("You won the spin doubling your bet!");
                    }
                } else {
                    fee.fees(true, bet, userID,"Spin", jda);
                    message.append("You lost the spin try again next time!");
                }
            }
        }else{
            fee.walletLowNoti(jda,"Spin",userID,bet);
            fee.fees(true, bet, userID,"Spin", jda);
            message.append("You lost the spin try again next time!");
        }

        message.setActionRows(ActionRow.of(Button.danger("restart-spin", "Restart"))).build();

        return message;
    }

    public Boolean game(int number){

        if(number == 18){
            return null;
        }else if(number > 50){
            return true;
        }else{
            return false;
        }

    }


    public int spinWheel(){
        Random rand = new Random();
        int winner = rand.nextInt(100) +1;

        return winner;
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
