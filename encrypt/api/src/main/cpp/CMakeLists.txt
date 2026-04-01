# CMakeの最小バージョン
cmake_minimum_required(VERSION 3.10.2)

# ライブラリ名を設定
project("mycrypto")

# 共有ライブラリ(SHARED)としてソースを登録
add_library(
        mycrypto # ライブラリ名
        SHARED   # 動的ライブラリ
        my_crypto.c # コンパイルするファイル
)

# Android標準のログライブラリなどをリンクしたい場合（任意）
find_library(log-lib log)
target_link_libraries(mycrypto ${log-lib})
