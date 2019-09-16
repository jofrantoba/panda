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

package org.panda_lang.panda.language.resource.expression.subparsers.assignation.array;

import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.language.architecture.prototype.array.ArrayClassPrototype;
import org.panda_lang.panda.language.runtime.expression.DynamicExpression;

public final class ArrayAccessor implements DynamicExpression {

    private final ClassPrototype type;
    private final Expression instanceExpression;
    private final Expression indexExpression;

    public ArrayAccessor(Expression instanceExpression, Expression indexExpression) {
        if (!(instanceExpression.getReturnType() instanceof ArrayClassPrototype)) {
            throw new RuntimeException("Array required");
        }

        this.type = ((ArrayClassPrototype) instanceExpression.getReturnType()).getType().fetch();
        this.instanceExpression = instanceExpression;
        this.indexExpression = indexExpression;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object call(ProcessStack stack, Object instance) {
        return getArrayInstance(stack, instance)[getIndex(stack, instance)];
    }

    public ArrayAssigner toAssignerExpression(Expression value) {
        return new ArrayAssigner(this, value);
    }

    public int getIndex(ProcessStack stack, Object instance) {
        return indexExpression.evaluate(stack, instance);
    }

    public <T> T[] getArrayInstance(ProcessStack stack, Object instance) {
        return instanceExpression.evaluate(stack, instance);
    }

    @Override
    public ClassPrototype getReturnType() {
        return type;
    }

}
