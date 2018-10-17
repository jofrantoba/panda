/*
 * Copyright (c) 2015-2018 Dzikoysk
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

public class ArrayUtils {

    /**
     * Check if the specified array contains null
     *
     * @return the array to search
     */
    public static boolean containsNull(Object[] array) {
        return contains(array, null);
    }

    /**
     * Check if the specified array contains the element
     *
     * @param array the array to search
     * @param element the element to search for
     * @return true if the specified array contains the element, otherwise false
     */
    public static boolean contains(Object[] array, Object element) {
        for (Object arrayElement : array) {
            if (element == null) {
                if (arrayElement == null) {
                    return true;
                }

                continue;
            }

            if (element.equals(arrayElement)) {
                return true;
            }
        }

        return false;
    }

}