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

package org.panda_lang.panda.framework.language.interpreter.messenger.formatter;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.PandaFrameworkConstants;
import org.panda_lang.panda.framework.design.interpreter.messenger.MessengerTypeFormatter;
import org.panda_lang.panda.framework.design.interpreter.messenger.formatters.MessengerDataFormatter;

import java.util.LinkedHashMap;
import java.util.Map;

public final class EnvironmentFormatter implements MessengerDataFormatter<Object> {

    private static final Map<String, String> ENVIRONMENT = new LinkedHashMap<>();

    static {
        ENVIRONMENT.put("Panda", PandaFrameworkConstants.VERSION);
        ENVIRONMENT.put("Java", System.getProperty("java.version"));
        ENVIRONMENT.put("OS", System.getProperty("os.name"));
    }

    @Override
    public void onInitialize(MessengerTypeFormatter<Object> typeFormatter) {
        typeFormatter
                .register("{{environment}}", (formatter, obj) -> {
                    StringBuilder content = new StringBuilder();

                    ENVIRONMENT.forEach((key, value) -> content
                            .append("{{newline}}  ")
                            .append(key)
                            .append(": ")
                            .append(value)
                    );

                    return content.toString();
                })
                .register("{{newline}}", (formatter, obj) -> System.lineSeparator());
    }

    public static void addEnvironmentInfo(String title, String content) {
        ENVIRONMENT.put(title, content);
    }

    @Override
    public @Nullable Class<Object> getType() {
        return null;
    }

}