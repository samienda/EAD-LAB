package books;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// SAMUEL ENDALE UGR/9314/14

@WebServlet("/searchBooks")
public class SearchBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    private DBConnectionManager dbManager = new DBConnectionManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String searchQuery = request.getParameter("title");


        response.setContentType("text/html");
        PrintWriter responseWriter = response.getWriter();

        try {

            dbManager.connect();
            Connection dbConnection = dbManager.getConnection();


            String query = "SELECT * FROM books WHERE title LIKE ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);


            preparedStatement.setString(1, "%" + searchQuery + "%");


            ResultSet rs = preparedStatement.executeQuery();

            responseWriter.println("<html>");
            responseWriter.println("<head><title>Search Results</title></head>");
            responseWriter.println("<body>");
            responseWriter.println("<h2>Search Results for: \"" + searchQuery + "\"</h2>");

            if (!rs.isBeforeFirst()) {
                responseWriter.println("<p>No books found matching the search query.</p>");
            } else {

                responseWriter.println("<table border='1' cellspacing='0' cellpadding='5'>");
                responseWriter.println("<tr><th>ID</th><th>Description</th><th>Status</th><th>Due Date</th></tr>");

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
            }

            responseWriter.println("</body>");
            responseWriter.println("</html>");

            rs.close();
            preparedStatement.close();
        } catch (Exception e) {

            e.printStackTrace();
            responseWriter.println("<h2>An error occurred while searching for tasks.</h2>");
        } finally {
            try {

                dbManager.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
