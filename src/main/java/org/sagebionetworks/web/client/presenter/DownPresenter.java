package org.sagebionetworks.web.client.presenter;

import org.sagebionetworks.repo.model.status.StackStatus;
import org.sagebionetworks.web.client.ClientProperties;
import org.sagebionetworks.web.client.GWTWrapper;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.SynapseJavascriptClient;
import org.sagebionetworks.web.client.place.Down;
import org.sagebionetworks.web.client.place.Home;
import org.sagebionetworks.web.client.utils.Callback;
import org.sagebionetworks.web.client.view.DownView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class DownPresenter extends AbstractActivity implements Presenter<Down> {
	public static final int SECOND_MS = 1000;
	//check back every 10s if down.
	public static final int DELAY_MS = 10000;
	public int timeToNextRefresh;
	private DownView view;
	GWTWrapper gwt;
	GlobalApplicationState globalAppState;
	Callback updateTimerCallback;
	SynapseJavascriptClient jsClient;
	boolean isCheckingStatus = false;
	@Inject
	public DownPresenter(
			final DownView view,
			final GWTWrapper gwt,
			GlobalApplicationState globalAppState,
			SynapseJavascriptClient jsClient) {
		this.view = view;
		this.gwt = gwt;
		this.globalAppState = globalAppState;
		this.jsClient = jsClient;
		updateTimerCallback = new Callback() {
			@Override
			public void invoke() {
				if (!isCheckingStatus && view.isAttached()) {
					timeToNextRefresh -= SECOND_MS;
					view.updateTimeToNextRefresh(timeToNextRefresh/1000);
					if (timeToNextRefresh <= 1) {
						checkForRepoDown();
					} else {
						view.setTimerVisible(true);
					}
				}
			}
		};
		gwt.scheduleFixedDelay(updateTimerCallback, SECOND_MS);
	}
	
	public void checkForRepoDown() {
		isCheckingStatus = true;
		view.setTimerVisible(false);
		jsClient.getStackStatus(new AsyncCallback<StackStatus>() {
			@Override
			public void onSuccess(StackStatus status) {
				switch(status.getStatus()) {
					case READ_WRITE :
						//it's up!
						repoIsUp();
						break;
					case READ_ONLY :
					case DOWN :
						//it's down, report the message and check again later
						view.setMessage(status.getCurrentMessage());
				}
				reset();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				view.setMessage(caught.getMessage());
				reset();
			}
			
			private void repoIsUp() {
				// note: if last place is not set then it will go to a default place.
				globalAppState.gotoLastPlace();
			}
			
			private void reset() {
				isCheckingStatus = false;
				timeToNextRefresh = DELAY_MS;
			}
		});
	}
	 
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(this.view.asWidget());
	}
	
	@Override
	public void setPlace(Down place) {
		view.init();
		timeToNextRefresh = DELAY_MS;
		checkForRepoDown();
	}
}
