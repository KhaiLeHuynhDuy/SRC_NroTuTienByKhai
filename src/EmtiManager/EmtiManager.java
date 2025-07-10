/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EmtiManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import nro.jdbc.daos.PlayerDAO;
import nro.server.Client;

/**
 *
 * @author Lucy An Trom
 */
public class EmtiManager {

    private static EmtiManager instance = null;
    private ScheduledExecutorService scheduler;

    public static synchronized EmtiManager getInstance() {
        if (instance == null) {
            instance = new EmtiManager();
        }
        return instance;
    }

    public void startAutoSave() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                handleAutoSave();
            } catch (Exception e) {
                System.out.println("[AutoSaveManager] start autosave error: " + e.getLocalizedMessage());
            }
        }, 15, 15, TimeUnit.MINUTES);
    }

    public void handleAutoSave() {
        Client.gI().getPlayers().forEach(player -> {
            PlayerDAO.updatePlayer(player);
        });
    }
}
