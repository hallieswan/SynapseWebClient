package org.sagebionetworks.web.unitserver;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.sagebionetworks.client.SynapseClient;
import org.sagebionetworks.client.exceptions.SynapseException;
import org.sagebionetworks.repo.model.UserProfile;
import org.sagebionetworks.repo.model.UserSessionData;
import org.sagebionetworks.repo.model.auth.Session;
import org.sagebionetworks.repo.model.versionInfo.SynapseVersionInfo;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.web.server.servlet.StackConfigServiceImpl;
import org.sagebionetworks.web.server.servlet.SynapseClientImpl;
import org.sagebionetworks.web.server.servlet.SynapseProvider;
import org.sagebionetworks.web.server.servlet.TokenProvider;
import org.sagebionetworks.web.shared.exceptions.RestServiceException;
import org.springframework.test.util.ReflectionTestUtils;

public class StackConfigServiceTest {

  @Mock
  SynapseProvider mockSynapseProvider;

  @Mock
  TokenProvider mockTokenProvider;

  @Mock
  UserSessionData mockUserSessionData;

  String testSessionToken = "12345abcde";

  @Mock
  SynapseClient mockSynapse;

  @Mock
  ThreadLocal<HttpServletRequest> mockThreadLocal;

  @Mock
  HttpServletRequest mockRequest;

  @Mock
  SynapseVersionInfo mockSynapseVersionInfo;

  @Captor
  ArgumentCaptor<String> stringCaptor;

  public static final String REPO_VERSION = "stack-1";
  private static final String HTTP_REQUEST_URL =
    "https://www.synapse.org/Portal/stackConfig";

  StackConfigServiceImpl stackConfigService;

  @Before
  public void before() throws SynapseException, JSONObjectAdapterException {
    MockitoAnnotations.initMocks(this);
    when(mockSynapseProvider.createNewClient()).thenReturn(mockSynapse);

    UserProfile testProfile = new UserProfile();
    testProfile.setOwnerId("123");

    stackConfigService = new StackConfigServiceImpl();
    stackConfigService.setSynapseProvider(mockSynapseProvider);
    stackConfigService.setTokenProvider(mockTokenProvider);
    Session testSession = new Session();
    testSession.setSessionToken(testSessionToken);
    testSession.setAcceptsTermsOfUse(true);
    when(mockUserSessionData.getProfile()).thenReturn(testProfile);
    when(mockUserSessionData.getSession()).thenReturn(testSession);
    when(mockSynapse.getMyProfile()).thenReturn(testProfile);

    ReflectionTestUtils.setField(
      stackConfigService,
      "perThreadRequest",
      mockThreadLocal
    );
    when(mockThreadLocal.get()).thenReturn(mockRequest);
    when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");
    when(mockRequest.getRequestURL())
      .thenReturn(new StringBuffer(HTTP_REQUEST_URL));

    when(mockSynapseVersionInfo.getVersion()).thenReturn(REPO_VERSION);
    when(mockSynapse.getVersionInfo()).thenReturn(mockSynapseVersionInfo);
  }

  @Test
  public void testGetStorageLocationSettingProperty()
    throws SynapseException, RestServiceException {
    String defaultStorageId = stackConfigService
      .getSynapseProperties()
      .get(SynapseClientImpl.DEFAULT_STORAGE_ID_PROPERTY_KEY);
    assertNotNull(defaultStorageId);
  }
}
