package io.github.blackfishlabs.forza;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class ForzaApplication {

    private static final Logger logger = LogManager.getLogger(ForzaApplication.class);

    public static void main(String[] args) {
        initialize();
    }

    private static void initialize() {
        lookAndFeel();
        SwingUtilities.invokeLater(TrayIconUI::createAndShowGUI);

        logger.info(ServerApplication.start(8185));
        logger.info(">> Workspace: " + ForzaProperties.getInstance().getDirApplication());
    }

    private static void lookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }


}
