package com.rpg;

/**
 * 狼男クラス
 * 強力な敵モンスター
 */
public class Werewolf extends Enemy {

    private static final int WEREWOLF_HP = 60;
    private static final int WEREWOLF_ATTACK = 40;  // 勇者の2倍
    private static final int WEREWOLF_EXP = 5;     // 経験値

    public Werewolf() {
        super("狼男", WEREWOLF_HP, WEREWOLF_ATTACK, WEREWOLF_EXP);
    }
}
