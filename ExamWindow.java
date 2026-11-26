import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class ExamWindow extends JFrame {

    String username;
    ArrayList<String[]> questions = new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    HashMap<Integer,String> answers = new HashMap<>();

    int index=0, time=600;

    JLabel qLabel,timer,title;
    JRadioButton[] options=new JRadioButton[4];
    ButtonGroup group=new ButtonGroup();

    JButton previous,next,submit;
    javax.swing.Timer clock;

    Color blue=new Color(25,75,145);
    Color light=new Color(240,245,252);

    public ExamWindow(String username) {
        this.username=username;
        loadQuestions();

        if(questions.isEmpty()) {
            JOptionPane.showMessageDialog(null,"No questions available!");
            dispose();
            return;
        }

        setTitle("Online Examination");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header=new JPanel(new BorderLayout());
        header.setBackground(blue);
        header.setBorder(BorderFactory.createEmptyBorder(20,25,20,25));

        title=new JLabel("ONLINE EXAMINATION");
        title.setFont(new Font("Arial",Font.BOLD,22));
        title.setForeground(Color.WHITE);

        timer=new JLabel("Time: 10:00");
        timer.setFont(new Font("Arial",Font.BOLD,17));
        timer.setForeground(Color.WHITE);

        header.add(title,BorderLayout.WEST);
        header.add(timer,BorderLayout.EAST);

        JPanel center=new JPanel(new GridLayout(5,1,12,12));
        center.setBackground(light);
        center.setBorder(BorderFactory.createEmptyBorder(30,40,30,40));

        qLabel=new JLabel();
        qLabel.setFont(new Font("Arial",Font.BOLD,17));

        center.add(qLabel);

        for(int i=0;i<4;i++) {
            options[i]=new JRadioButton();
            options[i].setFont(new Font("Arial",Font.PLAIN,15));
            options[i].setBackground(light);
            group.add(options[i]);
            center.add(options[i]);
        }

        JPanel footer=new JPanel();
        footer.setBackground(light);
        footer.setBorder(BorderFactory.createEmptyBorder(10,10,20,10));

        previous=new JButton("Previous");
        next=new JButton("Next");
        submit=new JButton("Submit");

        footer.add(previous);
        footer.add(next);
        footer.add(submit);

        add(header,BorderLayout.NORTH);
        add(center,BorderLayout.CENTER);
        add(footer,BorderLayout.SOUTH);

        previous.addActionListener(e -> {
            saveAnswer();
            if(index>0) {
                index--;
                showQuestion();
            }
        });

        next.addActionListener(e -> {
            saveAnswer();
            if(index<questions.size()-1) {
                index++;
                showQuestion();
            }
        });

        submit.addActionListener(e -> {
            int choice=JOptionPane.showConfirmDialog(
                this,"Are you sure you want to submit?",
                "Confirm Submission",
                JOptionPane.YES_NO_OPTION);

            if(choice==JOptionPane.YES_OPTION) {
                saveAnswer();
                finishExam();
            }
        });

        showQuestion();
        setVisible(true);

        clock=new javax.swing.Timer(1000,e -> {
            time--;
            timer.setText(String.format("Time: %02d:%02d",
                time/60,time%60));

            if(time<=0) {
                clock.stop();
                saveAnswer();
                finishExam();
            }
        });
        clock.start();
    }

    void loadQuestions() {
        try(Connection con=DBConnection.getConnection();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Questions")) {

            while(rs.next()) {
                ids.add(rs.getInt("id"));
                questions.add(new String[]{
                    rs.getString("question"),
                    rs.getString("a"),
                    rs.getString("b"),
                    rs.getString("c"),
                    rs.getString("d"),
                    rs.getString("answer")
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void showQuestion() {
        String[] q=questions.get(index);

        qLabel.setText("<html>Question "+(index+1)+
            " of "+questions.size()+"<br><br>"+q[0]+"</html>");

        String[] labels={"A. ","B. ","C. ","D. "};
        group.clearSelection();

        for(int i=0;i<4;i++) {
            options[i].setText(labels[i]+q[i+1]);
            options[i].setSelected(
                labels[i].substring(0,1).equals(
                    answers.get(ids.get(index))));
        }

        previous.setEnabled(index>0);
        next.setEnabled(index<questions.size()-1);
    }

    void saveAnswer() {
        for(int i=0;i<4;i++) {
            if(options[i].isSelected()) {
                answers.put(ids.get(index),
                    String.valueOf((char)('A'+i)));
            }
        }
    }

    void finishExam() {
        if(clock!=null) clock.stop();

        int score=0;

        for(int i=0;i<questions.size();i++) {
            String selected=answers.get(ids.get(i));

            if(selected!=null &&
                selected.equalsIgnoreCase(questions.get(i)[5]))
                score++;
        }

        double percentage=score*100.0/questions.size();

        String grade;
        if(percentage>=90) grade="A+";
        else if(percentage>=80) grade="A";
        else if(percentage>=70) grade="B";
        else if(percentage>=60) grade="C";
        else if(percentage>=50) grade="D";
        else grade="F";

        try(Connection con=DBConnection.getConnection();
            PreparedStatement ps=con.prepareStatement(
            "INSERT INTO Results(username,score,total) VALUES(?,?,?)")) {

            ps.setString(1,username);
            ps.setInt(2,score);
            ps.setInt(3,questions.size());
            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this,
            "EXAMINATION COMPLETED\n\n"+
            "Score: "+score+"/"+questions.size()+
            "\nPercentage: "+String.format("%.2f",percentage)+"%"+
            "\nGrade: "+grade);

        dispose();
        new StudentDashboard(username);
    }
}
