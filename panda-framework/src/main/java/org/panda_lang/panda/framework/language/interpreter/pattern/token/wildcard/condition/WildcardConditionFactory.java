package org.panda_lang.panda.framework.language.interpreter.pattern.token.wildcard.condition;

public interface WildcardConditionFactory {

    boolean handle(String condition);

    WildcardCondition create(String condition);

}