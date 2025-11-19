package com.rpg;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 戦闘状態クラス（ポケモン風の戦闘画面）
 * プレイヤーが敵と戦闘するゲーム状態
 */
public class BattleState implements GameState {

    private Hero player;
    private Character enemy;  // Character型に変更（KingとEnemyの両方を受け入れる）
    private String message;                // メッセージ表示用
    private int selectedOption = 0;        // 選択中のオプション
    private boolean playerTurn = true;     // プレイヤーのターンフラグ
    private int maxOptions;                // 利用可能なオプション数
    private Princess princess;             // お姫様（魔王戦のみ）

    public BattleState(Hero player, Character enemy) {
        this.player = player;
        this.enemy = enemy;

        // メッセージの設定
        if (enemy instanceof King) {
            this.message = enemy.getName() + "との戦いが始まった！";
        } else {
            this.message = "野生の" + enemy.getName() + "が現れた！";
        }

        // 魔王戦の場合、お姫様が応援
        if (enemy instanceof DemonKing) {
            this.princess = new Princess();
        }

        // 王様戦の場合、洗脳されたお姫様が王様を助ける
        if (enemy instanceof King && ((King)enemy).isIdentityRevealed()) {
            this.princess = new Princess();
            System.out.println("お姫様が現れた！");
            System.out.println("お姫様は王様に洗脳されている...");
            this.message = "邪悪な王とお姫様の二人を相手にしなければならない！";
        }

        updateMaxOptions();
    }

    /**
     * 利用可能なオプション数を更新
     */
    private void updateMaxOptions() {
        maxOptions = 2; // 基本: 攻撃、逃げる

        // 眠ることができる場合
        if (player.canSleep()) {
            maxOptions++; // 眠る
        }

        if (player instanceof SuperHero) {
            maxOptions++; // 飛ぶ/着陸（SuperHeroのみ）
        }
    }

    @Override
    public void enter() {
        System.out.println("戦闘モードに入りました");
    }

    @Override
    public void exit() {
        System.out.println("戦闘モードを終了しました");
    }

    @Override
    public void update() {
        // 戦闘モードでは毎フレームの更新は不要
    }

    @Override
    public void render(Graphics g) {
        // 背景
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, 800, 600);

        // 敵エリア（上部）
        g.setColor(Color.BLACK);
        g.setFont(new Font("MS Gothic", Font.BOLD, 20));
        g.drawString(enemy.getName(), 500, 100);

        // 敵のHPバー
        g.setFont(new Font("MS Gothic", Font.PLAIN, 16));
        g.drawString("HP: " + enemy.getHp() + "/" + enemy.getMaxHp(), 500, 130);
        drawHealthBar(g, 500, 140, 200, 20, enemy.getHp(), enemy.getMaxHp());

        // 敵のアイコン（赤い四角で代用）
        g.setColor(Color.RED);
        g.fillRect(500, 180, 100, 100);

        // プレイヤーエリア（下部）
        g.setColor(Color.BLACK);
        g.setFont(new Font("MS Gothic", Font.BOLD, 20));
        g.drawString(player.getName(), 100, 350);

        // プレイヤーのHPバー
        g.setFont(new Font("MS Gothic", Font.PLAIN, 16));
        g.drawString("HP: " + player.getHp() + "/" + player.getMaxHp(), 100, 380);
        drawHealthBar(g, 100, 390, 200, 20, player.getHp(), player.getMaxHp());

        // プレイヤーのレベルと経験値バー（間隔を広げる）
        g.setColor(Color.BLACK);
        g.drawString("Lv." + player.getLevel() + " EXP: " + player.getExp() + "/" + player.getExpToNextLevel(), 100, 425);
        drawExpBar(g, 100, 430, 200, 15, player.getExp(), player.getExpToNextLevel());

        // プレイヤーのアイコン（青い四角で代用）
        g.setColor(Color.BLUE);
        g.fillRect(100, 450, 100, 100);

        // 魔法使いエリア（プレイヤーの右）
        if (player.hasWizard()) {
            Wizard wizard = player.getWizard();
            g.setColor(Color.BLACK);
            g.setFont(new Font("MS Gothic", Font.BOLD, 20));
            g.drawString(wizard.getName(), 250, 350);

            // 魔法使いのHPバー
            g.setFont(new Font("MS Gothic", Font.PLAIN, 16));
            g.drawString("HP: " + wizard.getHp() + "/" + wizard.getMaxHp(), 250, 380);
            drawHealthBar(g, 250, 390, 200, 20, wizard.getHp(), wizard.getMaxHp());

            // 魔法使いのアイコン（紫の四角で代用）
            g.setColor(new Color(128, 0, 128));
            g.fillRect(250, 430, 100, 100);
        }

        // メッセージボックス（高さを100に増加）
        g.setColor(Color.WHITE);
        g.fillRect(50, 50, 400, 100);
        g.setColor(Color.BLACK);
        g.drawRect(50, 50, 400, 100);
        g.setFont(new Font("MS Gothic", Font.PLAIN, 16));
        drawMessage(g, message, 60, 75);

        // コマンドボックス
        g.setColor(Color.WHITE);
        g.fillRect(50, 450, 700, 100);
        g.setColor(Color.BLACK);
        g.drawRect(50, 450, 700, 100);

        // コマンドオプション
        g.setFont(new Font("MS Gothic", Font.BOLD, 18));

        int optionX = 70;
        int optionY = 470;
        int optionWidth = 150;
        int optionHeight = 40;
        int optionSpacing = 170;
        int currentOption = 0;

        // 攻撃オプション
        if (selectedOption == currentOption) {
            g.setColor(Color.YELLOW);
            g.fillRect(optionX, optionY, optionWidth, optionHeight);
        }
        g.setColor(Color.BLACK);
        g.drawRect(optionX, optionY, optionWidth, optionHeight);
        g.drawString("攻撃", optionX + 50, optionY + 27);
        currentOption++;

        // 眠るオプション（眠ることができる場合のみ）
        if (player.canSleep()) {
            optionX += optionSpacing;
            if (selectedOption == currentOption) {
                g.setColor(Color.YELLOW);
                g.fillRect(optionX, optionY, optionWidth, optionHeight);
            }
            g.setColor(Color.BLACK);
            g.drawRect(optionX, optionY, optionWidth, optionHeight);
            g.drawString("眠る", optionX + 50, optionY + 27);
            currentOption++;
        }

        // 飛ぶ/着陸オプション (SuperHeroのみ)
        if (player instanceof SuperHero) {
            SuperHero superHero = (SuperHero) player;
            optionX += optionSpacing;
            if (selectedOption == currentOption) {
                g.setColor(Color.YELLOW);
                g.fillRect(optionX, optionY, optionWidth, optionHeight);
            }
            g.setColor(Color.BLACK);
            g.drawRect(optionX, optionY, optionWidth, optionHeight);
            String flyText = superHero.isFlying() ? "着陸" : "飛ぶ";
            g.drawString(flyText, optionX + 50, optionY + 27);
            currentOption++;
        }

        // 逃げるオプション
        optionX += optionSpacing;
        if (selectedOption == currentOption) {
            g.setColor(Color.YELLOW);
            g.fillRect(optionX, optionY, optionWidth, optionHeight);
        }
        g.setColor(Color.BLACK);
        g.drawRect(optionX, optionY, optionWidth, optionHeight);
        g.drawString("逃げる", optionX + 40, optionY + 27);

        // ヒント表示なし
    }

    /**
     * HPバーを描画する
     * @param g グラフィックスオブジェクト
     * @param x X座標
     * @param y Y座標
     * @param width 幅
     * @param height 高さ
     * @param hp 現在のHP
     * @param maxHp 最大HP
     */
    private void drawHealthBar(Graphics g, int x, int y, int width, int height, int hp, int maxHp) {
        // 背景（灰色）
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);

        // HP量（緑/黄/赤）
        int currentWidth = (int) ((double) hp / maxHp * width);
        if (hp > maxHp * 0.5) {
            g.setColor(Color.GREEN);
        } else if (hp > maxHp * 0.2) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.fillRect(x, y, currentWidth, height);

        // 枠線
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    /**
     * 経験値バーを描画する
     * @param g グラフィックスオブジェクト
     * @param x X座標
     * @param y Y座標
     * @param width 幅
     * @param height 高さ
     * @param exp 現在の経験値
     * @param expToNext 次のレベルまでに必要な経験値
     */
    private void drawExpBar(Graphics g, int x, int y, int width, int height, int exp, int expToNext) {
        // 背景（灰色）
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);

        // 経験値量（シアン色）
        int currentWidth = (int) ((double) exp / expToNext * width);
        g.setColor(new Color(0, 200, 255));  // シアン色
        g.fillRect(x, y, currentWidth, height);

        // 枠線
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    /**
     * メッセージを描画する（改行対応）
     * @param g グラフィックスオブジェクト
     * @param msg メッセージ
     * @param x X座標
     * @param y Y座標
     */
    private void drawMessage(Graphics g, String msg, int x, int y) {
        String[] lines = msg.split("\n");
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], x, y + i * 20);
        }
    }

    @Override
    public void handleKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                selectedOption--;
                if (selectedOption < 0) {
                    selectedOption = maxOptions - 1;
                }
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                selectedOption++;
                if (selectedOption >= maxOptions) {
                    selectedOption = 0;
                }
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                if (playerTurn) {
                    executeAction();
                }
                break;
        }
    }

    /**
     * プレイヤーが選択したアクションを実行する
     */
    private void executeAction() {
        // ターンをロック
        playerTurn = false;

        int currentOption = 0;

        // 攻撃
        if (selectedOption == currentOption) {
            playerAttack();
            return;
        }
        currentOption++;

        // 眠る（眠ることができる場合のみ）
        if (player.canSleep()) {
            if (selectedOption == currentOption) {
                player.sleep();
                message = player.getName() + "は眠った！\nHPが全回復した！";
                // 敵の攻撃
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        enemyAttack();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
                return;
            }
            currentOption++;
        }

        // 飛ぶ/着陸 (SuperHeroのみ)
        if (player instanceof SuperHero) {
            if (selectedOption == currentOption) {
                SuperHero superHero = (SuperHero) player;
                if (superHero.isFlying()) {
                    superHero.land();
                    message = player.getName() + "は着陸した！";
                } else {
                    superHero.fly();
                    message = player.getName() + "は空を飛び始めた！";
                }
                // 敵の攻撃
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        enemyAttack();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
                return;
            }
            currentOption++;
        }

        // 逃げる
        if (selectedOption == currentOption) {
            message = "無事に逃げ出した！";
            // 遅延後にマップに戻る
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    GameManager.getInstance().changeState(new MapState(player));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * プレイヤーの攻撃処理
     */
    private void playerAttack() {
        int damage = player.getAttack();

        // 魔王戦でお姫様がいる場合、30%の確率で応援
        if (princess != null && player instanceof SuperHero) {
            if (new java.util.Random().nextInt(100) < 30) {
                int cheerMultiplier = princess.cheer(player);
                damage *= cheerMultiplier;
                message = princess.getName() + "が応援した！\n" +
                          player.getName() + "の攻撃力が" + cheerMultiplier + "倍になった！\n" +
                          player.getName() + "の攻撃！\n" + damage + "のダメージを与えた！";
            } else {
                message = player.getName() + "の攻撃！\n" + damage + "のダメージを与えた！";
            }
        } else {
            message = player.getName() + "の攻撃！\n" + damage + "のダメージを与えた！";
        }

        enemy.takeDamage(damage);

        if (!enemy.isAlive()) {
            message += "\n" + enemy.getName() + "を倒した！";
            // 倒した敵を記録
            player.addDefeatedEnemy(enemy.getName());

            // 経験値を獲得（Enemyの場合のみ）
            if (enemy instanceof Enemy) {
                int expGained = ((Enemy) enemy).getExpReward();
                boolean leveledUp = player.gainExp(expGained);
                message += "\n" + expGained + "の経験値を獲得した！";

                if (leveledUp) {
                    message += "\nレベルアップ！レベル" + player.getLevel() + "になった！";
                }
            }

            // 特殊な敵を倒した場合の処理
            if (enemy instanceof DemonKing) {
                // 魔王を倒した
                GameManager.getInstance().setDemonKingDefeated(true);
                // 遅延後にマップに戻る
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        GameManager.getInstance().changeState(new MapState(player));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else if (enemy instanceof King) {
                // 邪悪な王を倒した → 真エンディングへ
                message += "\n真の悪を倒した！";
                // 遅延後に真エンディングへ
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        GameManager.getInstance().changeState(new TrueEndingState(player));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else if (enemy instanceof Hermit) {
                // 隠士を倒した → 真の真エンディングへ
                message += "\n隠士を倒した！";
                // 遅延後に真の真エンディングへ
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        GameManager.getInstance().changeState(new TrueTrueEndingState(player));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                // 通常の敵を倒した → マップに戻る
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        GameManager.getInstance().changeState(new MapState(player));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        } else {
            // 魔法使いがいる場合は魔法使いの攻撃、いない場合は敵の反撃
            if (player.hasWizard()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        wizardAttack();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                // 敵の反撃
                new Thread(() -> {
                    try {
                        Thread.sleep(1500);
                        enemyAttack();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        }
    }

    /**
     * 魔法使いの攻撃処理
     */
    private void wizardAttack() {
        Wizard wizard = player.getWizard();
        int damage = wizard.getAttack();
        enemy.takeDamage(damage);
        message = wizard.getName() + "の攻撃！\n" + damage + "のダメージを与えた！";

        if (!enemy.isAlive()) {
            message += "\n" + enemy.getName() + "を倒した！";
            // 倒した敵を記録
            player.addDefeatedEnemy(enemy.getName());

            // 経験値を獲得（Enemyの場合のみ）
            if (enemy instanceof Enemy) {
                int expGained = ((Enemy) enemy).getExpReward();
                boolean leveledUp = player.gainExp(expGained);
                message += "\n" + expGained + "の経験値を獲得した！";

                if (leveledUp) {
                    message += "\nレベルアップ！レベル" + player.getLevel() + "になった！";
                }
            }

            // 特殊な敵を倒した場合の処理
            if (enemy instanceof DemonKing) {
                // 魔王を倒した
                GameManager.getInstance().setDemonKingDefeated(true);
                // 遅延後にマップに戻る
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        GameManager.getInstance().changeState(new MapState(player));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else if (enemy instanceof King) {
                // 邪悪な王を倒した → 真エンディングへ
                message += "\n真の悪を倒した！";
                // 遅延後に真エンディングへ
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        GameManager.getInstance().changeState(new TrueEndingState(player));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else if (enemy instanceof Hermit) {
                // 隠士を倒した → 真の真エンディングへ
                message += "\n隠士を倒した！";
                // 遅延後に真の真エンディングへ
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        GameManager.getInstance().changeState(new TrueTrueEndingState(player));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                // 通常の敵を倒した → マップに戻る
                new Thread(() -> {
                    try {
                        Thread.sleep(3000);
                        GameManager.getInstance().changeState(new MapState(player));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        } else {
            // 敵の反撃
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    enemyAttack();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * 敵の攻撃処理
     */
    private void enemyAttack() {
        int damage = enemy.getAttack();

        // 王様戦でお姫様が洗脳されている場合、30%の確率で応援
        if (enemy instanceof King && ((King)enemy).isIdentityRevealed() && princess != null && princess.isAlive()) {
            if (new java.util.Random().nextInt(100) < 30) {
                int supportMultiplier = princess.supportKing();
                damage *= supportMultiplier;
                message = princess.getDespairMessage() + "\n" +
                          "洗脳されたお姫様の応援で王様の攻撃力が" + supportMultiplier + "倍に！\n" +
                          enemy.getName() + "の反撃！\n" + damage + "のダメージを受けた！";
            } else {
                message = enemy.getName() + "の反撃！\n" + damage + "のダメージを受けた！";
            }
        }

        // 魔法使いがいる場合、ランダムでターゲットを選択
        boolean targetWizard = false;
        if (player.hasWizard()) {
            targetWizard = new java.util.Random().nextBoolean();
        }

        if (targetWizard) {
            // 魔法使いへの攻撃
            Wizard wizard = player.getWizard();
            wizard.takeDamage(damage);
            if (!(enemy instanceof King && ((King)enemy).isIdentityRevealed())) {
                message = enemy.getName() + "の反撃！\n" +
                          wizard.getName() + "に" + damage + "のダメージ！";
            } else {
                message += "\n" + wizard.getName() + "に" + damage + "のダメージ！";
            }

            if (!wizard.isAlive()) {
                message += "\n" + wizard.getName() + "が倒れた！";
                player.setWizard(null);  // 魔法使いを削除
            }
        } else {
            // プレイヤーへの攻撃
            // 王様と隠士は飛行を無視する
            if (enemy instanceof King || enemy instanceof Hermit) {
                player.forceTakeDamage(damage);
            } else {
                player.takeDamage(damage);
            }
            if (!(enemy instanceof King && ((King)enemy).isIdentityRevealed())) {
                message = enemy.getName() + "の反撃！\n" + damage + "のダメージを受けた！";
            }

            if (!player.isAlive()) {
                if (enemy instanceof King && ((King)enemy).isIdentityRevealed()) {
                    message += "\n洗脳されたお姫様の応援を受けた王様に...\nやられてしまった...\nゲームオーバー";
                } else {
                    message = "やられてしまった...\nゲームオーバー";
                }
                // ゲームオーバー処理
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        System.exit(0);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
                return;
            }
        }

        // 敵の攻撃後、プレイヤーのターンに戻す
        playerTurn = true;
    }
}
