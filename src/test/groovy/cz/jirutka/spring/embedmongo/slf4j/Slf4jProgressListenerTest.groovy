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

@Unroll
class Slf4jProgressListenerTest extends Specification {

    def logger = Mock(Logger)
    def listener = new Slf4jProgressListener(logger)


    def "progress: delegates multiples of ten percents to the logger on debug level"() {
        when:
            [1, 9, 10, 10, 30, 32, 100].each {
                listener.progress('foo', it)
            }
        then:
            1 * logger.debug(_, 'foo', 10)
            1 * logger.debug(_, 'foo', 30)
            1 * logger.debug(_, 'foo', 100)
            0 * logger.debug(*_)
    }

    def "#method: delegates to the logger on info level"() {
        when:
            listener./$method/(*args)
        then:
            1 * logger.info(*_)
        where:
            method  | args
            'done'  | ['label']
            'start' | ['label']
            'info'   | ['label', 'msg']
    }
}
