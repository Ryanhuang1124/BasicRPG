package com.rpg;

/**
 * 隠士クラス
 * レベル10到達後、マップ右上角に出現する謎のNPC
 * 転職と真実の情報を提供する
 */
public class Hermit extends Human {

    public Hermit() {
        super("隠士", 100000000, 0);  // HP: 1億、最終決戦用
    }

    @Override
    public String[] getDialogMessages(Hero hero) {
        // 邪悪な王を倒した後、20%の確率で決闘を挑む
        if (GameManager.getInstance().isEvilKingDefeated()) {
            if (new java.util.Random().nextInt(100) < 20) {
                // 決闘を挑む
                return new String[] {
                    "隠士：...",
                    "また一人、権力に溺れた者が現れたか。",
                    "",
                    "お前は邪悪な王を倒し、",
                    "この国の王になった。",
                    "",
                    "既にお前の力は私が授けたもの。",
                    "この事態を招いた責任は",
                    "私にもある...",
                    "",
                    "ならば...",
                    "私自身の手でお前を止めねばならぬ！",
                    "",
                    "（隠士が決闘を仕掛けてきた！）"
                };
            } else {
                // 通常の叱責メッセージ
                return new String[] {
                    "隠士：...",
                    "また一人、権力に溺れた者が現れたか。",
                    "",
                    "お前は邪悪な王を倒し、",
                    "この国の王になった。",
                    "",
                    "だが...お前は本当に正義なのか？",
                    "",
                    "圧倒的な力を手に入れた者は、",
                    "いつか必ず変わってしまう。",
                    "",
                    "お前もいずれ...",
                    "かつての王と同じ道を歩むだろう。",
                    "",
                    "...去れ、新たな王よ。"
                };
            }
        }

        // TrueHeroで転生可能な場合
        if (hero instanceof TrueHero) {
            TrueHero trueHero = (TrueHero) hero;
            if (trueHero.canRebirth()) {
                // レベル10以上で転生可能
                int nextRebirthCount = trueHero.getRebirthCount() + 1;
                return new String[] {
                    "隠士：また来たか、真勇者よ...",
                    "お前の力は既に頂点に達している。",
                    "だが、さらなる高みを目指すか？",
                    "転生すればレベルは1に戻るが、",
                    "能力を継承し、より強くなれるぞ。",
                    "（転生しますか？ Y/N）"
                };
            } else {
                // レベル10未満
                return new String[] {
                    "隠士：真実を知ってしまったか...",
                    "王は確かに魔王の真の主人だ。",
                    "彼は優秀な勇者を見つけ出し、\n密かに暗殺してきた。",
                    "お前が最初ではない...",
                    "もっと強くなりたければ、\nレベル10でまた来い。"
                };
            }
        } else if (hero instanceof SuperHero) {
            // SuperHeroの場合は転職不可
            return new String[] {
                "隠士：お前はすでに別の道を選んだ。",
                "魔法使いの力を借りたお前に、",
                "真勇者の資格はない。",
                "己の選択を貫け。"
            };
        } else {
            // 純粋なHeroの場合のみ転職可能
            return new String[] {
                "隠士：よくぞここまで来た...",
                "お前は独りで苦練を積み、",
                "己の力のみでここまで到達した。",
                "真実を知る覚悟はあるか？",
                "（転職しますか？ Y/N）"
            };
        }
    }

    /**
     * 勇者を真勇者に転職させ、真実を明かす（初回転職）
     * @param hero 勇者
     * @return 転職後のTrueHeroインスタンス
     */
    public TrueHero performJobChange(Hero hero) {
        TrueHero trueHero = new TrueHero(hero);
        return trueHero;
    }

    /**
     * 真勇者を転生させる（2回目以降）
     * @param trueHero 転生元の真勇者
     * @return 転生後のTrueHeroインスタンス
     */
    public TrueHero performRebirth(TrueHero trueHero) {
        TrueHero reborn = new TrueHero(trueHero);
        return reborn;
    }

    /**
     * 初回転職メッセージを取得
     * @return 転職と真実暴露のメッセージ
     */
    public String[] getJobChangeMessages() {
        return new String[] {
            "隠士：いいだろう...",
            "お前に真実を教えてやろう。",
            "この国の王様は...",
            "実は魔王の主人なのだ！",
            "王は優秀な勇者を見つけ出すために\nお姫様を攫わせた。",
            "そして強くなった勇者を...",
            "密かに暗殺してきたのだ！",
            "お前も暗殺リストに入っている。",
            "王に立ち向かい、\nこの国の真の悪を倒せ！",
            "お前は今「真勇者」に転職した！"
        };
    }

    /**
     * 転生メッセージを取得
     * @param rebirthCount 転生回数
     * @return 転生完了のメッセージ
     */
    public String[] getRebirthMessages(int rebirthCount) {
        return new String[] {
            "隠士：いいだろう...",
            "お前の覚悟、受け取った。",
            "転生の儀式を始める...",
            "...",
            "転生完了！",
            "お前は" + rebirthCount + "回目の転生を果たした！",
            "さらなる強さを手に入れろ！"
        };
    }

    /**
     * 隠士の特殊攻撃: 対象の最大HPの20%ダメージ
     * @param target 攻撃対象
     * @return 与えたダメージ
     */
    public int specialAttack(Hero target) {
        int damage = target.getMaxHp() / 5;  // 最大HPの20%
        target.takeDamage(damage);
        return damage;
    }

    /**
     * 隠士が倒されたかチェック
     * @return 倒された場合true
     */
    public boolean isDefeated() {
        return hp <= 0;
    }
}
