package com.thebigsound.recapturethegame.game;

import com.michaelwflaherty.cleverbotapi.CleverBotQuery;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Bot implements ChattableObject {

    private int sessionCode;
    private GameLobby lobby;

    private String conversationID;

    private String secretIdentity;

    private int points;

    public Bot(int sessionCode, GameLobby lobby) {
        this.sessionCode = sessionCode;
        this.lobby = lobby;

    }

    public void runUpdates() {
        String remove = null;

        for (String key : messageArray.keySet()) {
//            System.out.println("Entry: " + key);
            if (messageArray.get(key) < (System.currentTimeMillis() / 1000L)) {
                sendMessage(key);
                remove = key;
            }
        }
        if (remove != null) {
            messageArray.remove(remove);
        }

    }

    private HashMap<String, Long> messageArray = new HashMap<>();

    public void cleverBotPost(String message) {
        boolean toSendMessage = new Random().nextInt(2) == 0;
        int waitTime = new Random().nextInt(3) + 2;
        CleverBotQuery bot = new CleverBotQuery("CC9jefsVwbhcjxWVIdX33Mm4tUw", message);
        String response;
        if (conversationID != null) {
            bot.setConversationID(conversationID);
        }
        try {
            bot.sendRequest();
            response = bot.getResponse();
            conversationID = bot.getConversationID();
            if (toSendMessage) {
                messageArray.put(response, (System.currentTimeMillis() / 1000L) + waitTime);
                System.out.println("Added message \"" + response + "\" to queue to get posted in " + waitTime + " seconds.");
            }
        } catch (IOException e) {
//            response = e.getMessage();
        }
    }

    private void sendMessage(String message) {
        if (this.lobby.getGamePhase().equals(GamePhase.ACTIVE)) {
            this.lobby.broadcastText(secretIdentity + ": " + message);
        }
    }

    @Override
    public void setSecretIdentity(String identity) {
        secretIdentity = identity;
    }

    @Override
    public String getSecretIdentity() {
        return secretIdentity;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    public String getUsername() {
        return "Bot";
    }
}
