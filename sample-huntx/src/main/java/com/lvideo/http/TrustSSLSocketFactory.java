package com.lvideo.http;

import android.content.Context;

import com.agx.scaffold.JxLogger;
import com.lvideo.http.utils.TokenUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;

/**
 * Author: liuxiaojiang(liuxiaojiang@lvideo.com)
 * Date: 2016-02-18
 * Time: 18:00
 * Description: 用于https证书的验证
 */
public class TrustSSLSocketFactory {
    private static final String       KEY_STORE_TYPE_P12 = "PKCS12";
    private static       KeyManager[] mKeyManager;

    public static KeyManager[] createKeyManagers(Context pContext)
        throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (null != mKeyManager) {
            return mKeyManager;
        }

        if (pContext == null) {
            return null;
        }

        String password = TokenUtil.getKey(pContext);
        byte[] certData = TokenUtil.getCert(pContext);
        if (certData == null || certData.length == 0) {
            JxLogger.e("bad https cert");
            return mKeyManager;
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(certData);
        KeyStore             keyStore    = KeyStore.getInstance(KEY_STORE_TYPE_P12);
        keyStore.load(inputStream, password == null ? null : password.toCharArray());

        KeyManagerFactory keyManagerFactory =
            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password == null ? null : password.toCharArray());
        mKeyManager = keyManagerFactory.getKeyManagers();

        inputStream.close();

        return mKeyManager;
    }
}
