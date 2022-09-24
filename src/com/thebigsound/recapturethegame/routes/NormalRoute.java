package com.thebigsound.recapturethegame.routes;


import com.thebigsound.recapturethegame.Launcher;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.StringTokenizer;

public class NormalRoute extends HttpServlet {
    public Launcher launcher = Launcher.launcher;

    protected void returnError(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        JSONObject errorObject = new JSONObject();
        errorObject.put("error", true);
        errorObject.put("error_message", errorMessage);
        response.getWriter().println(errorObject.toJSONString());
    }

    protected String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }
    protected boolean verifyJSONArguments(JSONObject jsonObject, String... arguments) {
        for (String s : arguments) {
            if (!jsonObject.containsKey(s)) return false;
        }
        return true;
    }
}
