/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.language.structure.prototype.structure.field;

import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;

import javax.annotation.Nullable;

public class StaticValue {

    private final boolean external;
    private final Value value;
    private final Expression expression;
    private final ExecutableBranch copyOfBranch;

    private StaticValue(Value value) {
        this(false, value, null, null);
    }

    private StaticValue(Expression expression, ExecutableBranch copyOfBranch) {
        this(true, null, expression, copyOfBranch);
    }

    private StaticValue(boolean external, Value value, Expression expression, ExecutableBranch copyOfBranch) {
        this.external = external;
        this.value = value;
        this.expression = expression;
        this.copyOfBranch = copyOfBranch;
    }

    public Value getValue() {
        return external ? expression.getExpressionValue(copyOfBranch) : value;
    }

    public ClassPrototype getReturnType() {
        return external ? expression.getReturnType() : value.getType();
    }

    public static StaticValue of(Expression expression, @Nullable ExecutableBranch branch) {
        return new StaticValue(expression, branch != null ? branch.duplicate() : null);
    }

    public static StaticValue of(Value value) {
        return new StaticValue(value);
    }

}