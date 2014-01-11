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

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import cz.jirutka.spring.embedmongo.slf4j.Slf4jLevel;
import cz.jirutka.spring.embedmongo.slf4j.Slf4jProgressListener;
import cz.jirutka.spring.embedmongo.slf4j.Slf4jStreamProcessor;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.*;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.distribution.GenericVersion;
import de.flapdoodle.embed.process.runtime.Network;
import de.flapdoodle.embed.process.store.Downloader;
import de.flapdoodle.embed.process.store.IArtifactStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import java.io.IOException;

/**
 * {@linkplain FactoryBean} for EmbedMongo that runs MongoDB as managed process
 * and exposes preconfigured instance of {@link Mongo}.
 *
 * <p>Unlike EmbedMongo default settings, this factory uses Slf4j for all logging
 * by default so you can use any logging implementation you want. To tweak logging
 * levels use this classes as logger names:
 *
 * <ul>
 *   <li>de.flapdoodle.embed.mongo.MongodProcess</li>
 *   <li>de.flapdoodle.embed.process.store.Downloader</li>
 *   <li>cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean</li>
 * </ul>
 * </p>
 *
 * <p>This is not truly embedded Mongo as there's no Java implementation of the
 * MongoDB. EmbedMongo actually downloads original MongoDB binary for your
 * platform and executes it. EmbedMongo process is stopped automatically when
 * you close connection with {@link Mongo}.</p>
 *
 * @see <a href=https://github.com/flapdoodle-oss/embedmongo.flapdoodle.de>embedmongo.flapdoodle.de</a>
 *
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class EmbeddedMongoFactoryBean implements FactoryBean<Mongo>, DisposableBean {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedMongoFactoryBean.class);

    private String version;
    private Integer port;
    private String bindIp = "localhost";

    private MongodExecutable mongodExe;


    public Mongo getObject() throws IOException {
        LOG.info("Initializing embedded MongoDB instance");
        MongodStarter runtime = MongodStarter.getInstance(buildRuntimeConfig());
        mongodExe = runtime.prepare(buildMongodConfig());

        LOG.info("Starting embedded MongoDB instance");
        mongodExe.start();

        return new MongoClient(getBindIp(), getPort());
    }

    public Class<Mongo> getObjectType() {
        return Mongo.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void destroy() {
        if (mongodExe != null) {
            LOG.info("Stopping embedded MongoDB instance");
            mongodExe.stop();
        }
    }

    /**
     * The version of MongoDB to run e.g. 2.1.1, 1.6 v1.8.2, V2_0_4. When no
     * version is provided, then {@link Version.Main#PRODUCTION PRODUCTION}
     * is used by default.
     *
     * @param version The MongoDB version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    /**
     * The port MongoDB should run on. When no port is provided, then some free
     * server port is automatically assigned.
     *
     * @param port The port to run on.
     */
    public void setPort(int port) {
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Port number must be between 0 and 65535");
        }
        this.port = port;
    }

    public int getPort() {
        if (port == null) {
            try { port = Network.getFreeServerPort(); }
            catch (IOException ex) {
                LOG.error("Could not get free server port");
            }
        }
        return port;
    }

    /**
     * An IP address for the MongoDB instance to be bound to during its execution.
     * Default is localhost.
     *
     * @param bindIp The IPv4 or IPv6 address to bound.
     */
    public void setBindIp(String bindIp) {
        this.bindIp = bindIp;
    }

    public String getBindIp() {
        return bindIp;
    }


    private ProcessOutput buildOutputConfig() {
        Logger logger = LoggerFactory.getLogger(MongodProcess.class);

        return new ProcessOutput(
                new Slf4jStreamProcessor(logger, Slf4jLevel.TRACE),
                new Slf4jStreamProcessor(logger, Slf4jLevel.WARN),
                new Slf4jStreamProcessor(logger, Slf4jLevel.INFO));
    }

    private IRuntimeConfig buildRuntimeConfig() {
        return new RuntimeConfigBuilder()
                .defaults(Command.MongoD)
                .processOutput(buildOutputConfig())
                .artifactStore(buildArtifactStore())
                .build();
    }

    private IArtifactStore buildArtifactStore() {
        Logger logger = LoggerFactory.getLogger(Downloader.class);

        return new ArtifactStoreBuilder()
                .defaults(Command.MongoD)
                .download(new DownloadConfigBuilder()
                        .defaultsForCommand(Command.MongoD)
                        .progressListener(new Slf4jProgressListener(logger))
                        .build())
                .build();
    }

    private IMongodConfig buildMongodConfig() throws IOException {
        return new MongodConfigBuilder()
                .version(parseVersion(version))
                .net(new Net(getBindIp(), getPort(), Network.localhostIsIPv6()))
                .build();
    }

    private IFeatureAwareVersion parseVersion(String version) {
        if (version == null) {
            return Version.Main.PRODUCTION;
        }

        String versionEnumName = version.toUpperCase().replaceAll("\\.", "_");
        if (version.charAt(0) != 'V') {
            versionEnumName = "V" + versionEnumName;
        }
        try {
            return Version.valueOf(versionEnumName);
        } catch (IllegalArgumentException ex) {
            LOG.warn("Unrecognised MongoDB version '{}', this might be a new version that we don't yet know about. " +
                    "Attempting download anyway...", version);
            return Versions.withFeatures(new GenericVersion(version));
        }
    }
}
