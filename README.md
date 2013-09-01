Spring Factory Bean for EmbedMongo
==================================

[Spring](http://www.springsource.org/spring-framework) Factory Bean for [EmbedMongo](https://github.com/flapdoodle-oss/embedmongo.flapdoodle.de) that runs “embedded” MongoDB as managed process to use in integration tests (especially with CI). Unlike EmbedMongo default settings, this factory uses Slf4j for all logging by default so you can use any logging implementation you want.

EmbedMongo isn’t truly embedded Mongo as there’s no Java implementation of the MongoDB. It actually downloads original MongoDB binary for your platform and runs it. See [embedmongo.flapdoodle.de](https://github.com/flapdoodle-oss/embedmongo.flapdoodle.de) for more information.


Usage
-----

```xml
<bean id="mongo" class="cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean"
      p:version="2.2"
      p:bindIp="127.0.0.1"
      p:port="12345" />
```

All properties are optional and have usable default values.


Maven
-----

```xml
<dependency>
    <groupId>cz.jirutka.spring</groupId>
    <artifactId>embedmongo-spring</artifactId>
    <version>1.0</version>
</dependency>

<repository>
    <id>cvut-local-repos</id>
    <name>CVUT Repository Local</name>
    <url>http://repository.fit.cvut.cz/maven/local-repos/</url>
</repository>
```


TODO
----

* unit and integration tests


License
-------

This project is licensed under [MIT License](http://opensource.org/licenses/MIT).

