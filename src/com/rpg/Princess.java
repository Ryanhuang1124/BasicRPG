package com.rpg;

public class Princess extends Human{
	
	//お姫様クラス
	 	
	public Princess() {
		super("お姫様", 80, 0);
	}
	
	 //勇者に会ったらお姫様のセリフ
	 
	public void askForHelp() {
		talk("勇者様…！お願いです、お助けくださいまし…！");
	}
	
	 //お姫様がスーパー勇者に応援する
	 //応援できるのは、スーパー勇者が魔王と戦っている時だけ
	
	public int getCheerBoost(Enemy enemy) {
		if(enemy instanceof DemonKing) {
			talk("スーパー勇者様！が、頑張ってくださいましいいいっ！（大声）");
			talk("もし魔王を倒したら、特別のご褒美、あ．げ．る♡");
			
			 //スーパー勇者の攻撃力が2倍にする
			
			return 2;
		}
		return 1;
	}
}