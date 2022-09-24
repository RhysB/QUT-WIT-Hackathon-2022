package com.thebigsound.recapturethegame.routes.webpages;

import com.thebigsound.recapturethegame.routes.NormalRoute;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ChatRoute extends NormalRoute {


    protected void doGet(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        //Check if page exists in the cache
        String page = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "\n" +
                "\n" +
                "    <script>\n" +
                "        //Get sessionID\n" +
                "        var chatMessages = [];\n" +
                "\n" +
                "        var currentURL = window.location.protocol + \"//\" + window.location.host + \"/\" + window.location.pathname + window.location.search\n" +
                "        var url = new URL(currentURL);\n" +
                "        var sessionID = url.searchParams.get(\"sessionID\");\n" +
                "        console.log(sessionID);\n" +
                "\n" +
                "        setInterval(runCycle, 1000);\n" +
                "        setTimeout(runCycle, 500);\n" +
                "\n" +
                "\n" +
                "        //Run every how ever often\n" +
                "        console.log(\"Using: \" + (window.location.protocol + \"//\" + window.location.host + \"\" + window.location.pathname + \"/update?userID=\" + sessionID))\n" +
                "\n" +
                "        function sendChatMessage(message) {\n" +
                "            this.chatMessages.push(message);\n" +
                "        }\n" +
                "\n" +
                "\n" +
                "        function runCycle() {\n" +
                "            let xhr = new XMLHttpRequest();\n" +
                "            xhr.open(\"POST\", \"./update?userID=\" + sessionID);\n" +
                "            // xhr.open(\"POST\", \"http://localhost:7683/update?userID=\" + sessionID)\n" +
                "            xhr.setRequestHeader(\"Accept\", \"application/json\");\n" +
                "            xhr.setRequestHeader(\"Content-Type\", \"application/json\");\n" +
                "\n" +
                "            xhr.onreadystatechange = function () {\n" +
                "                if (xhr.readyState === 4) {\n" +
                "                    console.log(xhr.status);\n" +
                "                    console.log(xhr.responseText);\n" +
                "                }\n" +
                "            };\n" +
                "\n" +
                "            let jsonUpdateArray = JSON.parse(\"[]\");\n" +
                "\n" +
                "            //Queue chat messages\n" +
                "            for (let i = 0; i < chatMessages.length; i++) {\n" +
                "                var jsonUpdate = \"{\\n\" +\n" +
                "                    \"                    \\\"type\\\": \\\"message\\\",\\n\" +\n" +
                "                    \"                    \\\"message\\\": \" + chatMessages[i] + \"\\n\" +\n" +
                "                    \"                }\"\n" +
                "                jsonUpdateObject = JSON.parse(jsonUpdate)\n" +
                "                jsonUpdateArray.push(jsonUpdateArray)\n" +
                "\n" +
                "            }\n" +
                "\n" +
                "            let data = JSON.stringify(jsonUpdateArray);\n" +
                "\n" +
                "            xhr.send(data);\n" +
                "\n" +
                "            console.log(\"Cycle Run\")\n" +
                "        }\n" +
                "    </script>\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        //Send Page
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(page);


    }
//
//    private static double round(double value, int places) {
//        if (places < 0) throw new IllegalArgumentException();
//
//        BigDecimal bd = BigDecimal.valueOf(value);
//        bd = bd.setScale(places, RoundingMode.HALF_UP);
//        return bd.doubleValue();
//    }
}