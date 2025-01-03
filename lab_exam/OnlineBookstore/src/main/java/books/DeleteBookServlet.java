package books;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/deleteBook")
public class DeleteBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection manager instance
    private DBConnectionManager dbManager = new DBConnectionManager();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve book ID from the request
        String bookId = request.getParameter("id");

        // Prepare response writer to send feedback to the client
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Open database connection
            dbManager.connect();
            Connection conn = dbManager.getConnection();

            // Prepare the SQL query to delete the book
            String query = "DELETE FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            // Set query parameter
            stmt.setInt(1, Integer.parseInt(bookId));

            // Execute the query
            int rowsAffected = stmt.executeUpdate();

            // Respond with a success message
            if (rowsAffected > 0) {
                out.println("<h2>Books with ID " + bookId + " deleted successfully!</h2>");
            } else {
                out.println("<h2>No books found with ID " + bookId + ".</h2>");
            }

            // Close the statement
            stmt.close();
        } catch (Exception e) {
            // Handle exceptions and respond with an error message
            e.printStackTrace();
            out.println("<h2>An error occurred while deleting the book.</h2>");
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
