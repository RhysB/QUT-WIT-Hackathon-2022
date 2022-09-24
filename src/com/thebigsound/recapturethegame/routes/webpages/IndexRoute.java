package com.thebigsound.recapturethegame.routes.webpages;

import com.thebigsound.recapturethegame.Launcher;
import com.thebigsound.recapturethegame.routes.NormalRoute;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

public class IndexRoute extends NormalRoute {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClassLoader classLoader = Launcher.class.getClassLoader();
        URL indexURL = classLoader.getResource("resources/index.html");
        if (indexURL == null) {
            System.out.println("Unable to find index file");
        }
        InputStream inputStream = indexURL.openStream();
        String index = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));

        //Send Page
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(index);

    }
}
