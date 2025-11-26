package com.rpg.gui;

import com.rpg.*;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * GUI版ゲーム状態管理クラス (Singleton Pattern)
 * GameStateの切り替えと描画を管理
 */
public class GUIManager {

    // Singleton インスタンス
    private static GUIManager instance;

    // 現在のゲーム状態
    private GameState currentState;

    // プライベートコンストラクタ
    private GUIManager() {
    }

    /**
     * GUIManagerの唯一のインスタンスを取得
     */
    public static GUIManager getInstance() {
        if (instance == null) {
            instance = new GUIManager();
        }
        return instance;
    }

    /**
     * ゲーム状態を切り替える
     */
    public void changeState(GameState newState) {
        if (currentState != null) {
            currentState.exit();
        }
        currentState = newState;
        if (currentState != null) {
            currentState.enter();
        }
    }

    /**
     * 現在の状態を更新する
     */
    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }

    /**
     * 現在の状態を描画する
     */
    public void render(Graphics g) {
        if (currentState != null) {
            currentState.render(g);
        }
    }

    /**
     * キーボード入力を処理する
     */
    public void handleKeyPressed(KeyEvent e) {
        if (currentState != null) {
            currentState.handleKeyPressed(e);
        }
    }

    /**
     * 現在の状態を取得する
     */
    public GameState getCurrentState() {
        return currentState;
    }
}
