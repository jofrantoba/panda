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

package org.panda_lang.panda.language.resource.syntax.scope;

import org.panda_lang.framework.design.architecture.statement.Scope;
import org.panda_lang.framework.design.architecture.statement.VariableData;
import org.panda_lang.framework.design.interpreter.parser.Context;
import org.panda_lang.framework.design.interpreter.parser.pipeline.Pipelines;
import org.panda_lang.framework.design.interpreter.token.Snippetable;
import org.panda_lang.framework.language.architecture.statement.PandaVariableDataInitializer;
import org.panda_lang.framework.language.interpreter.pattern.Mappings;
import org.panda_lang.framework.language.resource.syntax.TokenTypes;
import org.panda_lang.framework.language.resource.syntax.keyword.Keywords;
import org.panda_lang.panda.language.interpreter.parser.RegistrableParser;
import org.panda_lang.panda.language.interpreter.parser.context.BootstrapInitializer;
import org.panda_lang.panda.language.interpreter.parser.context.ParserBootstrap;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Autowired;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Channel;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Ctx;
import org.panda_lang.panda.language.interpreter.parser.context.annotations.Src;
import org.panda_lang.panda.language.interpreter.parser.context.handlers.TokenHandler;
import org.panda_lang.panda.language.resource.syntax.PandaPriorities;

@RegistrableParser(pipeline = Pipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_LATE_DECLARATION)
public final class LateDeclarationParser extends ParserBootstrap<Void> {

    @Override
    protected BootstrapInitializer<Void> initialize(Context context, BootstrapInitializer<Void> initializer) {
        return initializer
                .handler(new TokenHandler(Keywords.LATE))
                .functional(pattern -> pattern
                        .keyword(Keywords.LATE).optional()
                        .keyword(Keywords.MUT).optional()
                        .keyword(Keywords.NIL).optional()
                        .type("type").optional().verifyNextType(TokenTypes.UNKNOWN)
                        .wildcard("name").verifyType(TokenTypes.UNKNOWN));
    }

    @Autowired(order = 1)
    public void parse(Context context, @Channel Mappings mappings, @Ctx Scope scope, @Src("type") Snippetable type, @Src("name") Snippetable name) {
        PandaVariableDataInitializer dataInitializer = new PandaVariableDataInitializer(context, scope);
        VariableData variableData = dataInitializer.createVariableData(type, name, mappings.has(Keywords.MUT.getValue()), mappings.has(Keywords.NIL.getValue()));
        scope.createVariable(variableData);
    }

}
