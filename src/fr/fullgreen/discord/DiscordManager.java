package fr.fullgreen.discord;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordManager {

    private DiscordRPC lib;
    private DiscordEventHandlers handlers;
    private DiscordRichPresence presence;

    public DiscordManager() {
        lib = DiscordRPC.INSTANCE;
        handlers = new DiscordEventHandlers();
        lib.Discord_Initialize("699586229154611211", handlers, true, "");
        presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.largeImageKey = "by2";
        presence.largeImageText = "";
        presence.smallImageKey = "by2";
        presence.details = "Serveur Mini Jeux";
        presence.state = "1.8 -> 1.15.2";
        lib.Discord_UpdatePresence(presence);
    }

    public void run() {
        new Thread("RPC-Callback-Handler") {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    lib.Discord_UpdatePresence(presence);
                    lib.Discord_RunCallbacks();
                    try {
                        Thread.sleep(2000);
                    }catch(InterruptedException e) {

                    }
                }
            }
        }.start();
    }

    public void updateTime() {
        presence.startTimestamp = System.currentTimeMillis() / 1000;
    }

}

