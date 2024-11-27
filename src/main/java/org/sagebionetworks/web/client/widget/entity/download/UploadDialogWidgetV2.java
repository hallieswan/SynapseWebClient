package org.sagebionetworks.web.client.widget.entity.download;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.context.SynapseReactClientFullContextPropsProvider;
import org.sagebionetworks.web.client.events.EntityUpdatedEvent;
import org.sagebionetworks.web.client.jsinterop.EntityUploadHandle;
import org.sagebionetworks.web.client.jsinterop.EntityUploadModalProps;
import org.sagebionetworks.web.client.jsinterop.React;
import org.sagebionetworks.web.client.jsinterop.ReactRef;
import org.sagebionetworks.web.client.jsinterop.SRC;
import org.sagebionetworks.web.client.widget.ReactComponent;

public class UploadDialogWidgetV2 extends Widget {

  private final GlobalApplicationState globalApplicationState;
  private final EventBus eventBus;
  private final SynapseReactClientFullContextPropsProvider contextProvider;

  private final ReactComponent reactComponent;

  private String entityId;
  private ReactRef<EntityUploadHandle> ref;

  @Inject
  public UploadDialogWidgetV2(
    GlobalApplicationState globalApplicationState,
    EventBus eventBus,
    SynapseReactClientFullContextPropsProvider contextProvider
  ) {
    this.globalApplicationState = globalApplicationState;
    this.eventBus = eventBus;
    this.contextProvider = contextProvider;

    this.reactComponent = new ReactComponent();
  }

  public void configure(String entityId) {
    this.entityId = entityId;
    globalApplicationState.setDropZoneHandler(fileList ->
      this.ref.current.handleUploads(fileList)
    );

    renderComponent(false);
  }

  private void renderComponent(boolean open) {
    this.ref = React.createRef();
    reactComponent.render(
      React.createElementWithSynapseContext(
        SRC.SynapseComponents.EntityUploadModal,
        EntityUploadModalProps.create(entityId, open, this::onClose, this.ref),
        contextProvider.getJsInteropContextProps()
      )
    );
  }

  public void show() {
    renderComponent(true);
  }

  private void onClose() {
    eventBus.fireEvent(new EntityUpdatedEvent(entityId));
    renderComponent(false);
  }

  @Override
  public Widget asWidget() {
    return reactComponent.asWidget();
  }

  @Override
  public void onUnload() {
    globalApplicationState.clearDropZoneHandler();

    super.onUnload();
  }
}
