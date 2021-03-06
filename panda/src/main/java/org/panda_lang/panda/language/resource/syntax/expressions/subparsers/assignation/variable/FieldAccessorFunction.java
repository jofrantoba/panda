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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.assignation.variable;

import org.panda_lang.framework.design.architecture.dynamic.Frameable;
import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.runtime.MemoryContainer;
import org.panda_lang.framework.design.runtime.ProcessStack;
import org.panda_lang.utilities.commons.function.ThrowingBiFunction;

final class FieldAccessorFunction implements ThrowingBiFunction<ProcessStack, Object, MemoryContainer, Exception> {

    private final Expression instanceExpression;

    public FieldAccessorFunction(Expression instanceExpression) {
        this.instanceExpression = instanceExpression;
    }

    @Override
    public MemoryContainer apply(ProcessStack stack, Object instance) throws Exception {
        return ((Frameable) instanceExpression.evaluate(stack, instance)).__panda__to_frame();
    }

}