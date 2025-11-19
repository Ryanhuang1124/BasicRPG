package com.rpg;

/**
 * 魔法使いクラス
 * プレイヤーの仲間として戦う魔法使い
 */
public class Wizard extends Human {

    private static final int WIZARD_HP = 80;
    private static final int WIZARD_ATTACK = 15;  // 勇者より少し弱い

    public Wizard() {
        super("魔法使い", WIZARD_HP, WIZARD_ATTACK);
    }

    @Override
    public String[] getDialogMessages(Hero hero) {
        // 魔法使いが仲間になった後の対話
        return new String[] {
            "魔法使い：共に戦おう、勇者よ！",
            "お前はもうスーパー勇者だ。",
            "魔王を倒す力を持っている！"
        };
    }
}
