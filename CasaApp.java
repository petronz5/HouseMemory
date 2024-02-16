import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CasaApp {

    private JFrame frame;
    private Map<String, JTextArea> stanzeAppunti;

    public CasaApp() {
        frame = new JFrame("Appunti della Casa");
        frame.setLayout(new GridLayout(3, 4));
        stanzeAppunti = new HashMap<>();

        for (int i = 1; i <= 12; i++) {
            String stanzaNome = "Stanza " + i;
            JTextArea appuntiArea = new JTextArea();
            stanzeAppunti.put(stanzaNome, appuntiArea);

            JPanel stanzaPanel = new JPanel(new BorderLayout());
            stanzaPanel.setBorder(BorderFactory.createTitledBorder(stanzaNome));
            stanzaPanel.add(new JScrollPane(appuntiArea), BorderLayout.CENTER);

            frame.add(stanzaPanel);
        }

        //Aggiungi un menu File con opzioni di salvataggio e caricamento
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


        JButton salvaButton = new JButton("SALVA");
        salvaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaAppunti();
            }
        });

        frame.add(salvaButton);

        caricaAppunti(); // Carica gli appunti salvati all'avvio dell'applicazione

        JButton modificaButton = new JButton("Modifica appunti");
        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apriFinestraModifica();
            }
        });

        frame.add(modificaButton);

        JButton coloreButton = new JButton("Cambia colore testo");
        coloreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cambiaColoreTesto();
            }
        });

        frame.add(coloreButton);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.isControlDown()){
                    switch (e.getKeyCode()){
                        case KeyEvent.VK_UP:
                            spostaStanza("SU");
                            break;
                        case KeyEvent.VK_DOWN:
                            spostaStanza("GIU");
                            break;
                        case KeyEvent.VK_RIGHT:
                            spostaStanza("DESTRA");
                            break;
                        case KeyEvent.VK_LEFT:
                            spostaStanza("SINISTRA");
                            break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        //Assicurati che il frame sia focalizzato per ricevere gli eventi della finest
        frame.setFocusable(true);
        frame.requestFocus();

        frame.setSize(1000, 900);
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

    private void apriFinestraModifica(){
        JDialog dialog = new JDialog(frame , "Modifica appunti" , true);
        dialog.setLayout(new BorderLayout());

        JTextArea appuntiArea = new JTextArea();
        appuntiArea.setText(stanzeAppunti.get("Stanza 1").getText()); // Esempio: Prendiamo gli appunti dalla prima stanza
        dialog.add(new JScrollPane(appuntiArea), BorderLayout.CENTER);

        JButton confermaButton = new JButton("Conferma");
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aggiorna gli appunti nella stanza
                stanzeAppunti.get("Stanza 1").setText(appuntiArea.getText());
                dialog.dispose();
            }
        });

        dialog.add(confermaButton, BorderLayout.SOUTH);

        dialog.setSize(400, 300);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void cambiaColoreTesto(){
        Color coloreScelto = JColorChooser.showDialog(frame , "Seleziona colore testo" , Color.black);

        if(coloreScelto != null){
            for(JTextArea appuntiArea : stanzeAppunti.values()){
                appuntiArea.setForeground(coloreScelto);
            }
        }
    }

    private void spostaStanza(String direzione){
        System.out.println("Spostati "+direzione);
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
