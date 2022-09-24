package com.thebigsound.recapturethegame.game;

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

    public ArrayList<JSONObject> getJsonUpdates() {
        return jsonUpdates;
    }

    public void setJsonUpdates(ArrayList<JSONObject> jsonUpdates) {
        this.jsonUpdates = jsonUpdates;
    }
}
