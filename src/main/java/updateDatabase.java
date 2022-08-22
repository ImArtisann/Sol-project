import com.merakianalytics.orianna.types.common.Queue;
import com.mongodb.client.FindIterable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

public class updateDatabase extends ListenerAdapter {


    public String lastTrans = "";
    public mongo mongo = new mongo();
    public SheetsQuickstart sheets = new SheetsQuickstart();

    @Override
    public void onReady(@NotNull ReadyEvent event){
        int MINUTES = 1;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run(){ // Function runs every MINUTES minutes.
                try {
                    updateDatabase();
                }catch (Exception e){
                    System.out.println(e);
                }


            }
        }, 0, 1000 * 60 * MINUTES);
    }

    public void updateDatabase()throws IOException, GeneralSecurityException {

        Document doc2 = (Document) mongo.mongoCo2.find(new Document("track", "tracking")).first();

        lastTrans = doc2.getString("last");

        List<List<Object>> values = sheets.run();


        if(values.get(1).get(11).toString().trim().equals(lastTrans)){
            System.out.println("Already in DB");
        }else{
            //lastTrans = values.get(1).get(11).toString().trim();
            int i = 0;
            for(List row : values){
                if(i == 0){
                    i++;
                }else if (lastTrans.equalsIgnoreCase(row.get(11).toString().trim())){
                    break;
                }else{
                    String wallet = row.get(7).toString().trim();
                    String[] discordID = row.get(8).toString().split(" ");
                    String[] payment = row.get(0).toString().split(" ");
                    if(userhaveaccount(discordID[0])){
                        Document doc = (Document) mongo.mongoCo.find(new Document("userID", discordID[0])).first();
                        double bal = doc.getDouble("balance");
                        Bson updatedvalue = new Document("balance", bal+Double.parseDouble(payment[0]));
                        Bson updateOperation = new Document("$set", updatedvalue);
                        mongo.mongoCo.updateOne(doc, updateOperation);

                    }else{
                        Document doc = (Document) new Document("userID", discordID[0]);
                        doc.append("balance", Double.parseDouble(payment[0]));
                        doc.append("wallet",wallet);
                        mongo.mongoCo.insertOne(doc);

                    }
                }


            }
            Document doc = (Document) mongo.mongoCo2.find(new Document("track", "tracking")).first();

            Bson updatedvalue = new Document("last",values.get(1).get(11).toString().trim());
            Bson updateOperation = new Document("$set", updatedvalue);
            mongo.mongoCo2.updateOne(doc, updateOperation);


        }
    }

    public boolean userhaveaccount(String userID){

        Document doc = (Document) mongo.mongoCo.find(new Document("userID", userID)).first();

        if (doc == null){
            return false;
        }else return true;

    }

}
