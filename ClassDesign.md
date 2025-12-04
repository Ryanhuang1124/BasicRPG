# BasicRPG クラス設計書（CUI版）

## 目次
1. [基底クラス](#基底クラス)
2. [プレイヤークラス](#プレイヤークラス)
3. [NPCクラス](#npcクラス)
4. [敵クラス](#敵クラス)
5. [管理クラス](#管理クラス)
6. [メインクラス](#メインクラス)

---

## 基底クラス

### **Character（抽象基底クラス）**
```java
// 役割：すべてのキャラクター（人間、モンスター）の基礎
# フィールド
- name: String                    // キャラクター名
- hp: int                         // 現在のHP
- maxHp: int                      // 最大HP
- maxAttack: int                  // 最大攻撃力
- random: Random                  // 乱数生成用

# コンストラクタ
+ Character(String, int, int)     // 名前、最大HP、最大攻撃力

# メソッド
+ takeDamage(int): void           // ダメージを受ける
+ forceTakeDamage(int): void      // 強制ダメージ（飛行無視）
+ heal(int): void                 // 回復する
+ isAlive(): boolean              // 生存しているかチェック
+ getAttack(): int                // ランダムな攻撃力を取得（1～maxAttack）
+ getName(): String               // 名前取得
+ getHp(): int                    // 現在HP取得
+ getMaxHp(): int                 // 最大HP取得
+ getMaxAttack(): int             // 最大攻撃力取得
+ setHp(int): void                // HP設定（セーブ復元用）
+ setMaxHp(int): void             // 最大HP設定
+ setMaxAttack(int): void         // 最大攻撃力設定
```

### **Human（抽象クラス）**
```java
// 役割：すべての人間キャラクター（勇者、NPC）の基礎
// 継承：Character
# メソッド
+ talk(String): void              // 対話メッセージ表示
+ getDialogMessages(Hero): String[] // 対話メッセージ取得（抽象）
```

### **Enemy（抽象クラス）**
```java
// 役割：すべての敵モンスターの基礎
// 継承：Character
# フィールド
- expReward: int                  // 倒したときの経験値報酬

# コンストラクタ
+ Enemy(String, int, int, int)    // 名前、HP、攻撃力、経験値

# メソッド
+ getExpReward(): int             // 経験値報酬取得
+ getAttackName(): String         // 攻撃方法の名前（抽象）
```

---

## プレイヤークラス

### **Hero（基底勇者クラス）**
```java
// 役割：プレイヤーが操作する主人公
// 継承：Human
# フィールド
- x: int                          // マップX座標
- y: int                          // マップY座標
- defeatedEnemies: Set<String>    // 倒した敵の記録
- wizard: Wizard                  // 仲間の魔法使い
- level: int                      // レベル
- exp: int                        // 現在の経験値
- expToNextLevel: int             // 次のレベルまでの必要経験値

# 初期値
HP: 100, 攻撃力: 15, レベル: 1, 座標: (8, 1)

# コンストラクタ
+ Hero(String)                    // 名前指定
# Hero(Hero)                      // コピーコンストラクタ（転職用）

# メソッド
+ move(int, int): void            // 移動（dx, dy）
+ addDefeatedEnemy(String): void  // 敵撃破記録
+ hasDefeated(String): boolean    // 特定の敵を倒したか
+ hasDefeatedAllBasicEnemies(): boolean // 3種敵全撃破チェック
+ setWizard(Wizard): void         // 魔法使い加入
+ getWizard(): Wizard             // 魔法使い取得
+ hasWizard(): boolean            // 魔法使いがいるか
+ sleep(): void                   // HP100%回復
+ canSleep(): boolean             // 眠れるか（基本true）
+ gainExp(int): boolean           // 経験値獲得、レベルアップ判定
- levelUp(): void                 // レベルアップ処理
- calculateExpToNextLevel(): int  // 必要経験値計算（level × 3）
+ getLevel(): int                 // レベル取得
+ getExp(): int                   // 経験値取得
+ getExpToNextLevel(): int        // 必要経験値取得
+ getJobTitle(): String           // 職業名取得（"勇者"）
+ getX(), getY(): int             // 座標取得
+ setX(int), setY(int): void      // 座標設定
```

### **SuperHero（スーパー勇者）**
```java
// 役割：魔法使いと協力して強くなった勇者（簡単ルート）
// 継承：Hero
# フィールド
- isFlying: boolean               // 飛行中フラグ

# 初期値
HP: 100, 攻撃力: 30（×2倍）

# コンストラクタ
+ SuperHero(Hero)                 // Heroから転職

# メソッド
+ fly(): void                     // 空を飛ぶ
+ land(): void                    // 着陸する
+ isFlying(): boolean             // 飛行中かチェック
+ getAttack(): int                // 飛行中は攻撃力半減
+ takeDamage(int): void           // 飛行中は回避
+ getJobTitle(): String           // "スーパー勇者"

# 特殊能力
- 飛行中：攻撃力半減、敵の攻撃回避
- King/Hermitは飛行無視攻撃可能
```

### **TrueHero（真勇者）**
```java
// 役割：独力で成長し、転生を繰り返す究極の戦士（困難ルート）
// 継承：Hero
# フィールド
- rebirthCount: int               // 転生回数
- baseMaxHp: double               // 基礎最大HP（転生時継承）
- baseMaxAttack: double           // 基礎攻撃力（転生時継承）

# 初期値（初回転職時）
レベル: 1, HP: 100, 攻撃力: 20

# コンストラクタ
+ TrueHero(Hero)                  // 初回転職（Heroから）
+ TrueHero(TrueHero)              // 転生（TrueHeroから）
+ TrueHero(Hero, int, int, int, boolean) // セーブ復元用

# メソッド
- recalculateStats(): void        // レベルに応じて能力値再計算
+ gainExp(int): boolean           // レベルアップ後に能力値再計算
+ canRebirth(): boolean           // 転生可能か（Lv10以上）
+ getRebirthCount(): int          // 転生回数取得
+ getBaseMaxAttack(): double      // 基礎攻撃力取得
+ getBaseMaxHp(): double          // 基礎最大HP取得
+ sleep(): void                   // 何もしない（戦闘中は眠れない）
+ canSleep(): boolean             // false（戦闘中は眠れない）
+ restAtInn(): void               // 村での休憩（HP100%回復）
+ getJobTitle(): String           // "真勇者" or "真勇者★N"

# 特殊能力
- パッシブ成長：能力値 = 基礎値 × 1.1^(レベル-1)
- 無限転生：レベル10で転生可能、能力継承
- 転生回数表示：★マーク付き職業名
```

---

## NPCクラス

### **Wizard（魔法使い）**
```java
// 役割：プレイヤーの仲間として戦う魔法使い
// 継承：Human
# 初期値
HP: 90, 攻撃力: 30（デフォルト）

# コンストラクタ
+ Wizard()                        // 対話用（攻撃力30）
+ Wizard(Hero)                    // 仲間加入時（hero攻撃力×1.5）

# メソッド
+ getAttack(): int                // 魔法攻撃（ランダム）
+ superExplosion(Enemy): int      // 必殺技エクスプロージョン
+ getDialogMessages(Hero): String[] // 対話メッセージ
+ talkTo(Hero, boolean): void     // CUI用対話

# 必殺技：エクスプロージョン
- 通常敵：即死（HPを0に）
- 魔王：現在HPの1/3ダメージ
- 使用後：魔法使いのHPが1になる

# 仲間加入条件
- 3種敵全撃破（Slime, Goblin, Werewolf）
- CUI版：100%確定加入
- TrueHeroには加入しない
```

### **King（王様）**
```java
// 役割：表向きはクエスト発行者、実は魔王の主人で真の黒幕
// 継承：Human
# フィールド
- identityRevealed: boolean       // 正体がバレたか

# 初期値
HP: 1,000,000, 攻撃力: 1,000,000

# メソッド
+ getDialogMessages(Hero): String[] // 対話メッセージ（状況により変化）
+ revealIdentity(): String[]      // 正体暴露メッセージ
+ isIdentityRevealed(): boolean   // 正体バレ確認
+ getAttackName(): String         // "絶対王権を振りかざした"
+ talkTo(Hero): void              // CUI用対話

# 対話内容（動的）
1. 通常時：クエスト発行
2. TrueHero対話時：真実を告げる選択肢
3. 正体暴露後：戦闘モード

# 特殊能力
- 飛行無視攻撃
- 洗脳されたお姫様が30%確率で応援（攻撃力×2）
```

### **Princess（お姫様）**
```java
// 役割：魔王戦では味方、王様戦では敵という二面性を持つNPC
// 継承：Human
# 初期値
HP: 80, 攻撃力: 50

# メソッド
+ talkTo(Hero): boolean           // CUI用対話、エンディング判定
+ getDialogMessages(Hero): String[] // 通常エンディングメッセージ
+ getCheerBoost(Enemy): int       // 魔王戦での応援
+ supportKing(): int              // 王様戦での応援（洗脳）
+ getDespairMessage(): String     // 洗脳時の悲痛なメッセージ

# 二面性システム
【魔王戦】味方として応援
- 対象：SuperHero
- 確率：30%
- 効果：攻撃力×2

【王様戦】洗脳されて敵として応援
- 対象：King
- 確率：30%
- 効果：攻撃力×2（最大2,000,000ダメージ）
- メッセージ：「助けて...王様...」「勇者を...倒して...」
```

### **Hermit（隠士）**
```java
// 役割：転職・転生を授ける謎のNPC、真実を明かす導師
// 継承：Human
# 初期値
HP: 100,000,000, 攻撃力: 0

# メソッド
+ getDialogMessages(Hero): String[] // 対話メッセージ（状況により変化）
+ performJobChange(Hero): TrueHero // 初回転職（Hero → TrueHero）
+ performRebirth(TrueHero): TrueHero // 転生（TrueHero → TrueHero）
+ getJobChangeMessages(): String[] // 転職メッセージ
+ getRebirthMessages(int): String[] // 転生メッセージ
+ talkTo(Hero, boolean): boolean  // CUI用対話、決闘判定
+ specialAttack(Hero): int        // 特殊攻撃（最大HPの20%）

# 対話内容（動的）
1. SuperHero：転職拒否
2. Hero Lv10+：真勇者転職提案
3. TrueHero Lv10未満：激励メッセージ
4. TrueHero Lv10+：転生提案
5. 邪悪な王撃破後：20%確率で決闘

# 出現条件
- レベル10以上
- 魔王撃破済み
- 位置：マップ右上角(14, 0) [GUI版のみ]
```

---

## 敵クラス

### **Slime（スライム）**
```java
// 役割：最も弱い敵モンスター
// 継承：Enemy
# 初期値
HP: 30, 攻撃力: 8, 経験値: 1

# メソッド
+ getAttackName(): String         // "毒を吐いた"
```

### **Goblin（ゴブリン）**
```java
// 役割：中級の敵モンスター
// 継承：Enemy
# 初期値
HP: 50, 攻撃力: 15, 経験値: 2

# メソッド
+ getAttackName(): String         // "棍棒で殴った"
```

### **Werewolf（狼男）**
```java
// 役割：強力な敵モンスター
// 継承：Enemy
# 初期値
HP: 60, 攻撃力: 30, 経験値: 5

# メソッド
+ getAttackName(): String         // "噛みついた"
```

### **DemonKing（魔王）**
```java
// 役割：最強の敵モンスター（表向きのラスボス）
// 継承：Enemy
# 初期値
HP: 150, 攻撃力: 75, 経験値: 50

# メソッド
+ getAttackName(): String         // "暗黒魔法を放った"

# 特殊
- お姫様が30%確率でSuperHeroを応援
- 魔法使いの必殺技で1/3ダメージ
```

### **EnemyFactory（敵生成ファクトリー）**
```java
// 役割：異なる種類の敵モンスターを生成（Factory Pattern）
# 定数
- HERO_BASE_ATTACK: int = 20      // 勇者の基準攻撃力

# メソッド
+ createRandomEnemy(): Enemy      // ランダム敵生成（Slime/Goblin/Werewolf）
+ createRandomEnemyForSuper(): Enemy // スーパー用（+DemonKing）
+ createSlime(): Enemy            // スライム生成
+ createGoblin(): Enemy           // ゴブリン生成
+ createWerewolf(): Enemy         // 狼男生成
+ createDemonKing(): Enemy        // 魔王生成
+ createEnemy(String): Enemy      // タイプ指定生成

# 敵出現ルール
- Hero Lv9以下：Slime, Goblin, Werewolf のみ
- SuperHero / TrueHero / Hero Lv10+：+ DemonKing 出現
```

---

## 管理クラス

### **GameManager（Singleton）**
```java
// 役割：ゲーム進行フラグとプレイヤー管理
# フィールド
- instance: GameManager           // シングルトンインスタンス
- player: Hero                    // プレイヤー（動的置き換え可能）
- demonKingDefeated: boolean      // 魔王撃破フラグ
- evilKingDefeated: boolean       // 邪悪な王撃破フラグ
- gameCleared: boolean            // ゲームクリアフラグ

# メソッド
+ getInstance(): GameManager      // インスタンス取得
+ reset(): void                   // ゲームリセット
+ setPlayer(Hero): void           // プレイヤー設定（転職時の置き換え）
+ getPlayer(): Hero               // プレイヤー取得
+ setDemonKingDefeated(boolean): void
+ isDemonKingDefeated(): boolean
+ setEvilKingDefeated(boolean): void
+ isEvilKingDefeated(): boolean
+ setGameCleared(boolean): void
+ isGameCleared(): boolean

# 使用例
Hero hero = new Hero("勇者");
GameManager.getInstance().setPlayer(hero);
// 転職時
TrueHero trueHero = new TrueHero(hero);
GameManager.getInstance().setPlayer(trueHero);
```

### **SaveManager（Singleton、CUI版専用）**
```java
// 役割：セーブ/ロード機能を提供
# 定数
- SAVE_DIR: String = "saves"      // セーブディレクトリ
- MAX_SLOTS: int = 3              // セーブスロット数

# メソッド
+ getInstance(): SaveManager      // インスタンス取得
+ save(int, Hero, boolean): boolean // セーブ実行
+ load(int): SaveData             // ロード実行
+ hasSaveData(int): boolean       // セーブデータ存在確認
+ hasAnySaveData(): boolean       // いずれかのスロットにデータあり
+ getSlotSummary(int): String     // スロット情報表示
+ restorePlayer(SaveData): Hero   // プレイヤー復元
- restoreHero(SaveData): Hero     // Hero復元
- restoreSuperHero(SaveData): SuperHero // SuperHero復元
- restoreTrueHero(SaveData): TrueHero // TrueHero復元
- restoreCommonData(Hero, SaveData): void // 共通データ復元
+ restoreGameManagerFlags(SaveData): void // フラグ復元
+ getMaxSlots(): int              // 最大スロット数取得

# セーブファイル
- 保存形式：シリアライズ（.dat）
- 保存場所：saves/save1.dat, save2.dat, save3.dat
```

### **SaveData（Serializable）**
```java
// 役割：ゲームの状態を保存するデータオブジェクト
# フィールド
## プレイヤー情報
- playerName: String              // プレイヤー名
- jobTitle: String                // 職業名
- hp, maxHp, attack, maxAttack: int
- level, exp: int
- x, y: int                       // 座標
- defeatedEnemies: Set<String>    // 倒した敵リスト

## TrueHero専用
- rebirthCount: int               // 転生回数
- baseMaxHp: int                  // 基礎最大HP
- baseMaxAttack: int              // 基礎攻撃力

## ゲーム進行フラグ
- wizardJoined: boolean           // 魔法使い加入
- demonKingDefeated: boolean      // 魔王撃破
- evilKingDefeated: boolean       // 邪悪な王撃破

## セーブ情報
- saveTime: long                  // セーブ時刻
- serialVersionUID: long = 1L     // シリアライズバージョン

# コンストラクタ
+ SaveData()                      // 空のデータ
+ SaveData(Hero, boolean)         // 現在の状態から作成

# メソッド
+ getSummary(): String            // 概要文字列取得
+ 各種Getter                      // すべてのフィールドのGetter

# 表示例
"勇者 Lv.10 (真勇者★2) - 2025/12/04 15:30"
```

---

## メインクラス

### **TextGame（CUI版メインクラス）**
```java
// 役割：ゲームループとメニュー管理
# フィールド
- scanner: Scanner                // ユーザー入力
- random: Random                  // 乱数生成
- player: Hero                    // プレイヤー（動的置き換え可能）
- wizardJoined: boolean           // 魔法使い加入フラグ
- running: boolean                // ゲームループフラグ

# メソッド
+ start(): void                   // ゲーム開始
- showTitleMenu(): boolean        // タイトルメニュー表示
- showLoadMenu(): boolean         // ロードメニュー表示
- loadGame(int): boolean          // ゲームロード
- showSaveMenu(): void            // セーブメニュー表示
- initGame(): void                // ゲーム初期化
- gameLoop(): void                // メインループ
- showMainMenu(): void            // 村のメニュー表示
- handleMainMenuChoice(int): void // メニュー選択処理

## NPC対話メソッド
- talkToWizard(): void            // 魔法使いとの対話
- talkToKing(): void              // 王様との対話
- talkToPrincess(): void          // お姫様との対話
- talkToHermit(): void            // 隠士との対話

## ゲームアクションメソッド
- explore(): void                 // 冒険に出る（70%敵遭遇）
- rest(): void                    // 休憩（HP回復）
- battle(Character, boolean): void // 戦闘処理
- handleEnemyDefeated(Character): void // 敵撃破処理

## エンディングメソッド
- showNormalEnding(): void        // 通常エンディング
- showTrueEnding(): void          // 真エンディング
- showTrueTrueEnding(): void      // 真の真エンディング

## ユーティリティメソッド
- showStatus(): void              // ステータス表示
- showHpBar(int, int): void       // HPバー表示
- showDialog(String, String[]): void // 対話表示
- clearScreen(): void             // 画面クリア
- waitForEnter(): void            // Enter待機
- readInt(): int                  // 整数入力

+ main(String[]): void            // エントリーポイント
```

---

## クラス関係図

```
Character (抽象基底)
├── Human (抽象)
│   ├── Hero (基底勇者)
│   │   ├── SuperHero (簡単ルート)
│   │   └── TrueHero (困難ルート)
│   ├── Wizard (魔法使い)
│   ├── King (王様・隠しボス)
│   ├── Princess (お姫様・二面性)
│   └── Hermit (隠士・導師)
└── Enemy (抽象)
    ├── Slime (弱)
    ├── Goblin (中)
    ├── Werewolf (強)
    └── DemonKing (ボス)

EnemyFactory (ファクトリー)
└── 敵生成メソッド群

GameManager (Singleton)
└── ゲーム状態管理

SaveManager (Singleton)
├── SaveData (シリアライズ)
└── セーブ/ロード機能

TextGame (メイン)
└── ゲームループと制御
```

---

## キャラクターパラメータ一覧

| クラス | HP | 攻撃力 | 経験値 | 特殊能力 |
|--------|----|----|--------|----------|
| **プレイヤー** |
| Hero | 100 | 15 | - | 眠る |
| SuperHero | 100 | 30 | - | 飛行（攻撃力半減、回避） |
| TrueHero | 100→∞ | 20→∞ | - | 複利成長、無限転生 |
| **NPC** |
| Wizard | 90 | 30 | - | 必殺技、2vs1援護 |
| King | 1,000,000 | 1,000,000 | なし | 飛行無視、お姫様応援 |
| Princess | 80 | 50 | - | 味方/敵応援（30%） |
| Hermit | 100,000,000 | 0 | なし | 最大HP20%攻撃、決闘 |
| **敵** |
| Slime | 30 | 8 | 1 | - |
| Goblin | 50 | 15 | 2 | - |
| Werewolf | 60 | 30 | 5 | - |
| DemonKing | 150 | 75 | 50 | お姫様応援対象 |

---

## 主要アルゴリズム

### 転生システム（TrueHero）
```java
// 初回転職時
TrueHero(Hero source) {
    this.baseMaxHp = source.getMaxHp();      // 100
    this.baseMaxAttack = source.getMaxAttack(); // 20
    this.level = 1;
    recalculateStats();
}

// 転生時
TrueHero(TrueHero source) {
    this.baseMaxHp = source.getMaxHp();      // 成長した値を継承
    this.baseMaxAttack = source.getMaxAttack(); // 成長した値を継承
    this.rebirthCount = source.rebirthCount + 1;
    this.level = 1;
    recalculateStats();
}

// 能力値再計算（複利成長）
recalculateStats() {
    double multiplier = Math.pow(1.1, level - 1);
    this.maxHp = (int)(baseMaxHp * multiplier);
    this.maxAttack = (int)(baseMaxAttack * multiplier);
}

// 数値例
Lv1:  HP 100,  攻撃 20  (基礎値)
Lv10: HP 234,  攻撃 47  (1.1^9 = 2.36倍)
転生: HP 234,  攻撃 47  (継承)
Lv10: HP 548,  攻撃 110 (さらに2.36倍)
```

### レベルアップシステム
```java
// 必要経験値計算
expToNextLevel = level * 3

// レベルアップ処理
gainExp(int amount) {
    exp += amount;
    if (exp >= expToNextLevel) {
        level++;
        exp = 0;
        expToNextLevel = level * 3;
        hp = maxHp;  // HP全回復
        if (this instanceof TrueHero) {
            recalculateStats();  // 能力値再計算
        }
        return true;
    }
    return false;
}
```

### お姫様の二面性システム
```java
// 魔王戦（味方として応援）
if (enemy instanceof DemonKing && player instanceof SuperHero) {
    if (random.nextInt(100) < 30) {  // 30%確率
        int multiplier = princess.getCheerBoost((Enemy)enemy);
        damage *= multiplier;  // 攻撃力×2
    }
}

// 王様戦（洗脳されて敵として応援）
if (enemy instanceof King && king.isIdentityRevealed()) {
    if (random.nextInt(100) < 30) {  // 30%確率
        int multiplier = princess.supportKing();
        enemyDamage *= multiplier;  // 攻撃力×2
        // 最大2,000,000ダメージ
    }
}
```

---

**作成日**：2025-12-04
**バージョン**：1.0（CUI版専用）
**対象**：BasicRPG すべてのクラス
