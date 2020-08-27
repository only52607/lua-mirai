package frontend;

import javax.swing.*;

public class GGUI {
    public static void main(String[] args) {
        System.out.println("123");
        JFrame frame = new JFrame("GGUI");
        frame.setContentPane(new GGUI().pan);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel pan;
    private JButton button1;
}
