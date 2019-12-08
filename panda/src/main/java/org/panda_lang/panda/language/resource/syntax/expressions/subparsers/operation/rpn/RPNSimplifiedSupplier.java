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

package org.panda_lang.panda.language.resource.syntax.expressions.subparsers.operation.rpn;

import org.panda_lang.framework.design.architecture.expression.Expression;
import org.panda_lang.framework.design.architecture.module.ModuleLoader;
import org.panda_lang.framework.design.architecture.prototype.Prototype;
import org.panda_lang.framework.design.runtime.ProcessStack;

public abstract class RPNSimplifiedSupplier<A, B, T> implements RPNOperationSupplier {

    public abstract T get(ProcessStack stack, Object instance, A a, B b);

    public abstract Prototype returnType(ModuleLoader loader);

    @Override
    public RPNOperationAction<T> of(ModuleLoader moduleLoader, Expression a, Expression b) {
        return new RPNOperationAction<T>() {
            @Override
            public T get(ProcessStack stack, Object instance) throws Exception {
                return RPNSimplifiedSupplier.this.get(stack, instance, a.evaluate(stack, instance), b.evaluate(stack, instance));
            }

            @Override
            public Prototype returnType(ModuleLoader loader) {
                return RPNSimplifiedSupplier.this.returnType(loader);
            }
        };
    }

    @Override
    public Prototype returnType(ModuleLoader loader, Prototype a, Prototype b) {
        return returnType(loader);
    }

}