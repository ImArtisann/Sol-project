import com.paymennt.solanaj.api.rpc.Cluster;
import com.paymennt.solanaj.api.rpc.SolanaRpcClient;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bson.Document;
import org.java_websocket.WebSocket;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class main {

    public static String token = "bot token";

    public static mongo start = new mongo();




    public static void main(String[] args)  throws LoginException, IOException, GeneralSecurityException {
        JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                .addEventListeners(new onjoin())
                .addEventListeners(new onready())
                .addEventListeners(new buttonInteractions())
                .addEventListeners(new selectinteractions())
                .addEventListeners(new updateDatabase())
                .setActivity(Activity.playing("Watching Degens"))
                .build();
        start.startDb();
        


    }


}
