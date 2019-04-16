package br.com.ks.teste.uds.main;

/**
 *
 * @author Jonny
 */
import br.com.ks.teste.uds.view.MainView;
import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MenuDesktopTray {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
            Logger.getLogger(MenuDesktopTray.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();

        BufferedImage bufferedImage = null;
        try {

            bufferedImage = ImageIO.read(ClassLoader.getSystemResource("image/uds.jpg"));

        } catch (IOException ex) {
            Logger.getLogger(MenuDesktopTray.class.getName()).log(Level.SEVERE, null, ex);
        }
        int trayIconWidth = new TrayIcon(bufferedImage).getSize().width;
        int trayIconHeight = new TrayIcon(bufferedImage).getSize().height;
        final TrayIcon trayIcon = new TrayIcon(bufferedImage.getScaledInstance(trayIconWidth, trayIconHeight, Image.SCALE_SMOOTH));
        trayIcon.setToolTip("UDS Tecnologia - Jonny Test");
        trayIcon.setImageAutoSize(true);

        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem aboutItem = new MenuItem("About");
        MenuItem restartServerItem = new MenuItem("Restart");
        MenuItem exitItem = new MenuItem("Exit");

        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(restartServerItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
            return;
        }

        final MainView mainView = new MainView();
        mainView.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                mainView.setVisible(false);
            }
        });
        mainView.start();

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {

                    mainView.setVisible(true);
                    mainView.setState(Frame.NORMAL);
                }
            }
        });

        aboutItem.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(null,
                    "UDS Tecnologia - Jonny Test");
        });

        restartServerItem.addActionListener((ActionEvent e) -> {
            mainView.stop();
            mainView.start();
        });

        exitItem.addActionListener((ActionEvent e) -> {
            mainView.stop();
            tray.remove(trayIcon);
            System.exit(0);
        });
    }
}
