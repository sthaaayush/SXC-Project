<%-- 
    Document   : welcome
    Created on : Jan 25, 2024, 6:46:33 PM
    Author     : aayus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="style.css">
        <title>Modern Login Page</title>
    </head>

    <body>
        
        <%
            HttpSession is_logged = request.getSession(false);
            boolean login_status = (boolean) is_logged.getAttribute("logged");
            out.println(login_status);
            if (login_status) {
                out.println("<a href='logoutMap'>Log Out</a>");
            } else {
                RequestDispatcher dispatch = request.getRequestDispatcher("index.jsp");
                dispatch.forward(request, response);
                out.println("not ok");
            }
        %>
    </body>

</html>