import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    Color blue = new Color(25,75,145);
    Color light = new Color(240,245,252);

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(550,450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(light);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("ADMIN DASHBOARD",JLabel.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,25));
        title.setForeground(Color.WHITE);

        JPanel header = new JPanel();
        header.setBackground(blue);
        header.setBorder(BorderFactory.createEmptyBorder(30,10,30,10));
        header.add(title);

        JButton questions = new JButton("Manage Questions");
        JButton results = new JButton("View Examination Results");
        JButton logout = new JButton("Logout");

        JButton[] buttons = {questions,results,logout};

        for(JButton b:buttons) {
            b.setFont(new Font("Arial",Font.BOLD,15));
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            b.setForeground(blue);
            b.setPreferredSize(new Dimension(280,45));
        }

        questions.addActionListener(e -> new QuestionManager());
        results.addActionListener(e -> new ResultWindow());

        logout.addActionListener(e -> {
            dispose();
            new Login();
        });

        JPanel center = new JPanel(new GridLayout(3,1,15,15));
        center.setBackground(light);
        center.setBorder(BorderFactory.createEmptyBorder(40,100,40,100));

        center.add(questions);
        center.add(results);
        center.add(logout);

        add(header,BorderLayout.NORTH);
        add(center,BorderLayout.CENTER);

        setVisible(true);
    }
}
