package com.github.situx.mafiaapp.util.network;

    import android.util.Log;

    import com.github.situx.mafiaapp.util.network.NetworkAPI;

    import org.apache.http.HttpResponse;
    import org.apache.http.HttpVersion;
    import org.apache.http.NameValuePair;
    import org.apache.http.StatusLine;
    import org.apache.http.auth.AuthScope;
    import org.apache.http.auth.UsernamePasswordCredentials;
    import org.apache.http.client.entity.UrlEncodedFormEntity;
    import org.apache.http.client.methods.HttpGet;
    import org.apache.http.client.methods.HttpPost;
    import org.apache.http.client.params.AuthPolicy;
    import org.apache.http.conn.ClientConnectionManager;
    import org.apache.http.conn.params.ConnManagerParams;
    import org.apache.http.conn.scheme.PlainSocketFactory;
    import org.apache.http.conn.scheme.Scheme;
    import org.apache.http.conn.scheme.SchemeRegistry;
    import org.apache.http.impl.client.DefaultHttpClient;
    import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
    import org.apache.http.message.BasicNameValuePair;
    import org.apache.http.params.BasicHttpParams;
    import org.apache.http.params.HttpConnectionParams;
    import org.apache.http.params.HttpParams;
    import org.apache.http.params.HttpProtocolParams;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;
    import java.security.KeyManagementException;
    import java.security.KeyStore;
    import java.security.KeyStoreException;
    import java.security.NoSuchAlgorithmException;
    import java.security.UnrecoverableKeyException;
    import java.security.cert.CertificateException;
    import java.util.ArrayList;
    import java.util.List;

    import javax.net.ssl.SSLSocketFactory;

    import static java.net.Proxy.Type.HTTP;


/**
     * Handles the data transfer between the application and the server.
     * @author Timo Homburg
     *
     */
    public class NetworkHandler extends DefaultHttpClient implements NetworkAPI {
        /**The default HTTP port.*/
        private static final Integer DEFAULT_HTTP=80;
        /**The default HTTPS port.*/
        private static final Integer DEFAULT_HTTPS=443;
        /**The  ok status of a http request.*/
        private static final Integer HTTP_OK=200;
        /**The instance of this network handler class.*/
        private static NetworkHandler instance;
        /**The username to login.*/
        private transient String username;
        /**The password for login.*/
        private transient String password;
        /**Port for the Http Connection.*/
        private final transient Integer httpPort;
        /**Port for the Https Connection.*/
        private final transient Integer httpsPort;
        /**HttpParams to configure the HTTPClient connection.*/
        private final transient HttpParams params;
        /**The socket factory for initiating secure connections via HTTPS.*/
        private transient LNDWASSLFactory socketfactory;
        /**The response of the HTTP server.*/
        private transient HttpResponse response;
        /**Defined timeout for the server connection.*/
        private transient Integer timeout;
        /**
         * Constructor for Network Handler.
         * @param timeout the connection timeout
         */
        public NetworkHandler(final Integer timeout){
            super();
            this.timeout=timeout;
            this.params=this.getParams();
            this.httpPort=NetworkHandler.DEFAULT_HTTP;
            this.httpsPort=NetworkHandler.DEFAULT_HTTPS;
            this.username="";
            this.password="";
        }

        /**
         * Second constructor for Network Handler.
         * @param timeout the connection timeout
         * @param httpport the httpport to choose
         * @param httpsport the httpsport to choose
         */
        public NetworkHandler(final Integer timeout,final Integer httpport,final Integer httpsport){
            super();
            this.timeout=timeout;
            this.params=this.getParams();
            this.httpPort=httpport;
            this.httpsPort=httpsport;
            this.username="";
            this.password="";
        }
        @Override protected ClientConnectionManager createClientConnectionManager() {
            try {
                final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                this.socketfactory = new LNDWASSLFactory(trustStore);
                //this.socketfactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                final HttpParams httpparams = new BasicHttpParams();
                HttpProtocolParams.setVersion(httpparams, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(httpparams, "UTF-8");

                final SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), this.httpPort));
                registry.register(new Scheme("https", this.socketfactory, this.httpsPort));
                return new ThreadSafeClientConnManager(httpparams, registry);
            } catch (final KeyManagementException e) {
                Log.v("KeyManagementException:",e.getMessage());
            } catch (final NoSuchAlgorithmException e) {
                Log.v("NoSuchAlgoException",e.getMessage());
            } catch (final KeyStoreException e) {
                Log.v("KeyStoreException:",e.getMessage());
            } catch (final UnrecoverableKeyException e) {
                Log.v("UnrecoverableException:",e.getMessage());
            } catch (final CertificateException e) {
                Log.v("CertificateException:",e.getMessage());
            } catch (final IOException e) {
                Log.v("IOException:",e.getMessage());
            }
            return null;
        }

        /**
         * Function for performing a login.
         * @param url the login URL
         * @param user the username for the login
         * @param passwd the password for the login
         * @return a success indicator
         * @throws IOException on IO errors
         */
        @Override
        public boolean login(final String url,final String user,final String passwd) throws IOException{
            HttpConnectionParams.setConnectionTimeout(this.params, this.timeout);
            HttpConnectionParams.setSoTimeout(this.params, this.timeout);
            ConnManagerParams.setTimeout(this.params, this.timeout);
            this.getCredentialsProvider().setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, AuthPolicy.BASIC),
                    new UsernamePasswordCredentials(user, passwd)
            );
            final HttpGet get = new HttpGet(url);
            this.response = this.execute(get);
            final StatusLine status = this.response.getStatusLine();
            //System.out.println("Statusline: "+status);
            if (status.getStatusCode() != NetworkHandler.HTTP_OK) {
                throw new IOException("Invalid response from server: " + status.toString());
            }
            return true;
        }

        /**
         * Gets a data file from a given URL.
         * @param url the url where to get the file from
         * @return the file as string
         * @throws IOException on error
         */
        @Override
        public String getData(final String url,final Boolean post,List<String> postparams) throws IOException{
            //System.out.println("Getting data from: "+url);
            HttpConnectionParams.setConnectionTimeout(this.params, this.timeout);
            HttpConnectionParams.setSoTimeout(this.params, this.timeout);
            ConnManagerParams.setTimeout(this.params, this.timeout);
            this.getCredentialsProvider().setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, AuthPolicy.BASIC),
                    new UsernamePasswordCredentials(this.username, this.password)
            );
            List <NameValuePair> nvps = new ArrayList<>();
            int i=0;
            for(String str:postparams){
                 nvps.add(new BasicNameValuePair("param"+i++,str));
            }
            if(post){
                Log.e("POST","IT");
                final HttpPost get = new HttpPost(url);
                get.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
                this.response = this.execute(get);

            }else{
                Log.e("GET","IT");
                final HttpGet get = new HttpGet(url);
                this.response = this.execute(get);
            }
            final StatusLine status = this.response.getStatusLine();
            if (status.getStatusCode() != NetworkHandler.HTTP_OK) {
                throw new IOException("Invalid response from server: " + status.toString());
            }
            final InputStream inputstream = this.response.getEntity().getContent();
            final BufferedReader jsonreader = new BufferedReader(new InputStreamReader(inputstream));
            final StringBuilder buffer=new StringBuilder();
            String temp=jsonreader.readLine();
            while(temp!=null){
                buffer.append(temp);
                temp=jsonreader.readLine();
            }
            jsonreader.close();
            //System.out.println("Got data: "+buffer.toString());
            return buffer.toString();
        }

        /**
         * Gets the instance of the network handler.
         * @param timeout the timeout to set
         * @return the current instance of NetworkHandler
         */
        public static NetworkHandler getInstance(final Integer timeout){
            if(NetworkHandler.instance==null){
                NetworkHandler.instance=new NetworkHandler(timeout);
            }
            return NetworkHandler.instance;
        }

        /**
         * Gets the connection timeout.
         * @return the connection timeout as Integer
         */
        public Integer getTimeout(){
            return this.timeout;
        }

        /**
         * Sets the connection timeout.
         * @param timeout the timeout in minutes to set
         */
        public void setTimeout(final Integer timeout){
            this.timeout=timeout;
        }

        /**
         * Sets the username for login procedures.
         * @param username the username to set
         */
        public void setUsername(final String username) {
            this.username = username;
        }

        /**
         * Sets the password for login procedures.
         * @param password the password to set
         */
        public void setPassword(final String password) {
            this.password = password;
        }

    }
