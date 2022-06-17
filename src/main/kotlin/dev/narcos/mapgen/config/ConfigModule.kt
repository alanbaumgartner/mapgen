package dev.narcos.mapgen.config

import org.koin.dsl.module

val CONFIG_MODULE = module {
    single { XteaConfig() }
}