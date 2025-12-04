package com.rpg;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * セーブデータクラス
 * ゲームの状態を保存するためのデータオブジェクト
 */
public class SaveData implements Serializable {

    private static final long serialVersionUID = 1L;

    // プレイヤー情報
    private String playerName;
    private String jobTitle;          // 職業名（勇者、スーパー勇者、真勇者など）
    private int hp;
    private int maxHp;
    private int attack;
    private int maxAttack;
    private int level;
    private int exp;
    private int x;
    private int y;
    private Set<String> defeatedEnemies;

    // TrueHero専用
    private int rebirthCount;
    private int baseMaxHp;
    private int baseMaxAttack;

    // ゲーム進行フラグ
    private boolean wizardJoined;
    private boolean hasMetKing;
    private boolean demonKingDefeated;
    private boolean evilKingDefeated;

    // セーブ情報
    private long saveTime;

    /**
     * 空のセーブデータを作成
     */
    public SaveData() {
        this.defeatedEnemies = new HashSet<>();
    }

    /**
     * 現在のゲーム状態からセーブデータを作成
     * @param player プレイヤー
     * @param wizardJoined 魔法使いが仲間になったか
     */
    public SaveData(Hero player, boolean wizardJoined) {
        this.playerName = player.getName();
        this.jobTitle = player.getJobTitle();
        this.hp = player.getHp();
        this.maxHp = player.getMaxHp();
        this.attack = player.getMaxAttack();
        this.maxAttack = player.getMaxAttack();
        this.level = player.getLevel();
        this.exp = player.getExp();
        this.x = player.getX();
        this.y = player.getY();
        this.defeatedEnemies = new HashSet<>(player.defeatedEnemies);
        this.wizardJoined = wizardJoined;

        // TrueHeroの場合、転生情報を保存
        if (player instanceof TrueHero) {
            TrueHero trueHero = (TrueHero) player;
            this.rebirthCount = trueHero.getRebirthCount();
            this.baseMaxHp = trueHero.getBaseMaxHp();
            this.baseMaxAttack = trueHero.getBaseMaxAttack();
        }

        // GameManagerからフラグを取得
        GameManager gm = GameManager.getInstance();
        this.hasMetKing = gm.hasMetKing();
        this.demonKingDefeated = gm.isDemonKingDefeated();
        this.evilKingDefeated = gm.isEvilKingDefeated();

        this.saveTime = System.currentTimeMillis();
    }

    // ===== Getter/Setter =====

    public String getPlayerName() {
        return playerName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getMaxAttack() {
        return maxAttack;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Set<String> getDefeatedEnemies() {
        return defeatedEnemies;
    }

    public int getRebirthCount() {
        return rebirthCount;
    }

    public int getBaseMaxHp() {
        return baseMaxHp;
    }

    public int getBaseMaxAttack() {
        return baseMaxAttack;
    }

    public boolean isWizardJoined() {
        return wizardJoined;
    }

    public boolean hasMetKing() {
        return hasMetKing;
    }

    public boolean isDemonKingDefeated() {
        return demonKingDefeated;
    }

    public boolean isEvilKingDefeated() {
        return evilKingDefeated;
    }

    public long getSaveTime() {
        return saveTime;
    }

    /**
     * セーブデータの概要を取得
     * @return 表示用の概要文字列
     */
    public String getSummary() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm");
        String timeStr = sdf.format(new java.util.Date(saveTime));
        return String.format("%s Lv.%d (%s) - %s", playerName, level, jobTitle, timeStr);
    }
}
