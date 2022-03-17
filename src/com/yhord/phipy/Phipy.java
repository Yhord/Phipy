package com.yhord.phipy;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Random;

public class Phipy extends JFrame {
    private final JLabel label_pb;
    private final ImageIcon ImgIcon_phipyBuddy = new ImageIcon(getClass().getResource("/resources/phipy.gif"));
    private final ImageIcon ImgIcon_pbAnim_eyebrow = new ImageIcon(getClass().getResource("/resources/phipy_eyebrow.gif"));
    private final int widthImg = 156;
    private final int heightImg = 198;

    private final String[] LINKS  = {"https://laec.fr/sommaire", "https://melenchon2022.fr", "https://melenchon2022.fr/programme/", "https://laec.fr/hasard", "https://laec.fr/hasard", "https://laec.fr/hasard", "https://laec.fr/hasard" ,"https://laec.fr/hasard", "https://laec.fr/hasard", "https://laec.fr/hasard"};

    private int waitMin = 5000; // Minimum time between events (ms)
    private int waitMax = 30000; // Maximum time between events (ms)

    public Phipy(String title) {
        super(title);

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, scrSize.width, scrSize.height);
        setType(Type.UTILITY);
        setUndecorated(true);
        setBackground(new Color(1.0f,1.0f,1.0f,0f));
        setAlwaysOnTop(true);
        setIconImage(new ImageIcon(getClass().getResource("/resources/phipy.png")).getImage());

        label_pb = new JLabel(ImgIcon_phipyBuddy);
        label_pb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(label_pb);

        initEvent();
        initConnections(label_pb);
        setVisible(true);
        teleport();
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

    public void initConnections(JLabel label) {
        MouseListenerHandler mlh = new MouseListenerHandler(this);
        label.addMouseListener(mlh);
        label.addMouseMotionListener(mlh);
    }

    public void callRandomEvent() {
        int num = random(1, 5);
        System.out.print("Event: ");
        switch (num) {
            case 1 -> { // Eyebrow animations
                System.out.println("Eyebrow");
                playAnim("eyebrow");
                playAudio("opening_JLM.wav");
            }
            case 2 -> { // Open link
                System.out.println("Open link");
                openBrowserTab();
            }
            case 3 -> { // Teleportation
                System.out.println("Teleportation");
                teleport();
            }
            case 4 -> { // Super Teleportation
                System.out.println("Super Teleportation");
                superTeleport();
            }
            case 5 -> { // Change the size of Phipy
                System.out.println("Changing the size");
                changeSize();
            }
        }
    }

    public void moveMainLabel(int x, int y) {
        label_pb.setBounds(x, y, label_pb.getIcon().getIconWidth() + 2 , label_pb.getIcon().getIconHeight() + 2);
    }

    public void playAnim(String animId) {
        // Resetting the animation
        label_pb.setIcon(ImgIcon_phipyBuddy);

        if (animId.equals("eyebrow")) {
            new Thread(() -> {
                label_pb.setIcon(ImgIcon_pbAnim_eyebrow);
                try {
                    Thread.sleep(3200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                label_pb.setIcon(ImgIcon_phipyBuddy);
            }).start();
        }
    }

    public void playAudio(String audioFileName) {
        try {
            InputStream in = getClass().getResourceAsStream("/resources/" + audioFileName);
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
        Random r = new Random();
        int num = r.nextInt(LINKS.length);
        return LINKS[num];
    }

    public void openBrowserTab() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(getRandomLink()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void teleport() {
        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (Math.random() * (scrSize.width - label_pb.getIcon().getIconWidth()));
        int y = (int) (Math.random() * (scrSize.height - label_pb.getIcon().getIconHeight()));
        moveMainLabel(x, y);
    }

    public void superTeleport() {
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

    public void changeSize() {
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
        }).run();
    }

    public int random(int lower, int upper) {
        return (int) (Math.random() * (upper+1 - lower)) + lower;
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

    public static void main(String[] args) {
        new Phipy("Phipy");
    }
}
