package com.rpg.cui;

import com.rpg.*;
import java.util.Random;
import java.util.Scanner;

/**
 * テキスト版RPGゲームのメインクラス
 * ターミナルで実行するコマンドライン版
 */
public class TextGame {

    private Scanner scanner;
    private Random random;
    private Hero player;
    private boolean wizardJoined = false;  // 魔法使いが仲間になったか
    private boolean running = true;        // ゲームループフラグ

    public TextGame() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    /**
     * ゲームを開始する
     */
    public void start() {
        boolean startGame = showTitleMenu();
        if (startGame) {
            gameLoop();
        }
    }

    /**
     * タイトルメニューを表示
     * @return ゲームを開始する場合true
     */
    private boolean showTitleMenu() {
        while (true) {
            clearScreen();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║                                        ║");
            System.out.println("║           BasicRPG テキスト版          ║");
            System.out.println("║         ～ターン制冒険ゲーム～         ║");
            System.out.println("║                                        ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println();

            System.out.println("1. 新規ゲーム");
            if (SaveManager.getInstance().hasAnySaveData()) {
                System.out.println("2. 続きから");
            }
            System.out.println("0. 終了");
            System.out.println();
            System.out.print("> ");

            int choice = readInt();

            if (choice == 0) {
                System.out.println("ゲームを終了します。");
                return false;
            } else if (choice == 1) {
                initGame();
                return true;
            } else if (choice == 2 && SaveManager.getInstance().hasAnySaveData()) {
                if (showLoadMenu()) {
                    return true;
                }
            }
        }
    }

    /**
     * ロードメニューを表示
     * @return ロード成功した場合true
     */
    private boolean showLoadMenu() {
        clearScreen();
        System.out.println("═══════════ セーブデータ選択 ═══════════");
        System.out.println();

        SaveManager sm = SaveManager.getInstance();
        for (int i = 1; i <= sm.getMaxSlots(); i++) {
            System.out.println(i + ". " + sm.getSlotSummary(i));
        }
        System.out.println("0. 戻る");
        System.out.println();
        System.out.print("> ");

        int slot = readInt();

        if (slot == 0) {
            return false;
        }

        if (slot >= 1 && slot <= sm.getMaxSlots() && sm.hasSaveData(slot)) {
            return loadGame(slot);
        }

        System.out.println("無効な選択です。");
        waitForEnter();
        return false;
    }

    /**
     * ゲームをロードする
     * @param slot スロット番号
     * @return ロード成功した場合true
     */
    private boolean loadGame(int slot) {
        SaveManager sm = SaveManager.getInstance();
        SaveData data = sm.load(slot);

        if (data == null) {
            System.out.println("ロードに失敗しました。");
            waitForEnter();
            return false;
        }

        // プレイヤーを復元
        player = sm.restorePlayer(data);
        GameManager.getInstance().reset();
        GameManager.getInstance().setPlayer(player);
        sm.restoreGameManagerFlags(data);
        wizardJoined = data.isWizardJoined();
        running = true;

        System.out.println("ロード完了！");
        waitForEnter();
        return true;
    }

    /**
     * セーブメニューを表示
     */
    private void showSaveMenu() {
        clearScreen();
        System.out.println("═══════════ セーブ ═══════════");
        System.out.println();

        SaveManager sm = SaveManager.getInstance();
        for (int i = 1; i <= sm.getMaxSlots(); i++) {
            System.out.println(i + ". " + sm.getSlotSummary(i));
        }
        System.out.println("0. 戻る");
        System.out.println();
        System.out.print("セーブするスロットを選択 > ");

        int slot = readInt();

        if (slot == 0) {
            return;
        }

        if (slot >= 1 && slot <= sm.getMaxSlots()) {
            sm.save(slot, player, wizardJoined);
            waitForEnter();
        } else {
            System.out.println("無効な選択です。");
            waitForEnter();
        }
    }

    /**
     * ゲームを初期化する
     */
    private void initGame() {
        player = new Hero("勇者");
        GameManager.getInstance().reset();
        GameManager.getInstance().setPlayer(player);
        wizardJoined = false;
        running = true;
    }

    /**
     * メインゲームループ
     */
    private void gameLoop() {
        while (running) {
            showMainMenu();
        }
    }

    /**
     * メインメニュー（村）を表示
     */
    private void showMainMenu() {
        clearScreen();
        showStatus();
        System.out.println();
        System.out.println("═══════════════ 村 ═══════════════");
        System.out.println();

        int optionNum = 1;

        System.out.println(optionNum++ + ". 魔法使いに会いに行く");

        // 王様（邪悪な王撃破前のみ）
        if (!GameManager.getInstance().isEvilKingDefeated()) {
            System.out.println(optionNum++ + ". 王様に会いに行く");
        }

        // お姫様（魔王撃破後かつ邪悪な王撃破前のみ）
        if (GameManager.getInstance().isDemonKingDefeated() &&
            !GameManager.getInstance().isEvilKingDefeated()) {
            System.out.println(optionNum++ + ". お姫様に会いに行く");
        }

        // 隠士（レベル10以上かつ魔王撃破後）
        if (player.getLevel() >= 10 && GameManager.getInstance().isDemonKingDefeated()) {
            System.out.println(optionNum++ + ". 小道を通って山奥の庵へ");
        }

        System.out.println(optionNum++ + ". 冒険に出る");
        System.out.println(optionNum++ + ". 休憩する（HP回復）");
        System.out.println("S. セーブ");
        System.out.println("0. ゲーム終了");
        System.out.println();
        System.out.print("> ");

        String input = scanner.nextLine().trim().toUpperCase();

        // セーブ処理
        if (input.equals("S")) {
            showSaveMenu();
            return;
        }

        // 数字入力の処理
        try {
            int choice = Integer.parseInt(input);
            handleMainMenuChoice(choice);
        } catch (NumberFormatException e) {
            System.out.println("無効な入力です。");
            waitForEnter();
        }
    }

    /**
     * メインメニューの選択を処理
     */
    private void handleMainMenuChoice(int choice) {
        if (choice == 0) {
            running = false;
            System.out.println("ゲームを終了します。ありがとうございました！");
            return;
        }

        int optionNum = 1;

        // 魔法使い
        if (choice == optionNum++) {
            talkToWizard();
            return;
        }

        // 王様（邪悪な王撃破前のみ）
        if (!GameManager.getInstance().isEvilKingDefeated()) {
            if (choice == optionNum++) {
                talkToKing();
                return;
            }
        }

        // お姫様（魔王撃破後かつ邪悪な王撃破前のみ）
        if (GameManager.getInstance().isDemonKingDefeated() &&
            !GameManager.getInstance().isEvilKingDefeated()) {
            if (choice == optionNum++) {
                talkToPrincess();
                return;
            }
        }

        // 隠士（レベル10以上かつ魔王撃破後）
        if (player.getLevel() >= 10 && GameManager.getInstance().isDemonKingDefeated()) {
            if (choice == optionNum++) {
                talkToHermit();
                return;
            }
        }

        // 冒険に出る
        if (choice == optionNum++) {
            explore();
            return;
        }

        // 休憩
        if (choice == optionNum++) {
            rest();
            return;
        }

        System.out.println("無効な選択です。");
        waitForEnter();
    }

    /**
     * 魔法使いとの対話
     */
    private void talkToWizard() {
        clearScreen();
        System.out.println("═══════════ 魔法使いの家 ═══════════");
        System.out.println();

        Wizard wizard;

        // すでに仲間になっている場合は、仲間のWizardと対話
        if (wizardJoined) {
            wizard = player.getWizard();
        } else {
            wizard = new Wizard();  // 対話用の一時オブジェクト
        }

        // 対話を実行
        wizard.talkTo(player, wizardJoined);

        // 条件達成かつ未加入の場合、仲間に加入
        // ただし、TrueHeroは別ルートなので転職不可
        if (!wizardJoined && player.hasDefeatedAllBasicEnemies() && !(player instanceof TrueHero)) {
            wizardJoined = true;
            Wizard newWizard = new Wizard(player);  // 勇者の攻撃力×1.5
            player.setWizard(newWizard);

            // スーパー勇者に転職
            SuperHero superHero = new SuperHero(player);
            GameManager.getInstance().setPlayer(superHero);
            player = superHero;

            System.out.println();
            System.out.println("★ 魔法使いが仲間になった！");
            System.out.println("★ スーパー勇者に変身した！");
            System.out.println("★ 攻撃力が2倍になった！");
            System.out.println("★ 飛行能力を獲得した！");
        }

        waitForEnter();
    }

    /**
     * 王様との対話
     */
    private void talkToKing() {
        clearScreen();
        System.out.println("═══════════ 王城 ═══════════");
        System.out.println();

        King king = new King();

        // 対話を実行
        king.talkTo(player);

        // TrueHeroで正体を暴く選択
        if (player instanceof TrueHero && !king.isIdentityRevealed()) {
            System.out.println();
            System.out.println("（真実を告げますか？）");
            System.out.println("1. はい");
            System.out.println("2. いいえ");
            System.out.print("> ");

            int choice = readInt();
            if (choice == 1) {
                System.out.println();
                // 正体暴露
                String[] revealMessages = king.revealIdentity();
                for (String msg : revealMessages) {
                    System.out.println(msg);
                }

                System.out.println();
                System.out.println("邪悪な王との戦いが始まる！");
                waitForEnter();

                // 王様と戦闘（お姫様も登場）
                battle(king, true);
                return;
            }
        }

        waitForEnter();
    }

    /**
     * お姫様との対話
     */
    private void talkToPrincess() {
        clearScreen();
        System.out.println("═══════════ 王城 ═══════════");
        System.out.println();

        Princess princess = new Princess();

        // 対話を実行（TrueHeroの場合は通常エンディングを表示しない）
        boolean showEnding = princess.talkTo(player);

        if (showEnding) {
            // 通常エンディング（SuperHeroのみ）
            showNormalEnding();
        } else {
            // TrueHeroの場合は対話のみ
            waitForEnter();
        }
    }

    /**
     * 隠士との対話
     */
    private void talkToHermit() {
        clearScreen();
        System.out.println("═══════════ 山奥の庵 ═══════════");
        System.out.println();

        Hermit hermit = new Hermit();
        boolean evilKingDefeated = GameManager.getInstance().isEvilKingDefeated();

        // 対話を実行
        boolean shouldBattle = hermit.talkTo(player, evilKingDefeated);

        // 邪悪な王を倒した後、決闘になる場合
        if (shouldBattle) {
            System.out.println();
            System.out.println("隠士が決闘を仕掛けてきた！");
            waitForEnter();
            battle(hermit, false);
            return;
        }

        // 邪悪な王撃破後の場合、対話のみで終了
        if (evilKingDefeated) {
            waitForEnter();
            return;
        }

        // TrueHeroで転生可能な場合
        if (player instanceof TrueHero) {
            TrueHero trueHero = (TrueHero) player;
            if (trueHero.canRebirth()) {
                System.out.println();
                System.out.println("（転生しますか？）");
                System.out.println("1. はい");
                System.out.println("2. いいえ");
                System.out.print("> ");

                int choice = readInt();
                if (choice == 1) {
                    TrueHero reborn = hermit.performRebirth(trueHero);
                    GameManager.getInstance().setPlayer(reborn);
                    player = reborn;

                    String[] rebirthMessages = hermit.getRebirthMessages(reborn.getRebirthCount());
                    for (String msg : rebirthMessages) {
                        System.out.println("隠士：" + msg);
                    }

                    System.out.println();
                    System.out.println("★ 転生完了！");
                    System.out.println("★ レベルが1にリセットされた！");
                    System.out.println("★ 能力を継承してさらに強くなれる！");
                }
            }
            waitForEnter();
            return;
        }

        // SuperHeroの場合、対話のみ
        if (player instanceof SuperHero) {
            waitForEnter();
            return;
        }

        // 純粋なHeroの場合 - 転職
        System.out.println();
        System.out.println("（転職しますか？）");
        System.out.println("1. はい");
        System.out.println("2. いいえ");
        System.out.print("> ");

        int choice = readInt();
        if (choice == 1) {
            TrueHero trueHero = hermit.performJobChange(player);
            GameManager.getInstance().setPlayer(trueHero);
            player = trueHero;

            String[] jobChangeMessages = hermit.getJobChangeMessages();
            for (String msg : jobChangeMessages) {
                System.out.println("隠士：" + msg);
            }

            System.out.println();
            System.out.println("★ 真勇者に転職した！");
            System.out.println("★ レベルが1にリセットされた！");
            System.out.println("★ パッシブ能力：レベル毎に能力10%上昇（複利）");
        }

        waitForEnter();
    }

    /**
     * 冒険に出る
     */
    private void explore() {
        clearScreen();
        System.out.println("═══════════ 冒険 ═══════════");
        System.out.println();
        System.out.println("冒険に出かけた...");
        System.out.println();

        // 70%の確率で敵と遭遇
        if (random.nextInt(100) < 70) {
            Enemy enemy;

            // 魔王現れる条件: SuperHero または TrueHero または Lv10以上
            if (player instanceof SuperHero || player instanceof TrueHero || player.getLevel() >= 10) {
                enemy = EnemyFactory.createRandomEnemyForSuper();
            } else {
                enemy = EnemyFactory.createRandomEnemy();
            }

            System.out.println("野生の" + enemy.getName() + "が現れた！");
            waitForEnter();
            battle(enemy, false);
        } else {
            System.out.println("何も見つからなかった...");
            waitForEnter();
        }
    }

    /**
     * 休憩する（HP回復）
     */
    private void rest() {
        clearScreen();
        System.out.println("═══════════ 休憩 ═══════════");
        System.out.println();

        // 非戦闘時は全員眠れる（TrueHeroも含む）
        if (player instanceof TrueHero) {
            ((TrueHero) player).restAtInn();
        } else {
            player.sleep();
        }
        System.out.println("ぐっすり眠った！");
        System.out.println("HPが全回復した！");
        waitForEnter();
    }

    /**
     * 戦闘を実行する
     * @param enemy 敵キャラクター
     * @param isKingBattle 王様戦かどうか
     */
    private void battle(com.rpg.Character enemy, boolean isKingBattle) {
        Princess princess = null;

        // 魔王戦または王様戦でお姫様が登場
        if (enemy instanceof DemonKing) {
            princess = new Princess();
        }
        if (isKingBattle && enemy instanceof King) {
            princess = new Princess();
            System.out.println();
            System.out.println("お姫様が現れた！");
            System.out.println("お姫様は王様に洗脳されている...");
            waitForEnter();
        }

        boolean battleRunning = true;

        while (battleRunning && player.isAlive() && enemy.isAlive()) {
            // 戦闘画面を表示
            clearScreen();
            System.out.println("═══════════ 戦闘 ═══════════");
            System.out.println();

            // 敵のステータス
            System.out.println("【" + enemy.getName() + "】");
            System.out.println("  HP: " + enemy.getHp() + "/" + enemy.getMaxHp());
            showHpBar(enemy.getHp(), enemy.getMaxHp());
            System.out.println();

            // プレイヤーのステータス
            System.out.println("【" + player.getName() + " (" + player.getJobTitle() + ")】");
            System.out.println("  Lv." + player.getLevel() + " HP: " + player.getHp() + "/" + player.getMaxHp());
            showHpBar(player.getHp(), player.getMaxHp());
            System.out.println("  EXP: " + player.getExp() + "/" + player.getExpToNextLevel());

            // 魔法使いのステータス
            if (player.hasWizard()) {
                Wizard wizard = player.getWizard();
                System.out.println();
                System.out.println("【" + wizard.getName() + "】");
                System.out.println("  HP: " + wizard.getHp() + "/" + wizard.getMaxHp());
                showHpBar(wizard.getHp(), wizard.getMaxHp());
            }

            System.out.println();
            System.out.println("────────────────────────────");
            System.out.println();

            // コマンド選択
            int optionNum = 1;
            System.out.println(optionNum++ + ". 攻撃");

            if (player.canSleep()) {
                System.out.println(optionNum++ + ". 眠る（HP全回復）");
            }

            if (player instanceof SuperHero) {
                SuperHero superHero = (SuperHero) player;
                if (superHero.isFlying()) {
                    System.out.println(optionNum++ + ". 着陸");
                } else {
                    System.out.println(optionNum++ + ". 飛ぶ");
                }
            }

            System.out.println(optionNum++ + ". 逃げる");
            System.out.println();
            System.out.print("> ");

            int choice = readInt();
            System.out.println();

            // プレイヤーの行動
            int currentOption = 1;

            // 攻撃
            if (choice == currentOption++) {
                int damage = player.getAttack();

                // 魔王戦でお姫様が応援（SuperHeroのみ、30%）
                if (princess != null && enemy instanceof DemonKing && player instanceof SuperHero) {
                    if (random.nextInt(100) < 30) {
                        int multiplier = princess.getCheerBoost((Enemy)enemy);  // 敵を渡す
                        damage *= multiplier;
                        System.out.println("お姫様：頑張って、勇者様！");
                        System.out.println("お姫様の応援で攻撃力が" + multiplier + "倍になった！");
                    }
                }

                enemy.takeDamage(damage);
                System.out.println(player.getName() + "の攻撃！");
                System.out.println(damage + "のダメージを与えた！");

                if (!enemy.isAlive()) {
                    handleEnemyDefeated(enemy);
                    battleRunning = false;
                    continue;
                }
            }
            // 眠る
            else if (player.canSleep() && choice == currentOption++) {
                player.sleep();
                System.out.println(player.getName() + "は眠った！");
                System.out.println("HPが全回復した！");
            }
            // 飛ぶ/着陸
            else if (player instanceof SuperHero && choice == currentOption++) {
                SuperHero superHero = (SuperHero) player;
                if (superHero.isFlying()) {
                    superHero.land();
                    System.out.println(player.getName() + "は着陸した！");
                } else {
                    superHero.fly();
                    System.out.println(player.getName() + "は空を飛び始めた！");
                }
            }
            // 逃げる
            else if (choice == currentOption) {
                System.out.println("無事に逃げ出した！");
                waitForEnter();
                battleRunning = false;
                continue;
            }
            else {
                System.out.println("無効な選択です。");
                waitForEnter();
                continue;
            }

            // 魔法使いのターン（プレイヤーが逃げなかった場合）
            if (player.hasWizard() && enemy.isAlive()) {
                Wizard wizard = player.getWizard();
                System.out.println();
                System.out.println("────────────────────────────");
                System.out.println("魔法使いのターン！");
                System.out.println("1. 魔法攻撃");
                System.out.println("2. エクスプロージョン（必殺技）");
                System.out.print("> ");

                int wizardChoice = readInt();
                System.out.println();

                if (wizardChoice == 1) {
                    // 通常の魔法攻撃
                    int wizardDamage = wizard.getAttack();
                    enemy.takeDamage(wizardDamage);
                } else if (wizardChoice == 2) {
                    // 必殺技エクスプロージョン
                    if (enemy instanceof Enemy) {
                        wizard.superExplosion((Enemy)enemy);
                    } else {
                        System.out.println("この敵には必殺技が使えない！");
                        int wizardDamage = wizard.getAttack();
                        enemy.takeDamage(wizardDamage);
                    }
                } else {
                    System.out.println("無効な選択です。通常攻撃を行います。");
                    int wizardDamage = wizard.getAttack();
                    enemy.takeDamage(wizardDamage);
                }

                if (!enemy.isAlive()) {
                    handleEnemyDefeated(enemy);
                    battleRunning = false;
                    continue;
                }
            }

            // 敵の反撃
            if (enemy.isAlive()) {
                System.out.println();
                int enemyDamage = enemy.getAttack();

                // 王様戦でお姫様が洗脳応援（30%）
                if (isKingBattle && princess != null && princess.isAlive()) {
                    if (random.nextInt(100) < 30) {
                        int multiplier = princess.supportKing();
                        enemyDamage *= multiplier;
                        System.out.println(princess.getDespairMessage());
                        System.out.println("洗脳されたお姫様の応援で攻撃力が" + multiplier + "倍に！");
                    }
                }

                // 攻撃対象を選択
                boolean targetWizard = false;
                if (player.hasWizard()) {
                    targetWizard = random.nextBoolean();
                }

                // 攻撃名を取得
                String attackName = (enemy instanceof Enemy) ? ((Enemy)enemy).getAttackName() : ((King)enemy).getAttackName();

                if (targetWizard) {
                    Wizard wizard = player.getWizard();
                    wizard.takeDamage(enemyDamage);
                    System.out.println(enemy.getName() + "は" + attackName + "！");
                    System.out.println(wizard.getName() + "に" + enemyDamage + "のダメージ！");

                    if (!wizard.isAlive()) {
                        System.out.println(wizard.getName() + "が倒れた！");
                        player.setWizard(null);
                    }
                } else {
                    // 王様と隠士は飛行を無視
                    if (enemy instanceof King || enemy instanceof Hermit) {
                        player.forceTakeDamage(enemyDamage);
                    } else {
                        player.takeDamage(enemyDamage);
                    }

                    if (player instanceof SuperHero && ((SuperHero)player).isFlying() &&
                        !(enemy instanceof King) && !(enemy instanceof Hermit)) {
                        System.out.println(enemy.getName() + "は" + attackName + "！");
                        System.out.println(player.getName() + "は飛んでいるので攻撃を回避した！");
                    } else {
                        System.out.println(enemy.getName() + "は" + attackName + "！");
                        System.out.println(enemyDamage + "のダメージを受けた！");
                    }
                }

                if (!player.isAlive()) {
                    System.out.println();
                    System.out.println("やられてしまった...");
                    System.out.println("ゲームオーバー");
                    waitForEnter();
                    running = false;
                    battleRunning = false;
                }
            }

            if (battleRunning) {
                waitForEnter();
            }
        }
    }

    /**
     * 敵を倒した時の処理
     */
    private void handleEnemyDefeated(com.rpg.Character enemy) {
        System.out.println();
        System.out.println(enemy.getName() + "を倒した！");
        player.addDefeatedEnemy(enemy.getName());

        // 経験値獲得（Enemyのみ）
        if (enemy instanceof Enemy) {
            int expGained = ((Enemy) enemy).getExpReward();
            boolean leveledUp = player.gainExp(expGained);
            System.out.println(expGained + "の経験値を獲得した！");

            if (leveledUp) {
                System.out.println("レベルアップ！レベル" + player.getLevel() + "になった！");
            }
        }

        // 特殊な敵の処理
        if (enemy instanceof DemonKing) {
            GameManager.getInstance().setDemonKingDefeated(true);
            System.out.println();
            System.out.println("魔王を倒した！");
            System.out.println("お姫様を救い出すことができる！");
        } else if (enemy instanceof King) {
            showTrueEnding();
        } else if (enemy instanceof Hermit) {
            showTrueTrueEnding();
        }

        waitForEnter();
    }

    /**
     * 通常エンディング
     */
    private void showNormalEnding() {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║         ＝＝＝ 通常エンディング ＝＝＝ ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("勇者は魔王を倒し、");
        System.out.println("お姫様を救い出した。");
        System.out.println("国は再び平和を取り戻した。");
        System.out.println();
        System.out.println("GAME CLEAR!");
        System.out.println("プレイしてくれてありがとう！");
        System.out.println();
        System.out.println("(隠しルートもあるよ！)");
        waitForEnter();

        GameManager.getInstance().setGameCleared(true);
        running = false;
    }

    /**
     * 真エンディング
     */
    private void showTrueEnding() {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║          ＝＝＝ 真エンディング ＝＝＝  ║");
        System.out.println("║            力を求めた者の末路          ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("邪悪な王を倒した...");
        System.out.println("洗脳されたお姫様も...力尽きた。");
        System.out.println();
        System.out.println("この国には王がいない。");
        System.out.println("誰かが統治しなければならない。");
        System.out.println();
        System.out.println("真勇者：...私が王になる。");
        System.out.println();
        System.out.println("圧倒的な力を手に入れた勇者は、");
        System.out.println("この国の新たな王となった。");
        System.out.println();
        System.out.println("かつて魔王を倒した英雄は、");
        System.out.println("いつしか誰も逆らえない");
        System.out.println("絶対的な支配者となった。");
        System.out.println();
        System.out.println("GAME CLEAR!");
        System.out.println("プレイしてくれてありがとう！");
        waitForEnter();

        GameManager.getInstance().setEvilKingDefeated(true);
        GameManager.getInstance().setGameCleared(true);
        running = false;
    }

    /**
     * 真の真エンディング
     */
    private void showTrueTrueEnding() {
        clearScreen();
        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║                                        ║");
        System.out.println("║      ＝＝＝ 真の真エンディング ＝＝＝  ║");
        System.out.println("║           全てを超越した者             ║");
        System.out.println("║                                        ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        System.out.println("隠士を...倒した。");
        System.out.println();
        System.out.println("もう誰も止められない。");
        System.out.println("もう誰も逆らえない。");
        System.out.println();
        System.out.println("真勇者は隠士をも倒し、");
        System.out.println("完全なる支配者となった。");
        System.out.println();
        System.out.println("転生を繰り返し、");
        System.out.println("無限の力を手に入れた者は、");
        System.out.println();
        System.out.println("神にも等しい存在となり、");
        System.out.println("この世界を永遠に支配する。");
        System.out.println();
        System.out.println("力を極めた先に待つもの...");
        System.out.println("それは孤独な支配のみ。");
        System.out.println();
        System.out.println("GAME CLEAR!");
        System.out.println("プレイしてくれてありがとう！");
        waitForEnter();

        GameManager.getInstance().setGameCleared(true);
        running = false;
    }

    // ===== ユーティリティメソッド =====

    /**
     * ステータスを表示する
     */
    private void showStatus() {
        System.out.println("┌──────────────────────────────────────┐");
        System.out.println("│ " + player.getName() + " (" + player.getJobTitle() + ")");
        System.out.println("│ Lv." + player.getLevel() + " HP: " + player.getHp() + "/" + player.getMaxHp() +
                          " EXP: " + player.getExp() + "/" + player.getExpToNextLevel());
        System.out.println("│ 攻撃力: " + player.getMaxAttack());
        if (player.hasWizard()) {
            System.out.println("│ 仲間: 魔法使い HP: " + player.getWizard().getHp() + "/" + player.getWizard().getMaxHp());
        }
        System.out.println("└──────────────────────────────────────┘");
    }

    /**
     * HPバーを表示する
     */
    private void showHpBar(int hp, int maxHp) {
        int barLength = 20;
        int filledLength = (int)((double)hp / maxHp * barLength);
        StringBuilder bar = new StringBuilder("  [");
        for (int i = 0; i < barLength; i++) {
            if (i < filledLength) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        System.out.println(bar.toString());
    }

    /**
     * 対話を表示する
     */
    private void showDialog(String speaker, String[] messages) {
        for (String message : messages) {
            if (message.isEmpty()) {
                System.out.println();
            } else {
                System.out.println(speaker + "：" + message);
            }
        }
        waitForEnter();
    }

    /**
     * 画面をクリアする
     */
    private void clearScreen() {
        // Windowsでは効かないことがあるので、改行で代用
        System.out.println("\n".repeat(50));
    }

    /**
     * Enterキーを待つ
     */
    private void waitForEnter() {
        System.out.println();
        System.out.println("(Enterで続ける)");
        scanner.nextLine();
    }

    /**
     * 整数を読み込む
     */
    private int readInt() {
        try {
            String line = scanner.nextLine();
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * メインメソッド
     */
    public static void main(String[] args) {
        TextGame game = new TextGame();
        game.start();
    }
}
