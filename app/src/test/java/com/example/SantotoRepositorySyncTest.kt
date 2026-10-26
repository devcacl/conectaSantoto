package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.SantotoDatabase
import com.example.data.model.Lead
import com.example.data.model.LeadStatus
import com.example.data.repository.SantotoRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class SantotoRepositorySyncTest {

    private lateinit var db: SantotoDatabase
    private lateinit var repo: SantotoRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, SantotoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repo = SantotoRepository(db.santotoDao())
    }

    @After
    fun tearDown() = db.close()

    private fun lead(remoteId: String?, status: LeadStatus = LeadStatus.NUEVO, createdAt: Long = System.currentTimeMillis()) =
        Lead(
            fullName = "Aspirante $remoteId",
            phone = "3001234567",
            email = "",
            cityOrSchool = "Colegio",
            programInterest = "Derecho",
            status = status,
            createdAt = createdAt,
            remoteId = remoteId
        )

    @Test
    fun `insertLead asigna un remoteId para poder sincronizar`() = runBlocking {
        repo.insertLead(lead(remoteId = null))
        val saved = repo.allLeads.first().single()
        assertNotNull(saved.remoteId)
    }

    @Test
    fun `leads de ejemplo sin remoteId no chocan entre si`() = runBlocking {
        db.santotoDao().insertLeads(listOf(lead(null), lead(null)))
        assertEquals(2, repo.allLeads.first().size)
    }

    @Test
    fun `aplicar el mismo snapshot dos veces no duplica y actualiza el estado`() = runBlocking {
        repo.applyRemoteLeads(listOf(lead("a")))
        repo.applyRemoteLeads(listOf(lead("a", status = LeadStatus.ADMITIDO)))

        val leads = repo.allLeads.first()
        assertEquals(1, leads.size)
        assertEquals(LeadStatus.ADMITIDO, leads.single().status)
    }

    @Test
    fun `un lead borrado en la nube se borra localmente si no es reciente`() = runBlocking {
        val old = System.currentTimeMillis() - 10 * 60 * 1000L
        repo.applyRemoteLeads(listOf(lead("a", createdAt = old), lead("b", createdAt = old)))

        repo.applyRemoteLeads(listOf(lead("b", createdAt = old)))

        assertEquals(listOf("b"), repo.allLeads.first().map { it.remoteId })
    }

    @Test
    fun `un lead recien creado no se borra aunque aun no aparezca en el snapshot`() = runBlocking {
        repo.insertLead(lead(remoteId = null))

        repo.applyRemoteLeads(emptyList())

        assertEquals(1, repo.allLeads.first().size)
    }

    @Test
    fun `los datos de ejemplo sin remoteId nunca se borran por un snapshot`() = runBlocking {
        db.santotoDao().insertLead(lead(null, createdAt = 0L))

        repo.applyRemoteLeads(emptyList())

        assertEquals(1, repo.allLeads.first().size)
    }
}
