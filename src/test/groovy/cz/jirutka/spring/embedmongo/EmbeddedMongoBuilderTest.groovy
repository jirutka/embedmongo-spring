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

import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.distribution.Versions
import de.flapdoodle.embed.process.distribution.GenericVersion
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class EmbeddedMongoBuilderTest extends Specification {

    def builder = new EmbeddedMongoBuilder()


    def "version: throws exception when given #desc"() {
        when:
            method.call(builder)
        then:
            thrown(IllegalArgumentException)
        where:
            method                            || desc
            ({ it.version(null as Version) }) || 'null Version'
            ({ it.version(null as String) })  || 'null string'
            ({ it.version('') })              || 'empty string'
            // this is ugly workaround to DRY-up calling of overloaded methods with null
    }

    def "version: parses version from string: #input"() {
        when:
            builder.version(input)
        then:
            builder.@version.asInDownloadPath() == expected.asInDownloadPath()
        where:
            input    || expected
            '2.7.1'  || Version.V2_7_1
            'v2.6.1' || Version.V2_6_1
            'V2_6_1' || Version.V2_6_1
            '9.9.9'  || Versions.withFeatures(new GenericVersion('9.9.9'))
    }

    def "port: throws exception when given illegal port number: #input"() {
        when:
            builder.port(input)
        then:
            thrown(IllegalArgumentException)
        where:
            input << [-1, 65536]
    }

    def "bindIp: throws exception when given #input"() {
        when:
            builder.bindIp(input)
        then:
            thrown(IllegalArgumentException)
        where:
            input || desc
            null  || 'null'
            ''    || 'empty string'
    }




}
