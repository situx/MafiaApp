package com.github.situx.mafiaapp.util.network;

import java.io.IOException;
import java.util.List;

/**The network interface of the application.
 * @author Timo Homburg
 *
 */
public interface NetworkAPI {
	/**Method for the user login.
	 * @param url the url for the login
	 * @param user the username for the login
	 * @param password the password for the login
	 * @throws java.io.IOException on connection errors
	 * @return success indicator
	 */
	boolean login(String url, String user, String password) throws IOException;
	/**
	 * Gets a file from the given url using the user credentials.
	 * @param url the url for the data transfer
	 * @throws java.io.IOException on connection errors
	 * @return the file content as String
	 */
    String getData(String url, Boolean post, List<String> postparams) throws IOException;



    /**
	 * Sets the login credential username.
	 * @param username the username to set
	 */
	void setUsername(String username);
	/**
	 * Sets the login credential password.
	 * @param password the password to set
	 */
	void setPassword(String password);
}
