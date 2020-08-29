package io.github.blackfishlabs.forza;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

class TrayIconUI {

    private static final Logger logger = LogManager.getLogger(TrayIconUI.class);
    private static final String LOGO_PNG = "logo_min.png";
    private static final String BLACKFISH_LABS = "BLACKFISH LABS";

    static void createAndShowGUI() {

        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported!");
            return;
        }

        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(createImage(ForzaProperties.getInstance().getDirImg().concat(LOGO_PNG)));
        final SystemTray tray = SystemTray.getSystemTray();

        final MenuItem developer = new MenuItem(BLACKFISH_LABS);
        final MenuItem aboutItem = new MenuItem("Sobre");
        final MenuItem exitItem = new MenuItem("Sair");

        popup.add(developer).setEnabled(false);
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            logger.error("TrayIcon could not be added.");
            return;
        }

        String sb = "FORZA APPLICATION - DESKTOP" +
                "\n" +
                "\n" +
                "BLACKFISH LABS - https://blackfishlabs.github.io";
        ActionListener actionListener = e -> show(sb);

        trayIcon.addActionListener(actionListener);
        aboutItem.addActionListener(actionListener);

        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            ServerApplication.stop();
            System.exit(0);
        });

        trayIcon.setImageAutoSize(true);
    }

    private static void show(String message) {
        JOptionPane.showMessageDialog(null, message, BLACKFISH_LABS, JOptionPane.INFORMATION_MESSAGE);
    }

    private static Image createImage(final String path) {
        final File file = new File(path);
        Image img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

}
