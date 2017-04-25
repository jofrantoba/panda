/*
 * Copyright (c) 2015-2017 Dzikoysk
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

package org.panda_lang.panda.language.composition.prototypes.lang;

import org.panda_lang.panda.core.structure.value.Value;
import org.panda_lang.panda.language.runtime.ExecutableBranch;
import org.panda_lang.panda.language.structure.overall.module.Module;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeRegistrationCall;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;
import org.panda_lang.panda.language.structure.prototype.structure.method.Method;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodCallback;
import org.panda_lang.panda.language.structure.prototype.structure.method.MethodVisibility;
import org.panda_lang.panda.language.structure.prototype.structure.method.variant.PandaMethod;

@ClassPrototypeRegistrationCall
public class SystemPrototype {

    static {
        ModuleRegistry registry = ModuleRegistry.getDefault();
        Module defaultModule = registry.getOrCreate("panda.lang");

        ClassPrototype systemPrototype = new ClassPrototype(defaultModule, "System");
        defaultModule.add(systemPrototype);

        Method printMethod = PandaMethod.builder()
                .methodName("print")
                .prototype(systemPrototype)
                .isStatic(true)
                .visibility(MethodVisibility.PUBLIC)
                .methodBody(new MethodCallback<System>() {
                    @Override
                    public void invoke(ExecutableBranch bridge, System instance, Value... parameters) {
                        StringBuilder node = new StringBuilder();

                        for (Value value : parameters) {
                            node.append(value.getObject());
                            node.append(", ");
                        }

                        String message = node.substring(0, node.length() - 2);
                        System.out.println(message);
                    }})
                .build();
        
        systemPrototype.getMethods().registerMethod(printMethod);
    }

}
