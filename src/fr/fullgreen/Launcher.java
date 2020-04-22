package fr.fullgreen;

import fr.theshark34.openauth.AuthPoints;
import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openauth.Authenticator;
import fr.theshark34.openauth.model.AuthAgent;
import fr.theshark34.openauth.model.response.AuthResponse;
import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.internal.InternalLaunchProfile;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.explorer.Explorer;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import fr.theshark34.swinger.Swinger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Launcher {

    public static final GameVersion VERSION = new GameVersion("1.9.4", GameType.V1_8_HIGHER);
    public static final GameInfos INFOS = new GameInfos("ByxusMC", VERSION, new GameTweak[] {GameTweak.FORGE});
    public static final File DIR = INFOS.getGameDir();


    private static AuthInfos authInfos;
    private static Thread updateThread;

    public static void auth(String username, String password) throws AuthenticationException {
        Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
        AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
        authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
    }

    public static void update() throws Exception {
        SUpdate su = new SUpdate("https://supdate.byxus.fr/supdate", DIR);
        su.getServerRequester().setRewriteEnabled(true);
        su.addApplication(new FileDeleter());

        updateThread = new Thread() {
            private int val;
            private int max;

            @Override
            public void run() {
                while(!this.isInterrupted()) {
                    if(BarAPI.getNumberOfFileToDownload() == 0) {
                        LauncherFrame.getInstance().getLauncherPanel().setInfoText("Verification des fichiers");
                        continue;
                    }
                    val = (int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000);
                    max = (int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000);

                    LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setMaximum(max);
                    LauncherFrame.getInstance().getLauncherPanel().getProgressBar().setValue(val);

                    LauncherFrame.getInstance().getLauncherPanel().setInfoText("Telechargement des fichiers " +
                            BarAPI.getNumberOfDownloadedFiles() + "/" + BarAPI.getNumberOfFileToDownload() + " " +
                            Swinger.percentage(val, max)+ "%");
                }
            }
        };
        updateThread.start();

        su.start();
        updateThread.interrupt();
    }

    public static void launch() throws LaunchException {

        ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(INFOS, GameFolder.BASIC, authInfos);
        profile.getVmArgs().addAll(Arrays.asList(LauncherFrame.getInstance().getLauncherPanel().getRamSelector().getRamArguments()));
        ExternalLauncher launcher = new ExternalLauncher(profile);

        Process p  = launcher.launch();

        try {
            Thread.sleep(5000L);
            LauncherFrame.getInstance().setVisible(false);
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }


    public static void interruptThread() {
        updateThread.interrupt();
    }

}

