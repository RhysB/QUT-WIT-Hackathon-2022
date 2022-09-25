package com.thebigsound.recapturethegame.game;

import com.thebigsound.recapturethegame.Launcher;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class Player implements ChattableObject {
    private final UUID sessionID;
    private final String username;
    private final int sessionCode;

    private long lastCheckin;

    private ArrayList<JSONObject> jsonUpdates = new ArrayList<>();

    private GameLobby lobby;

    private String secretIdentity;

    private int points;

    public Player(String username, int sessionCode, GameLobby lobby) {
        this.sessionID = UUID.randomUUID();
        this.username = username;
        this.sessionCode = sessionCode;
        this.lastCheckin = (System.currentTimeMillis() / 1000L);
        this.lobby = lobby;
    }


    public UUID getSessionID() {
        return sessionID;
    }

    public String getUsername() {
        return username;
    }

    public int getSessionCode() {
        return sessionCode;
    }

    public long getLastCheckin() {
        return lastCheckin;
    }

    public void checkedIn() {
        setLastCheckin(System.currentTimeMillis() / 1000L);
    }

    public void runUpdates() {

    }

    public int getPoints() {
        return points;
    }

    private boolean voted;

    //Method is run after each game has finished.
    public void refreshPlayer() {
        voted = false;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    public void setLastCheckin(long lastCheckin) {
        this.lastCheckin = lastCheckin;
    }

    public void sendMessage(String message) {
        JSONObject textEvent = new JSONObject();
        textEvent.put("type", "message");
        textEvent.put("message", message);
        getJsonUpdates().add(textEvent);

    }

    public ArrayList<JSONObject> getJsonUpdates() {
        return jsonUpdates;
    }

    private void processIncomingChatMessage(String message) {
        if (this.lobby.getGamePhase().equals(GamePhase.WAITING)) {
            sendMessage("You cannot send messages yet as the game hasn't started.");
            return;
        } else if (this.lobby.getGamePhase().equals(GamePhase.VOTING_PHASE)) {
            //Handle voting
            String[] messageParts = message.split(" ");
            if (messageParts.length < 2 || !messageParts[0].equalsIgnoreCase("/vote")) {
                sendMessage("Please use the syntax /vote (color)");
                sendMessage("An example is \"/vote blue\"");
                return;
            }
            String voteCast = messageParts[1];
            ChattableObject voteTarget = this.lobby.getChattableObjectFromSecretIdentity(voteCast);
            if (voteTarget == null) {
                sendMessage("Unable to find the user " + voteCast);
                return;
            }

            if(voteCast.equalsIgnoreCase(this.secretIdentity)) {
                sendMessage("Sorry, you cannot vote for yourself.");
                return;
            }

            sendMessage("You have voted for " + voteCast);
            voteTarget.setPoints(voteTarget.getPoints() + 1); //Add point to other player for being voted as bot
            if (voteTarget instanceof Bot) {
                this.setPoints(getPoints() + 1); //Add point to current player as they have accurately guessed the bot.
            }
            return;
        } else if (this.lobby.getGamePhase().equals(GamePhase.ACTIVE)) {
            this.lobby.broadcastText(this.secretIdentity + ": " + message);
            this.lobby.getBot().cleverBotPost(message); //This needs to be moved.
            return;
        }
        this.lobby.broadcastText(this.username + ": " + message);
//        this.lobby.getBot().cleverBotPost(message);
    }

    public void handleJsonUpdates(JSONArray jsonArray) {
        for (Object jsonUpdateOBJ : jsonArray) {
            JSONObject jsonObject = (JSONObject) jsonUpdateOBJ;

            if (jsonObject.containsKey("type") && String.valueOf(jsonObject.get("type")).equalsIgnoreCase("message")) {
                String message = String.valueOf(jsonObject.get("message"));
                processIncomingChatMessage(message);
//                this.lobby.broadcastTest(this.username + ": " + message);
            }

        }

        Launcher.getLauncher().runTimerTasks(); //Run timer tasks manually
    }

    public void setSecretIdentity(String identity) {
        secretIdentity = identity;
    }

    public String getSecretIdentity() {
        return secretIdentity;
    }
}
