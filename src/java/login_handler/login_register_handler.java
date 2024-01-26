package login_handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import security_protocol.hashing;

@WebServlet(name = "login_register_handler", urlPatterns = {"/login_register_handler"})
public class login_register_handler extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            //Checks if request if of post type
            if (request.getMethod().equalsIgnoreCase("post")) {
                out.println("<h1>Running</h1>");
                //Storing client data in variables
                String uemail = request.getParameter("uemail");
                String upass = request.getParameter("upassword");
                String check_flag = request.getParameter("submit");
                //Hash the validated password
                if (!upass.equals(" ")) {
                    upass = hashing.hashMD5(upass);
                } else {
                    upass = "error";
                }
                //SQL to get and set data in database
                String set_data = "INSERT INTO login_details (name, email, password) VALUES (?, ?, ?);";
                String get_data = "SELECT name, email, password FROM login_details;";

                //Database connection process
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost/remote_test_env_db", "root", "");

                    //Handle request if its for register or signUp
                    if (check_flag.equals("signUp")) {
                        //storing client name in variables 
                        String uname = request.getParameter("uname");
                        //Create statement to execute sql query
                        PreparedStatement signUp_statement = con.prepareCall(set_data);
                        //setting data to send through sq;
                        signUp_statement.setString(1, uname);
                        signUp_statement.setString(2, uemail);
                        signUp_statement.setString(3, upass);
                        int update_flag = signUp_statement.executeUpdate();
                        //checking if row is updated by data or not
                        if (update_flag != 0) {
                            out.println("Registered Sucessfully");
                            RequestDispatcher dispatch = request.getRequestDispatcher("index.html");
                            dispatch.forward(request, response);
                        } else {
                            out.println("Error Occured");
                            RequestDispatcher dispatch = request.getRequestDispatcher("LogMap");
                            dispatch.forward(request, response);
                        }
                    } //Handle request if its for login or signIn
                    else {
                        //Preparing statement to retrive data in database
                        PreparedStatement signIn_statement = con.prepareCall(get_data);
                        //Storing retrive data from database
                        ResultSet user_logs_data = signIn_statement.executeQuery();
                        //Initilizing flags and name for validation
                        boolean user_validate_flag = false;
                        String user_name = null;
                        //Verifying user with data in database
                        while (user_logs_data.next()) {
                            String gEmail = user_logs_data.getString("email"),
                                    gPass = user_logs_data.getString("password");
                            user_name = user_logs_data.getString("name");
                            //Execute and break loop when user is verified
                            if (gEmail.equals(uemail) && gPass.equals(upass)) {
                                user_validate_flag = true;
                                break;
                            }
                            //Or else it dont verify user
                        }
                        if (user_validate_flag) {
                            out.println("<h1>Welcome " + user_name + "</h1>");
                            HttpSession is_logged = request.getSession();
                            is_logged.setAttribute("logged", true);
                            out.println("<a href='logoutMap'>Log Out</a>");
                        } else {
                            out.println("Wrong Email or Password");
                        }
                    }
                    con.close();
                } catch (Exception e) {
                    out.println("Error occured: " + e.getMessage());
                }
            }
        }
    }
}
