package ca.uqac.balanceservice.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final WebClient.Builder webClientBuilder;
    private MongoClient mongoClient;
    private MongoDatabase db;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        db = mongoClient.getDatabase("pp3-db");
    }

    public Map<String, Object> getUserBalance(String token) {
        try {
            String username = webClientBuilder.build().get()
                    .uri("http://login-service/api/login/userFromToken?token=" + token)
                    .retrieve().bodyToMono(String.class).block();
            System.out.println(username);
            //String username = "obiwan2"; // testing purposes

            if (username.equals("")) { // could not get an user from the provided token
                throw new Exception("invalid token");
            }

            // getting the current "users" collection
            MongoCollection<Document> users = db.getCollection("users");

            Document doc = users.find(eq("username", username)).first();

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", username);
            map.put("balance", (long) doc.get("balance"));

            return map;
        } catch (Exception e) {
            return new HashMap<String, Object>();
        }
    }
}
