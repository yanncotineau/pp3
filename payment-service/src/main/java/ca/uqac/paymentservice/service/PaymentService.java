package ca.uqac.paymentservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.mongodb.client.model.Updates;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final WebClient.Builder webClientBuilder;
    private MongoClient mongoClient;
    private MongoDatabase db;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        db = mongoClient.getDatabase("pp3-db");
    }

    public Map<String, Object> pay(String rawPayment) {
        try {

            JsonNode json = new ObjectMapper().readTree(rawPayment);
            String token = json.get("token").asText();
            String receiver = json.get("receiver").asText();
            long amount = json.get("amount").asLong();

            String username = webClientBuilder.build().get()
                    .uri("http://login-service/api/login/userFromToken?token=" + token)
                    .retrieve().bodyToMono(String.class).block();

            if (username.equals("")) // could not get an user from the provided token
                throw new Exception("invalid token");

            if (username.equals(receiver)) // cannot send money to the same account
                throw new Exception("cannot send money to oneself");

            if (amount < 0) // the amount sent has to be positive
                throw new Exception("amount cannot be negative");

            // getting the current "users" collection
            MongoCollection<Document> users = db.getCollection("users");

            if (users.find(eq("username", receiver)).first() == null) // receiver has to be a existing user
                throw new Exception("invalid receiver");

            // getting current balances
            long senderCurrentBalance = (long) users.find(eq("username", username)).first().get("balance");
            long receiverCurrentBalance = (long) users.find(eq("username", receiver)).first().get("balance");

            if (amount > senderCurrentBalance) // cannot send more than sender has
                throw new Exception("cannot pay more than we have");

            // proceed to payment
            users.updateOne(eq("username", username), Updates.set("balance", senderCurrentBalance - amount));
            users.updateOne(eq("username", receiver), Updates.set("balance", receiverCurrentBalance + amount));

            // generating response JSON
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", username);
            map.put("balance", senderCurrentBalance - amount);

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, Object>();
        }
    }
}
