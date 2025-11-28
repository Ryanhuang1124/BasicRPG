package com.rpg;

public class Wizard extends Human{
	/*
	 * 魔法使いクラス
	 * 最大攻撃力は勇者の1.5倍
	 */
	
	public Wizard(Hero hero) {
		super("魔法使い", 90, (int)(hero.getMaxAttack() * 1.5));
	}
	
	/*
	 * 魔法使いの攻撃
	 */
	
	public int magicAttack() {
		int dmg = getAttack();
		System.out.println("魔法使いは魔法で攻撃して、" + dmg + "のダメージを与えた！");
		return dmg;
	}
	
	@Override
	public int attack() {
		return magicAttack();
	}
}
