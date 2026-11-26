import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class QuestionManager extends JFrame {

    JTextField question, a, b, c, d, answer;

    public QuestionManager() {
        setTitle("Question Manager");
        setSize(550, 450);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new GridLayout(8, 2, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        question = new JTextField();
        a = new JTextField();
        b = new JTextField();
        c = new JTextField();
        d = new JTextField();
        answer = new JTextField();

        p.add(new JLabel("Question:"));
        p.add(question);
        p.add(new JLabel("Option A:"));
        p.add(a);
        p.add(new JLabel("Option B:"));
        p.add(b);
        p.add(new JLabel("Option C:"));
        p.add(c);
        p.add(new JLabel("Option D:"));
        p.add(d);
        p.add(new JLabel("Correct Answer (A/B/C/D):"));
        p.add(answer);

        JButton add = new JButton("Add Question");
        JButton view = new JButton("View Questions");
        JButton delete = new JButton("Delete Question");

        p.add(add);
        p.add(view);

        add(delete);
        add(p);

        add.addActionListener(e -> addQuestion());
        view.addActionListener(e -> viewQuestions());
        delete.addActionListener(e -> deleteQuestion());

        setVisible(true);
    }

    void addQuestion() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
             "INSERT INTO Questions(question,a,b,c,d,answer) VALUES(?,?,?,?,?,?)")) {

            String key = answer.getText().trim().toUpperCase();

            if (question.getText().trim().isEmpty() ||
                a.getText().trim().isEmpty() ||
                b.getText().trim().isEmpty() ||
                c.getText().trim().isEmpty() ||
                d.getText().trim().isEmpty() ||
                !key.matches("[ABCD]")) {
                JOptionPane.showMessageDialog(this, "Fill all fields and enter A, B, C or D.");
                return;
            }

            ps.setString(1, question.getText());
            ps.setString(2, a.getText());
            ps.setString(3, b.getText());
            ps.setString(4, c.getText());
            ps.setString(5, d.getText());
            ps.setString(6, key);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Question Added!");

            question.setText("");
            a.setText("");
            b.setText("");
            c.setText("");
            d.setText("");
            answer.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    void viewQuestions() {
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Questions")) {

            StringBuilder data = new StringBuilder();

            while (rs.next()) {
                data.append("ID: ").append(rs.getInt("id"))
                    .append("\n").append(rs.getString("question"))
                    .append("\nA. ").append(rs.getString("a"))
                    .append("\nB. ").append(rs.getString("b"))
                    .append("\nC. ").append(rs.getString("c"))
                    .append("\nD. ").append(rs.getString("d"))
                    .append("\nCorrect: ").append(rs.getString("answer"))
                    .append("\n\n");
            }

            JOptionPane.showMessageDialog(this,
                data.length() == 0 ? "No questions found." : data.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void deleteQuestion() {
        String id = JOptionPane.showInputDialog(this, "Enter Question ID:");

        if (id == null) return;

        try {
            int questionId = Integer.parseInt(id.trim());

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(
                 "DELETE FROM Questions WHERE id=?")) {

                ps.setInt(1, questionId);
                int count = ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                    count > 0 ? "Question Deleted!" : "Question ID Not Found.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid numeric ID.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
