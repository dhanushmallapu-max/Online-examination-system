import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentDashboard extends JFrame {

    String username;

    public StudentDashboard(String username) {
        this.username = username;

        setTitle("Student Dashboard");
        setSize(500,350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel title = new JLabel("STUDENT DASHBOARD",JLabel.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,22));

        JButton exam = new JButton("Start Examination");
        JButton result = new JButton("View Results");
        JButton logout = new JButton("Logout");

        exam.addActionListener(e -> showInstructions());
        result.addActionListener(e -> new ResultWindow(username));

        logout.addActionListener(e -> {
            dispose();
            new Login();
        });

        JPanel panel = new JPanel(new GridLayout(4,1,15,15));
        panel.setBorder(BorderFactory.createEmptyBorder(40,60,40,60));

        panel.add(title);
        panel.add(exam);
        panel.add(result);
        panel.add(logout);

        add(panel);
        setVisible(true);
    }

    void showInstructions() {
        int total = 0;

        try(Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                "SELECT COUNT(*) FROM Questions")) {

            if(rs.next())
                total = rs.getInt(1);

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
            return;
        }

        if(total == 0) {
            JOptionPane.showMessageDialog(this,
                "No questions available!");
            return;
        }

        String message =
            "EXAMINATION INSTRUCTIONS\n\n" +
            "Total Questions: " + total + "\n" +
            "Duration: 10 Minutes\n" +
            "Each Correct Answer: 1 Mark\n" +
            "Negative Marking: None\n\n" +
            "Click OK to start the examination.";

        int choice = JOptionPane.showConfirmDialog(
            this,message,"Exam Instructions",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE);

        if(choice == JOptionPane.OK_OPTION) {
            new ExamWindow(username);
            dispose();
        }
    }
}
