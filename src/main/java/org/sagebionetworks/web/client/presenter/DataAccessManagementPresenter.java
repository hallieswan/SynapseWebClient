package org.sagebionetworks.web.client.presenter;

import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.place.DataAccessManagementPlace;
import org.sagebionetworks.web.client.view.DataAccessManagementView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class DataAccessManagementPresenter extends AbstractActivity implements Presenter<DataAccessManagementPlace> {

    private GlobalApplicationState globalApplicationState;
    private DataAccessManagementView view;

    @Inject
    public DataAccessManagementPresenter(DataAccessManagementView view, GlobalApplicationState globalApplicationState) {
        this.view = view;
        this.globalApplicationState = globalApplicationState;
        this.view.setPresenter(this);

    }

    @Override
    public void setPlace(DataAccessManagementPlace place) {this.view.setPresenter(this);}

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        this.view.render();
        panel.setWidget(view.asWidget());
    }

}
