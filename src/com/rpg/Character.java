package com.rpg;

import java.util.Random;

/**
 * キャラクター抽象基底クラス
 * すべてのキャラクター（人間、モンスター）の基礎
 */
public abstract class Character {

    protected String name;        // キャラクター名
    protected int hp;             // 現在のHP
    protected int maxHp;          // 最大HP
    protected int maxAttack;      // 最大攻撃力
    protected Random random;      // 乱数生成用

    public Character(String name, int maxHp, int maxAttack) {
        this.name = name;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.maxAttack = maxAttack;
        this.random = new Random();
    }

    /**
     * ダメージを受ける
     * @param damage 受けるダメージ量
     */
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    /**
     * 強制的にダメージを受ける（飛行無視などに使用）
     * @param damage 受けるダメージ量
     */
    public void forceTakeDamage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    /**
     * 回復する
     * @param amount 回復量
     */
    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    /**
     * 生存しているかチェック
     * @return 生存している場合true
     */
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     * ランダムな攻撃力を取得する
     * 1 ～ 最大攻撃力の範囲でランダムに決定
     * @return 攻撃力
     */
    public int getAttack() {
        return 1 + random.nextInt(maxAttack);
    }

    // ゲッター
    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getMaxAttack() {
        return maxAttack;
    }

    // セッター（セーブデータ復元用）
    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setMaxAttack(int maxAttack) {
        this.maxAttack = maxAttack;
    }
}
