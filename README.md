# DummyNote
一個簡單的記事 APP。

點選"+號"可新增筆記。

點選筆記後點"X符號"可刪除筆記。

點選筆記後點"鉛筆符號"可修改筆記。
# 主要技術
SQLiteDatabase、SQLiteOpenHelper、ListView。

# 架構說明
本 APP 自訂一個處理資料庫的類別，將資料庫相關的 SQLiteDatabase、SQLiteOpenHelper 操作都放與此類別中。

以 Fragment 中的 ListView 來呈現資料，然後透過 menu 按鈕來進行 UI 操作。
