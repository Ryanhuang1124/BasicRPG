package com.rpg;

/**
 * お姫様クラス
 * 魔王撃破後に登場するNPC
 */
public class Princess extends Human {

    public Princess() {
        super("お姫様", 80, 50);  // 王様の味方として戦う時の攻撃力50
    }

    @Override
    public String[] getDialogMessages(Hero hero) {
        // 王様を倒した後は姫は消える（TrueEndingに移行済み）
        // 通常エンディング（魔王のみ倒した場合）
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
     * スーパー勇者を応援する（魔王戦のみ）
     * 応援するとスーパー勇者の攻撃力が2倍になる
     * @param hero スーパー勇者
     * @return 応援による攻撃力ブースト倍率
     */
    public int cheer(Hero hero) {
        if (hero instanceof SuperHero) {
            System.out.println("お姫様：頑張って、勇者様！");
            System.out.println("お姫様の応援で攻撃力が2倍になった！");
            return 2;  // 攻撃力2倍
        }
        return 1;  // 応援なし
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
