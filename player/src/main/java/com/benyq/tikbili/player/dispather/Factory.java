/*
 * Copyright (C) 2021 bytedance
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
 *
 * Create Date : 2021/12/3
 */

package com.benyq.tikbili.player.dispather;

class Factory {
    static <T extends Event> T create(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException |
                 NullPointerException e ) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }
}
