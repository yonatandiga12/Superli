package Presentation.WebPresentation.Screens;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TestScreen extends Screen {
    public TestScreen() {
        super("This is a test page", null);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        header(resp);
        greet(resp);
        printMenu(resp, new String[]{"test0", "test1", "test2"});
        printForm(resp, new String[] {"fname", "lname"}, new String[]{"First name", "Last name"}, new String[]{"submit"});
        PrintWriter out = resp.getWriter();
        out.println("<h2>");
        out.println("This is custom");
        out.println("</h2>");
        out.println("<img src=\"http://store-images.s-microsoft.com/image/apps.36761.14424832148383988.585c1645-e3a7-439f-a690-30562bf63916.8866f19c-f81d-4074-9e56-399fa2e5052d\" alt=\"alternatetext\">");
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if (isButtonPressed(req, "submit")) {
            setError(String.format("Welcome %s %s", req.getParameter("fname"), req.getParameter("lname")));
            refresh(req, resp);
            return;
        }
        switch (getIndexOfButtonPressed(req)){
            case 0:
                setError("Error: You pressed test0");
                refresh(req, resp);
                break;
            case 1:
                setError("Error: You pressed test1");
                refresh(req, resp);
                break;
            case 2:
                setError("Error: You pressed test2");
                refresh(req, resp);
                break;
        }
    }
}
