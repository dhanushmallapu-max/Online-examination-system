import javax.swing.*;
import java.sql.*;

public class ResultWindow extends JFrame {

    public ResultWindow() {
        this(null);
    }

    public ResultWindow(String username) {
        setTitle("Examination Results");
        setSize(500,400);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea();
        area.setEditable(false);

        String sql = username == null
            ? "SELECT * FROM Results ORDER BY id DESC"
            : "SELECT * FROM Results WHERE username=? ORDER BY id DESC";

        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(sql)) {

            if(username!=null)
                ps.setString(1,username);

            ResultSet rs=ps.executeQuery();

            while(rs.next()) {
                area.append("Student: "+rs.getString("username")
                    +"\nScore: "+rs.getInt("score")
                    +"/"+rs.getInt("total")
                    +"\n----------------------\n");
            }

            if(area.getText().isEmpty())
                area.setText("No examination results available.");

        } catch(Exception e) {
            area.setText(e.getMessage());
        }

        add(new JScrollPane(area));
        setVisible(true);
    }
}
