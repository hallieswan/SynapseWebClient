package org.sagebionetworks.web.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface ChatView extends IsWidget {
  void render(
    String initMessage,
    String agentRegistrationId,
    String chatbotName
  );
  void scrollToTop();
}
