#include <jni.h>
#include <string.h>
#include <stdlib.h>

// 実際の暗号化ロジック (純粋なC)
void xor_process(unsigned char* data, int data_len, const unsigned char* key, int key_len) {
    for (int i = 0; i < data_len; i++) {
        data[i] ^= key[i % key_len];
    }
}

// JNIインターフェース関数
JNIEXPORT jbyteArray JNICALL
Java_com_peco2222_kitsune_encrypt_CryptoManager_encryptNative(JNIEnv *env, jobject thiz, jbyteArray data_, jbyteArray key_) {

    // 1. KotlinのByteArrayをCの配列に変換（ポインタの取得）
    jbyte *data = (*env)->GetByteArrayElements(env, data_, NULL);
    jbyte *key = (*env)->GetByteArrayElements(env, key_, NULL);
    jsize data_len = (*env)->GetArrayLength(env, data_);
    jsize key_len = (*env)->GetArrayLength(env, key_);

    // 2. 暗号化処理の実行
    xor_process((unsigned char*)data, data_len, (unsigned char*)key, key_len);

    // 3. 結果を返すための新しいjbyteArrayを作成
    jbyteArray result = (*env)->NewByteArray(env, data_len);
    (*env)->SetByteArrayRegion(env, result, 0, data_len, data);

    // 4. メモリの解放（重要：忘れるとメモリリークします）
    (*env)->ReleaseByteArrayElements(env, data_, data, 0);
    (*env)->ReleaseByteArrayElements(env, key_, key, JNI_ABORT); // 鍵は変更しないのでABORT

    return result;
}
