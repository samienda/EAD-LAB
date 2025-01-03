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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Servlet for handling book registration requests.
 */
@WebServlet("/register")
public class BookRegistrationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Extract task details from the HTTP request
        String bookTitle = request.getParameter("title");
        String bookAuthor = request.getParameter("author");
        String bookPrice = request.getParameter("price");

        // Set response content type and obtain writer
        response.setContentType("text/html");
        PrintWriter responseWriter = response.getWriter();

        // Load Spring application context and retrieve DB connection manager bean
        ApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        DBConnectionManager dbManager = appContext.getBean("dbConnectionManager", DBConnectionManager.class);

        try {
            // Establish a connection to the database
            dbManager.connect();
            Connection dbConnection = dbManager.getConnection();

            // Prepare SQL query for inserting task data
            String insertQuery = "INSERT INTO books (title, author, price) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(insertQuery);

            // Bind query parameters with task details
            preparedStatement.setString(1, bookTitle);
            preparedStatement.setString(2, bookAuthor);
            preparedStatement.setString(3, bookPrice);

            // Execute the query and handle the outcome
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                responseWriter.println("<h2>Book successfully added!</h2>");
            } else {
                responseWriter.println("<h2>Failed to add the book.</h2>");
            }

            // Close the prepared statement
            preparedStatement.close();
        } catch (Exception exception) {
            // Log exceptions and send an error message to the client
            exception.printStackTrace();
            responseWriter.println("<h2>Error occurred during book registration please try again.</h2>");
        } finally {
            try {
                // Disconnect from the database
                dbManager.disconnect();
            } catch (Exception disconnectException) {
                disconnectException.printStackTrace();
            }
        }
    }
}
