package com.rpg;

/**
 * 王様クラス
 * プレイヤーに任務を与えるNPC
 * 実は魔王の主人で、真のラスボス
 */
public class King extends Human {

    private boolean identityRevealed = false;  // 正体がバレたかどうか

    public King() {
        super("王様", 1000000, 1000000);  // 究極のラスボス：HP 100万、攻撃力 100万
    }

    @Override
    public String[] getDialogMessages(Hero hero) {
        // TrueHeroで正体を知っている場合
        if (hero instanceof TrueHero && !identityRevealed) {
            return new String[] {
                "王様：よく来た、勇者よ。",
                "魔王を倒したそうだな。\n流石だ...",
                "（真実を告げますか？ Y/N）"
            };
        } else if (identityRevealed) {
            // すでに正体をバラした後
            return new String[] {
                "邪悪な王：ふん、まだ生きていたか...",
                "お前のような優秀な勇者は\n危険すぎる。",
                "ここで消えてもらおう！"
            };
        } else {
            // 通常の対話
            return new String[] {
                "王様：よく来た、勇者よ！",
                "実は...我が娘、お姫様が\n魔王に攫われてしまったのだ！",
                "お前に頼みたいことがある。",
                "まずは冒険に出て魔物を倒し、\n経験を積んでくれないか？",
                "十分に強くなったら、\n魔法使いのところへ行きなさい。",
                "そして彼と協力して\n魔王を倒し、お姫様を救い出してほしい！",
                "頼んだぞ、勇者よ！"
            };
        }
    }

    /**
     * 正体を明かす
     * @return 正体暴露後のメッセージ
     */
    public String[] revealIdentity() {
        identityRevealed = true;
        // 名前を変更
        this.name = "邪悪な王";
        return new String[] {
            "勇者：王様！あなたこそが\n魔王の真の主人だったのですね！",
            "王様：...ふん。",
            "よくぞ気づいたな、勇者よ。",
            "そうだ、私が全ての黒幕だ！",
            "お姫様を攫わせたのも、\n魔物を放ったのも、",
            "すべては優秀な勇者を\n見つけ出すためだ！",
            "そして強くなった勇者を...",
            "魔王を使って始末してきた！",
            "お前で17人目だ。",
            "だが...お前は特別だ。",
            "転生を繰り返す力を持つとはな...",
            "面白い！私の真の力を見せてやろう！",
            "さあ、かかってこい！"
        };
    }

    /**
     * 正体がバレているかチェック
     * @return 正体がバレている場合true
     */
    public boolean isIdentityRevealed() {
        return identityRevealed;
    }


    /**
     * 攻撃方法の名前を取得
     * @return 攻撃方法の説明
     */
    public String getAttackName() {
        return "絶対王権を振りかざした";
    }

    /**
     * 勇者と対話する（CUI用）
     * @param hero 勇者
     */
    public void talkTo(Hero hero) {
        // TrueHeroで正体を知っている場合
        if (hero instanceof TrueHero && !identityRevealed) {
            talk("よく来た、勇者よ。");
            talk("魔王を倒したそうだな。流石だ...");
        }
        // すでに正体をバラした後
        else if (identityRevealed) {
            talk("ふん、まだ生きていたか...");
            talk("お前のような優秀な勇者は危険すぎる。");
            talk("ここで消えてもらおう！");
        }
        // 通常の対話
        else {
            talk("よく来た、勇者よ！");
            talk("実は...我が娘、お姫様が魔王に攫われてしまったのだ！");
            talk("お前に頼みたいことがある。");
            talk("まずは冒険に出て魔物を倒し、経験を積んでくれないか？");
            talk("十分に強くなったら、魔法使いのところへ行きなさい。");
            talk("そして彼と協力して魔王を倒し、お姫様を救い出してほしい！");
            talk("頼んだぞ、勇者よ！");
        }
    }
}
