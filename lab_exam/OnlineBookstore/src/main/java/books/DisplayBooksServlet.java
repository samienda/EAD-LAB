package books;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// SAMUEL ENDALE UGR/9314/14

@WebServlet("/displayBooks")
public class DisplayBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    private DBConnectionManager dbManager = new DBConnectionManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter responseWriter = response.getWriter();

        try {

            dbManager.connect();
            Connection dbConnection = dbManager.getConnection();


            String query = "SELECT * FROM books";
            Statement preparedStatement = dbConnection.createStatement();
            ResultSet rs = preparedStatement.executeQuery(query);

            responseWriter.println("<html>");
            responseWriter.println("<head><title>Book List</title></head>");
            responseWriter.println("<body>");
            responseWriter.println("<h2>Book List</h2>");
            responseWriter.println("<table border='1' cellspacing='0' cellpadding='5'>");
            responseWriter.println("<tr><th>ID</th><th>Title</th><th>Author</th><th>Price</th></tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String price = rs.getString("price");

                responseWriter.println("<tr>");
                responseWriter.println("<td>" + id + "</td>");
                responseWriter.println("<td>" + title + "</td>");
                responseWriter.println("<td>" + author + "</td>");
                responseWriter.println("<td>" + price + "</td>");
                responseWriter.println("</tr>");
            }

            responseWriter.println("</table>");
            responseWriter.println("</body>");
            responseWriter.println("</html>");

            rs.close();
            preparedStatement.close();
        } catch (Exception e) {

            e.printStackTrace();
            responseWriter.println("<h2>An error occurred while retrieving the books.</h2>");
        } finally {
            try {

                dbManager.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
