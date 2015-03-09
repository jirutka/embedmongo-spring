Spring Factory Bean for EmbedMongo
==================================
[![Build Status](https://img.shields.io/travis/jirutka/embedmongo-spring/master.svg?style=flat)](https://travis-ci.org/jirutka/embedmongo-spring)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.spring/embedmongo-spring/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.spring/embedmongo-spring)

[Spring](http://www.springsource.org/spring-framework) Factory Bean for [EmbedMongo](https://github.com/flapdoodle-oss/embedmongo.flapdoodle.de) that runs “embedded” MongoDB as managed process to use in integration tests (especially with CI). Unlike EmbedMongo default settings, this factory uses Slf4j for all logging by default so you can use any logging implementation you want.

Since v1.2 a convenient builder for Java-based configuration is also provided. This builder isn’t tied with the Spring Framework, so it can be useful even for non-Spring projects.

EmbedMongo isn’t truly embedded Mongo as there’s no Java implementation of the MongoDB. It actually downloads original MongoDB binary for your platform and runs it. See [embedmongo.flapdoodle.de](https://github.com/flapdoodle-oss/embedmongo.flapdoodle.de) for more information.


Usage
-----

```xml
<bean id="mongo" class="cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean"
      p:version="2.4.5"
      p:bindIp="127.0.0.1"
      p:port="12345" />
```

```java
@Bean(destroyMethod="close")
public Mongo mongo() throws IOException {
    return new EmbeddedMongoBuilder()
            .version("2.4.5")
            .bindIp("127.0.0.1")
            .port(12345)
            .build();
}
```

All properties are optional and have usable default values.


Maven
-----

Released versions are available in The Central Repository. Just add this artifact to your project:

```xml
<dependency>
    <groupId>cz.jirutka.spring</groupId>
    <artifactId>embedmongo-spring</artifactId>
    <version>1.3.1</version>
</dependency>
```

However if you want to use the last snapshot version, you have to add the Sonatype OSS repository:

```xml
<repository>
    <id>sonatype-snapshots</id>
    <name>Sonatype repository for deploying snapshots</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```

**Note:** If you’re stuck on Java 6 for any absurd reason, use version 1.2.2.


License
-------

This project is licensed under [MIT License](http://opensource.org/licenses/MIT).
