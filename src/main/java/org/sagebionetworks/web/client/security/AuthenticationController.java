package org.sagebionetworks.web.client.security;

import com.google.common.util.concurrent.FluentFuture;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sagebionetworks.repo.model.UserProfile;

public interface AuthenticationController {
  /**
   * Login the user
   *
   * @param username
   * @param password
   * @return
   */
  public void loginUser(
    String username,
    String password,
    AsyncCallback<UserProfile> callback
  );

  /**
   * sets a new access token
   *
   * @param token
   */
  void setNewAccessToken(
    String token,
    final AsyncCallback<UserProfile> callback
  );

  /**
   * attempts to load from an existing session cookie
   *
   * @param callback
   */
  void initializeFromExistingAccessTokenCookie(
    final AsyncCallback<UserProfile> callback
  );

  void initializeFromExistingAccessTokenCookie(
    final AsyncCallback<UserProfile> callback,
    boolean forceResetQueryClient
  );

  /**
   * Terminates the session of the current user
   */
  public void logoutUser();

  /**
   * Is the user logged in?
   *
   * @return
   */
  public boolean isLoggedIn();

  /**
   * Get the OwnerId/Principal id out of the UserProfile / UserSessionData in a lightweight fashion
   *
   * @return
   */
  public String getCurrentUserPrincipalId();

  /**
   * Get the current session token, if there is one
   *
   * @return
   */
  public String getCurrentUserAccessToken();

  /**
   * Get the UserProfile object
   *
   * @return
   */
  public UserProfile getCurrentUserProfile();

  public void updateCachedProfile(UserProfile updatedProfile);

  void checkForUserChange();
  void clearLocalStorage();
  FluentFuture<Void> getCheckForUserChangeFuture();
}
