package com.km2j.server.datastore;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.Transformer;

import com.google.appengine.api.datastore.Entity;
import com.km2j.server.external.AnimeBaseObject;
import com.km2j.shared.AnimeInfoBean;

public class AnimeEntityInfo {
  public static final String KIND_NAME = "AnimeBaseObject";
  public static final String TITLE_PROPERTY_NAME = "title";
  public static final String SHORT_TITLES_PROPERTY_NAME = "shortTitles";
  public static final String PUBLIC_URL_PROPERTY_NAME = "publicUrl";
  public static final String TWITTER_ACCOUNT_PROPERTY_NAME = "twitterAccount";
  public static final String TWITTER_HASH_TAG_PROPERTY_NAME = "twitterHashTag";
  public static final String COURS_ID_PROPERTY_NAME = "coursId";
  public static final String SEQUAL_PROPERTY_NAME = "sequel";
  public static final String SEX_PROPERTY_NAME = "sex";

  public Transformer<AnimeBaseObject, Entity> getAnimeBaseObjectToEntityTransformer() {
    return new Transformer<AnimeBaseObject, Entity>() {
      @Override
      public Entity transform(final AnimeBaseObject input) {
        final Entity entity = new Entity(KIND_NAME);
        entity.setProperty(TITLE_PROPERTY_NAME, input.getTitle());
        entity.setProperty(COURS_ID_PROPERTY_NAME, input.getCours_id());
        entity.setProperty(PUBLIC_URL_PROPERTY_NAME, input.getPublic_url());
        entity.setProperty(SEQUAL_PROPERTY_NAME, input.getSequel());
        entity.setProperty(SEX_PROPERTY_NAME, input.getSex());
        final List<String> shortTitles = Arrays.asList(input.getTitle_short1(),
            input.getTitle_short2(), input.getTitle_short3());
        entity.setProperty(SHORT_TITLES_PROPERTY_NAME, shortTitles);
        entity.setProperty(TWITTER_ACCOUNT_PROPERTY_NAME, input.getTwitter_account());
        entity.setProperty(TWITTER_HASH_TAG_PROPERTY_NAME, input.getTwitter_hash_tag());
        return entity;
      }
    };
  }

  public Transformer<Entity, AnimeInfoBean> getEntityToAnimeInfoBean() {
    return new Transformer<Entity, AnimeInfoBean>() {
      @SuppressWarnings("unchecked")
      @Override
      public AnimeInfoBean transform(final Entity input) {
        final AnimeInfoBean bean = new AnimeInfoBean();
        bean.setId(input.getKey().getId());
        bean.setTitle((String) input.getProperty(TITLE_PROPERTY_NAME));
        bean.setShortTitles((Collection<String>) input.getProperty(SHORT_TITLES_PROPERTY_NAME));
        bean.setPublicUrl((String) input.getProperty(PUBLIC_URL_PROPERTY_NAME));
        bean.setTwitterAccount((String) input.getProperty(TWITTER_ACCOUNT_PROPERTY_NAME));
        bean.setTwitterHashTag((String) input.getProperty(TWITTER_HASH_TAG_PROPERTY_NAME));
        bean.setCoursId((long) input.getProperty(COURS_ID_PROPERTY_NAME));
        bean.setSequel((long) input.getProperty(SEQUAL_PROPERTY_NAME));
        bean.setSex((long) input.getProperty(SEX_PROPERTY_NAME));
        return bean;
      }
    };
  }
}
