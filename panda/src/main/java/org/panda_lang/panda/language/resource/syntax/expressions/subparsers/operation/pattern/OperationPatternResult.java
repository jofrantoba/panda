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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.pattern;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.design.interpreter.token.Snippet;
import org.panda_lang.framework.language.interpreter.pattern.PatternResult;

import java.util.ArrayList;
import java.util.List;

public final class OperationPatternResult implements PatternResult {

    private final Snippet source;
    private final List<OperationPatternElement> elements;
    private boolean matched;

    protected OperationPatternResult(Snippet source) {
        this.elements = new ArrayList<>(3);
        this.source = source;
    }

    protected void succeed() {
        this.matched = true;
    }

    protected void addElement(OperationPatternElement element) {
        this.elements.add(element);
    }

    public int size() {
        return elements.size();
    }

    @Override
    public boolean isMatched() {
        return matched;
    }

    public @Nullable String get(int index) {
        if (index < 0 || index > size() - 1) {
            return null;
        }

        return elements.get(index).toString();
    }

    public List<OperationPatternElement> getElements() {
        return elements;
    }

    @Override
    public Snippet getSource() {
        return source;
    }

}
