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

package org.panda_lang.panda.language.structure.overall.imports;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPattern;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternHollows;
import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenPatternUtils;
import org.panda_lang.panda.core.interpreter.parser.pipeline.DefaultPipelines;
import org.panda_lang.panda.core.interpreter.parser.pipeline.registry.ParserRegistration;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.PandaScript;
import org.panda_lang.panda.framework.implementation.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGenerationCallback;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGenerationLayer;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.ParserGenerationType;
import org.panda_lang.panda.framework.language.interpreter.parser.generation.util.LocalCallback;
import org.panda_lang.panda.framework.language.interpreter.token.Token;
import org.panda_lang.panda.framework.language.interpreter.token.TokenRepresentation;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.overall.module.Module;
import org.panda_lang.panda.language.structure.overall.module.ModuleRegistry;

@ParserRegistration(target = DefaultPipelines.OVERALL, parserClass = ImportParser.class, handlerClass = ImportParserHandler.class)
public class ImportParser implements UnifiedParser {

    protected static final TokenPattern PATTERN = TokenPattern.builder()
            .unit(TokenType.KEYWORD, "import")
            .hollow()
            .unit(TokenType.SEPARATOR, ";")
            .build();

    @Override
    public void parse(ParserInfo info) {
        ParserGeneration generation = info.getComponent(Components.GENERATION);

        generation.getLayer(ParserGenerationType.HIGHER)
                .delegateImmediately(new ImportDeclarationParserCallback(), info);
    }

    @LocalCallback
    private static class ImportDeclarationParserCallback implements ParserGenerationCallback {

        @Override
        public void call(ParserInfo delegatedInfo, ParserGenerationLayer nextLayer) {
            TokenPatternHollows hollows = TokenPatternUtils.extract(PATTERN, delegatedInfo);
            TokenizedSource hollow = hollows.getGap(0);

            StringBuilder groupNameBuilder = new StringBuilder();

            for (TokenRepresentation representation : hollow.getTokensRepresentations()) {
                Token token = representation.getToken();
                groupNameBuilder.append(token.getTokenValue());
            }

            String importedGroupName = groupNameBuilder.toString();

            ModuleRegistry registry = ModuleRegistry.getDefault();
            Module module = registry.get(importedGroupName);

            if (module == null) {
                throw new PandaParserException("Unknown group " + importedGroupName);
            }

            Import anImport = new Import(module);
            ImportStatement importStatement = new ImportStatement(anImport);

            PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
            script.getStatements().add(importStatement);

            ImportRegistry importRegistry = script.getImportRegistry();
            importRegistry.include(anImport.getModule());
        }

    }

}