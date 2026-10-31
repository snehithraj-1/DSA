import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentSystem extends Frame implements ActionListener {
    TextField sid, sname, email, branch, marks, city;
    TextField cid, cname, credits, searchId;
    TextArea output;
    static final String URL = "jdbc:sqlite:javaapp.db";

    Color background = new Color(18, 25, 40);
    Color panelColor = new Color(30, 41, 59);
    Color inputColor = new Color(15, 23, 42);
    Color textColor = new Color(226, 232, 240);
    Color accentColor = new Color(14, 165, 233);
    Color secondaryColor = new Color(45, 212, 191);

    StudentSystem() {
        setTitle("Student Information System");
        setSize(900, 700);
        setLayout(new BorderLayout(12, 12));
        setBackground(background);

        Panel main = new Panel(new GridLayout(1, 2, 15, 0));
        main.setBackground(background);

        // Student panel
        Panel students = new Panel(new BorderLayout(5, 10));
        students.setBackground(panelColor);
        Label studentTitle = new Label("STUDENT DETAILS");
        styleLabel(studentTitle, 18, true);
        students.add(studentTitle, BorderLayout.NORTH);

        Panel studentFields = new Panel(new GridLayout(6, 2, 8, 12));
        studentFields.setBackground(panelColor);
        sid = addField(studentFields, "Student ID:");
        sname = addField(studentFields, "Student Name:");
        email = addField(studentFields, "Email:");
        branch = addField(studentFields, "Branch:");
        marks = addField(studentFields, "Marks:");
        city = addField(studentFields, "City:");
        
        // Default sample values
        sid.setText("10713");
        sname.setText("SNEHITH RAJ");
        email.setText("snehithraj@gmail.com");
        branch.setText("CSE");
        marks.setText("95");
        city.setText("Vijayawada");
        students.add(studentFields, BorderLayout.CENTER);

        Panel studentButtons = new Panel(new GridLayout(1, 2, 8, 5));
        studentButtons.setBackground(panelColor);
        Button addStudent = makeButton("Add Student", accentColor);
        Button searchStudent = makeButton("Search Student", secondaryColor);
        studentButtons.add(addStudent);
        studentButtons.add(searchStudent);
        students.add(studentButtons, BorderLayout.SOUTH);

        // Course panel
        Panel courses = new Panel(new BorderLayout(5, 10));
        courses.setBackground(panelColor);
        Label courseTitle = new Label("COURSE DETAILS");
        styleLabel(courseTitle, 18, true);
        courses.add(courseTitle, BorderLayout.NORTH);

        Panel courseFields = new Panel(new GridLayout(3, 2, 8, 12));
        courseFields.setBackground(panelColor);
        cid = addField(courseFields, "Course ID:");
        cname = addField(courseFields, "Course Name:");
        credits = addField(courseFields, "Credits:");
        
        cid.setText("303");
        cname.setText("JAVA");
        credits.setText("4");
        courses.add(courseFields, BorderLayout.CENTER);

        Panel courseButtons = new Panel(new GridLayout(1, 2, 8, 5));
        courseButtons.setBackground(panelColor);
        Button addCourse = makeButton("Add Course", accentColor);
        Button searchCourse = makeButton("Search Course", secondaryColor);
        courseButtons.add(addCourse);
        courseButtons.add(searchCourse);
        courses.add(courseButtons, BorderLayout.SOUTH);

        main.add(students);
        main.add(courses);

        Panel top = new Panel(new BorderLayout(10, 15));
        top.setBackground(background);
        top.add(main, BorderLayout.CENTER);

        Panel searchPanel = new Panel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchPanel.setBackground(panelColor);
        Label searchLabel = new Label("Search Student ID:");
        styleLabel(searchLabel, 14, false);
        searchPanel.add(searchLabel);
        searchId = new TextField(15);
        styleField(searchId);
        searchPanel.add(searchId);
        top.add(searchPanel, BorderLayout.SOUTH);
        top.setPreferredSize(new Dimension(880, 300));
        add(top, BorderLayout.NORTH);

        Panel outputPanel = new Panel(new BorderLayout(5, 5));
        outputPanel.setBackground(background);
        Label outputTitle = new Label("SYSTEM OUTPUT");
        styleLabel(outputTitle, 16, true);
        outputPanel.add(outputTitle, BorderLayout.NORTH);

        output = new TextArea("", 12, 80, TextArea.SCROLLBARS_VERTICAL_ONLY);
        output.setEditable(false);
        output.setBackground(inputColor);
        output.setForeground(textColor);
        output.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputPanel.add(output, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.CENTER);

        addStudent.addActionListener(this);
        searchStudent.addActionListener(this);
        addCourse.addActionListener(this);
        searchCourse.addActionListener(this);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
        createTables();
    }

    private TextField addField(Panel panel, String labelText) {
        Label label = new Label(labelText);
        styleLabel(label, 13, false);
        panel.add(label);
        TextField field = new TextField();
        styleField(field);
        panel.add(field);
        return field;
    }

    private void styleLabel(Label label, int size, boolean bold) {
        label.setForeground(textColor);
        label.setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, size));
        label.setBackground(panelColor);
    }

    private void styleField(TextField field) {
        field.setBackground(inputColor);
        field.setForeground(textColor);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private Button makeButton(String text, Color color) {
        Button button = new Button(text);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        return button;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private void createTables() {
        try (Connection con = connect();
             Statement st = con.createStatement()) {
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS student_details (" +
                "id INTEGER PRIMARY KEY, name TEXT, " +
                "email TEXT, branch TEXT, " +
                "marks INTEGER, city TEXT)"
            );
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS course_details (" +
                "id INTEGER PRIMARY KEY, name TEXT, credits INTEGER)"
            );
            output.setText(
                "CONNECTED TO SQLITE SUCCESSFULLY\n\n" +
                "Student and course tables are ready.\n" +
                "Enter details and select an operation."
            );
        } catch (SQLException e) {
            output.setText("Database error: " + e.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        try (Connection con = connect()) {
            if (action.equals("Add Student")) {
                String sql = "INSERT INTO student_details (id,name,email,branch,marks,city) VALUES (?,?,?,?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(sid.getText().trim()));
                    ps.setString(2, sname.getText().trim());
                    ps.setString(3, email.getText().trim());
                    ps.setString(4, branch.getText().trim());
                    ps.setInt(5, Integer.parseInt(marks.getText().trim()));
                    ps.setString(6, city.getText().trim());
                    ps.executeUpdate();
                    output.setText(
                        "STUDENT ADDED SUCCESSFULLY\n\n" +
                        "Student ID : " + sid.getText() +
                        "\nName       : " + sname.getText() +
                        "\nEmail      : " + email.getText() +
                        "\nBranch     : " + branch.getText() +
                        "\nMarks      : " + marks.getText() +
                        "\nCity       : " + city.getText()
                    );
                }
            } else if (action.equals("Search Student")) {
                String idText = searchId.getText().trim();
                if (idText.isEmpty()) idText = sid.getText().trim();
                try (PreparedStatement ps = con.prepareStatement("SELECT * FROM student_details WHERE id=?")) {
                    ps.setInt(1, Integer.parseInt(idText));
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            output.setText(
                                "STUDENT FOUND\n\n" +
                                "Student ID : " + rs.getInt("id") +
                                "\nName       : " + rs.getString("name") +
                                "\nEmail      : " + rs.getString("email") +
                                "\nBranch     : " + rs.getString("branch") +
                                "\nMarks      : " + rs.getInt("marks") +
                                "\nCity       : " + rs.getString("city")
                            );
                        } else {
                            output.setText("No student found for ID: " + idText);
                        }
                    }
                }
            } else if (action.equals("Add Course")) {
                String sql = "INSERT INTO course_details (id,name,credits) VALUES (?,?,?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(cid.getText().trim()));
                    ps.setString(2, cname.getText().trim());
                    ps.setInt(3, Integer.parseInt(credits.getText().trim()));
                    ps.executeUpdate();
                    output.setText(
                        "COURSE ADDED SUCCESSFULLY\n\n" +
                        "Course ID   : " + cid.getText() +
                        "\nCourse Name : " + cname.getText() +
                        "\nCredits     : " + credits.getText()
                    );
                }
            } else if (action.equals("Search Course")) {
                try (PreparedStatement ps = con.prepareStatement("SELECT * FROM course_details WHERE id=?")) {
                    ps.setInt(1, Integer.parseInt(cid.getText().trim()));
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            output.setText(
                                "COURSE FOUND\n\n" +
                                "Course ID   : " + rs.getInt("id") +
                                "\nCourse Name : " + rs.getString("name") +
                                "\nCredits     : " + rs.getInt("credits")
                            );
                        } else {
                            output.setText("Course not found.");
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
            output.setText("Please enter valid numeric IDs, marks and credits.");
        } catch (SQLException ex) {
            output.setText("Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new StudentSystem();
    }
}

