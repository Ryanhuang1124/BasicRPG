package com.rpg.gui;

import com.rpg.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 真エンディング状態クラス
 * 邪悪な王を倒した後、勇者が王位に就く黒いエンディング
 */
public class TrueEndingState implements GameState {

    private Hero player;
    private String[] messages;
    private int currentMessageIndex = 0;

    public TrueEndingState(Hero player) {
        this.player = player;
        this.messages = new String[] {
            "洗脳されたお姫様も...力尽きた。",
            "",
            "この国には王がいない。",
            "誰かが統治しなければならない。",
            "",
            "真勇者：...私が王になる。",
            "",
            "圧倒的な力を手に入れた勇者は、",
            "この国の新たな王となった。",
            "",
            "かつて魔王を倒した英雄は、",
            "いつしか誰も逆らえない",
            "絶対的な支配者となった。",
            "",
            "＝＝＝ 真エンディング ＝＝＝",
            "力を求めた者の末路",
            "",
            "GAME CLEAR!",
            "プレイしてくれてありがとう！"
        };

        // 王様と姫を削除
        GameManager.getInstance().setEvilKingDefeated(true);
        GameManager.getInstance().setGameCleared(true);
    }

    @Override
    public void enter() {
        System.out.println("真エンディングに入りました");
    }

    @Override
    public void exit() {
        System.out.println("真エンディングを終了しました");
    }

    @Override
    public void update() {
        // エンディングでは毎フレームの更新は不要
    }

    @Override
    public void render(Graphics g) {
        // 黒背景
        g.setColor(new Color(20, 20, 20));
        g.fillRect(0, 0, 800, 600);

        // エンディングテキスト
        g.setColor(Color.WHITE);
        g.setFont(new Font("MS Gothic", Font.PLAIN, 20));

        String currentMessage = messages[currentMessageIndex];
        drawMessage(g, currentMessage, 100, 250);

        // ヒント表示
        g.setFont(new Font("MS Gothic", Font.PLAIN, 14));
        if (currentMessageIndex < messages.length - 1) {
            g.drawString("Enterで次へ", 650, 550);
        } else {
            g.drawString("Enterで終了", 640, 550);
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
            // 中央揃え
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(lines[i]);
            int centerX = (800 - textWidth) / 2;
            g.drawString(lines[i], centerX, y + i * 30);
        }
    }

    @Override
    public void handleKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (currentMessageIndex < messages.length - 1) {
                // 次のメッセージへ
                currentMessageIndex++;
            } else {
                // ゲーム終了
                System.exit(0);
            }
        }
    }
}
