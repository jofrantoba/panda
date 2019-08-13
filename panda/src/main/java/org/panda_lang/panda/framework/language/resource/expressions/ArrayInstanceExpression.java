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

package org.panda_lang.panda.framework.language.resource.expressions;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.architecture.value.Value;
import org.panda_lang.panda.framework.design.runtime.flow.Flow;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.runtime.expression.ExpressionCallback;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.framework.language.architecture.value.PandaStaticValue;

import java.lang.reflect.Array;

public class ArrayInstanceExpression implements ExpressionCallback {

    private final ArrayClassPrototype prototype;
    private final Expression capacity;

    public ArrayInstanceExpression(ArrayClassPrototype prototype, Expression capacity) {
        this.prototype = prototype;
        this.capacity = capacity;
    }

    @Override
    public Value call(Expression expression, Flow flow) {
        int capacityValue = capacity.evaluate(flow).getValue();
        return new PandaStaticValue(prototype, Array.newInstance(prototype.getType().getAssociatedClass(), capacityValue));
    }

    @Override
    public ClassPrototype getReturnType() {
        return prototype;
    }

}