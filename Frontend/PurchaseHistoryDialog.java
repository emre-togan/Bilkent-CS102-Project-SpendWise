import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

// import Models.Order;
// import Services.ProfileService;

public class PurchaseHistoryDialog extends JDialog {

    public PurchaseHistoryDialog(JFrame parent) {
        super(parent, "Purchase History", true);
        setSize(600, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.WHITE_BG);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.WHITE_BG);
        headerPanel.setBorder(new EmptyBorder(15, 20, 10, 20));
        headerPanel.add(new JLabel("My Orders"), BorderLayout.WEST);
        headerPanel.add(new JButton("Export Data"), BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(UIConstants.WHITE_BG);

        // Will be send to backend
        tabbedPane.addTab("All Orders", createOrderListPanel("All"));
        tabbedPane.addTab("Delivered", createOrderListPanel("Delivered"));
        tabbedPane.addTab("Shipped", createOrderListPanel("Shipped"));
        tabbedPane.addTab("Processing", createOrderListPanel("Processing"));

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JScrollPane createOrderListPanel(String filterType) {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(UIConstants.WHITE_BG);
        listPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // backend
        List<Order> orders = ProfileService.getOrders(filterType);
        
        if (orders != null) {
            for (Order order : orders) {
                listPanel.add(createOrderItem(order));
                listPanel.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createOrderItem(Order order) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(new JLabel("Order #" + order.getOrderId()), BorderLayout.WEST);
        
        JLabel statusLabel = new JLabel(order.getStatus());
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(230, 230, 230));
        topPanel.add(statusLabel, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1));
        centerPanel.setOpaque(false);
        centerPanel.add(new JLabel("Date: " + order.getDate()));
        centerPanel.add(new JLabel("Items: " + order.getProductSummary()));
        centerPanel.add(new JLabel("Total: " + order.getTotalAmount()));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        
        JButton trackBtn = new JButton("Track Order");
        // Backend
        trackBtn.addActionListener(e -> ProfileService.trackOrder(order.getOrderId()));
        
        JButton buyAgainBtn = new JButton("Buy Again");
        buyAgainBtn.setBackground(UIConstants.PRIMARY_BLUE);
        buyAgainBtn.setForeground(Color.WHITE);
        // Backend
        buyAgainBtn.addActionListener(e -> ProfileService.buyAgain(order.getOrderId()));
        
        bottomPanel.add(trackBtn);
        bottomPanel.add(buyAgainBtn);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        return card;
    }
}