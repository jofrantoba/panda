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

package org.panda_lang.panda.utilities.commons;

public class TimeUtils {

    public static String toSeconds(long ms) {
        return (ms / 1000.0) + "s";
    }

    public static String toMilliseconds(long nano) {
        return (nano / 1000000.0) + "ms";
    }

    public static float getUptime(long uptime) {
        long current = System.currentTimeMillis() - uptime;
        return current / 1000.0F;
    }

}
