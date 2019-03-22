/*
 * Copyright (c) 2015-2019 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.framework.design.resource.parsers.expression.fixed;

import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;

import java.util.Collection;

class ExpressionParserWorker {

    private static final int NONE = -1;

    private enum Status {

        PROCESSING,
        DONE,
        ERROR

    }

    private final ExpressionSubparserWorker[] subparsers;
    private ExpressionResult<Expression> error = null;
    private int previousSubparser = -1;
    private int lastSucceededRead = 0;
    private int read = 0;

    protected ExpressionParserWorker(ExpressionParser parser, SourceStream source, Collection<ExpressionSubparser> subparsers) {
        this.subparsers = subparsers.stream()
                .map(ExpressionSubparser::createSubparser)
                .toArray(ExpressionSubparserWorker[]::new);
    }

    protected void finish(ExpressionContext context) {
        for (ExpressionSubparserWorker worker : subparsers) {
            // skip removed subparsers
            if (worker == null || worker.getSubparser().getType() != ExpressionSubparserType.MUTUAL) {
                continue;
            }

            ExpressionResult<Expression> result = worker.finish(context);

            if (result != null && result.isPresent()) {
                context.getResults().push(result.get());
                break;
            }
        }
    }

    protected boolean next(ExpressionContext context) {
        int cachedSubparser = previousSubparser;

        // try to use cached subparser
        if (previousSubparser != NONE) {
            // cache and reset values
            previousSubparser = NONE;

            // return result from cached subparser
            if (!this.next(context, cachedSubparser) && previousSubparser != NONE) {
                read++;
                return true;
            }
        }

        for (int index = 0; index < subparsers.length; index++) {
            // skip cached subparser
            if (previousSubparser == index) {
                continue;
            }

            // return result from subparser
            if (!this.next(context, index)) {
                break;
            }
        }

        read++;
        return true;
    }

    private boolean next(ExpressionContext context, int index) {
        ExpressionSubparserWorker worker = subparsers[index];

        // skip individual subparser if there's some content
        if (worker.getSubparser().getType() == ExpressionSubparserType.INDIVIDUAL && !context.getResults().isEmpty()) {
            return true;
        }

        ExpressionResult<Expression> result = worker.next(context);

        // if something went wrong
        if (result == null || result.containsError()) {

            // do not override previous error
            if (result != null && error == null) {
                error = result;
            }

            return true;
        }

        previousSubparser = index;

        if (result.isEmpty()) {
            return true;
        }

        // save the result, cleanup cache, move the index
        context.getResults().push(result.get());
        lastSucceededRead = read + 1;
        error = null;
        return false;
    }

    protected boolean hasError() {
        return getError() != null;
    }

    protected int getLastSucceededRead() {
        return lastSucceededRead;
    }

    protected ExpressionResult<Expression> getError() {
        return error;
    }

}
