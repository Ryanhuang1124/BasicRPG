package com.rpg;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 王様の正体暴露状態クラス
 * 正体を明かした後、戦闘に移行する特殊な状態
 */
public class KingRevealState implements GameState {

    private Hero player;
    private King king;
    private String[] messages;
    private int currentMessageIndex = 0;

    public KingRevealState(Hero player, King king, String[] messages) {
        this.player = player;
        this.king = king;
        this.messages = messages;
    }

    @Override
    public void enter() {
        System.out.println("王様の正体が明らかになった！");
    }

    @Override
    public void exit() {
        System.out.println("王様との対決が始まる...");
    }

    @Override
    public void update() {
        // 毎フレームの更新は不要
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
        if (currentMessageIndex >= 2) {
            g.drawString("邪悪な王", 70, 430);  // メッセージ2以降は名前変更
        } else {
            g.drawString("王様", 70, 430);
        }

        // メッセージ
        g.setFont(new Font("MS Gothic", Font.PLAIN, 18));
        String currentMessage = messages[currentMessageIndex];
        drawMessage(g, currentMessage, 70, 460);

        // ヒント表示
        g.setFont(new Font("MS Gothic", Font.PLAIN, 14));
        if (currentMessageIndex < messages.length - 1) {
            g.drawString("Enterで次へ", 650, 535);
        } else {
            g.drawString("Enterで戦闘開始", 630, 535);
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
                // 会話終了、王様との戦闘へ
                GameManager.getInstance().changeState(new BattleState(player, king));
            }
        }
    }
}
