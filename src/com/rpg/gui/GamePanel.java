package com.rpg.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * ゲーム描画パネル
 */
public class GamePanel extends JPanel {

    private static final int FPS = 60;
    private Timer gameTimer;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setFocusable(true);

        // キーボードリスナー
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GUIManager.getInstance().handleKeyPressed(e);
                repaint();
            }
        });

        // ゲームループ（毎秒60フレーム）
        gameTimer = new Timer(1000 / FPS, e -> {
            GUIManager.getInstance().update();
            repaint();
        });
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // アンチエイリアスを有効化
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 現在のゲーム状態を描画
        GUIManager.getInstance().render(g);
    }

    /**
     * ゲームループを停止
     */
    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }
}
