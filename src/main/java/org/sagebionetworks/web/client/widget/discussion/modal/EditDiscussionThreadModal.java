package org.sagebionetworks.web.client.widget.discussion.modal;

import org.sagebionetworks.repo.model.discussion.DiscussionThreadBundle;
import org.sagebionetworks.web.client.DiscussionForumClientAsync;
import org.sagebionetworks.web.client.utils.Callback;
import org.sagebionetworks.web.client.validation.ValidationResult;
import org.sagebionetworks.web.client.widget.entity.MarkdownEditorWidget;
import org.sagebionetworks.web.client.widget.entity.controller.SynapseAlert;
import org.sagebionetworks.web.shared.discussion.UpdateThread;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * A simple modal dialog for editing a thread.
 */
public class EditDiscussionThreadModal implements DiscussionThreadModalView.Presenter{

	private static final String EDIT_THREAD_MODAL_TITLE = "Edit Thread";
	private static final String SUCCESS_TITLE = "Thread edited";
	private static final String SUCCESS_MESSAGE = "A thread has been edited.";
	private DiscussionThreadModalView view;
	private DiscussionForumClientAsync discussionForumClient;
	private SynapseAlert synAlert;
	private MarkdownEditorWidget markdownEditor;
	private String threadId;
	private String title;
	private String message;
	Callback editThreadCallback;

	@Inject
	public EditDiscussionThreadModal(
			DiscussionThreadModalView view,
			DiscussionForumClientAsync discussionForumClient,
			SynapseAlert synAlert,
			MarkdownEditorWidget markdownEditor
			) {
		this.view = view;
		this.discussionForumClient = discussionForumClient;
		this.synAlert = synAlert;
		this.markdownEditor = markdownEditor;
		view.setPresenter(this);
		view.setAlert(synAlert.asWidget());
		view.setModalTitle(EDIT_THREAD_MODAL_TITLE);
		view.setMarkdownEditor(markdownEditor.asWidget());
	}

	public void configure(String threadId, String currentTitle, String currentMessage, Callback editThreadCallback) {
		this.threadId = threadId;
		this.editThreadCallback = editThreadCallback;
		this.title = currentTitle;
		this.message = currentMessage;
		markdownEditor.hideUploadRelatedCommands();
	}

	@Override
	public void show() {
		view.clear();
		view.setThreadTitle(title);
		markdownEditor.configure(message);
		view.showDialog();
	}

	@Override
	public void hide() {
		view.hideDialog();
	}

	@Override
	public void onSave() {
		synAlert.clear();
		String threadTitle = view.getThreadTitle();
		String messageMarkdown = markdownEditor.getMarkdown();
		ValidationResult result = new ValidationResult();
		result.requiredField("Title", threadTitle)
				.requiredField("Message", messageMarkdown);
		if (!result.isValid()) {
			synAlert.showError(result.getErrorMessage());
			return;
		}
		view.showSaving();
		UpdateThread updateThread = new UpdateThread();
		updateThread.setTitle(threadTitle);
		updateThread.setMessage(messageMarkdown);
		discussionForumClient.updateThread(threadId, updateThread, new AsyncCallback<DiscussionThreadBundle>(){
			@Override
			public void onFailure(Throwable caught) {
				view.resetButton();
				synAlert.handleException(caught);
			}

			@Override
			public void onSuccess(DiscussionThreadBundle result) {
				view.hideDialog();
				view.showSuccess(SUCCESS_TITLE, SUCCESS_MESSAGE);
				if (editThreadCallback != null) {
					editThreadCallback.invoke();
				}
			}

		});
	}

	@Override
	public Widget asWidget() {
		return view.asWidget();
	}
}
