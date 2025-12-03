package com.rpg;

/**
 * 真勇者クラス
 * 独りで苦練してLv10に到達した真の勇者（困難ルート）
 * 転生システム: レベル10到達後、レベル1に戻して能力を継承し、さらに強化可能
 * パッシブ能力: レベルアップ毎に攻撃力とHP上限が10%増加（複利）
 */
public class TrueHero extends Hero {

    private int rebirthCount;  // 転生回数
    private int baseMaxHp;     // 基礎最大HP（転生時に継承）
    private int baseMaxAttack; // 基礎攻撃力（転生時に継承）

    /**
     * 既存のHeroからTrueHeroに転職（初回転職）
     * @param source 転職元のHero
     */
    public TrueHero(Hero source) {
        super(source);  // 状態をコピー

        // 初回転職: 能力を継承してレベル1にリセット
        this.rebirthCount = 0;
        this.baseMaxHp = source.getMaxHp();       // 現在の最大HP継承（100）
        this.baseMaxAttack = source.getMaxAttack(); // 現在の攻撃力継承（20）

        // レベル1にリセット
        this.level = 1;
        this.exp = 0;
        this.expToNextLevel = 3;

        // 能力値をレベル1基準で再計算
        recalculateStats();
        this.hp = this.maxHp;  // HPを全回復

        System.out.println(name + "は真勇者に転職した！");
        System.out.println("レベルが1にリセットされた！");
        System.out.println("基礎攻撃力: " + baseMaxAttack + ", 基礎HP: " + baseMaxHp + " を継承！");
        System.out.println("パッシブ能力獲得: レベル毎に能力10%上昇（複利）");
    }

    /**
     * TrueHeroから転生（2回目以降）
     * @param source 転生元のTrueHero
     */
    public TrueHero(TrueHero source) {
        super(source);  // 状態をコピー

        // 転生: 現在の能力を継承してレベル1にリセット
        this.rebirthCount = source.rebirthCount + 1;
        this.baseMaxHp = source.getMaxHp();       // 成長した最大HP継承
        this.baseMaxAttack = source.getMaxAttack(); // 成長した攻撃力継承

        // レベル1にリセット
        this.level = 1;
        this.exp = 0;
        this.expToNextLevel = 3;

        // 能力値をレベル1基準で再計算
        recalculateStats();
        this.hp = this.maxHp;  // HPを全回復

        System.out.println(name + "は" + (rebirthCount + 1) + "回目の転生を行った！");
        System.out.println("レベルが1にリセットされた！");
        System.out.println("基礎攻撃力: " + baseMaxAttack + ", 基礎HP: " + baseMaxHp + " を継承！");
        System.out.println("さらに強くなる道が開けた！");
    }

    /**
     * セーブデータから復元するためのコンストラクタ
     * メッセージを出力しない静かな復元
     * @param source 基底Hero
     * @param rebirthCount 転生回数
     * @param baseMaxHp 基礎最大HP
     * @param baseMaxAttack 基礎攻撃力
     * @param silent trueの場合メッセージを出力しない
     */
    public TrueHero(Hero source, int rebirthCount, int baseMaxHp, int baseMaxAttack, boolean silent) {
        super(source);
        this.rebirthCount = rebirthCount;
        this.baseMaxHp = baseMaxHp;
        this.baseMaxAttack = baseMaxAttack;
        // 能力値は呼び出し側で設定済みなので再計算しない
    }

    /**
     * レベルに応じて能力値を再計算
     * 計算式: 能力値 = 基礎値 × 1.1^(レベル-1)
     */
    private void recalculateStats() {
        double multiplier = Math.pow(1.1, level - 1);
        this.maxHp = (int)(baseMaxHp * multiplier);
        this.maxAttack = (int)(baseMaxAttack * multiplier);
    }

    /**
     * レベルアップ時に能力値を再計算
     */
    @Override
    public boolean gainExp(int amount) {
        boolean leveledUp = super.gainExp(amount);
        // レベルアップ後、能力値を再計算
        recalculateStats();
        // レベルアップ時は新しいmaxHpで全回復
        if (leveledUp) {
            hp = maxHp;
        }
        return leveledUp;
    }

    /**
     * 転生可能かチェック（レベル10以上）
     * @return 転生可能な場合true
     */
    public boolean canRebirth() {
        return level >= 10;
    }

    /**
     * 転生回数を取得
     * @return 転生回数
     */
    public int getRebirthCount() {
        return rebirthCount;
    }

    /**
     * 基礎攻撃力を取得
     * @return 基礎攻撃力
     */
    public int getBaseMaxAttack() {
        return baseMaxAttack;
    }

    /**
     * 基礎最大HPを取得
     * @return 基礎最大HP
     */
    public int getBaseMaxHp() {
        return baseMaxHp;
    }

    /**
     * 眠ることができるかチェック（戦闘中）
     * @return 真勇者は戦闘中は眠れないのでfalse
     */
    @Override
    public boolean canSleep() {
        return false;  // 戦闘中は眠れない（BattleStateで使用）
    }

    /**
     * 眠る - HPを100%に回復する（非戦闘時のみ使用可能）
     */
    public void restAtInn() {
        this.hp = this.maxHp;
    }

    @Override
    public String getJobTitle() {
        if (rebirthCount > 0) {
            return "真勇者★" + rebirthCount;
        }
        return "真勇者";
    }
}
