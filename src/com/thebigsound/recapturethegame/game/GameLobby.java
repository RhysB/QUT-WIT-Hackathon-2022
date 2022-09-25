package com.thebigsound.recapturethegame.game;

import com.thebigsound.recapturethegame.Launcher;
import org.json.simple.JSONObject;

import java.util.*;

public class GameLobby {
    private int sessionCode;
    private ArrayList<ChattableObject> players = new ArrayList<>();
    private HashMap<Long, String> chatMessages = new HashMap<>();
    //    private int onlinePlayer = 0;
    private Launcher launcher;

    private int gameCount;

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    private GamePhase gamePhase;

    private long lastNotice;
    private long phaseStartTime;
    private long phaseEndTime;

    public Bot getBot() {
        return bot;
    }

    private Bot bot;

    public GameLobby(int sessionCode) {
        this.sessionCode = sessionCode;
        this.launcher = Launcher.getLauncher();
        this.gamePhase = GamePhase.WAITING;
        this.bot = new Bot(sessionCode, this);
        this.players.add(this.bot);
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
            chattableObject.runUpdates();
            if (chattableObject instanceof Player) {
                //Player Checks
                Player player = (Player) chattableObject;
                if (chattableObject instanceof Player) {
                    if ((player.getLastCheckin() + 120) < (System.currentTimeMillis() / 1000L)) {
                        //Player hasn't sent a message in three seconds. Disconnect them.
                        playerDisconnect(player);
                    }
//                return;
                }
            } else {
                //Bot Checks
            }
        }

        //Waiting phase
        if (gamePhase.equals(GamePhase.WAITING)) {
            if (onlinePlayers() >= 3) {
                gamePhase = GamePhase.COUNT_DOWN;
                broadcastText(onlinePlayers() + " have joined the game. The game will start in 10 seconds.");
                phaseStartTime = (System.currentTimeMillis() / 1000L) + 10;
            } else {
                if (lastNotice + 6 < (System.currentTimeMillis() / 1000L)) {
                    lastNotice = (System.currentTimeMillis() / 1000L);
                    broadcastText("Waiting on " + (3 - onlinePlayers()) + " more players to start the game");
                }
            }
        } else if (gamePhase.equals(GamePhase.COUNT_DOWN)) {
            if (onlinePlayers() < 3) {
                gamePhase = GamePhase.WAITING;
                broadcastText("The game no longer has enough players for a game.");
            } else {
                if (phaseStartTime < (System.currentTimeMillis() / 1000L)) {
                    if (lastNotice + 5 < (System.currentTimeMillis() / 1000L)) {
                        lastNotice = (System.currentTimeMillis() / 1000L);
                        broadcastText("The game will start in " + (phaseStartTime - (System.currentTimeMillis() / 1000L)) + " seconds.");
                    } else {
                        //Refresh players
                        for (ChattableObject chattableObject : this.players) {
                            if (chattableObject instanceof Player) {
                                ((Player) chattableObject).refreshPlayer();
                            }
                        }

                        generateSecretIdentities(); //Generate peoples random names
                        gamePhase = GamePhase.ACTIVE;
                        this.broadcastText("");
                        broadcastText("The game has started! It will run for 30 seconds.");
                        phaseEndTime = (System.currentTimeMillis() / 1000L) + 30;

                        //Post first question and send it to the bot
                        String starterMessage = this.generateStarterQuestion();
                        this.broadcastText(starterMessage);
                        this.bot.cleverBotPost(starterMessage);
                    }
                }
            }
        } else if (gamePhase.equals(GamePhase.ACTIVE)) {
            if (phaseEndTime > (System.currentTimeMillis() / 1000L)) {
                if (lastNotice + 10 < (System.currentTimeMillis() / 1000L)) {
                    lastNotice = (System.currentTimeMillis() / 1000L);
                    broadcastText("The game will end in " + (phaseEndTime - (System.currentTimeMillis() / 1000L)) + " seconds.");
                }
            } else {
                gamePhase = GamePhase.VOTING_PHASE;
                this.broadcastText("");
                broadcastText("The game has ended. You have twenty seconds to cast your vote. Please cast a vote for who you believe was a bot by typing /vote (color)");
                this.broadcastText("");
                phaseEndTime = (System.currentTimeMillis() / 1000L) + 20;
            }
        } else if (gamePhase.equals(GamePhase.VOTING_PHASE)) {
            if (phaseEndTime > (System.currentTimeMillis() / 1000L)) {
                if (lastNotice + 10 < (System.currentTimeMillis() / 1000L)) {
                    lastNotice = (System.currentTimeMillis() / 1000L);
                    broadcastText("You have " + (phaseEndTime - (System.currentTimeMillis() / 1000L)) + " seconds left to vote.");
                }
            } else {
                gameCount++;
                if (gameCount >= 3) {
                    gamePhase = GamePhase.GAME_ENDED;
                    broadcastText("The game has ended. The winner was: " + " " + ".");
                    phaseEndTime = (System.currentTimeMillis() / 1000L) + 30;
                    printWinningResults();
                } else {
                    //Intrim
                    gamePhase = GamePhase.WAITING;
                    broadcastText("Round " + gameCount + "/3 has ended.");
                }
            }
        } else if (gamePhase.equals(GamePhase.GAME_ENDED)) {
            if (phaseEndTime > (System.currentTimeMillis() / 1000L)) {
                if (lastNotice + 10 < (System.currentTimeMillis() / 1000L)) {
                    lastNotice = (System.currentTimeMillis() / 1000L);
                    broadcastText("The next game starts in " + (phaseEndTime - (System.currentTimeMillis() / 1000L)) + " seconds.");
                }
            } else {
                gamePhase = GamePhase.WAITING;
                System.out.println("The game in Lobby " + sessionCode + " has ended. Starting countdown for next game.");
            }
        }


    }

    public int onlinePlayers() {
        return players.size();
    }


    public void broadcastText(String text) {
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


    public void generateSecretIdentities() {
        //Loop thru all chattable objects to set secret identities
        for (ChattableObject chattableObject : players) {
            String color;
            while (true) {
                color = getRandomColor();
                //Loop thru entities again to ensure color isn't already used
                boolean alreadyUsed = false;
                for (ChattableObject chattableObject1 : players) {
                    if (chattableObject1.getSecretIdentity() != null && chattableObject1.getSecretIdentity().equalsIgnoreCase(color)) {
                        alreadyUsed = true;
                    }
                }
                if (!alreadyUsed) {
                    break;
                }
            }
            chattableObject.setSecretIdentity(color);
        }

    }

    public ArrayList<ChattableObject> getPlayers() {
        return players;
    }

    public ChattableObject getChattableObjectFromSecretIdentity(String identity) {
        for (ChattableObject chattableObject : this.getPlayers()) {
            if (chattableObject.getSecretIdentity().equalsIgnoreCase(identity)) {
                return chattableObject;
            }
        }
        return null;
    }


    public void printWinningResults() {
        Map<String, Integer> map = new HashMap<>();
        for (ChattableObject chattableObject : this.players) {
            map.put(chattableObject.getUsername(), chattableObject.getPoints()); //TODO: Get real name
        }
        Object[] a = map.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o1).getValue().compareTo(
                        ((Map.Entry<String, Integer>) o2).getValue());
            }
        });
        int j = 1;
        this.broadcastText("");
        this.broadcastText("Game Scoreboard:");
        for (int i = a.length - 1; i >= 0; i--) {
            Object e = a[i];
            this.broadcastText(j + ". " + ((Map.Entry<String, Integer>) e).getKey() + " - " + ((Map.Entry<String, Integer>) e).getValue() + " points");
            j++;
        }
        this.broadcastText("");

    }

    private String generateStarterQuestion() {
        final String[] conversationStarters = {"Tell me about you?", "Working on anything exciting lately?", "What color is the sky?", "What is your favourite movie?", "What is your story?", "1+2=3?", "Having fun?", "Who here is the bot?",
                "What is your opinion on bots?", "What is your favourite flavour of icecream?", "What is your favourite country?", "What is your opinion on humans?", "I think all of you are bots!"};
        Random r = new Random();
        int randomNumber = r.nextInt(conversationStarters.length);
        return conversationStarters[randomNumber];
    }

    private String getRandomColor() {
        final String[] colors = {"Red", "Blue", "Green", "Orange", "Pink", "Red", "Brown", "Black", "Grey", "Purple", "Cyan", "Lime", "White", "Yellow"};
        Random r = new Random();
        int randomNumber = r.nextInt(colors.length);
        return colors[randomNumber];
    }

}
