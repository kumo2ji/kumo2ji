package com.km2j.server;

import java.util.Collection;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.Environment;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.km2j.client.AnimeInfoService;
import com.km2j.server.api.AnimeInfoApi;
import com.km2j.server.api.IdRequest;
import com.km2j.server.api.PostAnimeInfoRequest;
import com.km2j.shared.AnimeInfoBean;
import com.km2j.shared.CollectionResponse;
import com.km2j.shared.CoursObject;
import com.km2j.shared.GetAnimeInfoRequest;


public class AnimeInfoServiceImpl extends RemoteServiceServlet implements AnimeInfoService {
  private static final long serialVersionUID = 1L;
  private Environment environment;
  private static final AnimeInfoApi api = new AnimeInfoApi();

  private void setUpForTest() {
    if (ApiProxy.getCurrentEnvironment() == null) {
      if (environment == null) {
        final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
        helper.setUp();
        environment = ApiProxy.getCurrentEnvironment();
      } else {
        ApiProxy.setEnvironmentForCurrentThread(environment);
      }
    }
  }

  @Override
  public Collection<CoursObject> getCoursObjects() {
    setUpForTest();
    return api.getCoursObjects();
  }

  @Override
  public boolean connectExternalAndPutCurrent() {
    setUpForTest();
    try {
      return api.connectExternalAndPutCurrent().getValue();
    } catch (final InternalServerErrorException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public CollectionResponse<AnimeInfoBean> getAnimeInfoBeans(final GetAnimeInfoRequest request) {
    setUpForTest();
    final com.google.api.server.spi.response.CollectionResponse<AnimeInfoBean> response =
        api.getAnimeInfoBeans(request);
    final CollectionResponse<AnimeInfoBean> collection = new CollectionResponse<AnimeInfoBean>();
    collection.setItems(response.getItems());
    collection.setCursor(response.getNextPageToken());
    return collection;
  }

  @Override
  public Collection<AnimeInfoBean> putAnimeInfoBeans(final Collection<AnimeInfoBean> beans) {
    setUpForTest();
    final PostAnimeInfoRequest request = new PostAnimeInfoRequest();
    request.setBeans(beans);
    final com.google.api.server.spi.response.CollectionResponse<AnimeInfoBean> response =
        api.putAnimeInfoBeans(request);
    return response.getItems();
  }

  @Override
  public boolean deleteAnimeInfo(final Collection<Long> ids) {
    setUpForTest();
    final IdRequest request = new IdRequest();
    request.setIds(ids);
    return api.deleteAnimeInfo(request).getValue();
  }
}
