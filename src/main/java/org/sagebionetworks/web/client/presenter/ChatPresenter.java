package org.sagebionetworks.web.client.presenter;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import org.sagebionetworks.web.client.GWTWrapper;
import org.sagebionetworks.web.client.place.ChatPlace;
import org.sagebionetworks.web.client.view.ChatView;

public class ChatPresenter
  extends AbstractActivity
  implements Presenter<ChatPlace> {

  private ChatPlace place;
  private ChatView view;
  private GWTWrapper gwt;

  @Inject
  public ChatPresenter(ChatView view, GWTWrapper gwt) {
    this.view = view;
    this.gwt = gwt;
    view.scrollToTop();
  }

  @Override
  public void start(AcceptsOneWidget panel, EventBus eventBus) {
    // Install the view
    panel.setWidget(view);
  }

  @Override
  public void setPlace(ChatPlace place) {
    this.place = place;
    // SWC-7109: Decode place parameters
    String initialMessage = place.getParam(ChatPlace.INITIAL_MESSAGE);
    if (initialMessage != null) {
      initialMessage = gwt.decodeQueryString(initialMessage);
    }
    String agentRegistrationId = place.getParam(
      ChatPlace.AGENT_REGISTRATION_ID
    );
    String chatbotName = place.getParam(ChatPlace.CHATBOT_NAME);
    if (chatbotName != null) {
      chatbotName = gwt.decodeQueryString(chatbotName);
    }

    view.render(initialMessage, agentRegistrationId, chatbotName);
  }
}
