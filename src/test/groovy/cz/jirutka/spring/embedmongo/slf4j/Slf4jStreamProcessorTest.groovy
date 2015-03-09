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
package cz.jirutka.spring.embedmongo.slf4j

import org.slf4j.Logger
import spock.lang.Specification
import spock.lang.Unroll

// This tests also Slf4jLevel.
class Slf4jStreamProcessorTest extends Specification {

    def logger = Mock(Logger)


    @Unroll
    def "process: delegates to the logger on the specified level: #level"() {
        when:
            new Slf4jStreamProcessor(logger, level) .process('foo')
        then:
            1 * logger./$method/('foo')
        where:
            level << [Slf4jLevel.TRACE, Slf4jLevel.DEBUG, Slf4jLevel.INFO, Slf4jLevel.WARN, Slf4jLevel.ERROR]
            method = level.name().toLowerCase()
    }

    def "process: delegates to the logger and strips line endings"() {
        setup:
            def processor = new Slf4jStreamProcessor(logger, Slf4jLevel.INFO)
        when:
            processor.process("foo${lineEnding}")
        then:
            1 * logger.info('foo')
        where:
            lineEnding << ['\n', '\n\n', '\r\n', '\r\n\r\n']
    }
}
