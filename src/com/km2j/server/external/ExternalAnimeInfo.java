package com.km2j.server.external;

import java.io.Reader;
import java.util.Collection;
import java.util.Map;

import com.km2j.server.JsonUtils;
import com.km2j.shared.Func;

public class ExternalAnimeInfo {
  private static final String END_POINT = "http://api.moemoe.tokyo/anime/v1/master/";
  private static final String COURS_OBJECT_URL = END_POINT + "cours";

  public String getCoursObjectUrlString() {
    return COURS_OBJECT_URL;
  }

  public String getBaseObjectUrlString(final String year) {
    return END_POINT + year;
  }

  public String getBaseObjectUrlString(final String year, final String cours) {
    return getBaseObjectUrlString(year) + "/" + cours;
  }

  public Func<Reader, Map<String, CoursObject>> getCoursObjectMapFunc() {
    return JsonUtils.getFuncForMap(CoursObject.class);
  }

  public Func<Reader, Collection<AnimeBaseObject>> getAnimeBaseObjectsFunc() {
    return JsonUtils.getFuncForCollection(AnimeBaseObject.class);
  }
}
