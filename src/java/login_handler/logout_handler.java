package login_handler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author aayus
 */
@WebServlet(name = "logout_handler", urlPatterns = {"/logout_handler"})
public class logout_handler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            //Invalidated session of user login
            HttpSession login_check = request.getSession(false);
            login_check.invalidate();
            //Dispatch to home page 
            RequestDispatcher dispatch = request.getRequestDispatcher("index.html");
            dispatch.include(request, response);
        }
    }
}
