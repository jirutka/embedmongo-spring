package eu.dndsys.ronhks.embeddedmongo;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.MongoClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class EmbeddedMongoSpringContextTest {

  @Autowired
  private MongoClient mongo;

  @Value("${mongo.databasename}")
  private String dbName;

  @Value("${mongo.collection.1}")
  private String collectionName1;

  @Value("${mongo.collection.2}")
  private String collectionName2;

  @Value("${mongo.collection.3}")
  private String collectionName3;

  @After
  public void afterTests() {
    dropCollections();
  }

  @Test
  public void test_collections_exits() {
    assertTrue(collectionExits(collectionName1));
    assertTrue(collectionExits(collectionName2));
    assertTrue(collectionExits(collectionName3));
  }

  private boolean collectionExits(String collectionName) {
    return mongo.getDB(dbName).getCollection(collectionName) != null;
  }


  private void dropCollections() {
    mongo.getDB(dbName).dropDatabase();
  }
}
