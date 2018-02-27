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

package org.panda_lang.panda.language.structure.general.expression.callbacks.instance;

import org.panda_lang.panda.design.architecture.value.PandaValue;
import org.panda_lang.panda.design.architecture.value.Value;
import org.panda_lang.panda.design.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.general.expression.Expression;
import org.panda_lang.panda.language.structure.general.expression.ExpressionCallback;
import org.panda_lang.panda.language.structure.general.expression.ExpressionUtils;
import org.panda_lang.panda.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.constructor.PrototypeConstructor;

public class InstanceExpressionCallback implements ExpressionCallback {

    private final ClassPrototype returnType;
    private final PrototypeConstructor constructor;
    private final Expression[] arguments;

    public InstanceExpressionCallback(ClassPrototype returnType, PrototypeConstructor constructor, Expression... arguments) {
        this.returnType = returnType;
        this.constructor = constructor;
        this.arguments = arguments;
    }

    @Override
    public Value call(Expression expression, ExecutableBranch branch) {
        Value[] values = ExpressionUtils.getValues(branch, arguments);
        Object instance = constructor.createInstance(branch, values);

        return new PandaValue(returnType, instance);
    }

    public ClassPrototype getReturnType() {
        return returnType;
    }

}
