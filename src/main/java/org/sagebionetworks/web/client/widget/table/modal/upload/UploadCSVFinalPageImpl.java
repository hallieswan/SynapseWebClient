package org.sagebionetworks.web.client.widget.table.modal.upload;

import java.util.ArrayList;
import java.util.List;

import org.sagebionetworks.repo.model.asynch.AsynchronousResponseBody;
import org.sagebionetworks.repo.model.table.ColumnModel;
import org.sagebionetworks.repo.model.table.TableEntity;
import org.sagebionetworks.repo.model.table.UploadToTableRequest;
import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.SynapseClientAsync;
import org.sagebionetworks.web.client.widget.asynch.AsynchronousProgressHandler;
import org.sagebionetworks.web.client.widget.asynch.JobTrackingWidget;
import org.sagebionetworks.web.client.widget.table.KeyboardNavigationHandler;
import org.sagebionetworks.web.client.widget.table.v2.schema.ColumnModelTableRow;
import org.sagebionetworks.web.client.widget.table.v2.schema.ColumnModelTableRowEditor;
import org.sagebionetworks.web.client.widget.table.v2.schema.ColumnModelUtils;
import org.sagebionetworks.web.shared.asynch.AsynchType;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class UploadCSVFinalPageImpl implements UploadCSVFinalPage {
	
	public static final double COLUMN_SIZE_BUFFER = 0.25;
	public static final String APPLYING_CSV_TO_THE_TABLE = "Applying CSV to the Table...";
	public static final String CREATING_TABLE_COLUMNS = "Creating table columns...";
	public static final String CREATING_THE_TABLE = "Creating the table...";

	UploadCSVFinalPageView view;
	SynapseClientAsync synapseClient;
	PortalGinInjector portalGinInjector;
	JobTrackingWidget jobTrackingWidget;
	KeyboardNavigationHandler keyboardNavigationHandler;
	
	String parentId;
	UploadToTableRequest uploadtoTableRequest;
	ModalPresenter presenter;
	List<ColumnModelTableRow> editors;
	
	@Inject
	public UploadCSVFinalPageImpl(UploadCSVFinalPageView view,
			SynapseClientAsync synapseClient,
			PortalGinInjector portalGinInjector,
			JobTrackingWidget jobTrackingWidget,
			KeyboardNavigationHandler keyboardNavigationHandler) {
		super();
		this.view = view;
		this.synapseClient = synapseClient;
		this.portalGinInjector = portalGinInjector;
		this.jobTrackingWidget = jobTrackingWidget;
		this.keyboardNavigationHandler = keyboardNavigationHandler;
	}

	@Override
	public void onPrimary() {
		createColumns();
	}

	@Override
	public void setModalPresenter(ModalPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}

	@Override
	public void configure(String fileName, String parentId,
			UploadToTableRequest request, List<ColumnModel> suggestedSchema) {
		view.setTableName(fileName);
		this.parentId = parentId;
		this.uploadtoTableRequest = request;
		this.keyboardNavigationHandler.removeAllRows();
		// prepare the columns
		List<ColumnModel> columns = preProcessColumns(suggestedSchema);
		editors = new ArrayList<ColumnModelTableRow>(columns.size());
		for(ColumnModel cm: columns){
			ColumnModelTableRowEditor editor = portalGinInjector.createNewColumnModelTableRowEditor();
			ColumnModelUtils.applyColumnModelToRow(cm, editor);
			editors.add(editor);
			this.keyboardNavigationHandler.bindRow(editor);
		}
		view.setColumnEditor(editors);
	}
	
	private void createColumns() {
		try{
			presenter.setLoading(true);
			List<ColumnModel> value = ColumnModelUtils.extractColumnModels(editors);
			// Create the columns
			synapseClient.createTableColumns(value, new AsyncCallback<List<ColumnModel>>(){

				@Override
				public void onFailure(Throwable caught) {
					presenter.setErrorMessage(caught.getMessage());
				}

				@Override
				public void onSuccess(List<ColumnModel> schema) {
					createTable(schema);
				}} );
		}catch(IllegalArgumentException e){
			presenter.setErrorMessage(e.getMessage());
		}
	}
	
	public void createTable(List<ColumnModel> schema){
		// Get the column model ids.
		List<String> columnIds = new ArrayList<String>(schema.size());
		for(ColumnModel cm: schema){
			columnIds.add(cm.getId());
		}
		TableEntity table = new TableEntity();
		table.setColumnIds(columnIds);
		table.setParentId(this.parentId);
		table.setName(this.view.getTableName());
		// Create the table
		synapseClient.createTableEntity(table, new AsyncCallback<TableEntity>() {
			
			@Override
			public void onSuccess(TableEntity result) {
				applyCSVToTable(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				presenter.setErrorMessage(caught.getMessage());
			}
		});
	}
	
	/**
	 * Apply the CSV to the table.
	 * @param table
	 */
	public void applyCSVToTable(final TableEntity table){
		// Get the preview request.
		this.uploadtoTableRequest.setTableId(table.getId());
		this.view.setTrackerVisible(true);
		jobTrackingWidget.startAndTrackJob(APPLYING_CSV_TO_THE_TABLE, false, AsynchType.TableCSVUpload, this.uploadtoTableRequest, new AsynchronousProgressHandler(){

			@Override
			public void onCancel() {
				presenter.onCancel();
			}

			@Override
			public void onComplete(AsynchronousResponseBody response) {
				// At this point the table should be created with CSV applied.
				presenter.onTableCreated(table);
			}

			@Override
			public void onFailure(Throwable failure) {
				presenter.setErrorMessage(failure.getMessage());
			}});
	}

	/**
	 * Pre-process the passed columns.  Returns a cloned list of ColumnModels, each modified as needed.
	 * @param adapter
	 * @param columns
	 * @return
	 */
	public List<ColumnModel> preProcessColumns(List<ColumnModel> columns) {
		for(ColumnModel cm: columns){
			if(cm.getMaximumSize() != null){
				// Add a buffer to the max size
				double startingMax = cm.getMaximumSize();
				cm.setMaximumSize((long)(startingMax+(startingMax*COLUMN_SIZE_BUFFER)));
			}
		}
		return columns;
	}
}
