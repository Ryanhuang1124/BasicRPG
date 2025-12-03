# BasicRPG

ターン制RPGゲーム（GUI版・CUI版）

## ビルド・実行方法

### コンパイル

```bash
javac -encoding UTF-8 -d bin src/com/rpg/*.java src/com/rpg/gui/*.java src/com/rpg/cui/*.java
```

### 実行前の準備（文字化け対策）

コンソールの文字コードをUTF-8に変更：

```bash
chcp 65001
```

### GUI版実行

**PowerShell:**
```powershell
java "-Dfile.encoding=UTF-8" -cp bin com.rpg.gui.GameWindow
```

**CMD / Bash:**
```bash
java -Dfile.encoding=UTF-8 -cp bin com.rpg.gui.GameWindow
```

### CUI版（テキスト版）実行

**PowerShell:**
```powershell
java "-Dfile.encoding=UTF-8" -cp bin com.rpg.cui.TextGame
```

**CMD / Bash:**
```bash
java -Dfile.encoding=UTF-8 -cp bin com.rpg.cui.TextGame
```

## 注意事項

- PowerShellでは `-D` パラメータを引用符で囲む必要がある
- 日本語が文字化けする場合は `chcp 65001` を実行してからゲームを起動する
