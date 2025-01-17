package org.sagebionetworks.web.client.jsinterop;

import jsinterop.annotations.JsNullable;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class SynapseChatProps extends ReactComponentProps {

  @JsNullable
  public String initialMessage;

  @JsNullable
  public String agentRegistrationId;

  @JsNullable
  public String chatbotName;

  @JsOverlay
  public static SynapseChatProps create(
    String initialMessage,
    String agentRegistrationId,
    String chatbotName
  ) {
    SynapseChatProps props = new SynapseChatProps();
    if (initialMessage != null) {
      props.initialMessage = initialMessage;
    }
    if (agentRegistrationId != null) {
      props.agentRegistrationId = agentRegistrationId;
    }
    if (chatbotName != null) {
      props.chatbotName = chatbotName;
    }
    return props;
  }
}
