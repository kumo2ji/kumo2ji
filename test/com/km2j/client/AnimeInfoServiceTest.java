package com.km2j.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.km2j.shared.AnimeInfoBean;
import com.km2j.shared.CollectionResponse;
import com.km2j.shared.CoursObject;
import com.km2j.shared.GetAnimeInfoRequest;

public class AnimeInfoServiceTest extends GWTTestCase {
  @Override
  public String getModuleName() {
    return "com.km2j.Kumo2jiTest";
  }

  @Test
  public void testConnectExternalAndPutCurrent() {
    final AnimeInfoServiceAsync service = GWT.create(AnimeInfoService.class);
    delayTestFinish(60 * 1000);
    service.connectExternalAndPutCurrent(new AsyncCallback<Boolean>() {
      @Override
      public void onFailure(final Throwable caught) {
        fail(caught.getMessage());
      }

      @Override
      public void onSuccess(final Boolean result) {
        assertTrue(result);
        service.getCoursObjects(new AsyncCallback<Collection<CoursObject>>() {
          @Override
          public void onSuccess(final Collection<CoursObject> result) {
            assertFalse(result.isEmpty());
            for (final CoursObject coursObject : result) {
              assertTrue(coursObject.getId() > 0);
              assertTrue(2000 < coursObject.getYear() && coursObject.getYear() < 3000);
              assertTrue(coursObject.getCours() > 0);
            }
            finishTest();
          }

          @Override
          public void onFailure(final Throwable caught) {
            fail(caught.getMessage());
          }
        });
      }
    });
  }

  @Test
  public void testGetAnimeInfoBeans() {
    final AnimeInfoServiceAsync service = GWT.create(AnimeInfoService.class);
    delayTestFinish(60 * 1000);
    service.connectExternalAndPutCurrent(new AsyncCallback<Boolean>() {

      @Override
      public void onFailure(final Throwable caught) {
        fail(caught.getMessage());
      }

      @Override
      public void onSuccess(final Boolean result) {
        assertTrue(result);
        final GetAnimeInfoRequest request = new GetAnimeInfoRequest();
        service.getAnimeInfoBeans(request, new AsyncCallback<CollectionResponse<AnimeInfoBean>>() {
          @Override
          public void onFailure(final Throwable caught) {
            fail(caught.getMessage());
          }

          @Override
          public void onSuccess(final CollectionResponse<AnimeInfoBean> result) {
            final Collection<AnimeInfoBean> beans = result.getItems();
            assertFalse(beans.isEmpty());
            for (final AnimeInfoBean bean : beans) {
              assertFalse(bean.getTitle().isEmpty());
              assertTrue(bean.getId() > 0);
              final CoursObject coursObject = bean.getCoursObject();
              assertTrue(coursObject.getId() > 0);
              assertTrue(2000 < coursObject.getYear() && coursObject.getYear() < 3000);
              assertTrue(coursObject.getCours() > 0);
            }
            finishTest();
          }
        });
      }
    });
  }

  @Test
  public void testPutAnimeInfoBeans() {
    final AnimeInfoServiceAsync service = GWT.create(AnimeInfoService.class);
    delayTestFinish(60 * 1000);
    service.connectExternalAndPutCurrent(new AsyncCallback<Boolean>() {
      @Override
      public void onFailure(final Throwable caught) {
        fail(caught.getMessage());
      }

      @Override
      public void onSuccess(final Boolean result) {
        assertTrue(result);
        service.getCoursObjects(new AsyncCallback<Collection<CoursObject>>() {
          @Override
          public void onFailure(final Throwable caught) {
            fail(caught.getMessage());
          }

          @Override
          public void onSuccess(final Collection<CoursObject> result) {
            assertFalse(result.isEmpty());
            final AnimeInfoBean bean = new AnimeInfoBean();
            bean.setTitle("test title");
            final CoursObject coursObject = result.iterator().next();
            bean.setCoursObject(coursObject);
            final List<AnimeInfoBean> beans = Arrays.asList(bean);
            service.putAnimeInfoBeans(beans, new AsyncCallback<Collection<AnimeInfoBean>>() {
              @Override
              public void onFailure(final Throwable caught) {
                fail(caught.getMessage());
              }

              @Override
              public void onSuccess(final Collection<AnimeInfoBean> result) {
                assertFalse(result.isEmpty());
                final AnimeInfoBean resultBean = result.iterator().next();
                assertTrue(resultBean.getTitle().equals(bean.getTitle()));
                assertTrue(resultBean.getId() != 0L);
                assertTrue(bean.getCoursObject().equals(coursObject));
                service.deleteAnimeInfo(Arrays.asList(resultBean.getId()),
                    new AsyncCallback<Boolean>() {

                  @Override
                  public void onFailure(final Throwable caught) {
                    fail(caught.getMessage());
                  }

                  @Override
                  public void onSuccess(final Boolean result) {
                    assertTrue(result);
                    final GetAnimeInfoRequest request = new GetAnimeInfoRequest();
                    service.getAnimeInfoBeans(request,
                        new AsyncCallback<CollectionResponse<AnimeInfoBean>>() {

                      @Override
                      public void onFailure(final Throwable caught) {
                        fail(caught.getMessage());
                      }

                      @Override
                      public void onSuccess(final CollectionResponse<AnimeInfoBean> result) {
                        final Collection<AnimeInfoBean> beans = result.getItems();
                        assertFalse(beans.isEmpty());
                        for (final AnimeInfoBean animeInfoBean : beans) {
                          if (animeInfoBean.getTitle().equals(bean.getTitle())) {
                            fail();
                          }
                        }
                        finishTest();
                      }
                    });
                  }
                });
              }
            });
          }
        });
      }
    });
  }
}
