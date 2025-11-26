package com.rpg.gui;

import com.rpg.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 対話状態クラス
 * NPCとの会話を表示するゲーム状態
 */
public class DialogState implements GameState {

    private Hero player;
    private String npcName;
    private String[] messages;
    private int currentMessageIndex = 0;
    private Human npc;  // NPC参照（特殊処理用）

    public DialogState(Hero player, String npcName, String[] messages) {
        this.player = player;
        this.npcName = npcName;
        this.messages = messages;
        this.npc = null;
    }

    public DialogState(Hero player, Human npc, String npcName, String[] messages) {
        this.player = player;
        this.npcName = npcName;
        this.messages = messages;
        this.npc = npc;
    }

    @Override
    public void enter() {
        System.out.println(npcName + "との会話を開始しました");
    }

    @Override
    public void exit() {
        System.out.println(npcName + "との会話を終了しました");
    }

    @Override
    public void update() {
        // 対話モードでは毎フレームの更新は不要
    }

    @Override
    public void render(Graphics g) {
        // 背景を少し暗くする
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, 800, 600);

        // 対話ボックス
        g.setColor(Color.WHITE);
        g.fillRect(50, 400, 700, 150);
        g.setColor(Color.BLACK);
        g.drawRect(50, 400, 700, 150);

        // NPC名前
        g.setFont(new Font("MS Gothic", Font.BOLD, 20));
        g.drawString(npcName, 70, 430);

        // メッセージ
        g.setFont(new Font("MS Gothic", Font.PLAIN, 18));
        String currentMessage = messages[currentMessageIndex];
        drawMessage(g, currentMessage, 70, 460);

        // ヒント表示
        g.setFont(new Font("MS Gothic", Font.PLAIN, 14));
        if (currentMessageIndex < messages.length - 1) {
            g.drawString("Enterで次へ", 650, 535);
        } else {
            g.drawString("Enterで閉じる", 640, 535);
        }
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
            g.drawString(lines[i], x, y + i * 25);
        }
    }

    @Override
    public void handleKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (currentMessageIndex < messages.length - 1) {
                // 次のメッセージへ
                currentMessageIndex++;
            } else {
                // 会話終了前に特殊処理チェック
                handleEndOfDialog();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_Y) {
            // Yキーで「はい」を選択
            handleYesChoice();
        } else if (e.getKeyCode() == KeyEvent.VK_N) {
            // Nキーで「いいえ」を選択
            handleNoChoice();
        }
    }

    /**
     * 会話終了時の処理
     */
    private void handleEndOfDialog() {
        String lastMessage = messages[messages.length - 1];

        // 選択肢がある場合は処理しない（Y/Nキー待ち）
        if (lastMessage.contains("Y/N")) {
            return;
        }

        // 隠士が決闘を仕掛けてきた場合
        if (lastMessage.contains("（隠士が決闘を仕掛けてきた！）") && npc instanceof Hermit) {
            Hermit hermit = (Hermit) npc;
            GUIManager.getInstance().changeState(new BattleState(player, hermit));
            return;
        }

        // 通常の会話終了
        GUIManager.getInstance().changeState(new MapState(player));
    }

    /**
     * 「はい」を選択した場合の処理
     */
    private void handleYesChoice() {
        String lastMessage = messages[messages.length - 1];

        // 転職/転生の選択（真勇者へ/真勇者の転生）
        if (lastMessage.contains("（転職しますか？ Y/N）") && npc instanceof Hermit) {
            Hermit hermit = (Hermit) npc;
            // 純粋なHeroからの初回転職
            TrueHero trueHero = hermit.performJobChange(player);
            GameManager.getInstance().setPlayer(trueHero);  // プレイヤーを置き換え
            String[] jobChangeMessages = hermit.getJobChangeMessages();
            GUIManager.getInstance().changeState(new DialogState(trueHero, "隠士", jobChangeMessages));
            return;
        }

        // TrueHeroの転生
        if (lastMessage.contains("（転生しますか？ Y/N）") && npc instanceof Hermit) {
            Hermit hermit = (Hermit) npc;
            if (player instanceof TrueHero) {
                TrueHero oldTrueHero = (TrueHero) player;
                TrueHero reborn = hermit.performRebirth(oldTrueHero);
                GameManager.getInstance().setPlayer(reborn);  // プレイヤーを置き換え
                String[] rebirthMessages = hermit.getRebirthMessages(reborn.getRebirthCount());
                GUIManager.getInstance().changeState(new DialogState(reborn, "隠士", rebirthMessages));
            }
            return;
        }

        // 真実を告げる選択（王様と戦闘へ）
        if (lastMessage.contains("（真実を告げますか？ Y/N）") && npc instanceof King) {
            King king = (King) npc;
            String[] revealMessages = king.revealIdentity();
            // 正体暴露メッセージを表示してから戦闘へ
            GUIManager.getInstance().changeState(new KingRevealState(player, king, revealMessages));
            return;
        }

        // 結婚の選択は削除（真エンディングでは勇者が王になる）
    }

    /**
     * 「いいえ」を選択した場合の処理
     */
    private void handleNoChoice() {
        String lastMessage = messages[messages.length - 1];

        // 選択肢がある場合は会話を終了してマップに戻る
        if (lastMessage.contains("Y/N")) {
            GUIManager.getInstance().changeState(new MapState(player));
        }
    }
}
