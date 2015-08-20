package com.km2j.server.datastore;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.km2j.server.external.AnimeBaseObject;

public class DatastoreUtils {
  private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static final AnimeEntityInfo animeEntityInfo = new AnimeEntityInfo();

  private DatastoreUtils() {}

  public static List<Key> putAnimeBaseObjects(final Collection<AnimeBaseObject> baseObjects) {
    final Collection<Entity> entities = CollectionUtils.collect(baseObjects,
        animeEntityInfo.getAnimeBaseObjectToEntityTransformer());
    return datastore.put(entities);
  }

  public static PreparedQuery queryAnimeBaseObjects() {
    final Query query = new Query(AnimeEntityInfo.KIND_NAME);
    return datastore.prepare(query);
  }
}
