package frontend;

import javax.swing.*;

public class LuaMiraiGUI {
    private JButton button1;
    private JPanel root;

    public static void main(String[] args) {
        JFrame frame = new JFrame("LuaMiraiGUI");
        frame.setContentPane(new LuaMiraiGUI().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
