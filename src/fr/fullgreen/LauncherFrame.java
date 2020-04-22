package fr.fullgreen;

import fr.fullgreen.discord.DiscordManager;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

import javax.swing.*;

public class LauncherFrame extends JFrame {

    private static LauncherFrame instance;
    private LauncherPanel launcherPanel;
    private static DiscordManager discord;

    public LauncherFrame() {
        this.setTitle("ByxusMc");
        this.setSize(962,581);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setUndecorated(true);
        this.setBackground(Swinger.TRANSPARENT);
        this.setIconImage(Swinger.getResource("by.png"));
        this.setContentPane(launcherPanel = new LauncherPanel());

        WindowMover mover  = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        Swinger.setSystemLookNFeel();
        Swinger.setResourcePath("/fr/fullgreen/res");

        instance = new LauncherFrame();
        discord = new DiscordManager();
    }

    public static LauncherFrame getInstance() {
        return instance;
    }

    public LauncherPanel getLauncherPanel() {
        return this.launcherPanel;
    }

}
