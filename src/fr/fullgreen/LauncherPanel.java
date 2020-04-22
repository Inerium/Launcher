package fr.fullgreen;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import fr.theshark34.swinger.textured.STexturedProgressBar;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LauncherPanel extends JPanel implements SwingerEventListener {

    private Image background = Swinger.getResource("overlay.png");
    private Saver saver = new Saver(new File(Launcher.DIR, "launcher.properties"));
    private JTextField usernameField = new JTextField(saver.get("username"));
    private JPasswordField passwordField = new JPasswordField();
    private STexturedButton playButton = new STexturedButton(Swinger.getResource("connexion.png"));
    private STexturedButton quitButton = new STexturedButton(Swinger.getResource("close.png"));
    private STexturedButton siteButton = new STexturedButton(Swinger.getResource("site.png"), Swinger.getResource("site.png"));
    private STexturedButton discordButton = new STexturedButton(Swinger.getResource("discord.png"), Swinger.getResource("discord.png"));
    private STexturedButton teamspeakButton = new STexturedButton(Swinger.getResource("twitter.png"), Swinger.getResource("twitter.png"));
    private STexturedButton forumButton = new STexturedButton(Swinger.getResource("forum.png"), Swinger.getResource("forum.png"));
    private STexturedButton ramButton = new STexturedButton(Swinger.getResource("option.png"), Swinger.getResource("option.png"));
    private static STexturedProgressBar progressBar = new STexturedProgressBar(Swinger.getResource("air.png"), Swinger.getResource("barfull.png"));
    private JLabel InfoLabel = new JLabel("", SwingConstants.CENTER);

    private RamSelector ramSelector = new RamSelector(new File(Launcher.DIR, "ram.txt"));
    public LauncherPanel() {
        this.setLayout(null);

        setBackground(new Color(255, 255, 255, 0));
        usernameField.setForeground(new Color(0, 0, 0));
        usernameField.setFont(new Font("Arial", 1, 20));
        usernameField.setCaretColor(new Color(0, 0, 0));
        usernameField.setOpaque(false);
        usernameField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        usernameField.setBounds(420, 340, 150, 32);
        add(usernameField);

        setBackground(new Color(255, 255, 255, 0));
        passwordField.setForeground(new Color(0, 0, 0));
        passwordField.setFont(new Font("Arial", 1, 20));
        passwordField.setCaretColor(new Color(0, 0, 0));
        passwordField.setOpaque(false);
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        passwordField.setBounds(420, 390, 150, 32);
        add(passwordField);

        playButton.setBounds(355, 445, 150, 30);
        playButton.addEventListener(this);
        add(playButton);

        quitButton.addEventListener(this);
        quitButton.setBounds(610, 256, 28, 28);
        quitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(quitButton);

        discordButton.addEventListener(this);
        discordButton.setOpaque(true);
        discordButton.setBounds(670, 250, 75, 75);
        discordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(discordButton);

        siteButton.addEventListener(this);
        siteButton.setOpaque(true);
        siteButton.setBounds(696, 400, 60, 60);
        siteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(siteButton);

        teamspeakButton.addEventListener(this);
        teamspeakButton.setOpaque(true);
        teamspeakButton.setBounds(716, 326, 60, 60);
        teamspeakButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(teamspeakButton);

        forumButton.addEventListener(this);
        forumButton.setOpaque(true);
        forumButton.setBounds(660, 470, 75, 75);
        forumButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(forumButton);

        ramButton.addEventListener(this);
        ramButton.setBounds(240, 250, 75, 75);
        ramButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(ramButton);

        progressBar.setBounds(164, 574, 610, 46);
        add(progressBar);

        InfoLabel.setForeground(Color.WHITE);
        InfoLabel.setBounds(340, 440, 400, 400);
        InfoLabel.setFont(new Font("SegoeUI", 0, 20));
        InfoLabel.setFocusable(true);
        add(InfoLabel);

    }


    @Override
    public void onEvent(SwingerEvent e) {
        if (e.getSource() == playButton) {

            setFieldsEnabled(false);

            if (usernameField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "Erreur, veuillez entrer un pseudo et un mot de passe valides.", "Erreur ByxusMC", JOptionPane.ERROR_MESSAGE);
                setFieldsEnabled(true);
                return;
            }

            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        Launcher.auth(usernameField.getText(), passwordField.getText());
                    } catch (AuthenticationException e) {
                        JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de se connecter : " + e.getErrorModel().getErrorMessage(), "Erreur ByxusMC", JOptionPane.ERROR_MESSAGE);
                        setFieldsEnabled(true);
                        return;
                    }

                    try {
                        Launcher.update();
                    } catch (Exception e) {
                        Launcher.interruptThread();
                        JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de mettre le jeu a jour : " + e, "Erreur ByxusMC", JOptionPane.ERROR_MESSAGE);
                        setFieldsEnabled(true);
                        return;
                    }

                    try {
                        Launcher.launch();
                    } catch (LaunchException e) {
                        JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur, impossible de lancer le jeu : " + e, " Erreur ByxusMC", JOptionPane.ERROR_MESSAGE);
                        setFieldsEnabled(true);
                    }

                }

            };
            t.start();
        } else if (e.getSource() == this.ramButton){
                ramSelector.display();
        } else if (e.getSource() == quitButton){
            System.exit(0);
        }else if(e.getSource() == discordButton)
            try {
                Desktop.getDesktop().browse(new URI("https://discordapp.com/invite/ZS553jt"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }else if(e.getSource() == siteButton)
            try {
                Desktop.getDesktop().browse(new URI("http://byxus.fr/"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }else if(e.getSource() == forumButton)
            try {
                Desktop.getDesktop().browse(new URI("https://forum.byxus.fr"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }else if(e.getSource() == teamspeakButton)
            try {
                Desktop.getDesktop().browse(new URI("https://twitter.com/byxusmc_net"));
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private void setFieldsEnabled(boolean enabled) {
        usernameField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
        playButton.setEnabled(enabled);

    }

    public STexturedProgressBar getProgressBar() {
        return progressBar;
    }

    public void setInfoText(String text) {
        InfoLabel.setText(text);
    }

    public RamSelector getRamSelector() {
        return ramSelector;
    }
}