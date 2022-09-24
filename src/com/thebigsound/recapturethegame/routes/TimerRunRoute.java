package com.thebigsound.recapturethegame.routes;

import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TimerRunRoute extends NormalRoute {

    protected void doPost(HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        this.launcher.runTimerTasks();
        JSONObject responseObject = new JSONObject();
        responseObject.put("error", false);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(responseObject.toJSONString());

    }

}
