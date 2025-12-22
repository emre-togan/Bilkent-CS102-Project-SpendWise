import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SignUpPanel extends JPanel {

    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private Runnable onSignInClicked;


    public SignUpPanel() {
        setLayout(new GridBagLayout()); 

        // 1. White Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(400, 650)); 
        cardPanel.setBackground(UIConstants.WHITE_BG);
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 2. Logo and header
        JLabel logoLabel;
        java.net.URL imgURL = getClass().getResource("/logo.png");
        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledImage));
        } 
        else {
            logoLabel = new JLabel("W$");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 40));
            logoLabel.setForeground(UIConstants.PRIMARY_GREEN);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(UIConstants.HEADING_FONT);
        titleLabel.setForeground(UIConstants.TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Form parts
        // Full Name 
        fullNameField = new JTextField();
        JPanel fullNamePanel = createFieldPanel("Full Name", fullNameField);

        // Username 
        usernameField = new JTextField();
        JPanel usernamePanel = createFieldPanel("Username", usernameField);

        // Email 
        emailField = new JTextField();
        JPanel emailPanel = createFieldPanel("Email Address", emailField);

        // Password 
        passwordField = new JPasswordField();
        JPanel passwordPanel = createFieldPanel("Password", passwordField);

        // Confirm Password 
        confirmPasswordField = new JPasswordField();
        JPanel confirmPassPanel = createFieldPanel("Confirm Password", confirmPasswordField);

        // 4. Button and link
        JButton signUpButton = new JButton("SIGN UP");
        signUpButton.setFont(UIConstants.BUTTON_FONT);
        signUpButton.setBackground(UIConstants.PRIMARY_GREEN);
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        signUpButton.setFocusPainted(false);
        signUpButton.setBorderPainted(false);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        signUpButton.addActionListener(e -> handleSignUp());

        JLabel loginLink = new JLabel("Back to Login"); 
        loginLink.setFont(UIConstants.BODY_FONT);
        loginLink.setForeground(UIConstants.PRIMARY_BLUE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // clicking link
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //backend
                JOptionPane.showMessageDialog(SignUpPanel.this, "Going back to login screen...");
                
            }
        });

        cardPanel.add(logoLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(20));
        
        cardPanel.add(fullNamePanel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(usernamePanel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(emailPanel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(passwordPanel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(confirmPassPanel);
        
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(signUpButton);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(loginLink);

        add(cardPanel);
    }
    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT); 

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(UIConstants.GRAY_TEXT);
       
        label.setAlignmentX(Component.CENTER_ALIGNMENT); 
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
        label.setHorizontalAlignment(SwingConstants.LEFT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(5)); 
        panel.add(field);
        return panel;
    }
    public void setOnSignInClicked(Runnable callback) {
        this.onSignInClicked = callback;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gp = new GradientPaint(0, 0, UIConstants.PRIMARY_GREEN, getWidth(), getHeight(), UIConstants.PRIMARY_BLUE);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void handleSignUp() { 
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());
        String fullName = new String(fullNameField.getText());
        String username = new String(usernameField.getText());
        String email = new String(emailField.getText());


        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
       //backend
    }
}