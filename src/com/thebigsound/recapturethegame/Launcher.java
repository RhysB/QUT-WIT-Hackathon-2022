package com.thebigsound.recapturethegame;

import com.thebigsound.recapturethegame.game.GameLobby;
import com.thebigsound.recapturethegame.game.Player;
import com.thebigsound.recapturethegame.routes.webpages.IndexRoute;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
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

    private static Launcher launcher;

    public static Launcher getLauncher() {
        return Launcher.launcher;
    }

    public static void main(String[] args) throws Exception {
        launcher = new Launcher(args);
    }


    private Launcher(String[] args) throws Exception {
        System.out.println("Starting ReCapture The Game");
        startWebServers(7683);

    }

    public void runTimerTasks() {
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
            gameLobby.runUpdates();

        }

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
        handler.addServlet(IndexRoute.class, "/index"); // Route for homepage


        //API


//        handler.addServlet(GetServersRoute.class, "/api/v1/getServers");
//        handler.addServlet(ServerPingRoute.class, "/api/v1/serverPing");
//        handler.addServlet(GetServerRoute.class, "/api/v1/getServer");
//        handler.addServlet(TimerRunRoute.class, "/timer-run");
//        handler.addServlet(HomeRoute.class, "/");

        handler.addServlet(GenericServlet.class, "/error");

        ErrorPageErrorHandler errorPageErrorHandler = new ErrorPageErrorHandler();
        errorPageErrorHandler.addErrorPage(404, "/error");
        errorPageErrorHandler.addErrorPage(500, "/error");
        handler.setErrorHandler(errorPageErrorHandler);


        server.setHandler(handler);


        //Start Server
        server.start();
        server.join();


    }
}