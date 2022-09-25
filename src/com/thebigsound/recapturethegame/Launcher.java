package com.thebigsound.recapturethegame;

import com.thebigsound.recapturethegame.game.GameLobby;
import com.thebigsound.recapturethegame.game.GamePhase;
import com.thebigsound.recapturethegame.game.Player;
import com.thebigsound.recapturethegame.routes.TimerRunRoute;
import com.thebigsound.recapturethegame.routes.api.CreateUser;
import com.thebigsound.recapturethegame.routes.api.UpdateRoute;
import com.thebigsound.recapturethegame.routes.error.GenericError;
import com.thebigsound.recapturethegame.routes.webpages.ChatRoute;
import com.thebigsound.recapturethegame.routes.webpages.IndexRoute;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.GenericServlet;
import java.util.*;

public class Launcher {
    private Server server;

    //    private ArrayList<Player> players = new ArrayList<>();
    private HashMap<Integer, GameLobby> lobbies = new HashMap<>();

    public HashMap<UUID, Player> getSessionIDToPlayer() {
        return sessionIDToPlayer;
    }

    private HashMap<UUID, Player> sessionIDToPlayer = new HashMap<>();

    public static Launcher launcher;

    public static Launcher getLauncher() {
        return launcher;
    }

    private long nextTick = 0;

    public static void main(String[] args) throws Exception {
        new Launcher(args);
    }


    private Launcher(String[] args) throws Exception {
        launcher = this;
        System.out.println("Starting ReCapture The Game");
        int port = 7683;

        System.out.println("Starting timer thread");
        TimerPingThread timerPingThread = new TimerPingThread(port);
        timerPingThread.start();
        System.out.println("Starting webserver");
        startWebServers(port);

    }

    public void runTimerTasks() {
        if (nextTick > (System.currentTimeMillis())) {
            return;
        }
//        System.out.println("Running Tick");
        Iterator iterator = lobbies.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();

            Integer lobbyID = (Integer) entry.getKey();
            GameLobby gameLobby = (GameLobby) entry.getValue();
            if (gameLobby.onlinePlayers() == 0) {
                System.out.println("Shutting down lobby " + lobbyID + " as it has no players online.");
                iterator.remove();
                continue;
            }
            //Lobby logic
//            gameLobby.runUpdates();

        }
        for (GameLobby lobby : lobbies.values()) {
            lobby.runUpdates();
        }
        nextTick = System.currentTimeMillis() + 500;
    }

    private void startWebServers(int port) throws Exception {
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});
        //Register Routes
        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath("/*");

        //Webpages
        handler.addServlet(IndexRoute.class, "/");
//        handler.addServlet(IndexRoute.class, "/index"); // Route for homepage
        handler.addServlet(UpdateRoute.class, "/update"); // Route for POST update endpoint
        handler.addServlet(TimerRunRoute.class, "/timer-run");
        handler.addServlet(CreateUser.class, "/api/create-user");
        handler.addServlet(ChatRoute.class, "/chat");

        handler.addServlet(GenericError.class, "/error");

        ErrorPageErrorHandler errorPageErrorHandler = new ErrorPageErrorHandler();
        errorPageErrorHandler.addErrorPage(404, "/error");
        errorPageErrorHandler.addErrorPage(500, "/error");
        handler.setErrorHandler(errorPageErrorHandler);


        server.setHandler(handler);


        //Start Server
        server.start();
        server.join();


    }

    public GameLobby getLobby(Integer sessionID) {
        if (this.lobbies.containsKey(sessionID)) {
            return this.lobbies.get(sessionID);
        }
        GameLobby lobby = new GameLobby(sessionID);
        this.lobbies.put(sessionID, lobby);
        return lobby;
    }


}