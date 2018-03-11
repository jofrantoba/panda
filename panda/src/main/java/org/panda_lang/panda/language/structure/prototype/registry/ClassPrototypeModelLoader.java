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

package org.panda_lang.panda.language.structure.prototype.registry;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.commons.lang3.StringUtils;
import org.panda_lang.panda.Panda;
import org.panda_lang.panda.design.architecture.prototype.PandaClassPrototypeUtils;
import org.panda_lang.panda.framework.design.architecture.detach.Value;
import org.panda_lang.panda.framework.design.architecture.detach.ExecutableBranch;
import org.panda_lang.panda.framework.design.architecture.detach.Module;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeModel.ClassDeclaration;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeModel.MethodDeclaration;
import org.panda_lang.panda.language.structure.prototype.registry.ClassPrototypeModel.ModuleDeclaration;
import org.panda_lang.panda.framework.design.architecture.detach.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.framework.design.architecture.detach.MethodCallback;
import org.panda_lang.panda.design.architecture.prototype.method.implementation.PandaMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class ClassPrototypeModelLoader {

    private final Panda panda;

    public ClassPrototypeModelLoader(Panda panda) {
        this.panda = panda;
    }

    public void load(Collection<Class<? extends ClassPrototypeModel>> models) {
        try {
            loadModels(models);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadModels(Collection<Class<? extends ClassPrototypeModel>> models) throws Exception {
        Collection<ClassPrototypeModelMethodRegister> methodRegisters = new ArrayList<>();
        ClassPool pool = ClassPool.getDefault();

        CtClass objectCtClass = pool.getCtClass(Object.class.getName());
        CtClass methodCallbackCtClass = pool.get(ClassPrototypeModelMethodCallback.class.getName());
        CtClass executableBranchCtClass = pool.get(ExecutableBranch.class.getName());
        CtClass valueArrayCtClass = pool.get(Value[].class.getName());
        CtClass valueCtClass = pool.get(Value.class.getName());
        CtClass[] implementationTypes = new CtClass[]{ executableBranchCtClass, objectCtClass, valueArrayCtClass };

        for (Class<? extends ClassPrototypeModel> modelClass : models) {
            ModuleDeclaration moduleDeclaration = modelClass.getAnnotation(ModuleDeclaration.class);

            ModuleRegistry registry = ModuleRegistry.getDefault();
            Module defaultModule = registry.getOrCreate(moduleDeclaration.value());

            ClassDeclaration classDeclaration = modelClass.getAnnotation(ClassDeclaration.class);
            ClassPrototype prototype = new PandaClassPrototype(defaultModule, classDeclaration.value(), modelClass);
            defaultModule.add(prototype);

            for (Method method : modelClass.getMethods()) {
                MethodDeclaration methodInfo = method.getAnnotation(MethodDeclaration.class);

                if (methodInfo == null) {
                    continue;
                }

                String methodCallbackClassName = modelClass.getSimpleName() + StringUtils.capitalize(method.getName()) + "MethodCallback";
                CtClass generatedMethodCallbackClass = pool.getOrNull(methodCallbackClassName);

                if (generatedMethodCallbackClass != null) {
                    continue;
                }

                generatedMethodCallbackClass = pool.makeClass(methodCallbackClassName);
                generatedMethodCallbackClass.setSuperclass(methodCallbackCtClass);

                CtMethod callbackImplementation = new CtMethod(CtClass.voidType, "invoke", implementationTypes, generatedMethodCallbackClass);
                boolean array = method.getParameters()[method.getParameters().length - 1].getType().isArray();
                StringBuilder values = new StringBuilder("");
                int valuesCount = 0;

                for (int i = 2; i < method.getParameters().length; ++i) {
                    values.append(",");

                    if (array && i == method.getParameterCount() - 1) {
                        values.append("values");
                        break;
                    }

                    values.append("(").append(Value.class.getName()).append(")");
                    values.append("$3[").append(i - 2).append("]");
                    ++valuesCount;
                }

                String instanceType = method.getParameters()[1].getType().getName();
                StringBuilder bodyBuilder = new StringBuilder("{");

                if (array) {
                    bodyBuilder.append(String.format("%s[] values = new %s[$3.length - %d];", valueCtClass.getName(), valueCtClass.getName(), valuesCount));
                    bodyBuilder.append("for (int i = 0; i < values.length; ++i) {");
                    bodyBuilder.append(String.format("values[i] = $3[%d + i];", valuesCount));
                    bodyBuilder.append("}");
                }

                if (methodInfo.isStatic()) {
                    bodyBuilder.append(String.format("%s.%s($1, (%s) null %s);", modelClass.getName(), method.getName(), instanceType, values.toString()));
                }
                else {
                    // TODO: Improve prototype mapping concept
                    bodyBuilder.append(String.format("%s typedInstance = (%s) $2;", modelClass.getName(), modelClass.getName()));
                    bodyBuilder.append(String.format("typedInstance.%s($1, (%s) $2 %s);", method.getName(), instanceType, values.toString()));
                }
                bodyBuilder.append("}");

                String body = bodyBuilder.toString();
                callbackImplementation.setBody(body);
                generatedMethodCallbackClass.addMethod(callbackImplementation);

                Class<MethodCallback<?>> methodCallbackClass = generatedMethodCallbackClass.toClass();
                MethodCallback<?> methodCallback = methodCallbackClass.newInstance();
                ClassPrototype[] parameterTypes = PandaClassPrototypeUtils.toTypes(method.getParameterTypes());

                methodRegisters.add(() -> {
                    PandaMethod pandaMethod = PandaMethod.builder()
                            .methodName(method.getName())
                            .prototype(prototype)
                            .returnType(PandaClassPrototype.forName(methodInfo.returnType()))
                            .isStatic(methodInfo.isStatic())
                            .visibility(methodInfo.visibility())
                            .methodBody(methodCallback)
                            .parameterTypes(parameterTypes)
                            .catchAllParameters(methodInfo.catchAllParameters())
                            .build();

                    prototype.getMethods().registerMethod(pandaMethod);
                });
            }
        }

        for (ClassPrototypeModelMethodRegister methodRegister : methodRegisters) {
            methodRegister.register();
        }
    }

}
