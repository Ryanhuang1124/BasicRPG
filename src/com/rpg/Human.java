package com.rpg;

/**
 * 人間抽象クラス
 * すべての人間キャラクター（勇者、魔法使い、王様、お姫様など）の基礎
 * 対話能力を持つ
 */
public abstract class Human extends Character {

    public Human(String name, int maxHp, int maxAttack) {
        super(name, maxHp, maxAttack);
    }

    public void talk(String message) {
		System.out.println(getName() + "：「" + message + "」");
	}

    /**
     * 対話メッセージを取得する
     * サブクラスでオーバーライドして固有の対話を実装
     * @param hero 勇者
     * @return 対話メッセージの配列
     */
    public abstract String[] getDialogMessages(Hero hero);
}
