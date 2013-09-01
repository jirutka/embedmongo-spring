/*
 * The MIT License
 *
 * Copyright 2013 Jakub Jirutka <jakub@jirutka.cz>.
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
package cz.jirutka.spring.embedmongo.slf4j;

import de.flapdoodle.embed.process.io.IStreamProcessor;
import org.slf4j.Logger;

/**
 * {@link de.flapdoodle.embed.process.io.IStreamProcessor} using SLF4J to log messages.
 *
 * <p>The constructor takes a {@link org.slf4j.Logger} that will be used to log messages,
 * and an instance of {@link Slf4jLevel} to determine which method to call
 * on the {@link org.slf4j.Logger} instance.</p>
 *
 * Copied from https://gist.github.com/adutra/2911479.
 *
 * @author Alexandre Dutra
 */
public class Slf4jStreamProcessor implements IStreamProcessor {

    private final Logger logger;
    private final Slf4jLevel level;


    public Slf4jStreamProcessor(Logger logger, Slf4jLevel level) {
        this.logger = logger;
        this.level = level;
    }

    public void process(String line) {
        level.log(logger, stripLineEndings(line));
    }

    public void onProcessed() {
    }


    protected String stripLineEndings(String line) {
        //we still need to remove line endings that are passed on by StreamToLineProcessor...
        return line.replaceAll("[\n\r]+", "");
    }
}
