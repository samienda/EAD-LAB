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

// SAMUEL ENDALE UGR/9314/14

@WebServlet("/deleteBook")
public class DeleteBookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    private DBConnectionManager dbManager = new DBConnectionManager();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String bookId = request.getParameter("id");


        response.setContentType("text/html");
        PrintWriter responseWriter = response.getWriter();

        try {

            dbManager.connect();
            Connection dbConnection = dbManager.getConnection();


            String query = "DELETE FROM books WHERE id = ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);

            preparedStatement.setInt(1, Integer.parseInt(bookId));

            int rowsAffected = preparedStatement.executeUpdate();


            if (rowsAffected > 0) {
                responseWriter.println("<h2>Books with ID " + bookId + " deleted successfully!</h2>");
            } else {
                responseWriter.println("<h2>No books found with ID " + bookId + ".</h2>");
            }

            preparedStatement.close();
        } catch (Exception e) {

            e.printStackTrace();
            responseWriter.println("<h2>An error occurred while deleting the book.</h2>");
        } finally {
            try {

                dbManager.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
