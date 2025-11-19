package com.rpg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 遊戲繪圖面板
 */
public class GamePanel extends JPanel {

    private static final int FPS = 60;
    private Timer gameTimer;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);

        // 鍵盤監聽
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GameManager.getInstance().handleKeyPressed(e);
                repaint();
            }
        });

        // 遊戲循環（每秒60幀）
        gameTimer = new Timer(1000 / FPS, e -> {
            GameManager.getInstance().update();
            repaint();
        });
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 啟用抗鋸齒
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 繪製當前遊戲狀態
        GameManager.getInstance().render(g);
    }

    /**
     * 停止遊戲循環
     */
    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }
}
