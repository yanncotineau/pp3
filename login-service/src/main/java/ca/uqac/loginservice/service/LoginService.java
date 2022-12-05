package ca.uqac.loginservice.service;

import ca.uqac.loginservice.security.TokenManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static com.mongodb.client.model.Filters.*;

@Service
public class LoginService {

    private MongoClient mongoClient;
    private MongoDatabase db;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        db = mongoClient.getDatabase("pp3-db");
    }

    public String getTokenFromUser(String rawUser) {
        try {
            // reading the username/password values we received from the request
            JsonNode json = new ObjectMapper().readTree(rawUser);
            String username = json.get("username").asText();
            String password = json.get("password").asText();

            // getting the current "users" collection
            MongoCollection<Document> users = db.getCollection("users");

            // checking if the received username/password credentials match with an already existing user
            if (users.find(and(eq("username", username), eq("password", password))).first() != null) { // if so, continue.

                System.out.println("successfully logged in as" + username);
                return TokenManager.getTokenFromUser(username, password);

            } else { // wrong credentials : either the username does not exist or the password is wrong.
                throw new Exception("wrong username or password");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getUserFromToken(String token) {
        try {
            return TokenManager.getUserFromToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
