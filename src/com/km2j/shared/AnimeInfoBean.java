package com.km2j.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class AnimeInfoBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private long id;
  private String title;
  private Collection<String> shortTitles;
  private String publicUrl;
  private String twitterAccount;
  private String twitterHashTag;
  private long coursId;
  private Date createdAt;
  private Date updatedAt;
  private long sex;
  private long sequel;

  public final long getId() {
    return id;
  }

  public final void setId(final long id) {
    this.id = id;
  }

  public final String getTitle() {
    return title;
  }

  public final void setTitle(final String title) {
    this.title = title;
  }

  public final Collection<String> getShortTitles() {
    return shortTitles;
  }

  public final void setShortTitles(final Collection<String> shortTitles) {
    this.shortTitles = shortTitles;
  }

  public final String getPublicUrl() {
    return publicUrl;
  }

  public final void setPublicUrl(final String publicUrl) {
    this.publicUrl = publicUrl;
  }

  public final String getTwitterAccount() {
    return twitterAccount;
  }

  public final void setTwitterAccount(final String twitterAccount) {
    this.twitterAccount = twitterAccount;
  }

  public final String getTwitterHashTag() {
    return twitterHashTag;
  }

  public final void setTwitterHashTag(final String twitterHashTag) {
    this.twitterHashTag = twitterHashTag;
  }

  public final long getCoursId() {
    return coursId;
  }

  public final void setCoursId(final long coursId) {
    this.coursId = coursId;
  }

  public final Date getCreatedAt() {
    return createdAt;
  }

  public final void setCreatedAt(final Date createdAt) {
    this.createdAt = createdAt;
  }

  public final Date getUpdatedAt() {
    return updatedAt;
  }

  public final void setUpdatedAt(final Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public final long getSex() {
    return sex;
  }

  public final void setSex(final long sex) {
    this.sex = sex;
  }

  public final long getSequel() {
    return sequel;
  }

  public final void setSequel(final long sequel) {
    this.sequel = sequel;
  }

}
