package com.rpg;

public class Wizard extends Human{
	
	//魔法使いクラス
	//最大攻撃力は勇者の1.5倍
	
	public Wizard(Hero hero) {
		super("魔法使い", 90, (int)(hero.getMaxAttack() * 1.5));
	}
	
	//魔法使いの通常攻撃
	
	public int magicAttack() {
		int dmg = getAttack();
		talk("雑魚め、一発喰らいなさい！");
		System.out.println("魔法使いは魔法で攻撃し、" + dmg + "のダメージを与えた！");
		return dmg;
	}
	
	//魔法使いの必殺技、一発で敵を全部倒す（HPを問わず）
	//敵が魔王の場合、現在HPの1/3のダメージを与える
	//しかし、魔法使いのHPが1になる
	
	public int superExplosion(Enemy enemy) {
		
		talk("ワハハ！俺の真の実力を見せてやろう！喰らえ！エクスプロージョン！");
		
		int dmg;
		
		if(enemy instanceof DemonKing) {
			dmg = enemy.getHp() / 3;
			System.out.println("魔法使いは必殺技を繰り出し、" + dmg + "のダメージを与えた！");
			System.out.println("魔王を弱らせた！");
		}
		else {
			dmg = enemy.getHp();
			System.out.println("魔法使いは必殺技を繰り出し、" + dmg + "のダメージを与えた！");
			System.out.println("敵を倒した！");
		}
		
		enemy.setHp(enemy.getHp() - dmg);
		this.setHp(1); //魔法使いのHpが1になる
		
		return dmg;
	}
	
	//勇者に誘われたセリフ
	//成功や失敗によってセリフが変わる
	
	public void joinHeroParty(int result) {
		switch(result) {
			case 1:
				talk("貴様の仲間になるわけねぇだろう！！！"); //失敗のセリフ1
				break;
			case 2:
				talk("ふん。貴様の実力で俺を仲間に？笑わせるな、雑魚勇者（笑"); //失敗のセリフ2
				break;
			case 3:
				talk("べ、別に…仲間にならないわけじゃないんだからねっ"); //成功のセリフ1
				break;
			case 4:
				talk("し、仕方ないわね…あんたがそこまで言うなら、仲間になってあげてもいいわよっ"); //成功のセリフ2
				break;
		}
	}
}
