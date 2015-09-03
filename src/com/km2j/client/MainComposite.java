package com.km2j.client;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.km2j.shared.AnimeInfoBean;
import com.km2j.shared.CollectionResponse;
import com.km2j.shared.CoursObject;

public class MainComposite extends Composite {

  private static MainCompositeUiBinder uiBinder = GWT.create(MainCompositeUiBinder.class);
  @UiField
  VerticalPanel verticalPanel;

  interface MainCompositeUiBinder extends UiBinder<Widget, MainComposite> {
  }

  public MainComposite() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public boolean addCoursObjects(final Collection<CoursObject> coursObjects) {
    for (final CoursObject coursObject : coursObjects) {
      verticalPanel.add(new Label(coursObject.toString()));
    }
    return true;
  }

  public boolean addAnimeInfos(final CollectionResponse<AnimeInfoBean> animeInfoResponse) {
    final Collection<AnimeInfoBean> beans = animeInfoResponse.getItems();
    for (final AnimeInfoBean bean : beans) {
      verticalPanel.add(new Label(bean.getTitle()));
    }
    return true;
  }

}
