package com.kasukusakura.yggdrasilgateway.yggdrasil.remote

import com.kasukusakura.yggdrasilgateway.yggdrasil.data.PlayerProfile
import io.ktor.http.*

public interface YggdrasilService {
    /** Domains allowed for texture URLs advertised by the upstream service. */
    public suspend fun skinDomains(): Set<String> = emptySet()

    public suspend fun hasJoined(params: Parameters): PlayerProfile?
    public suspend fun queryProfile(uuid: String, unsigned: Boolean): PlayerProfile?
    public suspend fun batchQuery(name: List<String>): List<PlayerProfile>
}