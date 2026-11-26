import javax.swing.*;

public class OnlineExamLauncher {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Login());
    }
}
