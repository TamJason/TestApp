package com.example.testapp.service.local

import com.example.testapp.config.AppConfiguration

object CacheService {
    private var cacheMap = HashMap<String, Long>()

    fun isCacheExpired(key: String): Boolean {
        return cacheMap[key]?.let {
            it + AppConfiguration.CACHE_VALIDITY_MS < System.currentTimeMillis()
        } ?: true
    }

    fun updateCacheValidity(key: String) {
        cacheMap[key] = System.currentTimeMillis()
    }
}