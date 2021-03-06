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

package org.panda_lang.framework.language.interpreter.messenger;

import org.panda_lang.utilities.commons.function.Option;
import org.jetbrains.annotations.Nullable;
import org.panda_lang.framework.FrameworkController;
import org.panda_lang.framework.PandaFrameworkException;
import org.panda_lang.framework.design.interpreter.messenger.Messenger;
import org.panda_lang.framework.design.interpreter.messenger.MessengerFormatter;
import org.panda_lang.framework.design.interpreter.messenger.MessengerInitializer;
import org.panda_lang.framework.design.interpreter.messenger.MessengerMessage;
import org.panda_lang.framework.design.interpreter.messenger.MessengerMessageTranslator;
import org.panda_lang.framework.design.interpreter.messenger.MessengerOutputListener;
import org.panda_lang.utilities.commons.iterable.ReversedIterable;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.List;

public final class PandaMessenger implements Messenger {

    private final Logger logger;
    private final MessengerInitializer initializer;
    private final MessengerFormatter formatter = new PandaMessengerFormatter(this);
    private final List<MessengerMessageTranslator<Object>> translators = new ArrayList<>();
    private MessengerOutputListener outputListener;
    private boolean initialized;

    public PandaMessenger(Logger logger, @Nullable MessengerInitializer initializer, @Nullable MessengerOutputListener outputListener) {
        this.logger = logger;
        this.initializer = Option.of(initializer).orElseGet(messenger -> {});
        this.outputListener = Option.of(outputListener).orElseGet(() -> new LoggerMessengerOutputListener(logger));
    }

    public PandaMessenger(FrameworkController controller) {
        this(controller.getLogger(), controller.getResources().getMessengerInitializer().getOrNull(), controller.getResources().getOutputListener().getOrNull());
    }

    public PandaMessenger(Logger logger) {
        this(logger, null, null);
    }

    @Override
    public boolean send(Object message) {
        if (!initialized) {
            initialized = true;
            initializer.onInitialize(this);
        }

        MessengerMessageTranslator<Object> translator = null;

        for (MessengerMessageTranslator<Object> messageTranslator : new ReversedIterable<>(translators)) {
            if (messageTranslator.getType().isAssignableFrom(message.getClass())) {
                if (translator != null && messageTranslator.getType().isAssignableFrom(translator.getType())) {
                    continue;
                }

                translator = messageTranslator;
            }
        }

        if (translator == null) {
            if (message instanceof Exception) {
                ((Exception) message).printStackTrace();
            }

            throw new PandaFrameworkException("Cannot translate a message - translator for " + message.getClass() + " not found");
        }

        return translator.translate(this, message);
    }

    @Override
    public void send(Level level, Object message) {
        MessengerMessage generatedMessage = new PandaMessengerMessage(level, message.toString());
        send(generatedMessage);
    }

    @Override
    public void send(MessengerMessage message) {
        outputListener.onMessage(message);
    }

    @Override
    public void addMessageTranslator(MessengerMessageTranslator<Object> translator) {
        translators.add(translator);
    }

    @Override
    public void setOutputListener(MessengerOutputListener listener) {
        this.outputListener = listener;
    }

    @Override
    public MessengerFormatter getMessengerFormatter() {
        return formatter;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

}
