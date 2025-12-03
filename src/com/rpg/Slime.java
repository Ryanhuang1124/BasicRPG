package com.rpg;

/**
 * スライムクラス
 * 最も弱い敵モンスター
 */
public class Slime extends Enemy {

    private static final int SLIME_HP = 30;
    private static final int SLIME_ATTACK = 8;   // 勇者より弱い
    private static final int SLIME_EXP = 1;      // 経験値

    public Slime() {
        super("スライム", SLIME_HP, SLIME_ATTACK, SLIME_EXP);
    }

    @Override
    public String getAttackName() {
        return "毒を吐いた";
    }
}
