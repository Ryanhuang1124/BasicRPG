package com.rpg;

public class Princess extends Human{
	
	//お姫様クラス
	 	
	public Princess() {
		super("お姫様", 90, 0);
	}
	
	 //お姫様いつものセリフ
	 
	public void getHelp() {
		talk("助けてください、勇者様！");
	}
	
	 //お姫様がスーパー勇者に応援する
	 //応援できるのは、スーパー勇者が魔王と戦っている時だけ
	
	public int getCheer(Hero hero, Enemy enemy) {
		if(hero instanceof SuperHero && enemy instanceof DemonKing) {
			talk("頑張ってください！スーパー勇者様！");
			
			 //スーパー勇者の攻撃力が2倍にする
			
			return 2;
		}
		return 1;
	}
}