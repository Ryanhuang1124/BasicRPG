package com.rpg.gui;

import com.rpg.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * 真の真エンディング状態クラス
 * 隠士をも倒した後、完全な支配者となるエンディング
 */
public class TrueTrueEndingState implements GameState {

    private Hero player;
    private String[] messages;
    private int currentMessageIndex = 0;

    public TrueTrueEndingState(Hero player) {
        this.player = player;
        this.messages = new String[] {
            "隠士を...倒した。",
            "",
            "もう誰も止められない。",
            "もう誰も逆らえない。",
            "",
            "真勇者は隠士をも倒し、",
            "完全なる支配者となった。",
            "",
            "転生を繰り返し、",
            "無限の力を手に入れた者は、",
            "",
            "神にも等しい存在となり、",
            "この世界を永遠に支配する。",
            "",
            "＝＝＝ 真の真エンディング ＝＝＝",
            "全てを超越した者",
            "",
            "力を極めた先に待つもの...",
            "それは孤独な支配のみ。",
            "",
            "GAME CLEAR!",
            "プレイしてくれてありがとう！"
        };

        GameManager.getInstance().setGameCleared(true);
    }

    @Override
    public void enter() {
        System.out.println("真の真エンディングに入りました");
    }

    @Override
    public void exit() {
        System.out.println("真の真エンディングを終了しました");
    }

    @Override
    public void update() {
        // エンディングでは毎フレームの更新は不要
    }

    @Override
    public void render(Graphics g) {
        // 深い黒背景
        g.setColor(new Color(10, 10, 10));
        g.fillRect(0, 0, 800, 600);

        // エンディングテキスト（金色）
        g.setColor(new Color(255, 215, 0));
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
     * メッセージを描画する（改行対応、中央揃え）
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
