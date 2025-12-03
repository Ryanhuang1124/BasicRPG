package com.rpg.gui;

import com.rpg.*;
import javax.swing.*;

/**
 * ゲームメインウィンドウ
 * ゲーム全体を管理するメインウィンドウクラス
 */
public class GameWindow extends JFrame {

    private GamePanel gamePanel;

    public GameWindow() {
        setTitle("BasicRPG - ターン制冒険ゲーム");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ゲームパネルを作成
        gamePanel = new GamePanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null); // ウィンドウを中央に配置

        // ゲームを初期化
        initGame();
    }

    /**
     * ゲームを初期化する
     * タイトル画面から開始
     */
    private void initGame() {
        // GameManagerをリセット
        GameManager.getInstance().reset();

        // タイトル画面から開始
        GUIManager.getInstance().changeState(new TitleState());
    }

    public static void main(String[] args) {
        // システムの外観を使用
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // イベントディスパッチスレッドでGUIを起動
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}
