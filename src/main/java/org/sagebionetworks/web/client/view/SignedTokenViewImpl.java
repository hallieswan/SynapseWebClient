package org.sagebionetworks.web.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.html.Div;
import org.sagebionetworks.repo.model.SignedTokenInterface;
import org.sagebionetworks.web.client.jsinterop.mui.Grid;
import org.sagebionetworks.web.client.widget.LoadingSpinner;
import org.sagebionetworks.web.client.widget.header.Header;

public class SignedTokenViewImpl implements SignedTokenView {

  public interface SignedTokenViewImplUiBinder
    extends UiBinder<Widget, SignedTokenViewImpl> {}

  @UiField
  SimplePanel synapseAlertContainer;

  @UiField
  Button okButton;

  @UiField
  Button confirmUnsubscribe;

  @UiField
  Button cancelUnsubscribe;

  @UiField
  Grid successUI;

  @UiField
  Heading successMessage;

  @UiField
  Modal confirmUnsubscribeUI;

  @UiField
  SimplePanel unsubscribeUserBadgeContainer;

  @UiField
  LoadingSpinner loadingUI;

  @UiField
  Div otherUI;

  private Presenter presenter;
  private Header headerWidget;

  Widget widget;
  HandlerRegistration confirmUnsubscribeHandler;

  @Inject
  public SignedTokenViewImpl(
    SignedTokenViewImplUiBinder binder,
    Header headerWidget
  ) {
    widget = binder.createAndBindUi(this);

    this.headerWidget = headerWidget;
    headerWidget.configure();
    ClickHandler okClickHandler = new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        presenter.okClicked();
      }
    };
    okButton.addClickHandler(okClickHandler);
    cancelUnsubscribe.addClickHandler(okClickHandler);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void setPresenter(final Presenter presenter) {
    this.presenter = presenter;
    headerWidget.configure();
    headerWidget.refresh();
    Window.scrollTo(0, 0); // scroll user to top of page
  }

  @Override
  public void clear() {
    successUI.setVisible(false);
    confirmUnsubscribeUI.hide();
    okButton.setVisible(true);
    loadingUI.setVisible(false);
  }

  @Override
  public void setSynapseAlert(Widget w) {
    synapseAlertContainer.setWidget(w);
  }

  @Override
  public void showSuccess(String message) {
    successMessage.setText(message);
    successUI.setVisible(true);
  }

  @Override
  public void showConfirmUnsubscribe(SignedTokenInterface signedToken) {
    confirmUnsubscribeUI.show();
    okButton.setVisible(false);
    if (confirmUnsubscribeHandler != null) {
      confirmUnsubscribeHandler.removeHandler();
    }
    confirmUnsubscribeHandler =
      confirmUnsubscribe.addClickHandler(event -> {
        presenter.unsubscribeConfirmed(signedToken);
      });
  }

  @Override
  public void setUnsubscribingUserBadge(Widget w) {
    unsubscribeUserBadgeContainer.setWidget(w);
  }

  @Override
  public void setLoadingVisible(boolean visible) {
    loadingUI.setVisible(visible);
    otherUI.setVisible(!visible);
  }
}
