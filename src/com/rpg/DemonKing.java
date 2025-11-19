package com.rpg;

/**
 * 魔王クラス
 * 最強の敵モンスター
 */
public class DemonKing extends Enemy {

    private static final int DEMON_KING_HP = 150;
    private static final int DEMON_KING_ATTACK = 100;  // 勇者の5倍
    private static final int DEMON_KING_EXP = 50;      // 経験値

    public DemonKing() {
        super("魔王", DEMON_KING_HP, DEMON_KING_ATTACK, DEMON_KING_EXP);
    }
}
