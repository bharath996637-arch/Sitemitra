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

    @Test
    fun testGoogleServerEndpoint() {
        val serverUrl = com.example.data.remote.SiteMitraServerConfig.GOOGLE_SERVER_WEB_URL
        assertEquals("https://ais-pre-teslros233v7olr72hky2x-119054699939.asia-northeast1.run.app", serverUrl)
        assertTrue(serverUrl.startsWith("https://"))
        assertEquals("Google Cloud Run (asia-northeast1)", com.example.data.remote.SiteMitraServerConfig.SERVER_REGION)
        assertEquals("http://10.0.2.2:3000/", com.example.data.remote.SiteMitraServerConfig.LOCAL_EMULATOR_SERVER_URL)
    }

    @Test
    fun testDataServerEntityDtoRoundTrip() {
        val job = com.example.data.local.entity.JobEntity(
            id = 42L,
            siteId = 1L,
            supervisorId = 2L,
            title = "Tile Masons Needed",
            category = "Mason",
            wagePerDay = 980,
            vacancies = 4,
            hiredCount = 1,
            siteName = "Nalini Towers",
            siteAddress = "Whitefield",
            area = "Whitefield",
            supervisorName = "Mestri Govindappa",
            supervisorPhone = "+91 98451 22340"
        )
        val roundTripped = com.example.data.remote.JobDto(
            id = job.id,
            siteId = job.siteId,
            supervisorId = job.supervisorId,
            title = job.title,
            category = job.category,
            wagePerDay = job.wagePerDay,
            vacancies = job.vacancies,
            hiredCount = job.hiredCount,
            siteName = job.siteName,
            siteAddress = job.siteAddress,
            area = job.area,
            supervisorName = job.supervisorName,
            supervisorPhone = job.supervisorPhone
        )
        assertEquals(job.id, roundTripped.id)
        assertEquals(job.title, roundTripped.title)
        assertEquals(job.wagePerDay, roundTripped.wagePerDay)
        assertEquals(job.siteName, roundTripped.siteName)
    }
}
