package com.rpg;

public class Hero extends Human{
	
	//勇者クラス
	
	public Hero(String name) {
		super(name, 100, 20);
	}
	
	//戦う
	
	public int heroAttack() {
		int dmg = getAttack();
		System.out.println(getName() + "は攻撃して、" + dmg + "のダメージを与えた！");
		return dmg;
	}
	
	//眠る
	
	public void sleep() {
		this.hp = this.maxHp;
		System.out.println(getName() + "は眠ってHPが全回復した！");
	}
	
	//逃げる
	
	public void run() {
		System.out.println(getName() + "は逃げ出した！");
	}
}