package com.rpg.gui;

import com.rpg.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * マップ探索状態クラス
 * プレイヤーがマップを探索するゲーム状態
 */
public class MapState implements GameState {

    private Hero player;
    private int[][] map;  // 0=道路, 1=草地, 2=NPC
    private static final int TILE_SIZE = 50;      // タイルサイズ
    private static final int MAP_WIDTH = 15;      // マップ幅
    private static final int MAP_HEIGHT = 12;     // マップ高さ
    private Random random = new Random();

    // NPC位置
    private static final int WIZARD_X = 2;
    private static final int WIZARD_Y = 0;
    private static final int KING_X = 7;
    private static final int KING_Y = 0;
    private static final int PRINCESS_X = 8;
    private static final int PRINCESS_Y = 0;
    private static final int HERMIT_X = 14;  // 右上角
    private static final int HERMIT_Y = 0;

    // ゲーム進行状態
    private boolean wizardJoined = false;      // 魔法使いが仲間になったか
    private boolean hasMetWizard = false;      // 魔法使いに会ったことがあるか

    public MapState(Hero player) {
        this.player = player;
        initMap();
    }

    /**
     * マップを初期化する
     * Y座標0-3: 安全地帯（道路のみ、草地なし）
     * Y座標4-11: すべて草地（遭遇エリア）
     */
    private void initMap() {
        map = new int[MAP_HEIGHT][MAP_WIDTH];

        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (y >= 0 && y <= 3) {
                    // Y座標0-3: 安全地帯（道路）
                    map[y][x] = 0;
                } else {
                    // Y座標4-11: すべて草地
                    map[y][x] = 1;
                }
            }
        }

        // NPC位置をマーク
        map[WIZARD_Y][WIZARD_X] = 2;
        map[KING_Y][KING_X] = 2;
        // お姫様は魔王撃破後のみ出現するため、初期状態では配置しない
    }

    @Override
    public void enter() {
        System.out.println("マップ探索モードに入りました");
    }

    @Override
    public void exit() {
        System.out.println("マップ探索モードを終了しました");
    }

    @Override
    public void update() {
        // マップモードでは毎フレームの更新は不要
    }

    @Override
    public void render(Graphics g) {
        // 背景
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, 800, 600);

        // マップを描画
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                int screenX = x * TILE_SIZE + 20;
                int screenY = y * TILE_SIZE + 20;

                if (map[y][x] == 0) {
                    // 道路 - 灰色
                    g.setColor(new Color(150, 150, 150));
                } else if (map[y][x] == 1) {
                    // 草地 - 緑色
                    g.setColor(new Color(100, 200, 100));
                } else if (map[y][x] == 2) {
                    // NPC位置 - 道路と同じ
                    g.setColor(new Color(150, 150, 150));
                }

                g.fillRect(screenX, screenY, TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(screenX, screenY, TILE_SIZE, TILE_SIZE);
            }
        }

        // NPCを描画
        drawNPC(g, WIZARD_X, WIZARD_Y, new Color(128, 0, 128), "魔");  // 紫色、「魔」

        // 王様は邪悪な王撃破前のみ描画
        if (!GameManager.getInstance().isEvilKingDefeated()) {
            drawNPC(g, KING_X, KING_Y, new Color(255, 215, 0), "王");      // 金色、「王」
        }

        // お姫様は魔王撃破後かつ邪悪な王撃破前のみ描画
        if (GameManager.getInstance().isDemonKingDefeated() && !GameManager.getInstance().isEvilKingDefeated()) {
            drawNPC(g, PRINCESS_X, PRINCESS_Y, new Color(255, 192, 203), "姫");  // ピンク色、「姫」
        }

        // 隠士はレベル10以上かつ魔王撃破後に描画
        if (player.getLevel() >= 10 && GameManager.getInstance().isDemonKingDefeated()) {
            drawNPC(g, HERMIT_X, HERMIT_Y, new Color(139, 69, 19), "隠");  // 茶色、「隠」
        }

        // プレイヤーを描画（青い四角）
        int playerScreenX = player.getX() * TILE_SIZE + 20;
        int playerScreenY = player.getY() * TILE_SIZE + 20;
        g.setColor(Color.BLUE);
        g.fillRect(playerScreenX + 5, playerScreenY + 5, TILE_SIZE - 10, TILE_SIZE - 10);

        // プレイヤー情報を表示
        g.setColor(Color.BLACK);
        g.setFont(new Font("MS Gothic", Font.BOLD, 16));
        g.drawString("勇者 Lv." + player.getLevel() + " HP: " + player.getHp() + "/" + player.getMaxHp(), 20, MAP_HEIGHT * TILE_SIZE + 50);
        g.drawString("EXP: " + player.getExp() + "/" + player.getExpToNextLevel(), 20, MAP_HEIGHT * TILE_SIZE + 70);
        g.drawString("方向キーで移動", 20, MAP_HEIGHT * TILE_SIZE + 90);
        g.drawString("NPCの前でEキーで話す", 20, MAP_HEIGHT * TILE_SIZE + 110);

        // NPC近くにいる場合のヒント表示
        if (isNearNPC()) {
            g.setColor(new Color(255, 255, 0, 200));
            g.fillRect(300, MAP_HEIGHT * TILE_SIZE + 40, 200, 30);
            g.setColor(Color.BLACK);
            g.drawString("Eキーで話しかける", 310, MAP_HEIGHT * TILE_SIZE + 60);
        }
    }

    /**
     * プレイヤーがNPCの近くにいるかチェック
     * @return NPCの隣にいる場合true
     */
    private boolean isNearNPC() {
        int px = player.getX();
        int py = player.getY();

        // NPCの隣接位置にいるかチェック（上下左右）
        boolean nearBasicNPC = isAdjacentTo(px, py, WIZARD_X, WIZARD_Y) ||
                               isAdjacentTo(px, py, KING_X, KING_Y);

        // お姫様（魔王撃破後のみ）
        if (GameManager.getInstance().isDemonKingDefeated()) {
            nearBasicNPC = nearBasicNPC || isAdjacentTo(px, py, PRINCESS_X, PRINCESS_Y);
        }

        // 隠士（レベル10以上かつ魔王撃破後）
        if (player.getLevel() >= 10 && GameManager.getInstance().isDemonKingDefeated()) {
            nearBasicNPC = nearBasicNPC || isAdjacentTo(px, py, HERMIT_X, HERMIT_Y);
        }

        return nearBasicNPC;
    }

    /**
     * 2つの位置が隣接しているかチェック
     */
    private boolean isAdjacentTo(int x1, int y1, int x2, int y2) {
        return (Math.abs(x1 - x2) == 1 && y1 == y2) ||
               (Math.abs(y1 - y2) == 1 && x1 == x2);
    }

    /**
     * NPCを描画する
     * @param g グラフィックスオブジェクト
     * @param x X座標
     * @param y Y座標
     * @param color 色
     * @param text 表示文字
     */
    private void drawNPC(Graphics g, int x, int y, Color color, String text) {
        int screenX = x * TILE_SIZE + 20;
        int screenY = y * TILE_SIZE + 20;

        g.setColor(color);
        g.fillOval(screenX + 5, screenY + 5, TILE_SIZE - 10, TILE_SIZE - 10);

        g.setColor(Color.WHITE);
        g.setFont(new Font("MS Gothic", Font.BOLD, 24));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        g.drawString(text, screenX + (TILE_SIZE - textWidth) / 2,
                          screenY + (TILE_SIZE + textHeight) / 2 - 3);
    }

    @Override
    public void handleKeyPressed(KeyEvent e) {
        // Eキーで対話
        if (e.getKeyCode() == KeyEvent.VK_E) {
            tryTalkToNPC();
            return;
        }

        int oldX = player.getX();
        int oldY = player.getY();

        // 方向キーの処理
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                player.move(0, -1);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                player.move(0, 1);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                player.move(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                player.move(1, 0);
                break;
        }

        // 境界チェック
        if (player.getX() < 0 || player.getX() >= MAP_WIDTH ||
            player.getY() < 0 || player.getY() >= MAP_HEIGHT) {
            // 境界に衝突したので位置を戻す
            player.setX(oldX);
            player.setY(oldY);
            return;
        }

        // NPC位置チェック
        boolean onNPC = (player.getX() == WIZARD_X && player.getY() == WIZARD_Y) ||
                        (player.getX() == KING_X && player.getY() == KING_Y) ||
                        (player.getX() == HERMIT_X && player.getY() == HERMIT_Y);

        if (GameManager.getInstance().isDemonKingDefeated()) {
            onNPC = onNPC || (player.getX() == PRINCESS_X && player.getY() == PRINCESS_Y);
        }

        if (onNPC) {
            // NPC位置に移動しようとした場合、位置を戻す
            player.setX(oldX);
            player.setY(oldY);
            return;
        }

        // 草地で移動したかチェック
        if (map[player.getY()][player.getX()] == 1) {
            // 草地上で、敵遭遇の判定
            if (random.nextInt(100) < 30) {  // 30% の確率で遭遇
                Enemy enemy = EnemyFactory.createRandomEnemy();
                System.out.println(enemy.getName() + "と遭遇した！");
                // 戦闘状態に切り替え
                GUIManager.getInstance().changeState(new BattleState(player, enemy));
            }
        }
    }

    /**
     * NPCに話しかける
     */
    private void tryTalkToNPC() {
        int px = player.getX();
        int py = player.getY();

        // 魔法使との対話
        if (isAdjacentTo(px, py, WIZARD_X, WIZARD_Y)) {
            // 3種類の敵を全て倒していない場合
            if (!player.hasDefeatedAllBasicEnemies()) {
                String[] messages = {
                    "魔法使い：ようこそ、若き勇者よ。",
                    "だが、お前にはまだ早い...",
                    "スライム、ゴブリン、狼男。",
                    "この3種類の魔物を全て倒してから\nまた来るがよい。"
                };
                GUIManager.getInstance().changeState(new DialogState(player, "魔法使い", messages));
                return;
            }

            if (!hasMetWizard) {
                // 初めて会う場合（条件達成済み）
                hasMetWizard = true;

                // 20%の確率で仲間になる
                if (random.nextInt(100) < 20) {
                    wizardJoined = true;
                    // 魔法使いを仲間にする
                    player.setWizard(new Wizard());
                    // スーパー勇者に転職（オブジェクト置き換え）
                    SuperHero superHero = new SuperHero(player);
                    GameManager.getInstance().setPlayer(superHero);
                    player = superHero;  // ローカル参照も更新
                    String[] messages = {
                        "魔法使い：ようこそ、若き勇者よ。",
                        "見事だ！3種類の魔物を倒したか。",
                        "お前には不思議な力を感じる...",
                        "よかろう、お前の冒険に同行しよう！",
                        "私の力で、お前をスーパー勇者に\n変身させてやろう！"
                    };
                    GUIManager.getInstance().changeState(new DialogState(player, "魔法使い", messages));
                } else {
                    String[] messages = {
                        "魔法使い：ようこそ、若き勇者よ。",
                        "見事だ！3種類の魔物を倒したか。",
                        "しかし今は忙しい...",
                        "悪いが、また今度にしてくれ。"
                    };
                    GUIManager.getInstance().changeState(new DialogState(player, "魔法使い", messages));
                }
            } else if (wizardJoined) {
                // すでに仲間になっている場合
                String[] messages = {
                    "魔法使い：共に戦おう、勇者よ！",
                    "お前はもうスーパー勇者だ。",
                    "魔王を倒す力を持っている！"
                };
                GUIManager.getInstance().changeState(new DialogState(player, "魔法使い", messages));
            } else {
                // 一度断られた後、再度話しかける場合
                if (random.nextInt(100) < 20) {
                    wizardJoined = true;
                    // 魔法使いを仲間にする
                    player.setWizard(new Wizard());
                    // スーパー勇者に転職（オブジェクト置き換え）
                    SuperHero superHero = new SuperHero(player);
                    GameManager.getInstance().setPlayer(superHero);
                    player = superHero;  // ローカル参照も更新
                    String[] messages = {
                        "魔法使い：ふむ、また来たか。",
                        "お前の執念、認めよう。",
                        "よかろう、お前の冒険に同行しよう！",
                        "私の力で、お前をスーパー勇者に\n変身させてやろう！"
                    };
                    GUIManager.getInstance().changeState(new DialogState(player, "魔法使い", messages));
                } else {
                    String[] messages = {
                        "魔法使い：まだ忙しいのだ...",
                        "また今度にしてくれ。"
                    };
                    GUIManager.getInstance().changeState(new DialogState(player, "魔法使い", messages));
                }
            }
            return;
        }

        // 王様との対話（邪悪な王撃破前のみ）
        if (!GameManager.getInstance().isEvilKingDefeated() &&
            isAdjacentTo(px, py, KING_X, KING_Y)) {
            King king = new King();
            String[] messages = king.getDialogMessages(player);
            GUIManager.getInstance().changeState(new DialogState(player, king, "王様", messages));
            return;
        }

        // お姫様との対話（魔王撃破後かつ邪悪な王撃破前のみ）
        if (GameManager.getInstance().isDemonKingDefeated() &&
            !GameManager.getInstance().isEvilKingDefeated() &&
            isAdjacentTo(px, py, PRINCESS_X, PRINCESS_Y)) {
            Princess princess = new Princess();
            String[] messages = princess.getDialogMessages(player);
            GUIManager.getInstance().changeState(new DialogState(player, princess, "お姫様", messages));
            return;
        }

        // 隠士との対話（レベル10以上かつ魔王撃破後）
        if (player.getLevel() >= 10 && GameManager.getInstance().isDemonKingDefeated() &&
            isAdjacentTo(px, py, HERMIT_X, HERMIT_Y)) {
            Hermit hermit = new Hermit();
            String[] messages = hermit.getDialogMessages(player);
            GUIManager.getInstance().changeState(new DialogState(player, hermit, "隠士", messages));
            return;
        }
    }
}
