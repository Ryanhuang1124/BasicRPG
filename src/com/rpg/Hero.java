package com.rpg;

import java.util.HashSet;
import java.util.Set;

/**
 * 勇者クラス
 * ゲーム内のプレイヤーが操作する主人公キャラクター
 */
public class Hero extends Human {

    protected int x;            // マップ上のX座標
    protected int y;            // マップ上のY座標
    protected Set<String> defeatedEnemies;  // 倒した敵の記録
    protected Wizard wizard;    // 仲間の魔法使い
    protected int level;           // レベル
    protected int exp;             // 現在の経験値
    protected int expToNextLevel;  // 次のレベルまでに必要な経験値

    public Hero(String name) {
        super(name, 100, 15);  // HP: 100, 攻撃力: 15
        this.x = 8;  // 初期位置（15x12マップ）
        this.y = 1;
        this.defeatedEnemies = new HashSet<>();
        this.level = 1;
        this.exp = 0;
        this.expToNextLevel = calculateExpToNextLevel();
    }

    /**
     * 状態コピー用コンストラクタ（転職時に使用）
     * 既存のHeroの状態を新しいインスタンスにコピーする
     */
    protected Hero(Hero source) {
        super(source.name, source.maxHp, source.maxAttack);
        this.hp = source.hp;
        this.x = source.x;
        this.y = source.y;
        this.defeatedEnemies = new HashSet<>(source.defeatedEnemies);
        this.wizard = source.wizard;
        this.level = source.level;
        this.exp = source.exp;
        this.expToNextLevel = source.expToNextLevel;
    }

    /**
     * 次のレベルまでに必要な経験値を計算
     * @return 必要な経験値（レベル * 3）
     */
    private int calculateExpToNextLevel() {
        return level * 3;
    }

    @Override
    public String[] getDialogMessages(Hero hero) {
        // 勇者は自分自身と対話しない
        return new String[] {};
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * プレイヤーを移動させる
     * @param dx X方向の移動量
     * @param dy Y方向の移動量
     */
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * 敵を倒した記録を追加
     * @param enemyName 倒した敵の名前
     */
    public void addDefeatedEnemy(String enemyName) {
        defeatedEnemies.add(enemyName);
    }

    /**
     * 特定の敵を倒したことがあるかチェック
     * @param enemyName 敵の名前
     * @return 倒したことがある場合true
     */
    public boolean hasDefeated(String enemyName) {
        return defeatedEnemies.contains(enemyName);
    }

    /**
     * 3種類の敵（スライム、ゴブリン、狼男）を全て倒したかチェック
     * @return 全て倒した場合true
     */
    public boolean hasDefeatedAllBasicEnemies() {
        return hasDefeated("スライム") &&
               hasDefeated("ゴブリン") &&
               hasDefeated("狼男");
    }

    /**
     * 魔法使いを仲間にする
     * @param wizard 魔法使いインスタンス
     */
    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    /**
     * 魔法使いを取得
     * @return 魔法使いインスタンス（いない場合null）
     */
    public Wizard getWizard() {
        return wizard;
    }

    /**
     * 魔法使いが仲間にいるかチェック
     * @return 仲間にいる場合true
     */
    public boolean hasWizard() {
        return wizard != null && wizard.isAlive();
    }

    /**
     * 眠る - HPを100%に回復する
     */
    public void sleep() {
        this.hp = this.maxHp;
    }

    /**
     * 眠ることができるかチェック
     * @return 基本的にはtrue（TrueHeroでオーバーライド）
     */
    public boolean canSleep() {
        return true;
    }

    /**
     * 経験値を獲得する
     * @param amount 獲得する経験値
     * @return レベルアップした場合true
     */
    public boolean gainExp(int amount) {
        exp += amount;
        System.out.println(name + "は" + amount + "の経験値を獲得した！");

        // レベルアップ判定
        if (exp >= expToNextLevel) {
            levelUp();
            return true;
        }
        return false;
    }

    /**
     * レベルアップ処理
     */
    private void levelUp() {
        level++;
        exp = 0;  // 経験値をリセット
        expToNextLevel = calculateExpToNextLevel();

        // HPを全回復
        hp = maxHp;

        System.out.println(name + "はレベル" + level + "に上がった！");
        System.out.println("HPが全回復した！");
    }

    /**
     * 現在のレベルを取得
     * @return レベル
     */
    public int getLevel() {
        return level;
    }

    /**
     * 現在の経験値を取得
     * @return 経験値
     */
    public int getExp() {
        return exp;
    }

    /**
     * 次のレベルまでに必要な経験値を取得
     * @return 必要な経験値
     */
    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    /**
     * 職業名を取得（サブクラスでオーバーライド）
     * @return 職業名
     */
    public String getJobTitle() {
        return "勇者";
    }
}
