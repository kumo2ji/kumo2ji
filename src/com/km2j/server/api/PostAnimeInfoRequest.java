package com.km2j.server.api;

import java.io.Serializable;
import java.util.Collection;

import com.km2j.shared.AnimeInfoBean;

public class PostAnimeInfoRequest implements Serializable {
  private static final long serialVersionUID = 1L;
  private Collection<AnimeInfoBean> beans;

  public Collection<AnimeInfoBean> getBeans() {
    return beans;
  }

  public void setBeans(final Collection<AnimeInfoBean> beans) {
    this.beans = beans;
  }
}
