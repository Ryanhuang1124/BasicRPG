package com.rpg;

/**
 * 魔法使いクラス
 * プレイヤーの仲間として戦う魔法使い
 */
public class Wizard extends Human {

    private static final int WIZARD_HP = 90;

    /**
     * デフォルトコンストラクタ（対話用）
     */
    public Wizard() {
        super("魔法使い", WIZARD_HP, 30);  // デフォルト攻撃力30
    }

    /**
     * 勇者の攻撃力に基づいて作成（仲間加入時）
     * @param hero 勇者
     */
    public Wizard(Hero hero) {
        super("魔法使い", WIZARD_HP, (int)(hero.getMaxAttack() * 1.5));
    }

    /**
     * 魔法攻撃（通常攻撃のオーバーライド）
     * @return ダメージ量
     */
    @Override
    public int getAttack() {
        int dmg = super.getAttack();  // 親クラスのランダム攻撃力を取得
        talk("雑魚め、一発喰らいなさい！");
        System.out.println("魔法使いは魔法で攻撃し、" + dmg + "のダメージを与えた！");
        return dmg;
    }

    /**
     * 必殺技：エクスプロージョン
     * 一発で敵を全部倒す（HPを問わず）
     * 敵が魔王の場合、現在HPの1/3のダメージを与える
     * しかし、魔法使いのHPが1になる
     * @param enemy 対象の敵
     * @return ダメージ量
     */
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

		enemy.takeDamage(dmg);
		this.hp = 1; //魔法使いのHpが1になる
		
		return dmg;
	}

    @Override
    public String[] getDialogMessages(Hero hero) {
        // TrueHeroの場合、仲間にならない
        if (hero instanceof TrueHero) {
            return new String[] {
                "魔法使い：ほう...お前は別の道を選んだか。",
                "真勇者よ、お前に私の力は不要だ。",
                "己の力を信じて進め。"
            };
        }
        // 魔法使いが仲間になった後の対話（GUI用）
        return new String[] {
            "魔法使い：共に戦おう、勇者よ！",
            "お前はもうスーパー勇者だ。",
            "魔王を倒す力を持っている！"
        };
    }

    /**
     * 勇者と対話する（CUI用）
     * @param hero 勇者
     * @param hasJoined すでに仲間になっているか
     */
    public void talkTo(Hero hero, boolean hasJoined) {
        // TrueHeroの場合、仲間にならない
        if (hero instanceof TrueHero) {
            talk("ほう...お前は別の道を選んだか。");
            talk("真勇者よ、お前に私の力は不要だ。");
            talk("己の力を信じて進め。");
        }
        // 3種類の敵を倒していない場合
        else if (!hero.hasDefeatedAllBasicEnemies()) {
            talk("ようこそ、若き勇者よ。");
            talk("だが、お前にはまだ早い...");
            talk("スライム、ゴブリン、狼男。");
            talk("この3種類の魔物を全て倒してからまた来るがよい。");
        }
        // すでに仲間になっている場合
        else if (hasJoined) {
            talk("共に戦おう、勇者よ！");
            talk("お前はもうスーパー勇者だ。");
            talk("魔王を倒す力を持っている！");
        }
        // 初めて仲間になる時
        else {
            talk("ようこそ、若き勇者よ。");
            talk("見事だ！3種類の魔物を倒したか。");
            talk("お前には不思議な力を感じる...");
            talk("よかろう、お前の冒険に同行しよう！");
            talk("私の力で、お前をスーパー勇者に変身させてやろう！");
        }
    }
}
