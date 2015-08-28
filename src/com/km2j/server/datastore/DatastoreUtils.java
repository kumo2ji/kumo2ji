package com.km2j.server.datastore;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.km2j.server.external.AnimeBaseObject;
import com.km2j.shared.CoursObject;

public class DatastoreUtils {
  private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private static final AnimeEntityInfo animeEntityInfo = new AnimeEntityInfo();
  private static final CoursEntityInfo coursEntityInfo = new CoursEntityInfo();

  private DatastoreUtils() {}

  public static void deleteAnimeInfo() {
    delete(AnimeEntityInfo.KIND_NAME);
  }

  public static void deleteCoursObject() {
    delete(CoursEntityInfo.KIND_NAME);
  }

  private static void delete(final String kindName) {
    final Query query = new Query(kindName);
    query.setKeysOnly();
    final PreparedQuery pQuery = datastore.prepare(query);
    final Collection<Key> keys =
        CollectionUtils.collect(pQuery.asIterable(), new Transformer<Entity, Key>() {
          @Override
          public Key transform(final Entity arg0) {
            return arg0.getKey();
          }
        });
    datastore.delete(keys);
  }

  public static List<Key> putAnimeBaseObjects(final Collection<AnimeBaseObject> baseObjects,
      final Map<String, CoursObject> coursMap) {
    final Collection<Entity> entities = toEntitiesFromAnimeBaseObjects(baseObjects);
    return datastore.put(entities);
  }

  public static List<Key> putCoursObjects(final Collection<CoursObject> coursObjects) {
    final Collection<Entity> entities = toEntitiesFromCoursObjects(coursObjects);
    return datastore.put(entities);
  }

  public static PreparedQuery queryAnimeBaseObjects() {
    final Query query = new Query(AnimeEntityInfo.KIND_NAME);
    return datastore.prepare(query);
  }

  public static PreparedQuery queryAnimeBaseObjectsWithId(final long id) {
    final Query query = new Query(AnimeEntityInfo.KIND_NAME);
    query.setFilter(createFilterWithId(id));
    return datastore.prepare(query);
  }

  public static PreparedQuery queryAnimeBaseObjects(final long year) {
    final Query query = new Query(AnimeEntityInfo.KIND_NAME);
    query.setFilter(createFilter(year));
    return datastore.prepare(query);
  }

  public static PreparedQuery queryAnimeBaseObjects(final long year, final long cours) {
    final Query query = new Query(AnimeEntityInfo.KIND_NAME);
    query.setFilter(createFilter(year, cours));
    return datastore.prepare(query);
  }

  private static Filter createFilterWithId(final long id) {
    final Key key = KeyFactory.createKey(CoursEntityInfo.KIND_NAME, id);
    return new FilterPredicate(AnimeEntityInfo.COURS_KEY_PROPERTY_NAME, FilterOperator.EQUAL, key);
  }

  private static Filter createFilter(final long year) {
    final Query coursQuery = queryCoursObjectForYear(year);
    return createFilter(coursQuery);
  }

  private static Filter createFilter(final long year, final long cours) {
    final Query coursQuery = queryCoursObjectForYear(year);
    final Query coursQueryForCours = queryCoursObjectForCours(cours);
    final Filter coursFilter =
        CompositeFilterOperator.and(coursQuery.getFilter(), coursQueryForCours.getFilter());
    coursQuery.setFilter(coursFilter);
    return createFilter(coursQuery);
  }

  private static Filter createFilter(final Query coursQuery) {
    coursQuery.setKeysOnly();
    final PreparedQuery coursPQuery = datastore.prepare(coursQuery);
    final Collection<Filter> filters =
        CollectionUtils.collect(coursPQuery.asIterable(), new Transformer<Entity, Filter>() {
          @Override
          public Filter transform(final Entity arg0) {
            return new FilterPredicate(AnimeEntityInfo.COURS_KEY_PROPERTY_NAME,
                FilterOperator.EQUAL, arg0.getKey());
          }
        });
    if (filters.size() == 1) {
      return filters.iterator().next();
    } else {
      return CompositeFilterOperator.or(filters);
    }
  }

  public static Entity queryCoursObject(final Key key) throws EntityNotFoundException {
    return datastore.get(key);
  }

  public static PreparedQuery queryCoursObject() {
    final Query query = new Query(CoursEntityInfo.KIND_NAME);
    return datastore.prepare(query);
  }

  private static Query queryCoursObjectForYear(final long year) {
    final Query query = new Query(CoursEntityInfo.KIND_NAME);
    final Filter filter =
        new FilterPredicate(CoursEntityInfo.YEAR_PROPERTY_NAME, FilterOperator.EQUAL, year);
    query.setFilter(filter);
    return query;
  }

  private static Query queryCoursObjectForCours(final long cours) {
    final Query query = new Query(CoursEntityInfo.KIND_NAME);
    final Filter filter =
        new FilterPredicate(CoursEntityInfo.COURS_PROPERTY_NAME, FilterOperator.EQUAL, cours);
    query.setFilter(filter);
    return query;
  }

  private static Collection<Entity> toEntitiesFromAnimeBaseObjects(
      final Collection<AnimeBaseObject> animeBaseObjects) {
    return CollectionUtils.collect(animeBaseObjects,
        animeEntityInfo.getAnimeBaseObjectToEntityTransformer());
  }

  private static Collection<Entity> toEntitiesFromCoursObjects(
      final Collection<CoursObject> coursObjects) {
    return CollectionUtils.collect(coursObjects,
        coursEntityInfo.getCoursObjectToEntityTransformer());
  }
}
