package com.km2j.client;

import java.util.Collection;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.km2j.shared.CoursObject;

public class MainEntryPoint implements EntryPoint {
  private final AnimeInfoServiceAsync service = GWT.create(AnimeInfoService.class);

  @Override
  public void onModuleLoad() {
    final MainComposite main = new MainComposite();
    RootPanel.get().add(main);

    service.getCoursObjects(new AsyncCallback<Collection<CoursObject>>() {
      @Override
      public void onSuccess(final Collection<CoursObject> result) {
        main.addCoursObjects(result);
      }

      @Override
      public void onFailure(final Throwable caught) {}
    });

  }

}
