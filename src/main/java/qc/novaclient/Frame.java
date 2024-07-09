package qc.novaclient;

import qc.novaclient.utils.ConfigReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFrame;

import fr.theshark34.swinger.util.WindowMover;

public class Frame extends JFrame {

    private static Frame instance;
    private final Panel panel;
    public static File saverFile = new File(String.valueOf(Launcher.getPath()), "user.stock");

    public Frame() {
        instance = this;
        this.setTitle("Nova Client v" + ConfigReader.getVersion());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(975, 605);
        this.setLocationRelativeTo(null);
        this.setIconImage(getImageFromURL());
        this.setContentPane(panel = new Panel());
        this.setResizable(false);
        this.setUndecorated(true);

        WindowMover mover = new WindowMover(this);
        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);

        this.setVisible(true);
    }

    static BufferedImage getImageFromURL() {
        try {
            URL url = new URL(
                    "https://github.com/Nova-Alcyone/Repo/blob/main/Launcher/images/nova_alcyone.png?raw=true");
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        Launcher.crashFile.mkdirs();
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

    public static File getSaver() {
        return saverFile;
    }

    public static String get(String string) {
        return null;
    }
}
