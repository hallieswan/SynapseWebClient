package org.sagebionetworks.web.client.widget.entity;

import org.gwtbootstrap3.client.ui.html.Span;
import org.sagebionetworks.web.client.IconsImageBundle;
import org.sagebionetworks.web.client.SynapseJSNIUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class EntityMetadataViewImpl extends Composite implements EntityMetadataView {
	
	interface EntityMetadataViewImplUiBinder extends UiBinder<Widget, EntityMetadataViewImpl> {
	}
	
	private static EntityMetadataViewImplUiBinder uiBinder = GWT
			.create(EntityMetadataViewImplUiBinder.class);

	@UiField
	HTMLPanel detailedMetadata;
	@UiField
	HTMLPanel dataUseContainer;
	@UiField
	TextBox idField;
	@UiField
	Span doiPanel;
	@UiField
	Span restrictionPanelV2;
	@UiField
	Span uploadDestinationPanel;
	@UiField
	Span uploadDestinationField;
		
	@UiField(provided = true)
	final IconsImageBundle icons;
	
	@Inject
	public EntityMetadataViewImpl(IconsImageBundle icons, final SynapseJSNIUtils jsniUtils) {
		this.icons = icons;
		initWidget(uiBinder.createAndBindUi(this));
		idField.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				idField.selectAll();
			}
		});
	}

	@Override
	public void setDoiWidget(IsWidget doiWidget) {
		doiPanel.clear();
		doiPanel.add(doiWidget);
	}
	
	@Override
	public void setUploadDestinationPanelVisible(boolean isVisible) {
		uploadDestinationPanel.setVisible(isVisible);
	}
	
	@Override
	public void setUploadDestinationText(String text) {
		uploadDestinationField.setText(text);
	}
	
	@Override
	public void clear() {
		dataUseContainer.setVisible(false);
		uploadDestinationField.setText("");
		uploadDestinationPanel.setVisible(false);
	}
	
	@Override
	public void setDetailedMetadataVisible(boolean visible) {
		detailedMetadata.setVisible(visible);
	}

	@Override
	public void setEntityId(String text) {
		idField.setText(text);
	}

	@Override
	public void setRestrictionPanelVisible(boolean visible) {
		dataUseContainer.setVisible(visible);
	}
	@Override
	public void setRestrictionWidgetV2(IsWidget restrictionWidget) {
		restrictionPanelV2.clear();
		restrictionPanelV2.add(restrictionWidget);
	}

	@Override
	public void setRestrictionWidgetV2Visible(boolean visible) {
		restrictionPanelV2.setVisible(visible);
	}
}
