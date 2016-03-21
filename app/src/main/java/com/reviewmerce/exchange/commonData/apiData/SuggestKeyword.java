package com.reviewmerce.exchange.commonData.apiData;


import org.json.JSONException;
import org.json.JSONObject;

public class SuggestKeyword  {
  private int type = 0;
  private String keyword = null;
  private String description = null;

  public SuggestKeyword(SuggestKeyword item)
  {
    setSuggestData(item);
  }
  public SuggestKeyword(String keyword, String description)
  {
    this.keyword = keyword;
    this.description = description;
  }
  public SuggestKeyword(JSONObject json) throws JSONException
  {
    toData(json);
  }

  public void setSuggestData(SuggestKeyword item)
  {
    this.type = item.getType();
    this.keyword = item.getKeyword();
    this.description = item.getDescription();
  }
  /**
   * a 6-character alphanumeric unique identifier assigned by reviewmerce.com (Reviewmerce ID)
   **/
  public String getKeyword() {
    return keyword;
  }
  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  
  /**
   **/
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public int getType()
  {
    return type;
  }
  public void setType(int type)
  {
    this.type = type;

  }
  public JSONObject toJson() throws JSONException
  {
    JSONObject json = new JSONObject();
    json.put("keyword",this.getKeyword());
    json.put("description",this.getDescription());
    return json;
  }
  public void toData(JSONObject json) throws JSONException
  {
    this.keyword = json.getString("keyword");
    this.description = json.getString("description");
  }
  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SuggestKeyword {\n");
    
    sb.append("  keyword: ").append(keyword).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
