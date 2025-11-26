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
     * プレイヤーを作成し、初期状態を設定
     */
    private void initGame() {
        // プレイヤーを作成
        Hero player = new Hero("勇者");

        // プレイヤーをGameManagerに登録
        GameManager.getInstance().setPlayer(player);

        // 初期状態をマップ探索に設定
        GUIManager.getInstance().changeState(new MapState(player));
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
