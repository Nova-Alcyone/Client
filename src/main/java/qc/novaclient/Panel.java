package qc.novaclient;

import qc.novaclient.utils.MicrosoftThread;

import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.*;


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

    private final Image background = getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/background.png?raw=true");
    private final JButton profil1 = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/profil-hover.png?raw=true"));
    private final JButton forum = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/Forum.png?raw=true"));
    private final JButton forum1 = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/forum-hover.png?raw=true"));
    private final JButton support = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/Support.png?raw=true"));
    private final JButton support1 = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/support-hover.png?raw=true"));
    private final JButton discord = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/Discord.png?raw=true"));
    private final JButton discord1 = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/discord-hover.png?raw=true"));
    private final JButton play = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/lancer.png?raw=true"));
    private final JButton play1 = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/play-clicked.png?raw=true"));
    private final JButton close = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/Close.png?raw=true"));
    private final RamSelector ramSelector = new RamSelector(qc.novaclient.Frame.getRamFile());
    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JMenuItem loginMenuItem = new JMenuItem("Connexion");
    private final JMenuItem disconnectMenuItem = new JMenuItem("Déconnexion");
    private final JMenuItem settingsMenuItem = new JMenuItem("Options");
    private final JButton boutique = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/boutique.png?raw=true"));
    private final JButton boutique1 = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/boutique-hover.png?raw=true"));
    private static JProgressBar progressBar = new JProgressBar();
    private static JProgressBar progressBar2 = new JProgressBar();
    private List<String> logMessages = new ArrayList<>(); // Create an in-memory log storage

    private boolean boutiqueHover;
    private boolean profilHover;
    private boolean forumHover;
    private boolean supportHover;
    private boolean discordHover;
    private boolean playClickedB;
    private boolean closeHover;
    private boolean loading;

    private void handleLaunchUpdateError(Exception exception) {
        exception.printStackTrace(); // You can print the stack trace for debugging purposes
        progressBar.setVisible(false);
        JOptionPane.showMessageDialog(null, "Essayez de redémarrer votre lanceur. Si le problème persiste, contactez le support.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
    public Panel() {
        this.setLayout(null);

        play.setBounds(342, 350, 283, 63);
        this.add(play);
        play.addActionListener(e -> {
            final String refresh_token = qc.novaclient.Frame.getSaver().get("refresh_token");
            if (refresh_token != null && !refresh_token.isEmpty()) {
                ramSelector.save();
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                loading = false;
                System.out.println(loading);
                progressBar.setVisible(false);
                progressBar2.setVisible(false);

                executor.schedule(() -> {
                    try {
                        progressBar.setString("Opérations de pré-lancement en cours...");
                        progressBar.setMaximum(100);
                        progressBar.setVisible(true);
                        CompletableFuture<Void> task1 = CompletableFuture.runAsync(() -> {
                            try {
                                Launcher.update();
                                loading = true;
                                Thread.sleep(500);
                                progressBar.setValue(100);
                                if (loading == true ) {
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
                        CompletableFuture<Void> task2 = CompletableFuture.runAsync(() -> {
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

        close.setBounds(900, 0, 51, 51);
        this.add(close);
        close.addActionListener(e -> System.exit(0));
        close.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                closeHover = true;
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                closeHover = false;
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

        JButton profil = createButton(getImageFromURL("https://github.com/Muyga/NovaRepo/blob/main/Launcher/images/Profil.png?raw=true"));
        profil.setBounds(12, 488, 51, 51);
        this.add(profil);
        profil.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                profilHover = true;
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                profilHover = false;
            }
        });
        profil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final String refresh_token = qc.novaclient.Frame.getSaver().get("refresh_token");
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
                if (refresh_token != null && !refresh_token.isEmpty()) {
                    popupMenu.add(disconnectMenuItem);
                    popupMenu.add(settingsMenuItem);
                    popupMenu.remove(loginMenuItem);
                } else {
                    popupMenu.add(loginMenuItem);
                    popupMenu.add(settingsMenuItem);
                }
            }
        });

        // ActionListener for loginMenuItem (Microsoft login event)
        loginMenuItem.addActionListener(e -> {
            Thread t = new Thread(new MicrosoftThread());
            t.start();
        });

        // ActionListener for settingsMenuItem (Settings event)
        settingsMenuItem.addActionListener(e -> ramSelector.display());

        disconnectMenuItem.addActionListener(e -> {
            qc.novaclient.Frame.getSaver().remove("refresh_token"); // Delete the stored refresh token
            JOptionPane.showMessageDialog(
                    null,
                    "Vous êtes désormais déconnecté.",
                    "Décconecté",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // Reset the UI to the initial state (remove userMenuItem and disconnectMenuItem, add loginMenuItem)
            popupMenu.remove(disconnectMenuItem);
            popupMenu.remove(settingsMenuItem);
            popupMenu.add(loginMenuItem);
            popupMenu.add(settingsMenuItem);
        });

        profil1.setBounds(12, 488, 188, 51);
        this.add(profil1);
        profil1.setVisible(false);

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
        }
        if (boutiqueHover) {
            boutique1.setVisible(true);
        }
        if (forumHover) {
            forum1.setVisible(true);
        }
        if (!forumHover) {
            forum1.setVisible(false);
        }
        if (supportHover) {
            support1.setVisible(true);
        }
        if (!supportHover) {
            support1.setVisible(false);
        }
        if (discordHover) {
            discord1.setVisible(true);
        }
        if (!discordHover) {
            discord1.setVisible(false);
        }
        if (profilHover) {
            profil1.setVisible(true);
        }
        if (!profilHover) {
            profil1.setVisible(false);
        }
        if (profilHover) {
            profil1.setVisible(true);
        }
        if (playClickedB) {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            play.setVisible(false);
            play1.setVisible(true);
            executor.schedule(() -> {
                playClickedB = false;
            }, 1, TimeUnit.MILLISECONDS);
            executor.shutdown();
        }
        if (!playClickedB) {
            play1.setVisible(false);
            play.setVisible(true);
        }
    }

    public RamSelector getRamSelector() {
        return ramSelector;
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