package com.rpg;

/**
 * 敵キャラクタークラス（モンスター抽象クラス）
 * ゲーム内の敵モンスターの基礎クラス
 */
public abstract class Enemy extends Character {

    protected int expReward;  // 倒したときの経験値報酬

    public Enemy(String name, int maxHp, int maxAttack, int expReward) {
        super(name, maxHp, maxAttack);
        this.expReward = expReward;
    }

    /**
     * 経験値報酬を取得
     * @return 経験値
     */
    public int getExpReward() {
        return expReward;
    }
}
