package com.movableink.app

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test

class DeeplinkMapperTest {
    @Test
    fun knownProductId_returnsProductIdWhenItExistsInTheCatalog() {
        assertEquals("1833330", knownProductId("1833330"))
    }

    @Test
    fun knownProductId_returnsNullWhenTheProductIsMissing() {
        assertNull(knownProductId("123"))
    }
}
