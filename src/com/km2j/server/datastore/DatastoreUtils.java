package com.km2j.server.datastore;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.km2j.server.external.AnimeBaseObject;
import com.km2j.server.external.CoursObject;
import com.km2j.shared.AnimeInfoBean;

public class DatastoreUtils {
  private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static final AnimeEntityInfo animeEntityInfo = new AnimeEntityInfo();

  private DatastoreUtils() {}

  public static List<Key> putAnimeBaseObjects(final Collection<AnimeBaseObject> baseObjects,
      final Map<String, CoursObject> coursMap) {
    final Collection<Entity> entities = toEntities(baseObjects, coursMap);
    return datastore.put(entities);
  }

  public static PreparedQuery queryAnimeBaseObjects() {
    final Query query = new Query(AnimeEntityInfo.KIND_NAME);
    return datastore.prepare(query);
  }

  public static Collection<AnimeInfoBean> toAnimeInfoBeans(final Iterable<Entity> entities) {
    return CollectionUtils.collect(entities, animeEntityInfo.getEntityToAnimeInfoBeanTransformer());
  }

  public static Collection<Entity> toEntities(final Collection<AnimeBaseObject> animeBaseObjects,
      final Map<String, CoursObject> coursMap) {
    return CollectionUtils.collect(animeBaseObjects,
        animeEntityInfo.getAnimeBaseObjectToEntityTransformer(coursMap));
  }
}
