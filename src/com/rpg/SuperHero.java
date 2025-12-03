package com.rpg;

/**
 * スーパー勇者クラス
 * 魔法使いと協力して強くなった勇者（簡単ルート）
 * HP: 100, 攻撃力: 40
 * 特殊能力: 飛行
 */
public class SuperHero extends Hero {

    private boolean isFlying;  // 飛行中かどうか

    /**
     * 既存のHeroからSuperHeroに転職
     * @param source 転職元のHero
     */
    public SuperHero(Hero source) {
        super(source);  // 状態をコピー
        this.maxAttack = 30;  // 攻撃力を2倍にする
        this.isFlying = false;
        System.out.println(name + "はスーパー勇者になった！");
        System.out.println("攻撃力が2倍になった！");
        System.out.println("空を飛ぶ能力を手に入れた！");
    }

    @Override
    public String getJobTitle() {
        return "スーパー勇者";
    }

    /**
     * 空を飛ぶ
     * 飛んでいる間は相手の攻撃を受けないが、自分の攻撃力も下がる
     */
    public void fly() {
        if (!isFlying) {
            isFlying = true;
            System.out.println(name + "は空を飛び始めた！");
        }
    }

    /**
     * 着陸する
     */
    public void land() {
        if (isFlying) {
            isFlying = false;
            System.out.println(name + "は着陸した！");
        }
    }

    /**
     * 飛行中かどうかチェック
     * @return 飛行中の場合true
     */
    public boolean isFlying() {
        return isFlying;
    }

    @Override
    public int getAttack() {
        int attack = super.getAttack();
        if (isFlying) {
            // 飛行中は攻撃力が下がる（半分）
            return attack / 2;
        } else {
            return attack;
        }
    }

    @Override
    public void takeDamage(int damage) {
        if (isFlying) {
            // 飛行中は攻撃を受けない
            System.out.println(name + "は飛んでいるので攻撃を回避した！");
        } else {
            super.takeDamage(damage);
        }
    }
}
