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

package org.panda_lang.framework.design.architecture.type;

public final class TypeModels {

    public static final String CLASS = "class";

    public static final String ENUM = "enum";

    public static final String INTERFACE = "interface";

    private TypeModels() { }

    public static boolean isClass(Type type) {
        return CLASS.equals(type.getModel());
    }

    public static boolean isInterface(Type type) {
        return INTERFACE.equals(type.getModel());
    }

    public static String of(Class<?> clazz) {
        if (clazz.isInterface()) {
            return INTERFACE;
        }
        else if (clazz.isEnum()) {
            return ENUM;
        }

        return CLASS;
    }

}
