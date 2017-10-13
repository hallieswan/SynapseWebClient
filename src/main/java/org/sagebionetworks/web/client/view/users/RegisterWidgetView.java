package org.sagebionetworks.web.client.view.users;

import org.sagebionetworks.repo.model.auth.NewUser;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public interface RegisterWidgetView extends IsWidget {

	/**
	 * Set the presenter.
	 * @param presenter
	 */
	void setPresenter(Presenter presenter);
	void setSynAlert(Widget w);

	void enableEmailAddressField(boolean enabled);

	/**
	 * Presenter interface
	 */
	interface Presenter {
		void registerUser(NewUser newUser);

		String getEncodedMembershipInvtnSignedToken();
	}
	void enableRegisterButton(boolean enable);
	void setVisible(boolean isVisible);

	void setEmail(String email);
	void clear();
	void showInfo(String message);
}
