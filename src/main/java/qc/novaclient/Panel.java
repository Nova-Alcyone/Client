// Refresh le code quand on ce connecte! Sinon les images et le nom ne se mettent pas a jour avant le relancement de l'application.

package qc.novaclient;

import qc.novaclient.utils.ConfigReader;
import qc.novaclient.utils.MicrosoftThread;
import qc.novaclient.utils.OptionPanel;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CompletableFuture;

public class Panel extends JPanel {

    private BufferedImage getImageFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private final Image background = getImageFromURL(
            "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/background.png");
    private final JButton forum = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/forum.png"));
    private final JButton settings = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/settings.png"));
    private final JButton settings1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/settings_hover.png"));
    private final JButton forum1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/forum_hover.png"));
    private final JButton support = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/support.png"));
    private final JButton support1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/support_hover.png"));
    private final JButton discord = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/discord.png"));
    private final JButton discord1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/discord_hover.png"));
    private final JButton play = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/lancer.png"));
    private final JButton play1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/play_clicked.png"));
    private final JButton boutique = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/boutique.png"));
    private final JButton boutique1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/boutique_hover.png"));
    private final JButton close = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/close.png"));

    private static JProgressBar progressBar = new JProgressBar();
    private static JProgressBar progressBar2 = new JProgressBar();

    private boolean boutiqueHover;
    private boolean settingsClicked = false;
    private boolean forumHover;
    private boolean supportHover;
    private boolean discordHover;
    private boolean playClickedB;
    private boolean loading;
    private boolean settingsHover;

    private void handleLaunchUpdateError(Exception exception) {
        exception.printStackTrace(); // You can print the stack trace for debugging purposes
        progressBar.setVisible(false);
        JOptionPane.showMessageDialog(null,
                "Essayez de redémarrer votre lanceur. Si le problème persiste, contactez le support.", "Erreur",
                JOptionPane.ERROR_MESSAGE);
    }

    public Panel() {
        this.setLayout(null);

        play.setBounds(342, 350, 283, 63);
        this.add(play);
        play.addActionListener(e -> {
            final String refresh_token = ConfigReader.getRefreshToken();
            if (refresh_token != null && !refresh_token.isEmpty()) {
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                loading = false;
                progressBar.setVisible(false);
                progressBar2.setVisible(false);

                executor.schedule(() -> {
                    try {
                        progressBar.setString("Opérations de pré-lancement en cours...");
                        progressBar.setMaximum(100);
                        progressBar.setVisible(true);
                        CompletableFuture.runAsync(() -> {
                            try {
                                Launcher.update();
                                loading = true;
                                Thread.sleep(500);
                                progressBar.setValue(100);
                                if (loading == true) {
                                    try {
                                        Launcher.launch();
                                        loading = false;
                                        progressBar.setVisible(false);
                                        progressBar2.setValue(0);
                                        progressBar2.setMaximum(100);
                                        progressBar2.setString("Lancement en cours...");
                                        progressBar2.setVisible(true);
                                        for (int i = 0; i <= 100; i += 1) {
                                            try {
                                                if (i == 100) {
                                                    break;
                                                }
                                                Thread.sleep(75);
                                                progressBar2.setValue(i);
                                            } catch (Exception updateException) {
                                                handleLaunchUpdateError(updateException);
                                                return;
                                            }
                                        }
                                    } catch (Exception launchException) {
                                        handleLaunchUpdateError(launchException);
                                    }
                                }
                                progressBar2.setVisible(false);
                            } catch (Exception updateException) {
                                handleLaunchUpdateError(updateException);
                                return;
                            }
                        });
                        CompletableFuture.runAsync(() -> {
                            progressBar.setValue(0);
                            while (loading == false) {
                                if (loading == false) {
                                    for (int i = 0; i < 90; i += 1) {
                                        try {
                                            if (i == 90 || loading == true) {
                                                break; // Break out of the loop when i reaches 90
                                            }
                                            Thread.sleep(500);
                                            progressBar.setValue(i);
                                        } catch (Exception updateException) {
                                            handleLaunchUpdateError(updateException);
                                            return;
                                        }
                                    }
                                }
                            }
                        });
                    } catch (Exception updateException) {
                        handleLaunchUpdateError(updateException);
                        return;
                    }
                    progressBar2.setVisible(false);
                }, 1, TimeUnit.SECONDS);

                executor.shutdown();
            } else {
                Thread t = new Thread(new MicrosoftThread());
                t.start();
            }

            playClickedB = true;
            play.repaint();
        });

        play.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        play1.setBounds(342, 350, 283, 63);
        this.add(play1);
        play1.setVisible(false);

        close.setBounds(925, 0, 51, 51);
        this.add(close);
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.exit(0);
            }

            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

        boutique.setBounds(12, 100, 51, 51);
        this.add(boutique);
        boutique.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://novaalcyone.com/shop");
                desktop.browse(uri);
            } catch (IOException | URISyntaxException excp) {
                excp.printStackTrace();
            }
        });
        boutique.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                boutiqueHover = true;
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                boutiqueHover = false;
            }
        });

        boutique1.setBounds(12, 100, 188, 51);
        this.add(boutique1);
        boutique1.setVisible(false);

        forum.setBounds(12, 170, 51, 51);
        this.add(forum);
        forum.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://novaalcyone.com/forum");
                desktop.browse(uri);
            } catch (IOException | URISyntaxException excp) {
                excp.printStackTrace();
            }
        });
        forum.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                forumHover = true;
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                forumHover = false;
            }
        });

        forum1.setBounds(12, 170, 188, 51);
        this.add(forum1);
        forum1.setVisible(false);

        support.setBounds(12, 240, 51, 51);
        this.add(support);
        support.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://novaalcyone.com/support/tickets");
                desktop.browse(uri);
            } catch (IOException | URISyntaxException excp) {
                excp.printStackTrace();
            }
        });
        support.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                supportHover = true;
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                supportHover = false;
            }
        });

        support1.setBounds(12, 240, 188, 51);
        this.add(support1);
        support1.setVisible(false);

        discord.setBounds(12, 310, 51, 51);
        this.add(discord);
        discord.addActionListener(e -> {
            Desktop desktop = Desktop.getDesktop();
            try {
                URI uri = new URI("https://discord.gg/XYhybrGzqb");
                desktop.browse(uri);
            } catch (IOException | URISyntaxException excp) {
                excp.printStackTrace();
            }
        });
        discord.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                discordHover = true;
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                discordHover = false;
            }
        });

        discord1.setBounds(12, 310, 188, 51);
        this.add(discord1);
        discord1.setVisible(false);

        // Create the progress bar
        progressBar = new JProgressBar();
        progressBar.setBounds(342, 530, 283, 20);
        progressBar.setStringPainted(true); // Show progress percentage
        this.add(progressBar);
        progressBar.setVisible(false);

        progressBar2 = new JProgressBar();
        progressBar2.setBounds(342, 530, 283, 20);
        progressBar2.setStringPainted(true); // Show progress percentage
        this.add(progressBar2);
        progressBar2.setVisible(false);

        settings.setBounds(12, 488, 51, 51);
        this.add(settings);
        settings.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                settingsHover = true;
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                settingsHover = false;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                OptionPanel settingsPanel = new OptionPanel();

                JFrame settingsFrame = new JFrame("Options");
                settingsFrame.setSize(621, 233);
                settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                settingsFrame.setLocationRelativeTo(null);
                settingsFrame.setResizable(false);
                settingsFrame.setIconImage(Frame.getImageFromURL());
                settingsFrame.getContentPane().add(settingsPanel);

                settingsFrame.setVisible(true);
            }
        });

        settings1.setBounds(12, 488, 188, 51);
        this.add(settings1);
        settings1.setVisible(false);
    }

    private JButton createButton(BufferedImage image) {
        JButton button = new JButton();
        button.setIcon(new ImageIcon(image));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);

        if (!boutiqueHover) {
            boutique1.setVisible(false);
            repaint();
        }
        if (boutiqueHover) {
            boutique1.setVisible(true);
        }
        if (forumHover) {
            forum1.setVisible(true);
            repaint();
        }
        if (!forumHover) {
            forum1.setVisible(false);
            repaint();
        }
        if (supportHover) {
            support1.setVisible(true);
            repaint();
        }
        if (!supportHover) {
            support1.setVisible(false);
            repaint();
        }
        if (discordHover && !settingsClicked) {
            discord1.setVisible(true);
            repaint();
        }
        if (!discordHover && !settingsClicked) {
            discord1.setVisible(false);
            repaint();
        }
        if (settingsHover) {
            settings1.setVisible(true);
            repaint();
        }
        if (!settingsHover) {
            settings1.setVisible(false);
            repaint();
        }
        if (playClickedB && settingsClicked != true) {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            play.setVisible(false);
            play1.setVisible(true);
            repaint();
            executor.schedule(() -> {
                playClickedB = false;
            }, 1, TimeUnit.MILLISECONDS);
            executor.shutdown();
        }
        if (!playClickedB && settingsClicked != true) {
            play1.setVisible(false);
            play.setVisible(true);
            repaint();
        }

        if (settingsClicked) {
            play1.setVisible(false);
            play.setVisible(false);
            repaint();
        }
    }

    public static void update() {
        try {
            boolean updatesAvailable = checkForUpdates();

            if (updatesAvailable) {
                downloadUpdates();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkForUpdates() {
        // Simulate checking for updates
        // Replace this with actual update checking logic
        return true; // Assume updates are available for demonstration
    }

    private static void downloadUpdates() {
        final double increment = 0.1; // Adjust this value based on your actual update progress

        // Simulate downloading updates
        for (double progress = 0; progress <= 1.0; progress += increment) {
            try {
                Thread.sleep(500); // Simulate downloading time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Update progress on the JavaFX Application thread
            double finalProgress = progress;
            javafx.application.Platform.runLater(() -> progressBar.setValue((int) (finalProgress * 100)));
        }
    }
}
