package com.rpg;

import java.util.Random;

/**
 * 敵生成ファクトリークラス (Factory Pattern)
 * 異なる種類の敵モンスターを生成する
 *
 * 攻撃力の倍率（勇者の基準攻撃力 = 20）:
 * - スライム: 0.5倍 (10)
 * - ゴブリン: 1.0倍 (20)
 * - 狼男: 2.0倍 (40)
 * - 魔王: 5.0倍 (100)
 */
public class EnemyFactory {

    private static Random random = new Random();
    private static final int HERO_BASE_ATTACK = 20;  // 勇者の基準攻撃力

    /**
     * ランダムな敵を生成する（通常の冒険用）
     * スライム、ゴブリン、狼男のいずれか
     * @return 生成された敵インスタンス
     */
    public static Enemy createRandomEnemy() {
        int type = random.nextInt(3);

        switch (type) {
            case 0:
                return createSlime();
            case 1:
                return createGoblin();
            case 2:
                return createWerewolf();
            default:
                return createSlime();
        }
    }

    /**
     * スーパー勇者用のランダムな敵を生成する
     * スライム、ゴブリン、狼男、魔王のいずれか
     * @return 生成された敵インスタンス
     */
    public static Enemy createRandomEnemyForSuper() {
        int type = random.nextInt(4);

        switch (type) {
            case 0:
                return createSlime();
            case 1:
                return createGoblin();
            case 2:
                return createWerewolf();
            case 3:
                return createDemonKing();
            default:
                return createSlime();
        }
    }

    /**
     * スライムを生成
     * HP: 30, 攻撃力: 勇者の0.5倍 (10)
     */
    public static Enemy createSlime() {
        return new Slime();
    }

    /**
     * ゴブリンを生成
     * HP: 50, 攻撃力: 勇者と同じ (20)
     */
    public static Enemy createGoblin() {
        return new Goblin();
    }

    /**
     * 狼男を生成
     * HP: 60, 攻撃力: 勇者の2倍 (40)
     */
    public static Enemy createWerewolf() {
        return new Werewolf();
    }

    /**
     * 魔王を生成
     * HP: 150, 攻撃力: 勇者の5倍 (100)
     */
    public static Enemy createDemonKing() {
        return new DemonKing();
    }

    /**
     * タイプを指定して敵を生成する
     * @param type 敵のタイプ
     * @return 生成された敵インスタンス
     */
    public static Enemy createEnemy(String type) {
        switch (type.toUpperCase()) {
            case "SLIME":
                return createSlime();
            case "GOBLIN":
                return createGoblin();
            case "WEREWOLF":
                return createWerewolf();
            case "DEMON_KING":
                return createDemonKing();
            default:
                return createSlime();
        }
    }
}
