import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login extends JFrame {

    JTextField user;
    JPasswordField pass;

    Color blue = new Color(25,75,145);
    Color light = new Color(240,245,252);

    public Login() {
        setTitle("Online Examination System");
        setSize(500,420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("ONLINE EXAMINATION",JLabel.CENTER);
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Student and Admin Portal",JLabel.CENTER);
        sub.setForeground(Color.WHITE);

        JPanel header = new JPanel(new GridLayout(2,1));
        header.setBackground(blue);
        header.setBorder(BorderFactory.createEmptyBorder(25,10,25,10));
        header.add(title);
        header.add(sub);

        user = new JTextField();
        pass = new JPasswordField();

        JButton login = new JButton("LOGIN");
        JButton register = new JButton("REGISTER");

        login.setBackground(blue);
        login.setForeground(Color.WHITE);
        login.setOpaque(true);
        register.setBackground(Color.WHITE);
        register.setForeground(blue);
        register.setOpaque(true);

        login.setFont(new Font("Arial",Font.BOLD,14));
        register.setFont(new Font("Arial",Font.BOLD,14));

        JPanel form = new JPanel(new GridLayout(3,2,12,18));
        form.setBackground(light);
        form.setBorder(BorderFactory.createEmptyBorder(35,45,35,45));

        form.add(new JLabel("Username:"));
        form.add(user);
        form.add(new JLabel("Password:"));
        form.add(pass);
        form.add(login);
        form.add(register);

        JLabel footer = new JLabel(
            "Online Examination System",JLabel.CENTER);

        add(header,BorderLayout.NORTH);
        add(form,BorderLayout.CENTER);
        add(footer,BorderLayout.SOUTH);

        login.addActionListener(e -> authenticate());
        register.addActionListener(e -> registerStudent());

        setVisible(true);
    }

    void authenticate() {
        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(
            "SELECT * FROM Users WHERE username=? AND password=?")) {

            ps.setString(1,user.getText());
            ps.setString(2,new String(pass.getPassword()));

            ResultSet rs=ps.executeQuery();

            if(rs.next()) {
                String name=user.getText();
                String role=rs.getString("role");
                dispose();

                if(role.equals("admin"))
                    new AdminDashboard();
                else
                    new StudentDashboard(name);
            } else {
                JOptionPane.showMessageDialog(this,"Invalid Login!");
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }

    void registerStudent() {
        String u=user.getText().trim();
        String p=new String(pass.getPassword());

        if(u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Enter username and password");
            return;
        }

        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(
            "INSERT INTO Users(username,password,role) VALUES(?,?,'student')")) {

            ps.setString(1,u);
            ps.setString(2,p);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,
                "Registration Successful!");

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this,
                "Username already exists!");
        }
    }
}
