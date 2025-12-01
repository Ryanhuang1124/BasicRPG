package com.rpg;

public class Hero extends Human{
	
	//勇者クラス
	
	public Hero(String name) {
		super(name, 100, 20);
	}
	
	//戦う
	
	public int heroAttack() {
		int dmg = getAttack();
		
		 //戦うセリフ
		
		String[] attackLines = {
				"これが俺の剣だ、覚悟しろ！",
				"邪悪を断つ！これが俺の使命だ！",
				"はあ！勇者の名にかけて！",
				"光よ…俺に力を！",
				"正義の刃よ、悪を断て！"
		};
		
		//乱数で決まる
		
		String line = attackLines[new Random().nextInt(attackLines.length)];
		talk(line);
		
		System.out.println(getName() + "は攻撃し、" + dmg + "のダメージを与えた！");
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