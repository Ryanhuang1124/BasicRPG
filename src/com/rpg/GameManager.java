package com.rpg;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * 遊戲管理器 (Singleton Pattern)
 * 負責管理遊戲狀態的切換
 */
public class GameManager {

    // Singleton 實例
    private static GameManager instance;

    // 當前遊戲狀態
    private GameState currentState;

    // プレイヤー（転職時に入れ替え可能）
    private Hero player;

    // 遊戲進度標記
    private boolean demonKingDefeated = false;  // 魔王已被擊敗
    private boolean evilKingDefeated = false;   // 邪惡國王已被擊敗
    private boolean gameCleared = false;        // 遊戲通關（結婚結局）

    // 私有建構子（防止外部創建實例）
    private GameManager() {
    }

    /**
     * 取得 GameManager 的唯一實例
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * 切換遊戲狀態
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
     * 更新當前狀態
     */
    public void update() {
        if (currentState != null) {
            currentState.update();
        }
    }

    /**
     * 繪製當前狀態
     */
    public void render(Graphics g) {
        if (currentState != null) {
            currentState.render(g);
        }
    }

    /**
     * 處理鍵盤輸入
     */
    public void handleKeyPressed(KeyEvent e) {
        if (currentState != null) {
            currentState.handleKeyPressed(e);
        }
    }

    /**
     * 取得當前狀態
     */
    public GameState getCurrentState() {
        return currentState;
    }

    /**
     * 設定魔王被擊敗
     */
    public void setDemonKingDefeated(boolean defeated) {
        this.demonKingDefeated = defeated;
    }

    /**
     * 檢查魔王是否被擊敗
     */
    public boolean isDemonKingDefeated() {
        return demonKingDefeated;
    }

    /**
     * 設定邪惡國王被擊敗
     */
    public void setEvilKingDefeated(boolean defeated) {
        this.evilKingDefeated = defeated;
    }

    /**
     * 檢查邪惡國王是否被擊敗
     */
    public boolean isEvilKingDefeated() {
        return evilKingDefeated;
    }

    /**
     * 設定遊戲通關
     */
    public void setGameCleared(boolean cleared) {
        this.gameCleared = cleared;
    }

    /**
     * 檢查遊戲是否通關
     */
    public boolean isGameCleared() {
        return gameCleared;
    }

    /**
     * プレイヤーを設定する
     */
    public void setPlayer(Hero player) {
        this.player = player;
    }

    /**
     * プレイヤーを取得する
     */
    public Hero getPlayer() {
        return player;
    }
}
