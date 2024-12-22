import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ConfigEditor extends JFrame {
    private JTextField portField;
    private JTextField repertoireField;
    private JCheckBox phpEnabledCheckBox;
    private File configFile;

    public ConfigEditor(String filePath) {
        // Initialise le fichier de configuration
        configFile = new File(filePath);

        // Configuration de la fenêtre
        setTitle("Éditeur de Configuration");
        setSize(600, 350); // Fenêtre plus large pour plus de contenu
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre à l'écran
        setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png")); // Définir l'icône de l'application
        setLayout(new BorderLayout());

        // Panel d'en-tête
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(40, 40, 40));
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Éditeur de Configuration Serveur");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 2, 10, 10)); // Plus d'espaces pour un rendu plus aéré

        
        // Champs de texte et labels pour les paramètres
        mainPanel.add(new JLabel("Port:"));
        portField = new JTextField();
        mainPanel.add(portField);

        mainPanel.add(new JLabel("Répertoire:"));
        repertoireField = new JTextField();
        mainPanel.add(repertoireField);

        mainPanel.add(new JLabel("PHP Activé:"));
        phpEnabledCheckBox = new JCheckBox();
        mainPanel.add(phpEnabledCheckBox);

        // Bouton de sauvegarde avec icône
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Sauvegarder", new ImageIcon("save_icon.png"));
        saveButton.setBackground(new Color(34, 139, 34));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.PLAIN, 14));
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(new SaveAction());
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel);

        // Ajouter le panel principal au centre
        add(mainPanel, BorderLayout.CENTER);

        // Charger la configuration existante
        loadConfig();

        // Footer avec des informations supplémentaires
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(40, 40, 40));
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2024 Configuration Serveur - Tous droits réservés.");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.WHITE);
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("PORT=")) {
                    portField.setText(line.substring(5));
                } else if (line.startsWith("REPERTOIRE=")) {
                    repertoireField.setText(line.substring(11));
                } else if (line.startsWith("PHP=")) {
                    phpEnabledCheckBox.setSelected(line.substring(4).equalsIgnoreCase("oui"));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement du fichier : " + e.getMessage());
        }
    }

    private void saveConfig() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(configFile))) {
            writer.println("PORT=" + portField.getText());
            writer.println("REPERTOIRE=" + repertoireField.getText());
            writer.println("PHP=" + (phpEnabledCheckBox.isSelected() ? "oui" : "non"));
            JOptionPane.showMessageDialog(this, "Configuration sauvegardée avec succès !");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    private class SaveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveConfig();
        }
    }

    public static void main(String[] args) {
        String filePath = "C:/Users/Mamy Tsiferana/Documents/ITU_L2/reseaux/Socket/conf.txt"; // Chemin vers le fichier de configuration
        SwingUtilities.invokeLater(() -> new ConfigEditor(filePath).setVisible(true));
    }
}
