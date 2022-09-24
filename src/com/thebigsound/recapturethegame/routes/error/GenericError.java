package com.thebigsound.recapturethegame.routes.error;




import com.thebigsound.recapturethegame.routes.NormalRoute;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GenericError extends NormalRoute {

    protected void doGet(HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<!DOCTYPE html>\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
                "<head>\n" +
                "  <title>No encontrado ;)</title>\n" +
                "  <style>\n" +
                "    body {\n" +
                "      background-color: #272B30;\n" +
                "      font-family: \"Arial\";\n" +
                "      height: 100%\n" +
                "    }\n" +
                "    h1 {\n" +
                "      color: #BBBFC2;\n" +
                "      text-shadow: -1px -1px 0 rgba(0, 0, 0, 0.3);\n" +
                "    }\n" +
                "   .footer {\n" +
                "     position: absolute;\n" +
                "     text-align: center;\n" +
                "     width: 100%;\n" +
                "     bottom: 0px;\n" +
                "     width: 100%;\n" +
                "     color: #BBBFC2;\n" +
                "     font-family: \"Arial\";\n" +
                "     text-shadow: -1px -1px 0 rgba(0, 0, 0, 0.3);\n" +
                "     font-size:80%\n" +
                "}\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <html>\n" +
                "    <center><h1><img src=\"https://i.imgur.com/h0lneyq.jpg\"/></center>\n" +
                "    <div class=\"footer\"></div>\n" +
                "  </html>\n" +
                "</body>\n" +
                "</html>");

    }


}
