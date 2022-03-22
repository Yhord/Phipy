package com.yhord.phipy;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class Phipy extends JFrame {
    private final JPanel panel_pb;
    private final JLabel label_pb;
    private SystemTray tray;
    private final ImageIcon ImgIcon_phipyBuddy = new ImageIcon(getClass().getResource("/resources/phipy.png"));
    private final ImageIcon ImgIcon_phipyBuddy_highQuality = new ImageIcon(getClass().getResource("/resources/phipy_highQuality.png"));
    private final TrayIcon trayIcon = new TrayIcon((ImgIcon_phipyBuddy_highQuality).getImage(), "Phipy");
    private final int widthImg = 156;
    private final int heightImg = 198;

    private final String[] LINKS  = {"https://laec.fr/sommaire", "https://melenchon2022.fr", "https://melenchon2022.fr/programme/", "https://laec.fr/hasard", "https://laec.fr/hasard", "https://laec.fr/hasard", "https://laec.fr/hasard" ,"https://laec.fr/hasard", "https://laec.fr/hasard", "https://laec.fr/hasard"};

    private int waitMin = 5000; // Minimum time between events (ms)
    private int waitMax = 20000; // Maximum time between events (ms)

    public Phipy(String title) {
        super(title);

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, scrSize.width, scrSize.height);
        setType(Type.UTILITY);
        setUndecorated(true);
        setBackground(new Color(1.0f,1.0f,1.0f,0f));
        setAlwaysOnTop(true);
        setIconImage(ImgIcon_phipyBuddy_highQuality.getImage());

        label_pb = new JLabel(ImgIcon_phipyBuddy);
        label_pb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel_pb = new JPanel();
        panel_pb.setBounds(0, 0, scrSize.width, scrSize.height);
        panel_pb.setBackground(new Color(1.0f,1.0f,1.0f,0f));

        panel_pb.add(label_pb);
        panel_pb.add(new JLabel());

        add(panel_pb);

        initTrayMenu();
        initEvent();
        initConnections();
        setVisible(true);
        teleport();
    }

    public void initTrayMenu() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        else {
            tray = SystemTray.getSystemTray();
            trayIcon.setImageAutoSize(true);

            PopupMenu popup = new PopupMenu();

            MenuItem aboutItem = new MenuItem("À propos");
            MenuItem exitItem = new MenuItem("Quitter");

            aboutItem.addActionListener(e -> openBrowserTab("https://github.com/Yhord/Phipy"));
            exitItem.addActionListener(e -> System.exit(0));

            popup.add(aboutItem);
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.out.println("TrayIcon could not be added.");
            }
        }
    }

    public void initEvent() {
        new Thread(() -> {
            try {
                int gn = random(waitMin, waitMax);
                System.out.println("Next event in " + gn + "ms = ~" + gn/1000 + "s");
                Thread.sleep(gn);
                callRandomEvent();
                initEvent();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void initConnections() {
        MouseListenerHandler mlh = new MouseListenerHandler(this);
        label_pb.addMouseListener(mlh);
        label_pb.addMouseMotionListener(mlh);
    }

    public void callRandomEvent() {
        int num = random(1, 5);
        System.out.print("Event: ");
        switch (num) {
            case 1: // Eyebrow animations
                System.out.println("Eyebrow");
                playAnim("eyebrow");
                playAudio("opening_JLM.wav");
                break;
            case 2: // Open link
                System.out.println("Open link");
                eventOpenBrowserTab();
                break;
            case 3: // Smooth Move
                System.out.println("Smooth Move");
                eventSmoothMove();
                break;
            case 4: // Super Teleportation
                System.out.println("Super Teleportation");
                eventSuperTeleport();
                break;
            case 5: // Change the size of Phipy
                System.out.println("Changing the size");
                eventChangeSize();
                break;
        }
    }

    public void playAnim(String animId) {
        if (animId.equals("eyebrow")) {
            playEyebrow();
        }
    }

    public void playEyebrow() {
        new Thread(() -> {
            for (int n=0; n<6; n++) { // Play the animation 6 times
                for (int i=0; i<5; i++) {
                    BufferedImage frame;
                    try {
                        frame = ImageIO.read(getClass().getResourceAsStream("/resources/animations/eyebrow/frame_" + i + ".png"));
                        label_pb.setIcon(new ImageIcon(frame));
                        repaint();
                        Thread.sleep(100);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void playAudio(String audioFileName) {
        try {
            InputStream in = getClass().getResourceAsStream("/resources/audio/" + audioFileName);
            InputStream bufferedIn = new BufferedInputStream(in);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRandomLink() {
        return LINKS[random(0, LINKS.length-1)];
    }

    public void openBrowserTab(String str) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(str));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eventOpenBrowserTab() {
        openBrowserTab(getRandomLink());
    }

    public void teleport() {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (Math.random() * (scrSize.width - label_pb.getIcon().getIconWidth()));
        int y = (int) (Math.random() * (scrSize.height - label_pb.getIcon().getIconHeight()));
        teleportTo(x, y);
    }

    public void teleportTo(int x, int y) {
        label_pb.setBounds(x, y, label_pb.getIcon().getIconWidth() + 2 , label_pb.getIcon().getIconHeight() + 2);
        repaint();
    }

    public void eventSmoothMove() {
        if (random(0, 1) == 1) {
            System.out.println("Phipy needs attention!");
            Point p = MouseInfo.getPointerInfo().getLocation();
            // Positioning Phipy in center of cursor
            int x = (int) p.getX() - label_pb.getIcon().getIconWidth()/2;
            int y = (int) p.getY() - label_pb.getIcon().getIconHeight()/2;
            smoothMoveTo(x, y);
        }
        else {
            Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = random(0, scrSize.width - label_pb.getIcon().getIconWidth());
            int y = random(0, scrSize.height - label_pb.getIcon().getIconHeight());
            smoothMoveTo(x, y);
        }
    }

    public void smoothMoveTo(int x, int y) {
        new smoothMoveThread(x, y).start();
    }

    public void eventSuperTeleport() {
        new Thread(() -> {
            playAudio("DBZ_Teleportation.wav");
             try {
                int j = random(1, 14);
                for (int i=0; i<j; i++) {
                    int gn = random(200, 1000);
                    Thread.sleep(gn); // 0.2 sec - 1 sec
                    teleport();
                    playAudio("DBZ_Teleportation.wav");
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            teleport();
        }).start();
    }

    public void eventChangeSize() {
        new Thread(() -> {
            try {
                boolean b = (Math.random() <= 0.5);
                int modifier = random(4, 10);
                if (b) {
                    label_pb.setIcon(new ImageIcon(ImgIcon_phipyBuddy.getImage().getScaledInstance(widthImg*modifier, heightImg*modifier, Image.SCALE_DEFAULT)));
                }
                else {
                    label_pb.setIcon(new ImageIcon(ImgIcon_phipyBuddy.getImage().getScaledInstance(widthImg/modifier, heightImg/modifier, Image.SCALE_DEFAULT)));
                }
                int gn = random(400, 4000);

                Thread.sleep(1);
                teleport();

                Thread.sleep(gn);
                label_pb.setIcon(ImgIcon_phipyBuddy);

                Thread.sleep(1);
                teleport();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public int random(int lower, int upper) {
        return (int) (Math.random() * (upper+1 - lower)) + lower;
    }

    public JPanel getMainPanel() {
        return panel_pb;
    }

    public JLabel getMainLabel() {
        return label_pb;
    }

    public void setWaitMin(int n) {
        this.waitMin = n;
    }

    public void setWaitMax(int n) {
        this.waitMax = n;
    }

    class smoothMoveThread extends Thread {
        private int x;
        private int y;

        public smoothMoveThread(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {
            try {
                int curr_x = label_pb.getX();
                int curr_y = label_pb.getY();

                // System.out.println(scrSize.width + "/" + scrSize.height);
                // System.out.println("x = " + x + " y = " + y + " currX = " + curr_x + " currY = " + curr_y);
                while (curr_x != x || curr_y != y) {
                    if (curr_x != x) {
                        if (curr_x < x) {
                            curr_x += 1;
                        }
                        else {
                            curr_x -= 1;
                        }
                    }
                    if (curr_y != y) {
                        if (curr_y < y) {
                            curr_y += 1;
                        }
                        else {
                            curr_y -= 1;
                        }
                    }
                    label_pb.setLocation(curr_x, curr_y);
                    repaint();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Phipy("Phipy");
    }
}
