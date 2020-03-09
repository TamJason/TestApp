package com.example.testapp.service.local

object CacheService {
    private var cacheMap = HashMap<String, Long>()
    private const val CACHE_VALIDITY = 60 * 1000 // 1 Min

    fun isCacheExpired(key: String): Boolean {
        return cacheMap[key]?.let {
            it + CACHE_VALIDITY < System.currentTimeMillis()
        } ?: true
    }

    fun updateCacheValidity(key: String) {
        cacheMap[key] = System.currentTimeMillis()
    }
}