package com.yhord.phipy;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseListenerHandler implements MouseListener, MouseMotionListener {
    private final int nbClickRequired = 694;
    private int nbClick = 0;

    private Phipy phipy;
    private volatile int scrX;
    private volatile int scrY;
    private volatile int x;
    private volatile int y;

    public MouseListenerHandler(Phipy phipy) {
        super();
        this.phipy = phipy;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        phipy.playAnim("eyebrow");
        phipy.playAudio("opening_JLM.wav");

        nbClick++;
        System.out.println("Click number: " + nbClick);
            if (nbClick > 650) {
                if (nbClick == nbClickRequired) {
                    System.exit(0);
                } else if (nbClick > 690) {
                    phipy.superTeleport();
                } else {
                    phipy.teleport();
                    if (nbClick == 680) {
                        phipy.setWaitMin(1000);
                        phipy.setWaitMax(5000);
                    }
                }
            }
            else if (nbClick < 201) {
                if (nbClick == 20) {
                    phipy.setWaitMax(20000);
                } else if (nbClick == 40) {
                    phipy.setWaitMin(2000);
                } else if (nbClick == 100) {
                    phipy.setWaitMax(10000);
                } else if (nbClick == 200) {
                    phipy.setWaitMax(8000);
                }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        scrX = e.getXOnScreen();
        scrY = e.getYOnScreen();
        x = phipy.getMainLabel().getX();
        y = phipy.getMainLabel().getY();

        if (e.getClickCount() >= 10) {
            phipy.teleport();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        phipy.getMainLabel().setLocation(this.x + (e.getXOnScreen() - this.scrX), this.y + (e.getYOnScreen() - this.scrY));
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
