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

package org.panda_lang.panda.language.interpreter.parser.context;

import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Handler;

import org.panda_lang.utilities.commons.function.Option;

final class BootstrapContentImpl implements BootstrapContent {

    private final String name;
    private final Object instance;
    private final Context context;
    private final Handler handler;
    private final IterationInitializer interceptor;
    private final Object pattern;

    BootstrapContentImpl(String name, Object instance, Context context, Handler handler, IterationInitializer interceptor, Object pattern) {
        this.name = name;
        this.instance = instance;
        this.context = context;
        this.handler = handler;
        this.interceptor = interceptor;
        this.pattern = pattern;
    }

    @Override
    public IterationInitializer getInitializer() {
        return interceptor;
    }

    @Override
    public Option<Handler> getHandler() {
        return Option.of(handler);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Option<R> getPattern() {
        return (Option<R>) Option.of(pattern);
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    @Override
    public String getName() {
        return name;
    }

}
