/*
 * Copyright [2021-present] [ahoo wang <ahoowang@qq.com> (https://github.com/Ahoo-Wang)].
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.ahoo.cosec.api.context.request

import me.ahoo.cosec.api.context.Attributes

interface Request : Attributes<Request, String, String> {
    companion object {
        const val APP_ID = "CoSec-App-Id"
        const val DEVICE_ID = "CoSec-Device-Id"
    }

    val appId: String
        get() {
            return getHeader(APP_ID).ifBlank { getQuery(APP_ID) }
        }
    val deviceId: String
        get() {
            return getHeader(DEVICE_ID).ifBlank { getQuery(DEVICE_ID) }
        }
    val path: String
    val method: String
    val remoteIp: String
    val origin: String
    val referer: String
    fun getHeader(key: String): String
    fun getQuery(key: String): String
}
