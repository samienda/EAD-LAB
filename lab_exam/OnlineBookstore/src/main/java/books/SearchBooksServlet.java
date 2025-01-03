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

@WebServlet("/searchBooks")
public class SearchBooksServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection manager instance
    private DBConnectionManager dbManager = new DBConnectionManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the search query from the request
        String searchQuery = request.getParameter("title");

        // Prepare response writer to send feedback to the client
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Open database connection
            dbManager.connect();
            Connection conn = dbManager.getConnection();

            // Prepare the SQL query to search for tasks by description
            String query = "SELECT * FROM books WHERE title LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Set query parameter (use % for partial matches)
            stmt.setString(1, "%" + searchQuery + "%");

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // Start generating the HTML response
            out.println("<html>");
            out.println("<head><title>Search Results</title></head>");
            out.println("<body>");
            out.println("<h2>Search Results for: \"" + searchQuery + "\"</h2>");

            // Check if there are results
            if (!rs.isBeforeFirst()) {
                out.println("<p>No books found matching the search query.</p>");
            } else {
                // Generate an HTML table for the results
                out.println("<table border='1' cellspacing='0' cellpadding='5'>");
                out.println("<tr><th>ID</th><th>Description</th><th>Status</th><th>Due Date</th></tr>");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String price = rs.getString("price");

                    out.println("<tr>");
                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + title + "</td>");
                    out.println("<td>" + author + "</td>");
                    out.println("<td>" + price + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
            }

            out.println("</body>");
            out.println("</html>");

            // Close the result set and statement
            rs.close();
            stmt.close();
        } catch (Exception e) {
            // Handle exceptions and respond with an error message
            e.printStackTrace();
            out.println("<h2>An error occurred while searching for tasks.</h2>");
        } finally {
            try {
                // Close the database connection
                dbManager.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
