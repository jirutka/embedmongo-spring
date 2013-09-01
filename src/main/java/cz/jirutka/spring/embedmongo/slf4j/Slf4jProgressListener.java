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

import de.flapdoodle.embed.process.io.progress.IProgressListener;
import org.slf4j.Logger;

/**
 * Listener for logging downloading progress.
 *
 * @author Jakub Jirutka <jakub@jirutka.cz>
 */
public class Slf4jProgressListener implements IProgressListener {

    private final Logger logger;
    private int lastPercent = -1;


    public Slf4jProgressListener(Logger logger) {
        this.logger = logger;
    }


    public void progress(String label, int percent) {
        if (percent != lastPercent && percent % 10 == 0) {
            logger.debug("{} : {} %", label, percent);
        }
        lastPercent = percent;
    }

    public void done(String label) {
        logger.info("{} : finished", label);
    }

    public void start(String label) {
        logger.info("{} : starting...", label);
    }

    public void info(String label, String message) {
        logger.info("{} : {}", label, message);
    }
}
