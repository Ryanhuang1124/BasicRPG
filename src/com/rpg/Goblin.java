package com.rpg;

/**
 * ゴブリンクラス
 * 中級の敵モンスター
 */
public class Goblin extends Enemy {

    private static final int GOBLIN_HP = 50;
    private static final int GOBLIN_ATTACK = 15;  // 勇者と同じ
    private static final int GOBLIN_EXP = 2;      // 経験値

    public Goblin() {
        super("ゴブリン", GOBLIN_HP, GOBLIN_ATTACK, GOBLIN_EXP);
    }

    @Override
    public String getAttackName() {
        return "棍棒で殴った";
    }
}
