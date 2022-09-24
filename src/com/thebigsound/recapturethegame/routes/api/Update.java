package com.thebigsound.recapturethegame.routes.api;

import com.thebigsound.recapturethegame.game.Player;
import com.thebigsound.recapturethegame.routes.NormalRoute;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

public class Update extends NormalRoute {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jsonDataRaw = request.getReader().lines().collect(Collectors.joining());
        if (jsonDataRaw.isEmpty() || jsonDataRaw.trim().isEmpty()) {
            returnError(response, "No JSON body");
            return;
        }

        JSONObject jsonObject;
        UUID uuid = UUID.fromString(request.getParameter("userID"));

        try {
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(jsonDataRaw);
        } catch (Exception e) {
            returnError(response, "Malformed JSON.");
            return;
        }

        if (!this.launcher.getSessionIDToPlayer().containsKey(uuid)) {
            this.returnError(response, "Unable to find UUID in UUID to Player hashmap");
            return;
        }

        Player player = this.launcher.getSessionIDToPlayer().get(uuid);


        System.out.println(player.getUsername() + " Has requested updates.");


        JSONArray jsonArray = new JSONArray();
        for (JSONObject jsonObject1 : player.getJsonUpdates()) {
            jsonArray.add(jsonObject1);
        }

        player.getJsonUpdates().clear(); //Clear the updates after they have been sent.

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(jsonArray.toJSONString());
    }
}
