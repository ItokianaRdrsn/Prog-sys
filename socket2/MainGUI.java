import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class MainGUI extends JFrame {
    private JButton startButton;
    private JButton stopButton;
    private JButton configButton;
    private ServeurMT serveur;

    public MainGUI() {
        setTitle("Contrôle Serveur et Config");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre à l'écran
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png")); // Définir une icône pour l'application

        // Définir le layout principal
        setLayout(new BorderLayout());

        // Barre de titre
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(40, 40, 40));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Contrôle Serveur et Éditeur de Configuration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel central avec un peu plus d'éléments
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1, 10, 10));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Ajout des boutons avec des icônes et du texte
        startButton = new JButton("Démarrer Serveur", new ImageIcon("start_icon.png"));
        startButton.setBackground(new Color(34, 139, 34));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.PLAIN, 14));
        startButton.setFocusPainted(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });
        centerPanel.add(startButton);

        stopButton = new JButton("Arrêter Serveur", new ImageIcon("stop_icon.png"));
        stopButton.setBackground(new Color(220, 20, 60));
        stopButton.setForeground(Color.WHITE);
        stopButton.setFont(new Font("Arial", Font.PLAIN, 14));
        stopButton.setFocusPainted(false);
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
        centerPanel.add(stopButton);

        configButton = new JButton("Éditeur de Configuration", new ImageIcon("config_icon.png"));
        configButton.setBackground(new Color(70, 130, 180));
        configButton.setForeground(Color.WHITE);
        configButton.setFont(new Font("Arial", Font.PLAIN, 14));
        configButton.setFocusPainted(false);
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openConfigEditor();
            }
        });
        centerPanel.add(configButton);

        // Ajout d'un panel pour une description ou des informations supplémentaires
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setBackground(new Color(245, 245, 245));
        JTextArea infoText = new JTextArea("Bienvenue dans l'outil de gestion de votre serveur. Vous pouvez démarrer et arrêter le serveur, " +
                "ainsi qu'éditer sa configuration via les boutons ci-dessous. " +
                "L'éditeur de configuration vous permet de modifier les paramètres tels que le port et le répertoire.");
        infoText.setFont(new Font("Arial", Font.PLAIN, 14));
        infoText.setForeground(new Color(50, 50, 50));
        infoText.setEditable(false);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setBackground(new Color(245, 245, 245));
        infoPanel.add(new JScrollPane(infoText), BorderLayout.CENTER);
        centerPanel.add(infoPanel);

        add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(40, 40, 40));
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2024 Mon Application - Tous droits réservés.");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        // Initialisation du serveur à null (pas encore démarré)
        serveur = null;

        setVisible(true);
    }

    // Démarrer le serveur
    private void startServer() {
        try {
            if (serveur == null) {
                serveur = new ServeurMT();
                new Thread(() -> {
                    try {
                        serveur.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                JOptionPane.showMessageDialog(this, "Serveur démarré !");
            } else {
                JOptionPane.showMessageDialog(this, "Le serveur est déjà en cours d'exécution.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du démarrage du serveur.");
        }
    }

    // Arrêter le serveur
    private void stopServer() {
        try {
            if (serveur != null) {
                serveur.stopServeur(); 
                JOptionPane.showMessageDialog(this, "Serveur arrêté !");
            } else {
                JOptionPane.showMessageDialog(this, "Le serveur n'est pas en cours d'exécution.");
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'arrêt du serveur.");
        }
    }

    // Ouvrir l'éditeur de configuration
    private void openConfigEditor() {
        String filePath = "C:/Users/Mamy Tsiferana/Documents/ITU_L2/reseaux/Socket/conf.txt"; // Le chemin du fichier de configuration
        SwingUtilities.invokeLater(() -> new ConfigEditor(filePath).setVisible(true));
    }

    public static void main(String[] args) {
        // Créer et afficher l'interface graphique
        SwingUtilities.invokeLater(() -> new MainGUI());
    }
}
