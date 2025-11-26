package com.rpg.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * ゲーム状態インターフェース (State Pattern)
 * すべてのゲーム状態はこのインターフェースを実装する
 */
public interface GameState {

    /**
     * この状態に入ったときに実行
     */
    void enter();

    /**
     * この状態を離れるときに実行
     */
    void exit();

    /**
     * ゲームロジックを更新（毎フレーム呼び出し）
     */
    void update();

    /**
     * 画面を描画（毎フレーム呼び出し）
     */
    void render(Graphics g);

    /**
     * キーボード入力を処理
     */
    void handleKeyPressed(KeyEvent e);
}
