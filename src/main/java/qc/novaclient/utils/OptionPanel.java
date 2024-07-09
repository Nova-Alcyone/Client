package qc.novaclient.utils;

import javax.imageio.ImageIO;
import javax.swing.*;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class OptionPanel extends JPanel {
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private BufferedImage backgroundImage;
    private ImageIcon userAvatar;
    private int userAvatarX;
    private int userAvatarY;
    private Timer finishCheckTimer;
    private static AuthInfos authInfos;
    public static String playerUsername;
    public static boolean result;

    public OptionPanel() {
        setLayout(null);

        // Load button icons
        ImageIcon button1Icon = loadIconFromURL(
                "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil_normal.png");
        ImageIcon button2Icon = loadIconFromURL(
                "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/gameplay_normal.png");
        ImageIcon button3Icon = loadIconFromURL(
                "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/connexion.png");
        ImageIcon button4Icon = loadIconFromURL(
                "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/disconnect_hover.png");
        ImageIcon button5Icon = loadIconFromURL(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjsQZq0RwnUAYySw4Y7eJb2JZxr3Wnw8-TfTDqLGtdQQ&s");

        button1 = new JButton(button1Icon);
        button2 = new JButton(button2Icon);
        button3 = new JButton(button3Icon);
        button4 = new JButton(button4Icon);
        button5 = new JButton(button5Icon);
        button6 = new JButton(button5Icon);

        button1.setContentAreaFilled(false);
        button2.setContentAreaFilled(false);
        button3.setContentAreaFilled(false);
        button4.setContentAreaFilled(false);

        button1.setBorderPainted(false);
        button2.setBorderPainted(false);
        button3.setBorderPainted(false);
        button4.setBorderPainted(false);
        button5.setBorderPainted(false);
        button6.setBorderPainted(false);

        button1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button3.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button4.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button1.setBounds(5, 25, button1Icon.getIconWidth(), button1Icon.getIconHeight());
        button2.setBounds(5, 125, button2Icon.getIconWidth(), button2Icon.getIconHeight());
        button3.setBounds(305, 75, button3Icon.getIconWidth(), button3Icon.getIconHeight());
        button4.setBounds(305, 75, button4Icon.getIconWidth(), button4Icon.getIconHeight());
        button5.setBounds(button1.getX() + 168, 25, 80, 51);
        button6.setBounds(button2.getX() + 168, 125, 80, 51);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ConfigReader.getRefreshToken() != null) {
                    setBackgroundImageFromURL(
                            "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil_open_logged_in.png");
                    add(button4);
                    remove(button3);
                    loadUserAvatar(370, 20, 50, 50);
                } else {
                    setBackgroundImageFromURL(
                            "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil_open_logged_off.png");
                    add(button3);
                    remove(button4);
                    removeUserAvatar();
                }
                add(button5);
                remove(button6);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setBackgroundImageFromURL(
                        "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/gameplay_on.png");

                remove(button3);
                remove(button4);
                remove(button5);
                add(button6);
                removeUserAvatar();
                repaint();
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigReader.removeRefreshToken();
                ConfigReader.removeUsername();
                removeUserAvatar();
                Thread t = new Thread(new MicrosoftThread());
                t.start();
                startFinishCheckTimer();
            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigReader.removeRefreshToken();
                ConfigReader.removeUsername();
                removeUserAvatar();
                remove(button4);
                add(button3);
                setBackgroundImageFromURL(
                        "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil_open_logged_off.png");
                repaint();

                JOptionPane.showMessageDialog(null,
                        "Lors d'une reconnexion, il est possible que vous\nrencontriez des problèmes de connexion automatique.\nRelancer le lanceur pour y remédier",
                        "Information importante",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });

        add(button1);
        add(button2);
        add(button5);

        if (ConfigReader.getRefreshToken() != null) {
            setBackgroundImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil_open_logged_in.png");
            add(button4);
            remove(button3);
            loadUserAvatar(370, 20, 50, 50);
        } else {
            setBackgroundImageFromURL(
                    "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil_open_logged_off.png");
            add(button3);
            remove(button4);
            removeUserAvatar();
        }
    }

    private void setBackgroundImageFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            backgroundImage = ImageIO.read(url);
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ImageIcon loadIconFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return new ImageIcon(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadUserAvatar(int x, int y, int width, int height) {
        String username = ConfigReader.getUsername();
        String avatarURL = "https://mc-heads.net/avatar/" + username;
        try {
            URL url = new URL(avatarURL);
            BufferedImage avatarImage = ImageIO.read(url);

            // Resize the loaded avatar image
            avatarImage = resizeImage(avatarImage, width, height);

            userAvatar = new ImageIcon(avatarImage);
            userAvatarX = x;
            userAvatarY = y;
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

    private void removeUserAvatar() {
        userAvatar = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        if (userAvatar != null) {
            userAvatar.paintIcon(this, g, userAvatarX, userAvatarY);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Background Image Example");
                frame.setSize(800, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                OptionPanel optionPanel = new OptionPanel();
                frame.add(optionPanel);
                frame.setVisible(true);
            }
        });
    }

    private void startFinishCheckTimer() {
        finishCheckTimer = new Timer();
        finishCheckTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (ConfigReader.getRefreshToken() != null) {
                    finishCheckTimer.cancel();
                    add(button4);
                    remove(button3);
                    setBackgroundImageFromURL(
                            "https://raw.githubusercontent.com/Nova-Alcyone/Repo/main/Launcher/images/profil_open_logged_in.png");
                    loadUserAvatar(370, 20, 50, 50);

                }
            }
        }, 0, 1);
    }

    public static void auth() throws MicrosoftAuthenticationException {
        MicrosoftAuthenticator microsoftAuthenticator = new MicrosoftAuthenticator();
        final String refresh_token = ConfigReader.getRefreshToken();
        MicrosoftAuthResult result = null;
        if (refresh_token != null && !refresh_token.isEmpty()) {
            result = microsoftAuthenticator.loginWithRefreshToken(refresh_token);
            playerUsername = result.getProfile().getName();
            ConfigReader.setUsername(playerUsername);
            authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(),
                    result.getProfile().getId());
        } else {
            result = microsoftAuthenticator.loginWithWebview();
            playerUsername = result.getProfile().getName();
            ConfigReader.setUsername(playerUsername);
            ConfigReader.setRefreshToken(result.getRefreshToken());
            authInfos = new AuthInfos(result.getProfile().getName(), result.getAccessToken(),
                    result.getProfile().getId());
        }
    }

    public static AuthInfos getAuthInfos() {
        return authInfos;
    }
}
