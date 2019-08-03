/*
 * The MIT License
 *
 * Copyright 2013-2014 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cz.jirutka.spring.embedmongo;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * {@linkplain FactoryBean} for EmbedMongo that runs MongoDB as a managed
 * process and exposes preconfigured instance of {@link MongoClient}.
 *
 * This class is a wrapper for {@link EmbeddedMongoBuilder} from this package.
 *
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class EmbeddedMongoFactoryBean implements FactoryBean<Mongo>, DisposableBean {

  private static final Logger LOG = LoggerFactory.getLogger(EmbeddedMongoFactoryBean.class);

  private final EmbeddedMongoBuilder builder = new EmbeddedMongoBuilder();
  private MongoClient mongoClient;

  @Override
  public MongoClient getObject() throws IOException {
    mongoClient = builder.build();
    return mongoClient;
  }

  @Override
  public Class<MongoClient> getObjectType() {
    return MongoClient.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void destroy() {
    if (mongoClient != null) {
      LOG.info("Stopping embedded MongoDB instance");
      mongoClient.close();
    }
  }

  /**
   * The version of MongoDB to run e.g. 2.1.1, 1.6 v1.8.2, V2_0_4. When no
   * version is provided, then {@link de.flapdoodle.embed.mongo.distribution.Version.Main#PRODUCTION PRODUCTION}
   * is used by default. The value must not be empty.
   */
  public void setVersion(String version) {
    builder.version(version);
  }

  /**
   * The port MongoDB should run on. When no port is provided, then some free
   * server port is automatically assigned. The value must be between 0 and 65535.
   */
  public void setPort(int port) {
    builder.port(port);
  }

  /**
   * An IPv4 or IPv6 address for the MongoDB instance to be bound to during
   * its execution. Default is a {@linkplain java.net.InetAddress#getLoopbackAddress()
   * loopback address}. The value must not be empty.
   */
  public void setBindIp(String bindIp) {
    builder.bindIp(bindIp);
  }

  public void setCollections(EmbeddedMongoCollection... embeddedMongoCollections) {
    builder.embeddedMongoCollections(embeddedMongoCollections);
  }

}
