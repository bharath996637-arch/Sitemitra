package com.example

import com.example.data.model.Language
import com.example.data.model.LocaleStrings
import com.example.data.model.TradeSkill
import com.example.data.model.UserRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testRolesAndTradeSkills() {
        assertEquals(4, UserRole.values().size)
        assertEquals(9, TradeSkill.values().size)
        assertNotNull(TradeSkill.MASON.icon)
        assertEquals("Worker", UserRole.WORKER.displayName)
    }

    @Test
    fun testTranslationsAcrossLanguages() {
        // Kannada
        val knMason = LocaleStrings.getTradeName(TradeSkill.MASON, Language.KANNADA)
        assertEquals("ಗಾರೆ ಮೇಸ್ತ್ರಿ / ಕಲ್ಲು ಕೆಲಸ", knMason)

        // Hindi
        val hiMason = LocaleStrings.getTradeName(TradeSkill.MASON, Language.HINDI)
        assertEquals("राजमिस्त्री (चुनाई)", hiMason)

        // Telugu
        val teMason = LocaleStrings.getTradeName(TradeSkill.MASON, Language.TELUGU)
        assertEquals("మేస్త్రీ / రాతి పని", teMason)

        // English
        val enTagline = LocaleStrings.get("tagline", Language.ENGLISH)
        assertTrue(enTagline.contains("Find Work"))
    }

    @Test
    fun testDefaultLocationCoordinates() {
        val defaultLoc = com.example.util.LocationRequestHelper.DEFAULT_BENGALURU_LOCATION
        assertEquals(12.9850, defaultLoc.latitude, 0.001)
        assertEquals(77.7314, defaultLoc.longitude, 0.001)
        assertTrue(defaultLoc.areaName.contains("Whitefield"))
    }

    @Test
    fun testLoginStepEnumAndPhoneSanitization() {
        assertEquals(3, com.example.ui.viewmodel.LoginStep.values().size)
        val rawInput = "+91 98450-78210"
        val sanitized = rawInput.filter { it.isDigit() }.takeLast(10)
        assertEquals("9845078210", sanitized)
        assertEquals(10, sanitized.length)
    }
}
