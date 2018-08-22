package com.example.LNDWA.util.network;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;
/**SSLSocketFactory to connect the application via HTTPS.
 * @author Timo Homburg
 *
 */
public class LNDWASSLFactory extends SSLSocketFactory {
	/**SSLContext for this application.*/
	private final transient SSLContext sslContext = SSLContext.getInstance("TLS");

    /**
     * SSL Factory for this program.
     * @param truststore the truststore to store the key
     * @throws java.security.NoSuchAlgorithmException if a calculation error occurs
     * @throws java.security.KeyManagementException on key management errors
     * @throws java.security.KeyStoreException on key storing errors
     * @throws java.security.UnrecoverableKeyException when a key is not recoverable
     */
	public LNDWASSLFactory(final KeyStore truststore)
		throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);
		final TrustManager trustmanager = new X509TrustManager() {
			public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
			}

			public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		this.sslContext.init(null, new TrustManager[] {trustmanager}, null);
	}

	@Override
	public Socket createSocket(final Socket socket,
			final String host, final int port,
			final boolean autoClose) throws IOException{
		return this.sslContext.getSocketFactory()
			.createSocket(socket, host, port, autoClose);
	}

	@Override
	public Socket createSocket() throws IOException {
		return this.sslContext.getSocketFactory().createSocket();
	}
}
