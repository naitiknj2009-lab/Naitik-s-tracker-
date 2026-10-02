package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CheckpointStatus
import com.example.data.InitialData
import com.example.data.SubjectType
import com.example.data.completedCount
import com.example.data.hasRevision
import com.example.data.progressFraction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("JEE Tracker", appName)
  }

  @Test
  fun `initial syllabus data contains all chapters and tests`() {
    assertEquals(29, InitialData.physicsChapters.size)
    assertEquals(22, InitialData.chemistryChapters.size)
    assertEquals(28, InitialData.mathematicsChapters.size)
    assertEquals(32, InitialData.tests.size)
    assertEquals(8, InitialData.weeklyReviews.size)

    assertEquals(SubjectType.PHYSICS, InitialData.physicsChapters.first().subject)
    assertEquals("Units, Dimensions & Measurements", InitialData.physicsChapters.first().name)
    assertEquals("Semiconductor Electronics: Materials, Devices & Simple Circuits", InitialData.physicsChapters.last().name)
  }

  @Test
  fun `chapter checkpoints computation`() {
    val chapter = InitialData.physicsChapters.first().copy(
      lecture = CheckpointStatus.DONE,
      dpp = CheckpointStatus.DONE,
      pyq = CheckpointStatus.REVISE
    )

    assertEquals(2, chapter.completedCount)
    assertTrue(chapter.hasRevision)
    assertEquals(2f / 7f, chapter.progressFraction, 0.001f)
  }
}
