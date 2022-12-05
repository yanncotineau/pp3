package ca.uqac.registrationservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final long initialBalance = 1000L;
    private MongoClient mongoClient;
    private MongoDatabase db;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        db = mongoClient.getDatabase("pp3-db");
    }

    public boolean registerUser(String rawUser) {
        try {
            // reading the username/password values we received from the request
            JsonNode json = new ObjectMapper().readTree(rawUser);
            String username = json.get("username").asText();
            String password = json.get("password").asText();

            // getting the current "users" collection
            MongoCollection<Document> users = db.getCollection("users");

            // checking if the received username already exists
            if (users.find(eq("username", username)).first() != null) { // if so, throw exception.
                throw new Exception("username \"" + username + "\" is already registered.");
            } else { // if not, insert new user in the database
                users.insertOne(new Document()
                        .append("username", username)
                        .append("password", password)
                        .append("balance", initialBalance));
                return true;
            }

        } catch (Exception e) { // database or JSON error
            e.printStackTrace();
            return false;
        }
    }
}
