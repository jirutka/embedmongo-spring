package cz.jirutka.spring.embedmongo;


public class EmbeddedMongoCollection {

  private String dbName;

  private String[] collectionsName;

  public EmbeddedMongoCollection(String dbName, String... collectionsName) {
    super();
    this.dbName = dbName;
    // collectionsName = new String[] { collectionName };
    this.collectionsName = collectionsName;
  }

  public String getDbName() {
    return dbName;
  }

  public String[] getCollectionsName() {
    return collectionsName;
  }
}
