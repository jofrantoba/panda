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

package org.panda_lang.framework.language.architecture.type;

import org.panda_lang.framework.design.architecture.type.Fields;
import org.panda_lang.framework.design.architecture.type.Type;
import org.panda_lang.framework.design.architecture.type.TypeField;
import org.panda_lang.utilities.commons.function.Option;

import java.util.List;

final class PandaFields extends AbstractProperties<TypeField> implements Fields {

    PandaFields(Type type) {
        super(TypeField.class, type);
    }

    @Override
    public Option<TypeField> getField(String name) {
        List<? extends TypeField> fields = getPropertiesLike(name);
        return fields.isEmpty() ? Option.none() : Option.of(fields.get(0));
    }

}
