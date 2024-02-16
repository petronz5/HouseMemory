import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CasaApp {

    private JFrame frame;
    private Map<String, JTextArea> stanzeAppunti;

    public CasaApp() {
        // Usa il look and feel Nimbus per un'interfaccia più moderna
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame = new JFrame("Appunti della Casa");
        frame.setLayout(new BorderLayout());
        stanzeAppunti = new HashMap<>();

        // Pannello centrale con un layout più flessibile
        JPanel pannelloCentrale = new JPanel(new GridLayout(3, 4, 10, 10));
        pannelloCentrale.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Stanze della casa con una disposizione più creativa
        String[] nomiStanze = {"Soggiorno", "Cucina", "Camera da Letto", "Studio", "Bagno", "Armadio Magico",
                "Giardino Segreto", "Sala dei Giochi", "Cantina Misteriosa", "Biblioteca Incantata", "Sala del Tesoro", "Observatory"};

        for (String stanzaNome : nomiStanze) {
            JTextArea appuntiArea = new JTextArea();
            appuntiArea.setLineWrap(true);
            appuntiArea.setWrapStyleWord(true);
            stanzeAppunti.put(stanzaNome, appuntiArea);

            JPanel stanzaPanel = new JPanel(new BorderLayout());
            stanzaPanel.setBorder(BorderFactory.createTitledBorder(stanzaNome));
            stanzaPanel.add(new JScrollPane(appuntiArea), BorderLayout.CENTER);

            pannelloCentrale.add(stanzaPanel);
        }

        // Aggiungi il pannello centrale al frame
        frame.add(pannelloCentrale, BorderLayout.CENTER);

        // Barra del menu con opzioni di File
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem salvaMenuItem = new JMenuItem("Salva");
        salvaMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaAppunti();
            }
        });

        JMenuItem caricaMenuItem = new JMenuItem("Carica");
        caricaMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                caricaAppunti();
            }
        });

        fileMenu.add(salvaMenuItem);
        fileMenu.add(caricaMenuItem);

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        // Pannello inferiore con bottoni aggiuntivi
        JPanel pannelloInferiore = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton salvaButton = new JButton("SALVA");
        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaAppunti();
            }
        });

        JButton modificaButton = new JButton("Modifica appunti");
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apriFinestraModifica();
            }
        });

        JButton coloreButton = new JButton("Cambia colore testo");
        coloreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiaColoreTesto();
            }
        });

        JButton ricercaButton = new JButton("Cerca");
        ricercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apriFinestraRicerca();
            }
        });

        pannelloInferiore.add(salvaButton);
        pannelloInferiore.add(modificaButton);
        pannelloInferiore.add(coloreButton);
        pannelloInferiore.add(ricercaButton);

        // Aggiungi il pannello inferiore al frame
        frame.add(pannelloInferiore, BorderLayout.SOUTH);

        // Configurazioni generali del frame
        configurazioneFrame();

        // Imposta le dimensioni e la chiusura del frame
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void salvaAppunti() {
        for (Map.Entry<String, JTextArea> entry : stanzeAppunti.entrySet()) {
            String stanzaNome = entry.getKey();
            JTextArea appuntiArea = entry.getValue();
            String appunti = appuntiArea.getText();

            // Salva gli appunti su file
            try (PrintWriter writer = new PrintWriter(new FileWriter(stanzaNome + ".txt"))) {
                writer.println(appunti);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        JOptionPane.showMessageDialog(frame, "Appunti salvati con successo!");
    }

    private void caricaAppunti() {
        for (Map.Entry<String, JTextArea> entry : stanzeAppunti.entrySet()) {
            String stanzaNome = entry.getKey();
            JTextArea appuntiArea = entry.getValue();

            // Carica gli appunti da file
            try (BufferedReader reader = new BufferedReader(new FileReader(stanzaNome + ".txt"))) {
                StringBuilder appunti = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    appunti.append(line).append("\n");
                }
                appuntiArea.setText(appunti.toString());
            } catch (IOException ex) {
                // Non c'è bisogno di gestire un'eccezione qui se il file non esiste
            }
        }
    }

    private void apriFinestraModifica() {
        JDialog dialog = new JDialog(frame, "Modifica appunti", true);
        dialog.setLayout(new BorderLayout());

        JTextArea appuntiArea = new JTextArea();
        appuntiArea.setText(stanzeAppunti.get("Soggiorno").getText()); // Esempio: Prendiamo gli appunti dal soggiorno
        dialog.add(new JScrollPane(appuntiArea), BorderLayout.CENTER);

        JButton confermaButton = new JButton("Conferma");
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aggiorna gli appunti nella stanza
                stanzeAppunti.get("Soggiorno").setText(appuntiArea.getText());
                dialog.dispose();
            }
        });

        dialog.add(confermaButton, BorderLayout.SOUTH);

        dialog.setSize(400, 300);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void cambiaColoreTesto() {
        Color coloreScelto = JColorChooser.showDialog(frame, "Seleziona colore testo", Color.black);

        if (coloreScelto != null) {
            for (JTextArea appuntiArea : stanzeAppunti.values()) {
                appuntiArea.setForeground(coloreScelto);
            }
        }
    }

    private void apriFinestraRicerca() {
        JDialog dialog = new JDialog(frame, "Ricerca", true);
        dialog.setLayout(new BorderLayout());

        JTextField ricercaField = new JTextField();
        JButton cercaButton = new JButton("Cerca");

        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eseguiRicerca(ricercaField.getText());
            }
        });

        JPanel ricercaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ricercaPanel.add(ricercaField);
        ricercaPanel.add(cercaButton);

        dialog.add(ricercaPanel, BorderLayout.NORTH);

        JTextArea risultatiArea = new JTextArea();
        risultatiArea.setEditable(false);

        dialog.add(new JScrollPane(risultatiArea), BorderLayout.CENTER);

        JButton chiudiButton = new JButton("Chiudi");
        chiudiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(chiudiButton, BorderLayout.SOUTH);

        dialog.setSize(400, 300);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void eseguiRicerca(String termineRicerca) {
        StringBuilder risultati = new StringBuilder();

        for (Map.Entry<String, JTextArea> entry : stanzeAppunti.entrySet()) {
            String stanzaNome = entry.getKey();
            JTextArea appuntiArea = entry.getValue();
            String appunti = appuntiArea.getText();

            if (appunti.contains(termineRicerca)) {
                risultati.append("Trovato in ").append(stanzaNome).append("\n");
                appuntiArea.setSelectionStart(appunti.indexOf(termineRicerca));
                appuntiArea.setSelectionEnd(appunti.indexOf(termineRicerca) + termineRicerca.length());
            }
        }

        if (risultati.length() == 0) {
            risultati.append("Nessun risultato trovato.");
        }

        JOptionPane.showMessageDialog(frame, risultati.toString());
    }

    // Configurazione del frame con un aspetto più moderno
    private void configurazioneFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CasaApp();
            }
        });
    }
}
