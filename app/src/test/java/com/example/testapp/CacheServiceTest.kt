package com.example.testapp

import com.example.testapp.service.local.CacheService
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CacheServiceTest {

    @Before
    fun clearCache() {
        CacheService.clearCache()
    }

    @Test
    fun emptyKey() {
        Assert.assertEquals(true, CacheService.isCacheExpired("aaa"))
    }

    @Test
    fun existingKeyNotExpired() {
        val key = "a"
        CacheService.updateCacheValidity(key)
        Assert.assertEquals(false, CacheService.isCacheExpired(key))
    }

    @Test
    fun expiredAfterCacheClear() {
        val key = "a"
        CacheService.updateCacheValidity(key)
        Assert.assertEquals(false, CacheService.isCacheExpired(key))
        CacheService.clearCache()
        Assert.assertEquals(true, CacheService.isCacheExpired(key))
    }
}