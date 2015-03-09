/*
 * The MIT License
 *
 * Copyright 2013-2015 Jakub Jirutka <jakub@jirutka.cz>.
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
package cz.jirutka.spring.embedmongo

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.core.Appender
import com.mongodb.MongoClient
import org.slf4j.LoggerFactory
import spock.lang.Specification

class EmbeddedMongoBuilderIT extends Specification {

    def builder = new EmbeddedMongoBuilder()
    MongoClient client


    def cleanup() {
        client?.close()
    }

    def "should start MongoDB instance"() {
        when:
            client = builder.build()
        then:
            client instanceof MongoClient
            client.getDB('local').stats.ok()
    }

    def "Embedded Mongo should log through slf4j"() {
        setup:
            def logAppender = Mock(Appender)
            (LoggerFactory.getLogger('de.flapdoodle.embed') as Logger).with {
                it.level = Level.DEBUG
                it.addAppender(logAppender)
            }
        when:
            client = builder.build()
        then:
            (1.._) * logAppender.doAppend(_)
    }
}
