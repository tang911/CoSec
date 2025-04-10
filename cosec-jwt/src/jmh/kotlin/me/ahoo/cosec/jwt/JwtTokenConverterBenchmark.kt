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
package me.ahoo.cosec.jwt

import com.auth0.jwt.algorithms.Algorithm
import me.ahoo.cosec.api.token.CompositeToken
import me.ahoo.cosec.api.token.TokenTenantPrincipal
import me.ahoo.cosec.jwt.Jwts.toPrincipal
import me.ahoo.cosec.principal.SimpleTenantPrincipal
import me.ahoo.cosid.test.MockIdGenerator
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Threads

/**
 * JwtTokenConverterBenchmark .
 *
 *
 * ----------------------------- Report --------------------------------
 *
 * <pre>
 * # JMH version: 1.33
 * # VM version: JDK 11.0.13, OpenJDK 64-Bit Server VM, 11.0.13+8-LTS
 * # VM invoker: /Library/Java/JavaVirtualMachines/zulu-11.jdk/Contents/Home/bin/java
 * # VM options: <none>
 * # Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
 * # Warmup: 5 iterations, 10 s each
 * # Measurement: 5 iterations, 10 s each
 * # Timeout: 10 min per iteration
 * # Threads: 6 threads, will synchronize iterations
 * # Benchmark mode: Throughput, ops/time
 * ---------------------------------------------------------------------
 * Benchmark                   Mode  Cnt        Score        Error  Units
 * asPrincipalWithNoneVerify  thrpt   25  3413361.362 ± 274500.733  ops/s
 * asPrincipalWithVerify      thrpt   25   963915.375 ±  80948.670  ops/s
 * ---------------------------------------------------------------------
 * </pre>
 *
 * @author ahoo wang
 */
@State(Scope.Benchmark)
@Threads(6)
open class JwtTokenConverterBenchmark {
    private lateinit var algorithm: Algorithm
    private lateinit var jwtTokenConverter: JwtTokenConverter
    private lateinit var jwtTokenVerifier: JwtTokenVerifier
    private lateinit var token: CompositeToken

    @Setup
    fun init() {
        algorithm = Algorithm.HMAC256("FyN0Igd80Gas8stTavArGKOYnS9uLWGA_")
        jwtTokenConverter = JwtTokenConverter(MockIdGenerator.INSTANCE, algorithm)
        jwtTokenVerifier = JwtTokenVerifier(algorithm)
        token = jwtTokenConverter.toToken(SimpleTenantPrincipal.ANONYMOUS)
    }

    @Benchmark
    fun asPrincipalWithVerify(): TokenTenantPrincipal {
        return jwtTokenVerifier.verify(token)
    }

    @Benchmark
    fun asPrincipalWithNoneVerify(): TokenTenantPrincipal {
        return toPrincipal(token.accessToken)
    }
}
