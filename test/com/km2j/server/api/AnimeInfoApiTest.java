package com.km2j.server.api;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.km2j.server.datastore.AnimeEntityInfo;
import com.km2j.server.datastore.DatastoreUtils;
import com.km2j.server.external.ExternalAnimeInfoUtils;
import com.km2j.shared.AnimeInfoBean;
import com.km2j.shared.CoursObject;

public class AnimeInfoApiTest {
  // private static final Logger logger = Logger.getLogger(AnimeInfoApiTest.class.getName());
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
  public void testDeleteAnimeInfo() {
    try {
      final BooleanResponse connectResponse = api.connectExternalAndPutCurrent();
      assertTrue(connectResponse.getValue());
      assertTrue(
          CollectionUtils.isNotEmpty(api.getAnimeInfoBeans(new AnimeInfoRequestBean()).getItems()));
      final BooleanResponse deleteResponse = api.deleteAnimeInfo();
      assertTrue(deleteResponse.getValue());
      assertTrue(
          CollectionUtils.isEmpty(api.getAnimeInfoBeans(new AnimeInfoRequestBean()).getItems()));
    } catch (final InternalServerErrorException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testDeleteCoursObject() {
    try {
      final BooleanResponse connectResponse = api.connectExternalAndPutCurrent();
      assertTrue(connectResponse.getValue());
      assertTrue(CollectionUtils.isNotEmpty(api.getCoursObjects()));
      final BooleanResponse deleteResponse = api.deleteCoursObject();
      assertTrue(deleteResponse.getValue());
      assertTrue(CollectionUtils.isEmpty(api.getCoursObjects()));
    } catch (final InternalServerErrorException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testStoreFromExternalAnimeInfo() {
    try {
      api.connectExternalAndPutAll();
    } catch (final InternalServerErrorException e) {
      fail(e.getMessage());
    }
    final PreparedQuery pQuery = DatastoreUtils.queryAnimeBaseObjects();
    assertTrue(pQuery.countEntities(FetchOptions.Builder.withDefaults()) > 0);
    for (final Entity entity : pQuery.asIterable()) {
      final Transformer<Entity, AnimeInfoBean> transformer =
          animeEntityInfo.getEntityToAnimeInfoBeanTransformer();
      final AnimeInfoBean bean = transformer.transform(entity);
      final CoursObject coursObject = bean.getCoursObject();
      assertTrue(2000 < coursObject.getYear() && coursObject.getYear() < 3000);
      assertTrue(0 < coursObject.getCours() && coursObject.getCours() < 5);
      assertThat(bean.getId(), is(entity.getKey().getId()));
      assertTrue(StringUtils.isNotEmpty(bean.getTitle()));
      assertTrue(StringUtils.isNotEmpty(bean.getTwitterAccount()));
      assertTrue(StringUtils.isNotEmpty(bean.getTwitterHashTag()));
      for (final String shortTitle : bean.getShortTitles()) {
        assertTrue(StringUtils.isNotEmpty(shortTitle));
      }
    }
  }

  @Test
  public void testStoreCurrentAnimeInfo() {
    try {
      api.connectExternalAndPutCurrent();
      final Map<String, CoursObject> map = ExternalAnimeInfoUtils.requestCoursObjectMap();
      final CoursObject current = Collections.max(map.values(), new Comparator<CoursObject>() {
        @Override
        public int compare(final CoursObject o1, final CoursObject o2) {
          return (int) (o1.getId() - o2.getId());
        }
      });
      final PreparedQuery pQuery = DatastoreUtils.queryAnimeBaseObjects();
      assertThat(pQuery.countEntities(FetchOptions.Builder.withDefaults()), not(0));
      final Transformer<Entity, AnimeInfoBean> transformer =
          animeEntityInfo.getEntityToAnimeInfoBeanTransformer();
      for (final Entity entity : pQuery.asIterable()) {
        final AnimeInfoBean bean = transformer.transform(entity);
        final CoursObject coursObject = bean.getCoursObject();
        assertThat(coursObject, is(current));
        assertTrue(StringUtils.isNotEmpty(bean.getTitle()));
        assertTrue(StringUtils.isNotEmpty(bean.getTwitterAccount()));
        assertTrue(StringUtils.isNotEmpty(bean.getTwitterHashTag()));
        for (final String shortTitle : bean.getShortTitles()) {
          assertTrue(StringUtils.isNotEmpty(shortTitle));
        }
      }
    } catch (final InternalServerErrorException | IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetAllAnimeInfoBeans() {
    try {
      api.connectExternalAndPutCurrent();
    } catch (final InternalServerErrorException e) {
      fail(e.getMessage());
    }
    final Collection<AnimeInfoBean> beans =
        api.getAnimeInfoBeans(new AnimeInfoRequestBean()).getItems();
    assertTrue(CollectionUtils.isNotEmpty(beans));
    for (final AnimeInfoBean bean : beans) {
      assertTrue(StringUtils.isNotEmpty(bean.getTitle()));
      assertTrue(StringUtils.isNotEmpty(bean.getTwitterAccount()));
      assertTrue(StringUtils.isNotEmpty(bean.getTwitterHashTag()));
      final CoursObject coursObject = bean.getCoursObject();
      assertTrue(2000 < coursObject.getYear() && coursObject.getYear() < 3000);
      assertTrue(0 < coursObject.getCours() && coursObject.getCours() < 5);
      for (final String shortTitle : bean.getShortTitles()) {
        assertTrue(StringUtils.isNotEmpty(shortTitle));
      }
    }
  }

  @Test
  public void testGetAnimeInfoBeans() {
    try {
      api.connectExternalAndPutAll();
      final Map<String, CoursObject> coursMap = ExternalAnimeInfoUtils.requestCoursObjectMap();
      for (final CoursObject coursObject : coursMap.values()) {
        final AnimeInfoRequestBean request = new AnimeInfoRequestBean();
        final CoursObject requestCoursObject = new CoursObject();
        requestCoursObject.setYear(coursObject.getYear());
        requestCoursObject.setCours(coursObject.getCours());
        request.setCoursObject(requestCoursObject);
        final Collection<AnimeInfoBean> animeInfoBeans = api.getAnimeInfoBeans(request).getItems();
        assertTrue(CollectionUtils.isNotEmpty(animeInfoBeans));
        for (final AnimeInfoBean bean : animeInfoBeans) {
          assertTrue(StringUtils.isNotEmpty(bean.getTitle()));
          assertTrue(StringUtils.isNotEmpty(bean.getTwitterAccount()));
          assertTrue(StringUtils.isNotEmpty(bean.getTwitterHashTag()));
          final CoursObject storedCoursObject = bean.getCoursObject();
          assertThat(storedCoursObject, is(coursObject));
          for (final String shortTitle : bean.getShortTitles()) {
            assertTrue(StringUtils.isNotEmpty(shortTitle));
          }
        }
      }
    } catch (final InternalServerErrorException | IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetAnimeInfoBeansForYear() {
    try {
      api.connectExternalAndPutAll();
      final Map<String, CoursObject> coursMap = ExternalAnimeInfoUtils.requestCoursObjectMap();
      final Collection<Long> years =
          CollectionUtils.collect(coursMap.values(), new Transformer<CoursObject, Long>() {
            @Override
            public Long transform(final CoursObject arg0) {
              return arg0.getYear();
            }
          });
      final Set<Long> yearSet = new HashSet<Long>(years);
      assertTrue(CollectionUtils.isNotEmpty(yearSet));
      for (final Long year : yearSet) {
        final AnimeInfoRequestBean request = new AnimeInfoRequestBean();
        final CoursObject requestCoursObject = new CoursObject();
        requestCoursObject.setYear(year);
        request.setCoursObject(requestCoursObject);
        final Collection<AnimeInfoBean> animeInfoBeans = api.getAnimeInfoBeans(request).getItems();
        assertTrue(CollectionUtils.isNotEmpty(animeInfoBeans));
        for (final AnimeInfoBean bean : animeInfoBeans) {
          assertTrue(StringUtils.isNotEmpty(bean.getTitle()));
          assertTrue(StringUtils.isNotEmpty(bean.getTwitterAccount()));
          assertTrue(StringUtils.isNotEmpty(bean.getTwitterHashTag()));
          final CoursObject storedCoursObject = bean.getCoursObject();
          assertThat(storedCoursObject.getYear(), is(year));
          assertTrue(0 < storedCoursObject.getCours() && storedCoursObject.getCours() < 5);
          for (final String shortTitle : bean.getShortTitles()) {
            assertTrue(StringUtils.isNotEmpty(shortTitle));
          }
        }
      }
    } catch (final InternalServerErrorException | IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetAnimeInfoBeansWithId() {
    try {
      api.connectExternalAndPutAll();
      final Map<String, CoursObject> coursMap = ExternalAnimeInfoUtils.requestCoursObjectMap();
      for (final CoursObject coursObject : coursMap.values()) {
        final AnimeInfoRequestBean request = new AnimeInfoRequestBean();
        final CoursObject requestCoursObject = new CoursObject();
        requestCoursObject.setId(coursObject.getId());
        request.setCoursObject(requestCoursObject);
        final Collection<AnimeInfoBean> animeInfoBeans = api.getAnimeInfoBeans(request).getItems();
        assertTrue(CollectionUtils.isNotEmpty(animeInfoBeans));
        for (final AnimeInfoBean bean : animeInfoBeans) {
          assertTrue(StringUtils.isNotEmpty(bean.getTitle()));
          assertTrue(StringUtils.isNotEmpty(bean.getTwitterAccount()));
          assertTrue(StringUtils.isNotEmpty(bean.getTwitterHashTag()));
          final CoursObject storedCoursObject = bean.getCoursObject();
          assertThat(storedCoursObject, is(coursObject));
          for (final String shortTitle : bean.getShortTitles()) {
            assertTrue(StringUtils.isNotEmpty(shortTitle));
          }
        }
      }
    } catch (final InternalServerErrorException | IOException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGetCoursObjects() {
    try {
      api.connectExternalAndPutAll();
      final Collection<CoursObject> coursObjects = api.getCoursObjects();
      assertTrue(CollectionUtils.isNotEmpty(coursObjects));
      for (final CoursObject coursObject : coursObjects) {
        assertTrue(0 < coursObject.getId());
        assertTrue(2000 < coursObject.getYear() && coursObject.getYear() < 3000);
        assertTrue(0 < coursObject.getCours() && coursObject.getCours() < 5);
      }
    } catch (final InternalServerErrorException e) {
      fail(e.getMessage());
    }
  }
}
