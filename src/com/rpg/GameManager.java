package com.rpg;

/**
 * ゲーム管理クラス (Singleton Pattern)
 * プレイヤーとゲーム進行フラグを管理する共通クラス
 * GUI版とCUI版の両方で使用
 */
public class GameManager {

    // Singleton インスタンス
    private static GameManager instance;

    // プレイヤー（転職時に入れ替え可能）
    private Hero player;

    // ゲーム進行フラグ
    private boolean demonKingDefeated = false;  // 魔王撃破済み
    private boolean evilKingDefeated = false;   // 邪悪な王撃破済み
    private boolean gameCleared = false;        // ゲームクリア

    // プライベートコンストラクタ（外部からのインスタンス生成を防ぐ）
    private GameManager() {
    }

    /**
     * GameManagerの唯一のインスタンスを取得
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * ゲームをリセットする（新しいゲーム用）
     */
    public void reset() {
        this.player = null;
        this.demonKingDefeated = false;
        this.evilKingDefeated = false;
        this.gameCleared = false;
    }

    // ===== プレイヤー管理 =====

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

    // ===== ゲーム進行フラグ =====

    /**
     * 魔王撃破フラグを設定
     */
    public void setDemonKingDefeated(boolean defeated) {
        this.demonKingDefeated = defeated;
    }

    /**
     * 魔王撃破済みかチェック
     */
    public boolean isDemonKingDefeated() {
        return demonKingDefeated;
    }

    /**
     * 邪悪な王撃破フラグを設定
     */
    public void setEvilKingDefeated(boolean defeated) {
        this.evilKingDefeated = defeated;
    }

    /**
     * 邪悪な王撃破済みかチェック
     */
    public boolean isEvilKingDefeated() {
        return evilKingDefeated;
    }

    /**
     * ゲームクリアフラグを設定
     */
    public void setGameCleared(boolean cleared) {
        this.gameCleared = cleared;
    }

    /**
     * ゲームクリア済みかチェック
     */
    public boolean isGameCleared() {
        return gameCleared;
    }
}
