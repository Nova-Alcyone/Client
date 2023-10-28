package qc.novaclient;

import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.swinger.util.WindowMover;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class Frame extends JFrame {

    private static Frame instance;
    private final  Panel panel;
    private final  static File ramFile = new File(String.valueOf(Launcher.getPath()), "ram.txt");
    public static File saverFile = new File(String.valueOf(Launcher.getPath()), "user.stock");
    public static Saver saver = new Saver(saverFile);

    public Frame() {
        instance = this;
        this.setTitle("Nova Launcher");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(950, 550);
        this.setUndecorated(true);
        this.setLocationRelativeTo(null);
        this.setIconImage(getImageFromURL());
        this.setContentPane(panel = new Panel());

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);
    }

    private BufferedImage getImageFromURL() {
        try {
            URL url = new URL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/Nova-Antares.png?raw=true");
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        Launcher.crashFile.mkdirs();
        if (!ramFile.exists()) {
            ramFile.createNewFile();
        }
        if (!saverFile.exists()) {
            saverFile.createNewFile();
        }

        instance = new Frame();
    }


    public static Frame getInstance() {
        return instance;
    }

    public Panel getPanel() {
        return this.panel;
    }

    public static File getRamFile() {
        return ramFile;
    }

    public static Saver getSaver() {
        return saver;
    }
}