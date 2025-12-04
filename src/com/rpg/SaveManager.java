package com.rpg;

import java.io.*;

/**
 * セーブ管理クラス (Singleton Pattern)
 * セーブ・ロード機能を提供する
 */
public class SaveManager {

    private static SaveManager instance;
    private static final String SAVE_DIR = "saves";
    private static final String SAVE_FILE_PREFIX = "save";
    private static final String SAVE_FILE_EXT = ".dat";
    private static final int MAX_SLOTS = 3;

    private SaveManager() {
        // セーブディレクトリを作成
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * SaveManagerの唯一のインスタンスを取得
     */
    public static SaveManager getInstance() {
        if (instance == null) {
            instance = new SaveManager();
        }
        return instance;
    }

    /**
     * セーブファイルのパスを取得
     * @param slot スロット番号（1-3）
     * @return ファイルパス
     */
    private String getSaveFilePath(int slot) {
        return SAVE_DIR + "/" + SAVE_FILE_PREFIX + slot + SAVE_FILE_EXT;
    }

    /**
     * ゲームをセーブする
     * @param slot スロット番号（1-3）
     * @param player プレイヤー
     * @param wizardJoined 魔法使いが仲間になったか
     * @return 成功した場合true
     */
    public boolean save(int slot, Hero player, boolean wizardJoined) {
        if (slot < 1 || slot > MAX_SLOTS) {
            System.out.println("無効なスロット番号です。");
            return false;
        }

        SaveData data = new SaveData(player, wizardJoined);

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(getSaveFilePath(slot)))) {
            oos.writeObject(data);
            System.out.println("スロット" + slot + "にセーブしました。");
            return true;
        } catch (IOException e) {
            System.out.println("セーブに失敗しました: " + e.getMessage());
            return false;
        }
    }

    /**
     * ゲームをロードする
     * @param slot スロット番号（1-3）
     * @return セーブデータ（失敗した場合null）
     */
    public SaveData load(int slot) {
        return load(slot, true);
    }

    /**
     * ゲームをロードする（メッセージ表示オプション付き）
     * @param slot スロット番号（1-3）
     * @param showMessage メッセージを表示するか
     * @return セーブデータ（失敗した場合null）
     */
    private SaveData load(int slot, boolean showMessage) {
        if (slot < 1 || slot > MAX_SLOTS) {
            if (showMessage) {
                System.out.println("無効なスロット番号です。");
            }
            return null;
        }

        File file = new File(getSaveFilePath(slot));
        if (!file.exists()) {
            if (showMessage) {
                System.out.println("スロット" + slot + "にセーブデータがありません。");
            }
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(getSaveFilePath(slot)))) {
            SaveData data = (SaveData) ois.readObject();
            if (showMessage) {
                System.out.println("スロット" + slot + "からロードしました。");
            }
            return data;
        } catch (IOException | ClassNotFoundException e) {
            if (showMessage) {
                System.out.println("ロードに失敗しました: " + e.getMessage());
            }
            return null;
        }
    }

    /**
     * セーブデータが存在するかチェック
     * @param slot スロット番号（1-3）
     * @return 存在する場合true
     */
    public boolean hasSaveData(int slot) {
        if (slot < 1 || slot > MAX_SLOTS) {
            return false;
        }
        File file = new File(getSaveFilePath(slot));
        return file.exists();
    }

    /**
     * いずれかのスロットにセーブデータが存在するかチェック
     * @return 存在する場合true
     */
    public boolean hasAnySaveData() {
        for (int i = 1; i <= MAX_SLOTS; i++) {
            if (hasSaveData(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * セーブデータの概要を取得
     * @param slot スロット番号（1-3）
     * @return 概要文字列（データがない場合は空きスロット表示）
     */
    public String getSlotSummary(int slot) {
        if (!hasSaveData(slot)) {
            return "--- 空きスロット ---";
        }

        // メッセージ表示なしでロード
        SaveData data = load(slot, false);
        if (data != null) {
            return data.getSummary();
        }
        return "--- データ破損 ---";
    }

    /**
     * セーブデータからプレイヤーを復元する
     * @param data セーブデータ
     * @return 復元されたプレイヤー
     */
    public Hero restorePlayer(SaveData data) {
        Hero player;
        String jobTitle = data.getJobTitle();

        // 職業に応じて適切なクラスを生成
        if (jobTitle.startsWith("真勇者")) {
            player = restoreTrueHero(data);
        } else if (jobTitle.equals("スーパー勇者")) {
            player = restoreSuperHero(data);
        } else {
            player = restoreHero(data);
        }

        return player;
    }

    /**
     * 通常の勇者を復元
     */
    private Hero restoreHero(SaveData data) {
        Hero player = new Hero(data.getPlayerName());
        restoreCommonData(player, data);
        return player;
    }

    /**
     * スーパー勇者を復元
     */
    private SuperHero restoreSuperHero(SaveData data) {
        // まず通常のHeroを作成して状態を復元
        Hero tempHero = new Hero(data.getPlayerName());
        restoreCommonData(tempHero, data);

        // SuperHeroに変換
        SuperHero player = new SuperHero(tempHero);
        return player;
    }

    /**
     * 真勇者を復元
     */
    private TrueHero restoreTrueHero(SaveData data) {
        // まず通常のHeroを作成
        Hero tempHero = new Hero(data.getPlayerName());
        restoreCommonData(tempHero, data);

        // TrueHeroに変換（静かな復元）
        TrueHero player = new TrueHero(tempHero,
            data.getRebirthCount(),
            data.getBaseMaxHp(),
            data.getBaseMaxAttack(),
            true);
        return player;
    }

    /**
     * 共通データを復元
     */
    private void restoreCommonData(Hero player, SaveData data) {
        player.setHp(data.getHp());
        player.setMaxHp(data.getMaxHp());
        player.setMaxAttack(data.getMaxAttack());
        player.level = data.getLevel();
        player.exp = data.getExp();
        player.expToNextLevel = data.getLevel() * 3;
        player.setX(data.getX());
        player.setY(data.getY());

        // 倒した敵を復元
        for (String enemy : data.getDefeatedEnemies()) {
            player.addDefeatedEnemy(enemy);
        }

        // 魔法使いが仲間の場合
        if (data.isWizardJoined()) {
            player.setWizard(new Wizard());
        }
    }

    /**
     * GameManagerのフラグを復元
     */
    public void restoreGameManagerFlags(SaveData data) {
        GameManager gm = GameManager.getInstance();
        gm.setHasMetKing(data.hasMetKing());
        gm.setDemonKingDefeated(data.isDemonKingDefeated());
        gm.setEvilKingDefeated(data.isEvilKingDefeated());
    }

    /**
     * 最大スロット数を取得
     * @return 最大スロット数
     */
    public int getMaxSlots() {
        return MAX_SLOTS;
    }
}
