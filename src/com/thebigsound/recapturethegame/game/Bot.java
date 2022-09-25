package com.thebigsound.recapturethegame.game;

import com.michaelwflaherty.cleverbotapi.CleverBotQuery;

import java.io.IOException;

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

    public void cleverBotPost(String message) {
        CleverBotQuery bot = new CleverBotQuery("CC9jefsVwbhcjxWVIdX33Mm4tUw", message);
        String response;
        if (conversationID != null) {
            bot.setConversationID(conversationID);
        }
        try {
            bot.sendRequest();
            response = bot.getResponse();
            conversationID = bot.getConversationID();
            sendMessage(response);
        } catch (IOException e) {
            response = e.getMessage();
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
