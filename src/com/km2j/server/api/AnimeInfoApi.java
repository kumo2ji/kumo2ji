package com.km2j.server.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.appengine.api.datastore.PreparedQuery;
import com.km2j.server.datastore.AnimeEntityInfo;
import com.km2j.server.datastore.DatastoreUtils;
import com.km2j.server.external.AnimeBaseObject;
import com.km2j.server.external.CoursObject;
import com.km2j.server.external.ExternalAnimeInfoUtils;
import com.km2j.shared.AnimeInfoBean;
import com.km2j.shared.CoursBean;

@Api
public class AnimeInfoApi {
  private static final AnimeEntityInfo animeEntityInfo = new AnimeEntityInfo();

  @ApiMethod
  public void storeFromExternalAnimeInfo() throws InternalServerErrorException {
    try {
      final List<AnimeBaseObject> list = new ArrayList<AnimeBaseObject>();
      final Map<String, CoursObject> coursMap = ExternalAnimeInfoUtils.requestCoursObjectMap();
      for (final CoursObject coursObject : coursMap.values()) {
        final String year = String.valueOf(coursObject.getYear());
        final String cours = String.valueOf(coursObject.getCours());
        list.addAll(ExternalAnimeInfoUtils.requestAnimeBaseObjects(year, cours));
      }
      DatastoreUtils.putAnimeBaseObjects(list, coursMap);
    } catch (final IOException e) {
      throw new InternalServerErrorException(e);
    }
  }

  @ApiMethod
  public void updateCurrentAnimeInfo() throws InternalServerErrorException {
    try {
      final Map<String, CoursObject> coursMap = ExternalAnimeInfoUtils.requestCoursObjectMap();
      final Collection<CoursObject> coursObjects = coursMap.values();
      final CoursObject current = Collections.max(coursObjects, new Comparator<CoursObject>() {
        @Override
        public int compare(final CoursObject o1, final CoursObject o2) {
          return (int) (o1.getId() - o2.getId());
        }
      });
      final String year = String.valueOf(current.getYear());
      final String cours = String.valueOf(current.getCours());
      DatastoreUtils.putAnimeBaseObjects(
          ExternalAnimeInfoUtils.requestAnimeBaseObjects(year, cours), coursMap);
    } catch (final IOException e) {
      throw new InternalServerErrorException(e);
    }
  }

  @ApiMethod(path = "all")
  public Collection<AnimeInfoBean> getAllAnimeInfoBeans() {
    final PreparedQuery preparedQuery = DatastoreUtils.queryAnimeBaseObjects();
    return CollectionUtils.collect(preparedQuery.asIterable(),
        animeEntityInfo.getEntityToAnimeInfoBeanTransformer());
  }

  @ApiMethod
  public Collection<AnimeInfoBean> getAnimeInfoBeans(final CoursBean coursBean) {
    return null;

  }
}
