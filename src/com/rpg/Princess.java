package com.rpg;

/**
 * お姫様クラス
 * 魔王撃破後に登場するNPC
 */
public class Princess extends Human {

    public Princess() {
        super("お姫様", 80, 50);  // 王様の味方として戦う時の攻撃力50
    }

    /**
     * 勇者と対話する（CUI用）
     * 魔王撃破後の対話
     * @param hero 勇者
     * @return 通常エンディングを表示するか
     */
    public boolean talkTo(Hero hero) {
        // 邪悪な王撃破後（洗脳解除、父を失った悲しみ）
        if (GameManager.getInstance().isEvilKingDefeated()) {
            talk("お父様...");
            return false;
        }
        // TrueHeroの場合、通常エンディングを表示しない（洗脳されているため無言）
        if (hero instanceof TrueHero) {
            talk("...");
            return false;
        }
        // SuperHeroの場合、通常エンディング
        talk("勇者様...");
        talk("あなたが魔王を倒してくれたのですね。");
        talk("本当にありがとうございます！");
        talk("あなたは真の英雄です！");
        return true;
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

    @Override
    public String[] getDialogMessages(Hero hero) {
        // 邪悪な王撃破後（洗脳解除、父を失った悲しみ）
        if (GameManager.getInstance().isEvilKingDefeated()) {
            return new String[] {
                "お姫様：お父様..."
            };
        }
        // TrueHeroの場合、通常エンディングを表示しない（洗脳されているため無言）
        if (hero instanceof TrueHero) {
            return new String[] {
                "お姫様：..."
            };
        }
        // 通常エンディング（SuperHeroで魔王を倒した場合）
        return new String[] {
            "お姫様：勇者様...",
            "あなたが魔王を倒してくれたのですね。",
            "本当にありがとうございます！",
            "あなたは真の英雄です！",
            "",
            "＝＝＝ 通常エンディング ＝＝＝",
            "勇者は魔王を倒し、",
            "お姫様を救い出した。",
            "国は再び平和を取り戻した。",
            "",
            "GAME CLEAR!",
            "プレイしてくれてありがとう！",
            "",
            "(隠しルートもあるよ！)"
        };
    }

    /**
     * 邪悪な王を応援する（王様戦のみ）
     * 洗脳されているため、王様の攻撃力を2倍にする
     * @return 応援による攻撃力ブースト倍率
     */
    public int supportKing() {
        if (!isAlive()) {
            return 1;
        }
        System.out.println("お姫様は洗脳されている！");
        System.out.println("お姫様：王様...頑張って...");
        System.out.println("お姫様の洗脳された応援で王様の攻撃力が2倍になった！");
        return 2;  // 攻撃力2倍
    }

    /**
     * 王様戦での悲痛なメッセージ
     * @return メッセージ配列
     */
    public String getDespairMessage() {
        String[] messages = {
            "助けて...王様...",
            "勇者を...倒して...",
            "私を...守って..."
        };
        return messages[(int)(Math.random() * messages.length)];
    }

}
