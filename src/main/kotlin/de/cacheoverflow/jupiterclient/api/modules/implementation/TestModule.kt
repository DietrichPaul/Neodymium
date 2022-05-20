package de.cacheoverflow.jupiterclient.api.modules.implementation

import de.cacheoverflow.jupiterclient.JupiterClient
import de.cacheoverflow.jupiterclient.api.modules.Module

@Module.Declaration("Test", "module.test.description", Module.EnumCategory.COMBAT)
class TestModule(private val client: JupiterClient) : Module(client)