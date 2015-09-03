package com.km2j.client;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.km2j.shared.AnimeInfoBean;
import com.km2j.shared.CollectionResponse;
import com.km2j.shared.CoursObject;
import com.km2j.shared.GetAnimeInfoRequest;

@RemoteServiceRelativePath("animeInfo")
public interface AnimeInfoService extends RemoteService {
  Collection<CoursObject> getCoursObjects();

  CollectionResponse<AnimeInfoBean> getAnimeInfoBeans(GetAnimeInfoRequest request);

  Collection<AnimeInfoBean> putAnimeInfoBeans(Collection<AnimeInfoBean> beans);

  boolean deleteAnimeInfo(Collection<Long> ids);

  boolean connectExternalAndPutCurrent();
}
