package com.rpg.gui;

import com.rpg.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * タイトル画面状態クラス
 * 新規ゲーム・続きから選択画面
 */
public class TitleState implements GameState {

    private int selectedOption = 0;  // 0: 新規ゲーム, 1: 続きから
    private boolean showLoadMenu = false;  // ロードメニュー表示中
    private int selectedSlot = 0;  // 選択中のスロット

    @Override
    public void enter() {
        System.out.println("タイトル画面に入りました");
    }

    @Override
    public void exit() {
        System.out.println("タイトル画面を終了しました");
    }

    @Override
    public void update() {
        // タイトル画面では更新処理なし
    }

    @Override
    public void render(Graphics g) {
        // 背景
        g.setColor(new Color(30, 30, 50));
        g.fillRect(0, 0, 800, 650);

        if (showLoadMenu) {
            renderLoadMenu(g);
        } else {
            renderTitleMenu(g);
        }
    }

    /**
     * タイトルメニューを描画
     */
    private void renderTitleMenu(Graphics g) {
        // タイトル
        g.setColor(Color.WHITE);
        g.setFont(new Font("MS Gothic", Font.BOLD, 48));
        String title = "BasicRPG";
        FontMetrics fm = g.getFontMetrics();
        int titleX = (800 - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, 150);

        // サブタイトル
        g.setFont(new Font("MS Gothic", Font.PLAIN, 24));
        String subtitle = "～ターン制冒険ゲーム～";
        fm = g.getFontMetrics();
        int subtitleX = (800 - fm.stringWidth(subtitle)) / 2;
        g.drawString(subtitle, subtitleX, 200);

        // メニュー
        g.setFont(new Font("MS Gothic", Font.PLAIN, 28));
        int menuY = 320;
        int menuSpacing = 50;

        // 新規ゲーム
        if (selectedOption == 0) {
            g.setColor(Color.YELLOW);
            g.drawString("▶ ", 280, menuY);
        } else {
            g.setColor(Color.WHITE);
        }
        g.drawString("新規ゲーム", 310, menuY);

        // 続きから（セーブデータがある場合のみ）
        menuY += menuSpacing;
        if (SaveManager.getInstance().hasAnySaveData()) {
            if (selectedOption == 1) {
                g.setColor(Color.YELLOW);
                g.drawString("▶ ", 280, menuY);
            } else {
                g.setColor(Color.WHITE);
            }
            g.drawString("続きから", 310, menuY);
        } else {
            g.setColor(Color.GRAY);
            g.drawString("続きから（データなし）", 310, menuY);
        }

        // 操作説明
        g.setColor(Color.GRAY);
        g.setFont(new Font("MS Gothic", Font.PLAIN, 16));
        g.drawString("↑↓: 選択  Enter: 決定", 300, 550);
    }

    /**
     * ロードメニューを描画
     */
    private void renderLoadMenu(Graphics g) {
        // タイトル
        g.setColor(Color.WHITE);
        g.setFont(new Font("MS Gothic", Font.BOLD, 32));
        String title = "セーブデータ選択";
        FontMetrics fm = g.getFontMetrics();
        int titleX = (800 - fm.stringWidth(title)) / 2;
        g.drawString(title, titleX, 100);

        // スロット一覧
        SaveManager sm = SaveManager.getInstance();
        g.setFont(new Font("MS Gothic", Font.PLAIN, 22));
        int slotY = 200;
        int slotSpacing = 80;

        for (int i = 0; i < sm.getMaxSlots(); i++) {
            // 背景ボックス
            if (selectedSlot == i) {
                g.setColor(new Color(80, 80, 120));
            } else {
                g.setColor(new Color(50, 50, 70));
            }
            g.fillRect(100, slotY - 30, 600, 60);

            // スロット情報
            if (selectedSlot == i) {
                g.setColor(Color.YELLOW);
                g.drawString("▶", 110, slotY + 5);
            }

            g.setColor(sm.hasSaveData(i + 1) ? Color.WHITE : Color.GRAY);
            g.drawString("スロット " + (i + 1) + ": " + sm.getSlotSummary(i + 1), 140, slotY + 5);

            slotY += slotSpacing;
        }

        // 戻るオプション
        if (selectedSlot == sm.getMaxSlots()) {
            g.setColor(Color.YELLOW);
            g.drawString("▶", 110, slotY + 5);
        }
        g.setColor(Color.WHITE);
        g.drawString("戻る", 140, slotY + 5);

        // 操作説明
        g.setColor(Color.GRAY);
        g.setFont(new Font("MS Gothic", Font.PLAIN, 16));
        g.drawString("↑↓: 選択  Enter: 決定  Esc: 戻る", 270, 550);
    }

    @Override
    public void handleKeyPressed(KeyEvent e) {
        if (showLoadMenu) {
            handleLoadMenuInput(e);
        } else {
            handleTitleMenuInput(e);
        }
    }

    /**
     * タイトルメニューの入力処理
     */
    private void handleTitleMenuInput(KeyEvent e) {
        int maxOption = SaveManager.getInstance().hasAnySaveData() ? 1 : 0;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                selectedOption = Math.max(0, selectedOption - 1);
                break;
            case KeyEvent.VK_DOWN:
                selectedOption = Math.min(maxOption, selectedOption + 1);
                break;
            case KeyEvent.VK_ENTER:
                if (selectedOption == 0) {
                    // 新規ゲーム
                    startNewGame();
                } else if (selectedOption == 1 && SaveManager.getInstance().hasAnySaveData()) {
                    // 続きから
                    showLoadMenu = true;
                    selectedSlot = 0;
                }
                break;
        }
    }

    /**
     * ロードメニューの入力処理
     */
    private void handleLoadMenuInput(KeyEvent e) {
        SaveManager sm = SaveManager.getInstance();
        int maxSlot = sm.getMaxSlots();  // 戻るオプション含む

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                selectedSlot = Math.max(0, selectedSlot - 1);
                break;
            case KeyEvent.VK_DOWN:
                selectedSlot = Math.min(maxSlot, selectedSlot + 1);
                break;
            case KeyEvent.VK_ENTER:
                if (selectedSlot == maxSlot) {
                    // 戻る
                    showLoadMenu = false;
                } else if (sm.hasSaveData(selectedSlot + 1)) {
                    // ロード
                    loadGame(selectedSlot + 1);
                }
                break;
            case KeyEvent.VK_ESCAPE:
                showLoadMenu = false;
                break;
        }
    }

    /**
     * 新規ゲームを開始
     */
    private void startNewGame() {
        Hero player = new Hero("勇者");
        GameManager.getInstance().reset();
        GameManager.getInstance().setPlayer(player);
        GUIManager.getInstance().changeState(new MapState(player));
    }

    /**
     * ゲームをロード
     */
    private void loadGame(int slot) {
        SaveManager sm = SaveManager.getInstance();
        SaveData data = sm.load(slot);

        if (data == null) {
            return;
        }

        Hero player = sm.restorePlayer(data);
        GameManager.getInstance().reset();
        GameManager.getInstance().setPlayer(player);
        sm.restoreGameManagerFlags(data);

        // MapStateに遷移（wizardJoined情報を渡す）
        MapState mapState = new MapState(player);
        mapState.setWizardJoined(data.isWizardJoined());
        GUIManager.getInstance().changeState(mapState);
    }
}
