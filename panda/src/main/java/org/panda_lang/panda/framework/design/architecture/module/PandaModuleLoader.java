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

package org.panda_lang.panda.framework.design.architecture.module;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.PandaFramework;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeMetadata;
import org.panda_lang.panda.framework.design.architecture.prototype.ClassPrototypeReference;
import org.panda_lang.panda.framework.language.architecture.prototype.array.ArrayClassPrototypeUtils;
import org.panda_lang.panda.framework.language.architecture.prototype.array.PandaArray;
import org.panda_lang.panda.framework.language.runtime.PandaRuntimeException;
import org.panda_lang.panda.utilities.commons.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PandaModuleLoader implements ModuleLoader {

    private final Map<String, Module> importedModules;

    public PandaModuleLoader() {
        this.importedModules = new HashMap<>(2);
    }

    @Override
    public PandaModuleLoader include(Module module) {
        this.importedModules.put(module.getName(), module);
        return this;
    }

    @Override
    public @Nullable ClassPrototypeReference forClass(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        if (name.contains(":")) {
            throw new PandaRuntimeException("Not implemented");
        }

        if (name.endsWith(PandaArray.IDENTIFIER)) {
            return ArrayClassPrototypeUtils.obtain(this, name);
        }

        for (Module module : importedModules.values()) {
            Optional<ClassPrototypeReference> reference = module.get(name);

            if (reference.isPresent()) {
                return reference.get();
            }
        }

        StringBuilder content = new StringBuilder();

        for (Module value : importedModules.values()) {
            content.append(value.getReferences().stream().map(ClassPrototypeMetadata::getClassName).collect(Collectors.toList()).toString());
        }

        PandaFramework.getLogger().debug(content.toString());
        return null;
    }

    @Override
    public Module get(String name) {
        return importedModules.get(name);
    }

}
