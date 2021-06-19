# VerDirectRenewed

-- _A plugin for Spigot to gather items drop in a way that go directly into player's inventory._

## 概要

ブロックの破壊や敵のドロップによるアイテムを直接インベントリに投入するようにするプラグインです.

## デモ

![demo](https://raw.githubusercontent.com/blackbracken/VerDirectRenewed/master/demo.gif)

## 設定
```yaml
# トリガーとなるアイテムの設定
# これらを満たすいずれかのアイテムがプレイヤーのインベントリにあれば回収機能が発動する
# 
# Material, Name, LoreのLimitをすべてfalseにしたものを入れておくと, いつでも発動するようになる
TriggerAttributes:
  "0": # 任意の重複せず大き過ぎない数字
    Material:
      Limit: true # アイテムの種類による制限を行うかどうか
      Value: BARREL # アイテムの種類 (cf. https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
    Name:
      Limit: false # アイテムの名前による制限を行うか
    Lore:
      Limit: false # アイテムの説明文による制限を行うか
  "1":
    Material:
      Limit: true
      Value: STICK
    Name:
      Limit: true
      Value: "&cWand" # アイテム名. `&`によるカラーコードが有効
    Lore:
      Limit: true
      Value: # アイテムの説明文. 複数行に対応していて, '&'によるカラーコードが有効
        - "&9so great"
        - "&9MagicWand"
  "2":
    Material:
      Limit: false
    Name:
      Limit: true
      Value: "&aCollector"
    Lore:
      Limit: false

# 回収機能が発動するイベントの設定
Event:
  # ブロックの破壊
  BlockBreak:
    Enable: true # 回収を行うか
    Delay: 4 # イベントが発生してから回収を行うまでの時間 (1/20秒単位)
    Range: 2.0 # 回収を行う範囲 (破壊したブロックなど対象からの距離)
  # 羊の毛刈り
  Shear:
    Enable: true
    Delay: 4
    Range: 2.0
  # モブの殺害
  Creature:
    Enable: true
    Delay: 4
    Range: 2.0

Directing:
  Sound: true
  Effect: false
```

## パーミッション

|パーミッション|効果|デフォルト|
|:-:|:-:|:-:|
|verdirect.*|このプラグインのすべての権限を持つ|op|
|verdirect.use|回収することができるようになる|true|

## 検証環境

* Minecraft 1.16.5
* Java 8 + Spigot 1.16.5-R0.1-SNAPSHOT

## ライセンス

* MIT License

## 注釈

このプラグインは [朱サバ](https://seesaawiki.jp/vermilion/) のために開発されました