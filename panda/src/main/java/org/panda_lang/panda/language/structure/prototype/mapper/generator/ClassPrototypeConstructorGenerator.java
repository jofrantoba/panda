package org.panda_lang.panda.language.structure.prototype.mapper.generator;

import org.panda_lang.panda.design.architecture.value.Value;
import org.panda_lang.panda.design.runtime.ExecutableBranch;
import org.panda_lang.panda.design.architecture.prototype.ClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.PandaClassPrototype;
import org.panda_lang.panda.design.architecture.prototype.constructor.PrototypeConstructor;

import java.lang.reflect.Constructor;

public class ClassPrototypeConstructorGenerator {

    private final Class<?> type;
    private final ClassPrototype prototype;
    private final Constructor<?> constructor;

    public ClassPrototypeConstructorGenerator(Class<?> type, ClassPrototype prototype, Constructor<?> constructor) {
        this.type = type;
        this.prototype = prototype;
        this.constructor = constructor;
    }

    public PrototypeConstructor generate() {
        ClassPrototype[] parameters = new ClassPrototype[constructor.getParameterCount()];

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = PandaClassPrototype.forClass(constructor.getParameterTypes()[i]);
        }

        // TODO: Generate bytecode
        PrototypeConstructor generatedConstructor = new PrototypeConstructor() {
            @Override
            public Object createInstance(ExecutableBranch bridge, Value... values) {
                try {
                    Object[] args = new Object[values.length];

                    for (int i = 0; i < values.length; i++) {
                        args[i] = values[i].getValue();
                    }

                    return constructor.newInstance(args);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public ClassPrototype[] getParameterTypes() {
                return parameters;
            }
        };

        return generatedConstructor;
    }

}
