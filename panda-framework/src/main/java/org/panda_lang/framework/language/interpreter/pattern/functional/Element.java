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

package org.panda_lang.framework.language.interpreter.pattern.functional;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

final class Element<T> implements Buildable<T> {

    private final String id;
    private final boolean optional;
    private final Reader<?> reader;
    private final Collection<? extends Verifier<?>> verifiers;
    private final List<Function<?, ?>> mappers;

    protected Element(FunctionalPatternElementBuilder<?, ?> builder) {
        this.id = builder.id;
        this.optional = builder.optional;
        this.reader = builder.reader;
        this.verifiers = builder.verifies;
        this.mappers = builder.mappers;
    }

    @Override
    public Element<T> build() {
        return this;
    }

    public List<Function<?, ?>> getMappers() {
        return mappers;
    }

    public Collection<? extends Verifier<?>> getVerifiers() {
        return verifiers;
    }

    public Reader<?> getReader() {
        return reader;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getId() {
        return id;
    }

}
