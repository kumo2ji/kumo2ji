package com.km2j.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.km2j.shared.AnimeInfoBean;
import com.km2j.shared.CollectionResponse;
import com.km2j.shared.CoursObject;
import com.km2j.shared.GetAnimeInfoRequest;

public interface AnimeInfoServiceAsync {

  void getCoursObjects(AsyncCallback<Collection<CoursObject>> callback);

  void connectExternalAndPutCurrent(AsyncCallback<Boolean> callback);

  void getAnimeInfoBeans(GetAnimeInfoRequest request,
      AsyncCallback<CollectionResponse<AnimeInfoBean>> callback);

  void putAnimeInfoBeans(Collection<AnimeInfoBean> beans,
      AsyncCallback<Collection<AnimeInfoBean>> callback);

  void deleteAnimeInfo(Collection<Long> ids, AsyncCallback<Boolean> callback);

}
