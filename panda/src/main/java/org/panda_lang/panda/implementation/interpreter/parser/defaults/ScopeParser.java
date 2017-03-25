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

package org.panda_lang.panda.implementation.interpreter.parser.defaults;

import org.panda_lang.panda.framework.interpreter.lexer.token.TokenizedSource;
import org.panda_lang.panda.framework.interpreter.lexer.token.distributor.SourceStream;
import org.panda_lang.panda.framework.interpreter.parser.Parser;
import org.panda_lang.panda.framework.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.interpreter.parser.generation.ParserGeneration;
import org.panda_lang.panda.framework.interpreter.parser.pipeline.ParserPipeline;
import org.panda_lang.panda.framework.interpreter.parser.UnifiedParser;
import org.panda_lang.panda.framework.interpreter.parser.pipeline.registry.PipelineRegistry;
import org.panda_lang.panda.implementation.interpreter.parser.util.Components;
import org.panda_lang.panda.implementation.structure.wrapper.Scope;
import org.panda_lang.panda.implementation.interpreter.lexer.token.distributor.PandaSourceStream;
import org.panda_lang.panda.implementation.interpreter.parser.pipeline.DefaultPipelines;

public class ScopeParser implements Parser {

    private final Scope scope;

    public ScopeParser(Scope scope) {
        this.scope = scope;
    }

    public void parse(ParserInfo info, TokenizedSource body) {
        ParserGeneration generation = info.getComponent(Components.GENERATION);

        PipelineRegistry pipelineRegistry = info.getComponent(Components.PIPELINE_REGISTRY);
        ParserPipeline pipeline = pipelineRegistry.getPipeline(DefaultPipelines.SCOPE);

        SourceStream stream = new PandaSourceStream(body);
        info.setComponent(Components.SOURCE_STREAM, stream);

        while (stream.hasUnreadSource()) {
            UnifiedParser parser = pipeline.handle(stream);

            parser.parse(info);
            generation.executeImmediately(info);
        }
    }

    public Scope getScope() {
        return scope;
    }

}