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

package org.panda_lang.panda.framework.language.resource.parsers.expression.assignation;

import org.jetbrains.annotations.Nullable;
import org.panda_lang.panda.framework.design.architecture.statement.Statement;
import org.panda_lang.panda.framework.design.architecture.statement.StatementCell;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaComponents;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPipelines;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaPriorities;
import org.panda_lang.panda.framework.design.interpreter.parser.ParserData;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.BootstrapParserBuilder;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.UnifiedParserBootstrap;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Autowired;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Component;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.annotations.Src;
import org.panda_lang.panda.framework.design.interpreter.parser.bootstrap.layer.LocalData;
import org.panda_lang.panda.framework.design.interpreter.parser.component.UniversalComponents;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.ParserHandler;
import org.panda_lang.panda.framework.design.resource.parsers.ParserRegistration;
import org.panda_lang.panda.framework.design.interpreter.parser.pipeline.PipelinePath;
import org.panda_lang.panda.framework.design.interpreter.pattern.PandaTokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.TokenPattern;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResult;
import org.panda_lang.panda.framework.design.interpreter.pattern.token.extractor.ExtractorResultElement;
import org.panda_lang.panda.framework.design.interpreter.token.snippet.Snippet;
import org.panda_lang.panda.framework.design.interpreter.token.stream.SourceStream;
import org.panda_lang.panda.framework.design.runtime.expression.Expression;
import org.panda_lang.panda.framework.language.interpreter.parser.PandaParserFailure;
import org.panda_lang.panda.framework.language.interpreter.token.stream.PandaSourceStream;

import java.util.Optional;

@ParserRegistration(target = PandaPipelines.SCOPE_LABEL, priority = PandaPriorities.SCOPE_ASSIGNATION)
public class AssignationParser extends UnifiedParserBootstrap {

    private static final String PATTERN = "<*declaration> (=|+=|-=|`*=|/=) <assignation:reader expression> [;]";

    private TokenPattern pattern;

    @Override
    public BootstrapParserBuilder initialize(ParserData data, BootstrapParserBuilder defaultBuilder) {
        this.pattern = PandaTokenPattern.builder()
                .compile(PATTERN)
                .build(data);

        return defaultBuilder.pattern(PATTERN);
    }

    @Override
    public boolean customHandle(@Nullable ParserHandler handler, ParserData data, SourceStream source) {
        ExtractorResult result = pattern.extract(data, source);

        if (!result.isMatched()) {
            return false;
        }

        Optional<ExtractorResultElement> declaration = result.getWildcard("*declaration");

        if (!declaration.isPresent()) {
            return false;
        }

        SourceStream stream = new PandaSourceStream(declaration.get().getValue());

        AssignationSubparser subparser = data
                .getComponent(UniversalComponents.PIPELINE)
                .getPipeline(PandaPipelines.ASSIGNER)
                .handleWithUpdatedSource(data, stream);

        if (subparser == null) {
            return false;
        }

        return !stream.hasUnreadSource();
    }

    @Autowired
    public void parse(ParserData data, LocalData local, @Component PipelinePath registry, @Src("*declaration") Snippet declaration, @Src("assignation") Expression assignation) throws Throwable {
        ParserData delegatedData = data.fork();
        delegatedData.setComponent(AssignationComponents.SCOPE, delegatedData.getComponent(UniversalComponents.SCOPE_LINKER).getCurrentScope());

        AssignationSubparser subparser = registry.getPipeline(PandaPipelines.ASSIGNER).handle(data, declaration);
        StatementCell cell = delegatedData.getComponent(PandaComponents.CONTAINER).reserveCell();
        Statement statement = subparser.parseAssignment(delegatedData, declaration, assignation);

        if (statement == null) {
            throw new PandaParserFailure("Cannot parse assignment", delegatedData);
        }

        cell.setStatement(statement);
    }

}