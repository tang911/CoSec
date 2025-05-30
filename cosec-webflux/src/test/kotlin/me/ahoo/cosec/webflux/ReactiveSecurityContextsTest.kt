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

package me.ahoo.cosec.webflux

import me.ahoo.cosec.api.context.SecurityContext
import me.ahoo.cosec.context.SimpleSecurityContext
import me.ahoo.cosec.principal.SimpleTenantPrincipal
import me.ahoo.cosec.webflux.ReactiveSecurityContexts.getSecurityContext
import me.ahoo.cosec.webflux.ReactiveSecurityContexts.getSecurityContextOrEmpty
import me.ahoo.cosec.webflux.ReactiveSecurityContexts.writeSecurityContext
import me.ahoo.test.asserts.assert
import org.hamcrest.MatcherAssert.*
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.kotlin.test.test
import reactor.util.context.Context

class ReactiveSecurityContextsTest {

    @Test
    fun writeSecurityContext() {
        Mono.empty<Void>()
            .writeSecurityContext(SimpleSecurityContext.anonymous())
            .test()
            .expectAccessibleContext()
            .assertThat {
                it.getSecurityContext().principal.assert().isEqualTo(SimpleTenantPrincipal.ANONYMOUS)
            }
            .hasKey(SecurityContext.KEY)
            .then()
            .verifyComplete()
    }

    @Test
    fun getEmpty() {
        Context.empty()
            .getSecurityContextOrEmpty()
            .let {
                it.assert().isNull()
            }
        Assertions.assertThrows(IllegalStateException::class.java) {
            Context.empty().getSecurityContext()
        }
    }
}
