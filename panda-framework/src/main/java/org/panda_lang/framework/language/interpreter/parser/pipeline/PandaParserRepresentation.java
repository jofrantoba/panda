/*
 * Copyright (c) 2020 Dzikoysk
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

package org.panda_lang.framework.language.interpreter.parser.pipeline;

import org.panda_lang.framework.design.interpreter.parser.Parser;
import org.panda_lang.framework.design.interpreter.parser.ParserRepresentation;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;

public final class PandaParserRepresentation<P extends Parser> implements ParserRepresentation<P> {

    private final P parser;
    private final Handler handler;
    private final double priority;
    private int usages;

    public PandaParserRepresentation(P parser, Handler handler, double priority) {
        this.parser = parser;
        this.handler = handler;
        this.priority = priority;
    }

    @Override
    public void increaseUsages() {
        usages++;
    }

    @Override
    public int getUsages() {
        return usages;
    }

    @Override
    public double getPriority() {
        return priority;
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public P getParser() {
        return parser;
    }

}