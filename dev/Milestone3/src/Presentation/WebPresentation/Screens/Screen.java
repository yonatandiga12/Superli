package Presentation.WebPresentation.Screens;

import Presentation.BackendController;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.ViewModels.HR.EmployeeServlet;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;
import Presentation.WebPresentation.WebMain;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public abstract class Screen extends HttpServlet {

    /***
     * greeting message, used in greet method
     */
    private String greeting;

    /***
     * see the isAllowed method
     * allowed == null -> anyone may visit
     * allowed.length == 0 -> any logged in user may visit
     * else -> only the types in the array may visit
     */
    private final Set<Class<? extends Employee>> allowed;

    private String error = null;

    public static BackendController controller = new BackendController();

    public Screen(String greeting, Set<Class<? extends Employee>> allowed) {
        this.greeting = greeting;
        this.allowed = allowed;
    }

    public Screen(String greeting) {
        this(greeting, null);
    }

    /***
     * Prints greeting as a heading
     * @param resp the response to print to
     * @throws IOException
     */
    protected void greet(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println(String.format("<h1>%s</h1>", greeting));
    }

    /***
     * basic header for all pages
     * @param resp the response to write to
     * @throws IOException
     */
    protected void header(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<header>");
        out.println("<form method=\"post\">");
        out.println("<input type=\"submit\" name=\"home\" value=\"home\">");
        out.println("<input type=\"submit\" name=\"logout\" value=\"logout\">");
        out.println("<input type=\"submit\" name=\"clean\" value=\"clean cookies\" style=\"direction: rtl;\">");
        out.println("</form>");
        out.println("</header>");
    }

    /***
     * handles post possible from the header
     * @param req the request sent
     * @param resp the response to write to
     * @return true if a post occurred and handle from the header
     * @throws IOException
     */
    protected static boolean handleHeader(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (isButtonPressed(req, "home")) {
            redirect(resp, EmployeeServlet.class);
            return true;
        }
        if (isButtonPressed(req, "logout")){
            if (Login.isLoggedIn(req, resp))
                Login.logout(req, resp);
            redirect(resp, Login.class);
            return true;
        }
        if (isButtonPressed(req, "clean")) {
            Cookie[] cookies = req.getCookies();
            if (cookies != null)
                for (Cookie c : cookies) {
                    c.setMaxAge(0);
                    resp.addCookie(c);
                }
            refresh(req, resp);
            return true;
        }
        return false;
    }

    protected void setError(String error) {
        this.error = error;
    }

    protected String getError() {
        return error;
    }

    protected void cleanError(){
        error = null;
    }

    protected boolean isError(){
        return error != null;
    }

    /***
     * Prints error as red text if screen has an error logged
     * @param resp the response to print to
     * @throws IOException
     */
    protected void handleError(HttpServletResponse resp) throws IOException {
        if (!isError())
            return;
        PrintWriter out = resp.getWriter();
        out.println(String.format("<p style=\"color:red\">%s</p><br><br>", getError()));
        cleanError();
    }

    /***
     * checks if the visitor has permission to visit this page
     * allowed == null -> anyone may visit
     * allowed.length == 0 -> any logged in user may visit
     * else -> only the types in the array may visit
     * @param req the request sent
     * @param resp the response to send
     * @return true if visiting this screen is allowed
     */
    protected boolean isAllowed(HttpServletRequest req, HttpServletResponse resp) {
        return allowed == null ||
                (Login.isLoggedIn(req, resp) &&
                        (allowed.isEmpty() || allowed.contains(Login.getLoggedUser(req).getClass())));
    }

    protected static boolean isAllowed(HttpServletRequest req, HttpServletResponse resp, Set<Class<? extends Employee>> allowed) {
        return allowed == null ||
                (Login.isLoggedIn(req, resp) &&
                        (allowed.isEmpty() || allowed.contains(Login.getLoggedUser(req).getClass())));
    }

    /***
     * prints a form of submit buttons.
     * buttons names are the index of their value in menuOptions
     * @param resp the response to print to
     * @param menuOptions the values of the buttons in the menu
     * @throws IOException
     */
    public static void printMenu(HttpServletResponse resp, String[] menuOptions) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<form method=\"post\">");
        for (int i = 0; i < menuOptions.length; i++)
            out.println(String.format("<input type=\"submit\" name=\"%s\" value=\"%s\"><br><br>", i, menuOptions[i]));
        out.println("</form>");
    }

    /***
     * prints a form to fill.
     * buttons names are their values.
     * placeholders' length is expected to match textBoxes' length
     * @param resp the response to print to
     * @param textBoxes the names of the text boxes
     * @param placeholders the hints for the text boxes
     * @param buttons buttons of the form
     * @throws IOException
     */
    protected static void printForm(HttpServletResponse resp, String[] textBoxes, String[] placeholders, String[] buttons) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<form method=\"post\">\n");
        for (int i = 0; i < textBoxes.length; i++)
            out.println(String.format("<input type=\"text\" name=\"%s\" placeholder=\"%s\"><br><br>", textBoxes[i], placeholders[i]));
        for (String button : buttons)
            out.println(String.format("<input type=\"submit\" name=\"%s\" value=\"%s\"><br><br>", button, button));
        out.println("</form>");
    }

    /***
     * checks if any button whose name is a positive integer was pressed.
     * @param req the sent request
     * @return the name of the pressed button if pressed, else returns -1
     * @throws ServletException
     * @throws IOException
     */
    protected static int getIndexOfButtonPressed(HttpServletRequest req) throws ServletException, IOException {
        for (int i = 0; i < 100 ; i++) {
            if (req.getParameter(String.valueOf(i)) != null)
                return i;
        }
        return -1;
    }

    /***
     * checks if a button of name pressedButton was pressed
     * @param req the sent request
     * @param pressedButton the name of the button
     * @return true if the button with the given name was pressed, else returns false
     */
    protected static boolean isButtonPressed(HttpServletRequest req, String pressedButton) {
        return req.getParameter(pressedButton) != null;
    }

    /***
     * sends the response to the page from the request
     * @param req the sent request
     * @param resp the response to return
     * @param params parameters to be sent to the GET on refresh
     * @param paramVals values for params: paramVals[i] is the value for params[i]
     * @throws IOException
     */
    protected static void refresh(HttpServletRequest req, HttpServletResponse resp, String[] params, String[] paramVals) throws IOException {
        resp.sendRedirect(req.getServletPath() + buildGetParams(params, paramVals));
    }

    protected static void refresh(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        refresh(req, resp, null, null);
    }

    /***
     * redirects the response to the path of the given servlet
     * assumes the given servlet is registered at WebMain.servletToPath
     * @param resp the response to redirect
     * @param redirectTo the servlet we want to serve
     * @param params parameters to be sent to the GET on redirect
     * @param paramVals values for params: paramVals[i] is the value for params[i]
     * @throws IOException
     */
    protected static void redirect(HttpServletResponse resp, Class<? extends Servlet> redirectTo, String[] params, String[] paramVals) throws IOException {
        resp.sendRedirect(WebMain.servletToPath.get(redirectTo) + buildGetParams(params, paramVals));
    }

    protected static void redirect(HttpServletResponse resp, Class<? extends Servlet> redirectTo) throws IOException {
        redirect(resp, redirectTo, null, null);
    }

    /***
     * @param req the request sent
     * @param paramName the name of the parameter we want it's value
     * @return null if no parameter with name paramName was sent else its value if
     */
    protected static String getParamVal(HttpServletRequest req, String paramName) {
        return req.getParameter(paramName);
    }

    private static String buildGetParams(String[] params, String[] paramVals){
        if (params == null)
            return "";
        String[] paramsAndValues = new String[params.length];
        for (int i = 0; i < params.length; i++)
            paramsAndValues[i] = params[i] + "=" + paramVals[i];
        return "?" + String.join("&", paramsAndValues);
    }

    @Override
    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    @Override
    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
}