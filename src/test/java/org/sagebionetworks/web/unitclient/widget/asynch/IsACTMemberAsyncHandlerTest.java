package org.sagebionetworks.web.unitclient.widget.asynch;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.sagebionetworks.web.client.widget.asynch.IsACTMemberAsyncHandlerImpl.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sagebionetworks.repo.model.ACTAccessRequirement;
import org.sagebionetworks.repo.model.AccessRequirement;
import org.sagebionetworks.repo.model.RestrictableObjectDescriptor;
import org.sagebionetworks.repo.model.UserBundle;
import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.SynapseJSNIUtils;
import org.sagebionetworks.web.client.UserProfileClientAsync;
import org.sagebionetworks.web.client.cache.SessionStorage;
import org.sagebionetworks.web.client.security.AuthenticationController;
import org.sagebionetworks.web.client.utils.CallbackP;
import org.sagebionetworks.web.client.widget.Button;
import org.sagebionetworks.web.client.widget.accessrequirements.ManagedACTAccessRequirementWidget;
import org.sagebionetworks.web.client.widget.accessrequirements.ManagedACTAccessRequirementWidgetView;
import org.sagebionetworks.web.client.widget.accessrequirements.CreateAccessRequirementButton;
import org.sagebionetworks.web.client.widget.accessrequirements.createaccessrequirement.CreateAccessRequirementWizard;
import org.sagebionetworks.web.client.widget.accessrequirements.requestaccess.CreateDataAccessRequestWizard;
import org.sagebionetworks.web.client.widget.asynch.IsACTMemberAsyncHandler;
import org.sagebionetworks.web.client.widget.asynch.IsACTMemberAsyncHandlerImpl;
import org.sagebionetworks.web.client.widget.entity.WikiPageWidget;
import org.sagebionetworks.web.client.widget.entity.controller.SynapseAlert;
import org.sagebionetworks.web.client.widget.table.modal.wizard.ModalWizardWidget;
import org.sagebionetworks.web.shared.WikiPageKey;
import org.sagebionetworks.web.test.helper.AsyncMockStubber;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class IsACTMemberAsyncHandlerTest {
	IsACTMemberAsyncHandlerImpl widget;
	@Mock
	UserProfileClientAsync mockUserProfileClient;
	@Mock
	SessionStorage mockSessionStorage;
	@Mock
	AuthenticationController mockAuthController;
	@Mock
	SynapseJSNIUtils mockJsniUtils;
	@Mock
	CallbackP<Boolean> mockCallback;
	@Mock
	UserBundle mockUserBundle;
	public static final String CURRENT_USER_ID = "33325";
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		widget = new IsACTMemberAsyncHandlerImpl(mockUserProfileClient, mockSessionStorage, mockAuthController, mockJsniUtils);
		when(mockAuthController.isLoggedIn()).thenReturn(true);
		when(mockAuthController.getCurrentUserPrincipalId()).thenReturn(CURRENT_USER_ID);
	}

	@Test
	public void testAnonymous() {
		when(mockAuthController.isLoggedIn()).thenReturn(false);
		widget.isACTMember(mockCallback);
		verify(mockCallback).invoke(false);
	}
	@Test
	public void testCache() {
		when(mockSessionStorage.getItem(SESSION_KEY_PREFIX + CURRENT_USER_ID)).thenReturn(Boolean.TRUE.toString());
		widget.isACTMember(mockCallback);
		verify(mockCallback).invoke(true);
		
		when(mockSessionStorage.getItem(SESSION_KEY_PREFIX + CURRENT_USER_ID)).thenReturn(Boolean.FALSE.toString());
		widget.isACTMember(mockCallback);
		verify(mockCallback).invoke(false);
	}
	
	@Test
	public void testRpcSuccess() {
		Boolean isACTMember = false;
		when(mockUserBundle.getIsACTMember()).thenReturn(isACTMember);
		AsyncMockStubber.callSuccessWith(mockUserBundle).when(mockUserProfileClient).getMyOwnUserBundle(anyInt(), any(AsyncCallback.class));
		widget.isACTMember(mockCallback);
		verify(mockUserProfileClient).getMyOwnUserBundle(anyInt(), any(AsyncCallback.class));
		verify(mockSessionStorage).setItem(SESSION_KEY_PREFIX + CURRENT_USER_ID, isACTMember.toString());
		verify(mockCallback).invoke(isACTMember);
	}
	@Test
	public void testRpcFailure() {
		String message = "an error occurred";
		Exception ex = new Exception(message);
		AsyncMockStubber.callFailureWith(ex).when(mockUserProfileClient).getMyOwnUserBundle(anyInt(), any(AsyncCallback.class));
		widget.isACTMember(mockCallback);
		verify(mockUserProfileClient).getMyOwnUserBundle(anyInt(), any(AsyncCallback.class));
		verify(mockJsniUtils).consoleError(message);
		verify(mockCallback).invoke(false);
	}
}
