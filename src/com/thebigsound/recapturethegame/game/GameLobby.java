package com.thebigsound.recapturethegame.game;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GameLobby {
    private int sessionCode;
    private ArrayList<ChattableObject> players = new ArrayList<>();
    private HashMap<Long, String> chatMessages = new HashMap<>();
    private int onlinePlayer = 0;


    public GameLobby(int sessionCode) {
        this.sessionCode = sessionCode;
    }


    public void playerConnect(Player player) {

    }

    public void playerDisconnect(Player player) {

    }

    public void runUpdates() {
        for(ChattableObject chattableObject : this.players) {
            //Player Checks
            if(chattableObject instanceof Player) {


                return;
            }
            //Bot Checks

        }
    }

    public int onlinePlayers() {
        return players.size();
    }


    public void broadcastEvent(JSONObject jsonObject) {
        for(ChattableObject player : this.players) {
            if(player instanceof Player) {
                Player playerInstance = (Player) player;
                playerInstance.getJsonUpdates().add(jsonObject);
        }
    }

}
