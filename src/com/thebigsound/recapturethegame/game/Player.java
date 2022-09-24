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

    public Player(String username, int sessionCode, GameLobby lobby) {
        this.sessionID = UUID.randomUUID();
        this.username = username;
        this.sessionCode = sessionCode;
        this.lastCheckin = (System.currentTimeMillis()/1000L);
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
        setLastCheckin(System.currentTimeMillis()/1000L);
    }

    public void runUpdates() {

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
        if(this.lobby.getGamePhase().equals(GamePhase.WAITING)) {
            sendMessage("You cannot send messages yet as the game hasn't started.");
            return;
        }
        this.lobby.broadcastTest(this.username + ": " + message);
    }

    public void handleJsonUpdates(JSONArray jsonArray) {
        for(Object jsonUpdateOBJ : jsonArray) {
            JSONObject jsonObject = (JSONObject) jsonUpdateOBJ;

            if(jsonObject.containsKey("type") && String.valueOf(jsonObject.get("type")).equalsIgnoreCase("message")) {
                String message = String.valueOf(jsonObject.get("message"));
                processIncomingChatMessage(message);
//                this.lobby.broadcastTest(this.username + ": " + message);
            }

        }

        Launcher.getLauncher().runTimerTasks(); //Run timer tasks manually
    }

}
