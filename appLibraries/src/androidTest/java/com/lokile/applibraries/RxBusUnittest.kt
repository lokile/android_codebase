package com.lokile.applibraries

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lokile.applibraries.managers.RxBus
import com.lokile.applibraries.managers.RxBusListener
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RxBusUnittest {
    val busListener by lazy {
        object : RxBusListener {
            override val uuid = 1
        }
    }

    @Before
    fun setup() {

    }

    @After
    fun teardown() {
        RxBus.INSTANCE.unregister(busListener)
    }

    @Test
    fun testRxBus() {
        //register event
        var received = 0
        RxBus.INSTANCE.register<Int>(busListener) {
            if (it == 2) {
                received++
            }
        }

        RxBus.INSTANCE.post(2)
        RxBus.INSTANCE.post(2)
        RxBus.INSTANCE.post(2)
        Thread.sleep(500)
        assertTrue(received == 3)

        //unregister event
        received = 0
        RxBus.INSTANCE.unregister(busListener)
        RxBus.INSTANCE.post(2)
        RxBus.INSTANCE.post(2)
        RxBus.INSTANCE.post(2)
        Thread.sleep(500)
        assertTrue(received == 0)
    }

    @Test
    fun testUUIDAllocation() {
        val uuid1 = RxBus.INSTANCE.newUUID()
        val uuid2 = RxBus.INSTANCE.newUUID()
        val uuid3 = RxBus.INSTANCE.newUUID()
        assert(uuid1 == uuid2 - 1)
        assert(uuid2 == uuid3 - 1)
    }
}