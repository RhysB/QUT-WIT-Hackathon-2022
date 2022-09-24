package com.thebigsound.recapturethegame.game;

import com.thebigsound.recapturethegame.Launcher;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GameLobby {
    private int sessionCode;
    private ArrayList<ChattableObject> players = new ArrayList<>();
    private HashMap<Long, String> chatMessages = new HashMap<>();
    private int onlinePlayer = 0;
    private Launcher launcher;

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    private GamePhase gamePhase;

    private long lastNotice;


    public GameLobby(int sessionCode) {
        this.sessionCode = sessionCode;
        this.launcher = Launcher.getLauncher();
        this.gamePhase = GamePhase.WAITING;
    }


    public void playerConnect(Player player) {
        System.out.println("Player " + player.getUsername() + " has connected to the lobby " + sessionCode);
        players.add(player);
        this.launcher.getSessionIDToPlayer().put(player.getSessionID(), player);
        player.checkedIn();
        player.runUpdates();
    }

    public void playerDisconnect(Player player) {
        System.out.println("Player " + player.getUsername() + " has disconnected from the lobby " + sessionCode);
        this.launcher.getSessionIDToPlayer().remove(player.getSessionID()); //Remove session ID from map to invalidate connection.
        this.players.remove(player);
    }

    public void runUpdates() {
        for (ChattableObject chattableObject : this.players) {
            //Player Checks
            Player player = (Player) chattableObject;
            if (chattableObject instanceof Player) {
                if ((player.getLastCheckin() + 120) < (System.currentTimeMillis() / 1000L)) {
                    //Player hasn't sent a message in three seconds. Disconnect them.
                    playerDisconnect(player);
                }
//                return;
            }
            //Bot Checks
        }

        //Waiting phase
        if (gamePhase.equals(GamePhase.WAITING)) {
            if (onlinePlayers() >= 3) {
                gamePhase = GamePhase.COUNT_DOWN;
                broadcastTest(onlinePlayers() + " have joined the game. The game will start in 30 seconds.");
            } else {
                if (lastNotice + 6 < (System.currentTimeMillis() / 1000L)) {
                    lastNotice = (System.currentTimeMillis() / 1000L);
                    broadcastTest("Waiting on " + (3 - onlinePlayers()) + " more players to start the game");
                }
            }
        }


    }

    public int onlinePlayers() {
        return players.size();
    }


    public void broadcastTest(String text) {
        JSONObject textEvent = new JSONObject();
        textEvent.put("type", "message");
        textEvent.put("message", text);
        broadcastEvent(textEvent);
    }

    public void broadcastEvent(JSONObject jsonObject) {
        for (ChattableObject player : this.players) {
            if (player instanceof Player) {
                Player playerInstance = (Player) player;
                playerInstance.getJsonUpdates().add(jsonObject);
            }
        }

    }
}
