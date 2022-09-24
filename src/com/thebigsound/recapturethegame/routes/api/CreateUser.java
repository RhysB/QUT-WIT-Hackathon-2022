package com.thebigsound.recapturethegame.routes.api;

import com.thebigsound.recapturethegame.game.GameLobby;
import com.thebigsound.recapturethegame.game.Player;
import com.thebigsound.recapturethegame.routes.NormalRoute;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateUser extends NormalRoute {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        Integer sessionID = Integer.parseInt(request.getParameter("gameID"));

        System.out.println("Connection Request: Username: " + username + ", GameID: " + sessionID);

        GameLobby gameLobby = this.launcher.getLobby(sessionID);
        Player player = new Player(username, sessionID, gameLobby);
        gameLobby.playerConnect(player);

        response.sendRedirect("../chat?sessionID=" + player.getSessionID());

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        Integer sessionID = Integer.parseInt(request.getParameter("gameID"));

        if (username == null || sessionID == null) {
            this.returnError(response, "Either username, or sessionID is null in the get parameters.");
            return;
        }

        GameLobby gameLobby = this.launcher.getLobby(sessionID);
        Player player = new Player(username, sessionID, gameLobby);
        gameLobby.playerConnect(player);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionID", player.getSessionID().toString());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(jsonObject.toJSONString());

    }
}
