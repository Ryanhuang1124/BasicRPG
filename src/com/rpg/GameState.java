package com.rpg;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * 遊戲狀態介面 (State Pattern)
 * 所有遊戲狀態都要實作這個介面
 */
public interface GameState {

    /**
     * 進入這個狀態時執行
     */
    void enter();

    /**
     * 離開這個狀態時執行
     */
    void exit();

    /**
     * 更新遊戲邏輯（每幀呼叫）
     */
    void update();

    /**
     * 繪製畫面（每幀呼叫）
     */
    void render(Graphics g);

    /**
     * 處理鍵盤輸入
     */
    void handleKeyPressed(KeyEvent e);
}
