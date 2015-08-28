package com.km2j.server.datastore;

import org.apache.commons.collections4.Transformer;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.km2j.shared.CoursObject;

public class CoursEntityInfo {
  public static final String KIND_NAME = "CoursObject";
  public static final String YEAR_PROPERTY_NAME = "year";
  public static final String COURS_PROPERTY_NAME = "cours";

  public Transformer<CoursObject, Entity> getCoursObjectToEntityTransformer() {
    return new Transformer<CoursObject, Entity>() {
      @Override
      public Entity transform(final CoursObject arg0) {
        final Key key = createKey(arg0);
        final Entity entity = new Entity(key);
        entity.setProperty(YEAR_PROPERTY_NAME, arg0.getYear());
        entity.setProperty(COURS_PROPERTY_NAME, arg0.getCours());
        return entity;
      }
    };
  }

  public Transformer<Entity, CoursObject> getEntityToCoursObjectTransformer() {
    return new Transformer<Entity, CoursObject>() {
      @Override
      public CoursObject transform(final Entity arg0) {
        final CoursObject coursObject = new CoursObject();
        coursObject.setId(arg0.getKey().getId());
        coursObject.setYear((long) arg0.getProperty(YEAR_PROPERTY_NAME));
        coursObject.setCours((long) arg0.getProperty(COURS_PROPERTY_NAME));
        return coursObject;
      }
    };
  }

  public Key createKey(final CoursObject coursObject) {
    return KeyFactory.createKey(KIND_NAME, coursObject.getId());
  }
}
