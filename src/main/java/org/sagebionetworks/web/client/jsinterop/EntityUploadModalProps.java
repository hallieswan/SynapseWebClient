package org.sagebionetworks.web.client.jsinterop;

import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class EntityUploadModalProps extends ReactComponentProps {

  @FunctionalInterface
  @JsFunction
  public interface Callback {
    void run();
  }

  public String entityId;

  public boolean open;

  public Callback onClose;

  public ReactRef<EntityUploadHandle> ref;

  @JsOverlay
  public static EntityUploadModalProps create(
    String containerId,
    boolean open,
    Callback onClose,
    ReactRef<EntityUploadHandle> ref
  ) {
    EntityUploadModalProps props = new EntityUploadModalProps();
    props.entityId = containerId;
    props.open = open;
    props.onClose = onClose;
    props.ref = ref;
    return props;
  }
}
