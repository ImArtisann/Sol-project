import com.mongodb.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class mongo implements mongo1{

    public static MongoClient mongoClient;
    public static MongoDatabase mongodb;
    public static MongoCollection<Document> mongoCo;
    public static MongoDatabase mongodb2;
    public static MongoCollection<Document> mongoCo2;


    public void startDb(){
        this.mongoClient = MongoClients.create("mongo uri");
        this.mongodb = mongoClient.getDatabase("HouseOfDegens");
        this.mongoCo = mongodb.getCollection("econ");
        this.mongoCo2 = mongodb.getCollection("lastTrans");
    }
}
