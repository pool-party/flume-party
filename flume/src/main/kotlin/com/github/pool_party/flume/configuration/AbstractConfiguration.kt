package com.github.pool_party.flume.configuration

import com.natpryce.konfig.*
import kotlin.reflect.KProperty
import kotlin.time.Duration
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.overriding

abstract class AbstractConfiguration {

    val APP_URL by string()
    val USERNAME by string()
    val PORT by int()
    val HOST by string()

    val DEBUG by boolean()

    val LONGPOLL by boolean()

    val TELEGRAM_TOKEN by string()

    private val configuration = EnvironmentVariables()
        .overriding(ConfigurationProperties.fromResource("default.properties"))
        .let {
            val testProperties = "test.properties"
            if (ClassLoader.getSystemClassLoader().getResource(testProperties) != null)
                ConfigurationProperties.fromResource(testProperties) overriding it
            else it
        }

    init {
        if (DEBUG) {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "DEBUG")
        }
    }

    protected fun <K> boolean() = Configured<Boolean, K>(booleanType)

    protected fun <K> int() = Configured<Int, K>(intType)

    protected fun <K> long() = Configured<Long, K>(longType)

    protected fun <K> string() = Configured<String, K>(stringType)

    protected fun <K> double() = Configured<Double, K>(doubleType)

    protected fun <K> seconds() = ConfiguredDuration<K>("seconds", Duration::seconds)

    private val <T> KProperty<T>.configName
        get() = name.lowercase().replace('_', '.')

    inner class Configured<T, K>(private val parse: (PropertyLocation, String) -> T) {

        private var value: T? = null

        operator fun getValue(thisRef: K, property: KProperty<*>): T {
            if (value == null) {
                value = configuration[Key(property.configName, parse)]
            }
            return value!!
        }

        operator fun setValue(thisRef: K, property: KProperty<*>, value: T) {
            this.value = value
        }
    }

    inner class ConfiguredDuration<K>(private val name: String, private val duration: (Int) -> Duration) {

        private var value: Duration? = null

        operator fun getValue(thisRef: K, property: KProperty<*>): Duration {
            if (value == null) {
                value = duration(configuration[Key("${property.configName}.$name", intType)])
            }
            return value!!
        }
    }
}
