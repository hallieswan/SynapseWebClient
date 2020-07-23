package org.sagebionetworks.web.client.widget.table.v2.results.cell;

import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ValidationState;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * View with zero business logic.
 * 
 * @author jhill
 *
 */
public class JSONListCellEditorViewImpl implements JSONListCellEditorView {
	public interface Binder extends UiBinder<Widget, JSONListCellEditorViewImpl> {
	}

	@UiField
	FormGroup formGroup;
	@UiField
	TextBox textBox;
	@UiField
	Icon editButton;

	@UiField
	HelpBlock helpBlock;

	EditJSONListModal editJSONListModal;

	Widget widget;
	Presenter presenter;

	@Inject
	public JSONListCellEditorViewImpl(Binder binder) {
		widget = binder.createAndBindUi(this);
		// users want us to select all on focus see SWC-2213
		textBox.addFocusHandler(event -> {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					textBox.selectAll();
				}
			});
		});
		textBox.addClickHandler(clickEvent -> presenter.onEditButtonClick() );
		editButton.addClickHandler(clickEvent -> presenter.onEditButtonClick());
	}

	@Override
	public Widget asWidget() {
		return formGroup;
	}

	@Override
	public void setValue(String value) {
		textBox.setValue(value);
	}

	@Override
	public String getValue() {
		return textBox.getValue();
	}

	@Override
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return textBox.addKeyDownHandler(handler);
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		textBox.fireEvent(event);
	}

	@Override
	public int getTabIndex() {
		return textBox.getTabIndex();
	}

	@Override
	public void setAccessKey(char key) {
		textBox.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		textBox.setFocus(focused);
	}

	@Override
	public void setTabIndex(int index) {
		textBox.setTabIndex(index);
	}

	@Override
	public void setValidationState(ValidationState state) {
		this.formGroup.setValidationState(state);
	}

	@Override
	public void setHelpText(String help) {
		helpBlock.setVisible(help != null && !help.trim().isEmpty());
		this.helpBlock.setText(help);
	}

	@Override
	public void setPlaceholder(String placeholder) {
		textBox.setPlaceholder(placeholder);
	}

	@Override
	public String getPlaceholder() {
		return textBox.getPlaceholder();
	}


	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}