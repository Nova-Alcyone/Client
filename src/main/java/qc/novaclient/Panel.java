// Quand on clique sur le lanceur, il ne bouge plus comme avant... ajouter un bar en haut? Sinon il faut revoir le clique pour sortir du Panel Option. 
// Refresh le code quand on ce connecte! Sinon les images et le nom ne se mettent pas a jour avant le relancement de l'application.
// Regarder pour le bouton profil qui bouge, sinon c'est pas GRAVE mais dérangeant.

package qc.novaclient;

import qc.novaclient.utils.ConfigReader;
import qc.novaclient.utils.MicrosoftThread;

import javax.imageio.ImageIO;
import javax.swing.*;

import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;

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
import java.awt.geom.*;

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
    private final Image settingsPanelLoggedOut = getImageFromURL(
            "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil-open-logged-OutOf.png");
    private final Image settingsPanelLoggedIn = getImageFromURL(
            "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil-open-logged-in.png");
    private final Image settingsPanelGameplay = getImageFromURL(
            "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/gameplay-on.png");
    private final JButton settings = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/settings.png"));
    private final JButton settings1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/settings-hover.png"));
    private final JButton forum = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/Forum.png"));
    private final JButton forum1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/forum-hover.png"));
    private final JButton support = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/Support.png"));
    private final JButton support1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/support-hover.png"));
    private final JButton discord = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/Discord.png"));
    private final JButton discord1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/discord-hover.png"));
    private final JButton play = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/lancer.png"));
    private final JButton play1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/play-clicked.png"));
    private final JButton close = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/Close.png"));
    private final JButton boutique = createButton(
            getImageFromURL("https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/boutique.png"));
    private final JButton boutique1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/boutique-hover.png"));
    private final JButton gameplay = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/gameplay-normal.png"));
    private final JButton gameplay1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/gameplay-clicked.png"));
    private final JButton profil = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil-normal.png"));
    private final JButton profil1 = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil-clicked.png"));
    private final JButton connexion = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/connexion.png"));
    private final JButton deconnexion = createButton(
            getImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/disconnect-hover.png"));

    // Create a method to generate a rounded image
    private BufferedImage getRoundedImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage roundedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = roundedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, width, height);
        g2.setClip(circle);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();
        return roundedImage;
    }

    private JLabel usernameLabel = new JLabel(ConfigReader.getUsername());

    private Image profilPicture;
    private Image roundedProfilPicture;

    private static JProgressBar progressBar = new JProgressBar();
    private static JProgressBar progressBar2 = new JProgressBar();

    private boolean boutiqueHover;
    private boolean settingsHover;
    private boolean settingsClicked = false;
    private boolean settingsClickedIn;
    private boolean forumHover;
    private boolean supportHover;
    private boolean discordHover;
    private boolean playClickedB;
    private boolean loading;
    private boolean gameplayClicked = false;
    private boolean profilClicked = false;
    private boolean OptionsPanel = false;

    private void handleLaunchUpdateError(Exception exception) {
        exception.printStackTrace(); // You can print the stack trace for debugging purposes
        progressBar.setVisible(false);
        JOptionPane.showMessageDialog(null,
                "Essayez de redémarrer votre lanceur. Si le problème persiste, contactez le support.", "Erreur",
                JOptionPane.ERROR_MESSAGE);
    }

    public Panel() {
        this.setLayout(null);
        if (ConfigReader.getUsername() != null) {
            String username = ConfigReader.getUsername();
            int maxCharactersPerLine = (username.length() <= 8) ? 8 : 16; // Use 8 characters if username is short,
                                                                          // otherwise 16

            usernameLabel.setFont(new Font("Verdana", Font.BOLD, 25));

            // Calculate the x-position to center the label within the bounds
            int xPosition = 340 + (200 - (maxCharactersPerLine * 15)) / 2;

            usernameLabel.setBounds(xPosition, 250, maxCharactersPerLine * 15, 300);
            usernameLabel.setForeground(Color.WHITE);
            this.add(usernameLabel);
            usernameLabel.setVisible(false);

            // Create the URL based on ConfigReader.getUsername()
            String imageUrl = "https://mc-heads.net/avatar/" + ConfigReader.getUsername();

            // Load the original image
            profilPicture = getImageFromURL(imageUrl);

            // Create and store the rounded image
            if (profilPicture != null) {
                roundedProfilPicture = getRoundedImage((BufferedImage) profilPicture);
            }
        }

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (profil.isVisible() || gameplay.isVisible()) {
                    if (!isWithinImageBounds(e.getX(), e.getY())) {
                        profilClicked = false;
                        gameplayClicked = false;
                        settingsClicked = false;
                        OptionsPanel = false;
                        gameplay.setVisible(false);
                        profil.setVisible(false);
                        discord.setVisible(true);
                        settings.setVisible(true);
                        usernameLabel.setVisible(false);
                        repaint();
                    }
                }
            }

            private boolean isWithinImageBounds(int x, int y) {
                Rectangle imageBounds = new Rectangle(12, 300, 621, 233); // Define image bounds here
                return imageBounds.contains(x, y);
            }
        });

        play.setBounds(342, 350, 283, 63);
        this.add(play);
        play.addActionListener(e -> {
            final String refresh_token = ConfigReader.getRefreshToken();
            if (refresh_token != null && !refresh_token.isEmpty()) {
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

        close.setBounds(900, 0, 51, 51);
        this.add(close);
        close.addActionListener(e -> System.exit(0));
        close.addMouseListener(new MouseAdapter() {
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
                if (ConfigReader.getRefreshToken() != null) {
                    settingsClickedIn = true;
                    settingsClicked = true;
                    OptionsPanel = true;

                    settings1.setVisible(false);
                    settings.setVisible(false);
                    discord1.setVisible(false);
                    discord.setVisible(false);
                } else {
                    settingsClickedIn = false;
                    settingsClicked = true;
                    OptionsPanel = true;

                    settings1.setVisible(false);
                    settings.setVisible(false);
                    discord1.setVisible(false);
                    discord.setVisible(false);
                }
            }
        });

        settings1.setBounds(12, 488, 188, 51);
        this.add(settings1);
        settings1.setVisible(false);

        profil.setBounds(20, 350, 188, 51);
        this.add(profil);
        profil.setVisible(false);
        profil.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            public void mouseClicked(MouseEvent e) {
                profilClicked = true;
                gameplayClicked = false;
                repaint();
            }
        });

        profil1.setBounds(20, 350, 198, 51);
        this.add(profil1);
        profil1.setVisible(false);

        gameplay.setBounds(20, 450, 188, 51);
        this.add(gameplay);
        gameplay.setVisible(false);
        gameplay.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            public void mouseClicked(MouseEvent e) {
                gameplayClicked = true;
                profilClicked = false;
                repaint();
            }
        });

        gameplay1.setBounds(20, 450, 198, 51);
        this.add(gameplay1);
        gameplay1.setVisible(false);

        connexion.setBounds(335, 460, 188, 51);
        this.add(connexion);
        connexion.setVisible(false);
        connexion.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            public void mouseClicked(MouseEvent e) {
                Thread t = new Thread(new MicrosoftThread());
                t.start();
            }
        });

        deconnexion.setBounds(335, 460, 188, 51);
        this.add(deconnexion);
        deconnexion.setVisible(false);
        deconnexion.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }

            public void mouseClicked(MouseEvent e) {
                ConfigReader.removeRefreshToken();
                ConfigReader.removeUsername();
            }
        });

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

        if (settingsClickedIn && !ConfigReader.getRefreshToken().isEmpty() || ConfigReader.getRefreshToken() != null
                || profilClicked && ConfigReader.getRefreshToken() != null) {
            g.drawImage(settingsPanelLoggedIn, 12, 300, 621, 233, this);
        } else if (!settingsClickedIn && ConfigReader.getRefreshToken() == null
                || profilClicked && ConfigReader.getRefreshToken() == null) {
            g.drawImage(settingsPanelLoggedOut, 12, 300, 621, 233, this);
        }
        if (!OptionsPanel) {
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
        }
        if (gameplayClicked) {
            g.drawImage(settingsPanelGameplay, 12, 300, 621, 233, this);
        }
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
        if (discordHover && !settingsClicked) {
            discord1.setVisible(true);
        }
        if (!discordHover && !settingsClicked) {
            discord1.setVisible(false);
        }
        if (settingsHover) {
            settings1.setVisible(true);
        }
        if (!settingsHover) {
            settings1.setVisible(false);
        }
        if (gameplayClicked) {
            usernameLabel.setVisible(false);
            gameplay1.setVisible(true);
            profil1.setVisible(false);
            profil.setVisible(true);
            profilClicked = false;
            deconnexion.setVisible(false);
            connexion.setVisible(false);
        } else if (profilClicked) {
            if (ConfigReader.getRefreshToken() != null) {
                connexion.setVisible(false);
                deconnexion.setVisible(true);
            } else {
                deconnexion.setVisible(false);
                connexion.setVisible(true);
            }
            profil1.setVisible(true);
            gameplay1.setVisible(false);
            usernameLabel.setVisible(true);
            gameplay.setVisible(true);
            gameplayClicked = false;
        } else if (settingsClicked) {
            if (ConfigReader.getRefreshToken() != null) {
                connexion.setVisible(false);
                deconnexion.setVisible(true);
            } else {
                deconnexion.setVisible(false);
                connexion.setVisible(true);
            }
            usernameLabel.setVisible(true);
            profil1.setVisible(true);
            gameplay.setVisible(true);
        } else {
            gameplay1.setVisible(false);
            profil1.setVisible(false);
        }
        if (playClickedB && settingsClicked != true) {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            play.setVisible(false);
            play1.setVisible(true);
            executor.schedule(() -> {
                playClickedB = false;
            }, 1, TimeUnit.MILLISECONDS);
            executor.shutdown();
        }
        if (!playClickedB && settingsClicked != true) {
            play1.setVisible(false);
            play.setVisible(true);
        }

        if (settingsClicked) {
            play1.setVisible(false);
            play.setVisible(false);
        }
        if (roundedProfilPicture != null && profil1.isVisible()) {
            g.drawImage(roundedProfilPicture, 400, 325, 50, 50, this);
            usernameLabel.setVisible(true);
        }
        if (!OptionsPanel) {
            deconnexion.setVisible(false);
            connexion.setVisible(false);
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

    public RamSelector getRamSelector() {
        return null;
    }
}