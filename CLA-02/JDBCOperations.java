import java.sql.*;

public class JDBCOperations {
    static final String URL = "jdbc:sqlite:javaapp.db";

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL);
             Statement st = con.createStatement()) {

            // 1. Create table
            st.executeUpdate("DROP TABLE IF EXISTS student");
            st.executeUpdate(
                "CREATE TABLE student (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT, " +
                "branch TEXT, " +
                "email TEXT, " +
                "marks INT, " +
                "city TEXT)"
            );
            System.out.println("SQLite table created successfully.");

            // 2. Insert eight sample records
            String insertSQL =
                "INSERT INTO student VALUES " +
                "(1,'SNEHITH RAJ','CSE','snehithraj@gmail.com',91,'Vijayawada')," +
                "(2,'VARSITA','CSE','varsita@gmail.com',96,'Mangalagiri')," +
                "(3,'SAZIDA','CSE','sazida@gmail.com',88,'Guntur')," +
                "(4,'SOHAN','CSE','sohan@gmail.com',92,'Hyderabad')," +
                "(5,'SUSANTH','AI&DS','susanth@gmail.com',94,'Visakhapatnam')," +
                "(6,'RAMU','ECE','ramu@gmail.com',79,'Guntur')," +
                "(7,'MANOJ','CSE','manoj@gmail.com',90,'Mangalagiri')," +
                "(8,'SNEHITH RAJ','IT','snehithraj10713@gmail.com',85,'Vijayawada')";
            st.executeUpdate(insertSQL);
            System.out.println("8 records inserted.");
            showAll(st, "ALL RECORDS AFTER INSERT");

            // 3. Update a record
            int updated = st.executeUpdate(
                "UPDATE student SET branch='AI&DS', marks=89 WHERE id=3"
            );
            System.out.println("\nRows updated: " + updated);
            showAll(st, "RECORDS AFTER UPDATE");

            // 4. Select a record by ID
            System.out.println("\nSEARCH RESULT: STUDENT WITH ID = 2");
            try (ResultSet rs = st.executeQuery("SELECT * FROM student WHERE id=2")) {
                printRecords(rs);
            }

            // 5. Select records using a condition
            System.out.println("\nSTUDENTS WITH MARKS >= 90");
            try (ResultSet rs = st.executeQuery(
                    "SELECT * FROM student WHERE marks >= 90 ORDER BY marks DESC")) {
                printRecords(rs);
            }

            // 6. Delete a record
            int deleted = st.executeUpdate("DELETE FROM student WHERE id=8");
            System.out.println("\nRows deleted: " + deleted);
            showAll(st, "RECORDS AFTER DELETE");

            System.out.println("\nProgram completed successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showAll(Statement st, String title) throws SQLException {
        System.out.println("\n--- " + title + " ---");
        try (ResultSet rs = st.executeQuery("SELECT * FROM student ORDER BY id")) {
            printRecords(rs);
        }
    }

    private static void printRecords(ResultSet rs) throws SQLException {
        System.out.println("ID | Name | Branch | Email | Marks | City");
        System.out.println("---------------------------------------------------------------");
        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println(
                rs.getInt("id") + " | " +
                rs.getString("name") + " | " +
                rs.getString("branch") + " | " +
                rs.getString("email") + " | " +
                rs.getInt("marks") + " | " +
                rs.getString("city")
            );
        }
        if (!found) {
            System.out.println("No records found.");
        }
    }
}
