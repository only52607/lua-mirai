import "javax.swing.JFrame"
import "javax.swing.JLabel"
import "javax.swing.JPasswordField"
import "javax.swing.JButton"
import "javax.swing.JTextField"
import "javax.swing.JPanel"
import "java.awt.event.ActionListener"
import "java.lang.Thread"
import "java.lang.Runnable"

function placeComponents(panel)
    panel:setLayout(null)

    userLabel = JLabel("账号：")
    userLabel:setBounds(10,20,80,25)
    panel:add(userLabel)

    userText = JTextField(20)
    userText:setBounds(100,20,165,25)
    panel:add(userText)

    passwordLabel = JLabel("密码：")
    passwordLabel:setBounds(10,50,80,25)
    panel:add(passwordLabel)

    passwordText = JPasswordField(20)
    passwordText:setBounds(100,50,165,25)
    panel:add(passwordText)

    loginButton = JButton("登录")
    loginButton:setBounds(10, 80, 80, 25)
    loginButton:setActionCommand("login")

    panel:add(loginButton)
end

frame = JFrame("QQ登录")
frame:setSize(300, 160)
frame:setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
panel = JPanel()
frame:add(panel)
placeComponents(panel)
frame:setVisible(true)
