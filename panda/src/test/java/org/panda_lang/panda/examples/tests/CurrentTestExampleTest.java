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

package org.panda_lang.panda.examples.tests;

import org.junit.jupiter.api.Test;
import org.panda_lang.panda.examples.ExamplesLauncher;

class CurrentTestExampleTest {

    @Test
    void helloWorld() {
        ExamplesLauncher.launch("", "hello_world.panda");
    }

    @Test
    void literalMethod() {
        ExamplesLauncher.launch("tests", "literal_methods.panda");
    }

    @Test
    void testCurrentTest() {
        for (int i = 0; i < 1; i++) {
            ExamplesLauncher.launch("tests", "current_test.panda");
        }
    }

    @Test
    void testClassTest() {
        ExamplesLauncher.launch("tests", "class_test.panda");
    }

}
