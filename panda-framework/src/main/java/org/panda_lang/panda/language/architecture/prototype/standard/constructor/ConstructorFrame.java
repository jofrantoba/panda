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

package org.panda_lang.panda.language.architecture.prototype.standard.constructor;

import org.panda_lang.panda.framework.design.architecture.dynamic.LivingFrame;
import org.panda_lang.panda.framework.design.architecture.prototype.parameter.PrototypeParameter;
import org.panda_lang.panda.framework.design.runtime.ProcessStack;
import org.panda_lang.panda.language.architecture.statement.ParametrizedAbstractFrame;
import org.panda_lang.panda.language.architecture.statement.ParametrizedLivingFrame;

import java.util.List;

public class ConstructorFrame extends ParametrizedAbstractFrame {

    public ConstructorFrame(List<PrototypeParameter> parameters) {
        super(parameters);
    }

    @Override
    public ConstructorLivingFrame revive(ProcessStack stack, Object instance) {
        return new ConstructorLivingFrame(this, (LivingFrame) instance);
    }

    public List<PrototypeParameter> getParameters() {
        return parameters;
    }

    public static class ConstructorLivingFrame extends ParametrizedLivingFrame<ConstructorFrame> {

        public ConstructorLivingFrame(ConstructorFrame scope, LivingFrame instance) {
            super(scope, instance);
        }

    }
}
