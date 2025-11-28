package com.rpg;


 //人間キャラクターの基底クラス
 //勇者、王様、お姫様、魔法使い、スーパー勇者の共通部分
 

public abstract class Human extends Character{
	public Human(String name, int maxHp, int maxAttack) {
		super(name, maxHp, maxAttack);
	}
	
	//話す機能（全部の人間キャラクター共通の能力）
	
	public void talk(String message) {
		System.out.println(getName() + "：「" + message + "」");
	}
}