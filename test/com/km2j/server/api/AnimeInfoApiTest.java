package com.km2j.server.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.km2j.server.datastore.AnimeEntityInfo;
import com.km2j.server.datastore.DatastoreUtils;
import com.km2j.server.external.CoursObject;
import com.km2j.server.external.ExternalAnimeInfoUtils;
import com.km2j.shared.AnimeInfoBean;

public class AnimeInfoApiTest {
  private static final Logger logger = Logger.getLogger(AnimeInfoApiTest.class.getName());
  private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static final AnimeInfoApi api = new AnimeInfoApi();
  private static final AnimeEntityInfo animeEntityInfo = new AnimeEntityInfo();

  private static final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  @Before
  public void setUp() throws Exception {
    helper.setUp();
  }

  @After
  public void tearDown() throws Exception {
    helper.tearDown();
  }

  @Test
  public void testStoreFromExternalAnimeInfo() {
    try {
      api.storeFromExternalAnimeInfo();
    } catch (final InternalServerErrorException e) {
      fail(e.getMessage());
    }
    final PreparedQuery pQuery = DatastoreUtils.queryAnimeBaseObjects();
    assertTrue(pQuery.countEntities(FetchOptions.Builder.withDefaults()) > 0);
    for (final Entity entity : pQuery.asIterable()) {
      final Transformer<Entity, AnimeInfoBean> transformer =
          animeEntityInfo.getEntityToAnimeInfoBeanTransformer();
      final AnimeInfoBean bean = transformer.transform(entity);
      assertTrue(2000 < bean.getYear() && bean.getYear() < 3000);
      assertTrue(0 < bean.getCours() && bean.getCours() < 5);
      assertThat(bean.getId(), is(entity.getKey().getId()));
      try {
        final URL url = new URL(bean.getPublicUrl());
        final InputStream stream = url.openStream();
        stream.close();
      } catch (final IOException e) {
        logger.info(e.getMessage());
      }
      assertTrue(StringUtils.isNotEmpty(bean.getTitle()));
      assertTrue(StringUtils.isNotEmpty(bean.getTwitterAccount()));
      assertTrue(StringUtils.isNotEmpty(bean.getTwitterHashTag()));
    }
  }

  @Test
  public void testStoreCurrentAnimeInfo() {
    try {
      api.updateCurrentAnimeInfo();
      final Map<String, CoursObject> map = ExternalAnimeInfoUtils.requestCoursObjectMap();
      final CoursObject current = Collections.max(map.values(), new Comparator<CoursObject>() {
        @Override
        public int compare(final CoursObject o1, final CoursObject o2) {
          return (int) (o1.getId() - o2.getId());
        }
      });
      final PreparedQuery pQuery = DatastoreUtils.queryAnimeBaseObjects();
      assertThat(pQuery.countEntities(FetchOptions.Builder.withDefaults()), not(0));
      for (final Entity iterable_element : pQuery.asIterable()) {

      }
    } catch (final InternalServerErrorException | IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetAllAnimeInfoBeans() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetAnimeInfoBeans() {
    fail("Not yet implemented");
  }

}
