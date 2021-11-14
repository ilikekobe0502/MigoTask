package com.migo.task.model.db

import android.content.Context
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.migo.task.model.db.dao.PassDao
import com.migo.task.model.enums.PassType
import com.migo.task.model.vo.Pass
import com.migo.task.ui.pass.DAYS_PRICE
import com.migo.task.ui.pass.HOURS_PRICE
import junit.framework.TestCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest : TestCase() {

    private lateinit var db: PassDatabase
    private lateinit var passDao: PassDao

    private val fakeDayData = Pass(
        id = 1,
        type = PassType.TYPE_DAY,
        timeValue = 1,
        price = DAYS_PRICE * 1,
    )
    private val fakeHourData1 = Pass(
        id = 2,
        type = PassType.TYPE_HOUR,
        timeValue = 1,
        price = HOURS_PRICE * 1,
    )
    private val fakeHourData2 = Pass(
        id = 3,
        type = PassType.TYPE_HOUR,
        timeValue = 2,
        price = HOURS_PRICE * 2,
    )

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PassDatabase::class.java).build()
        passDao = db.PassDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertPass() = runBlocking {
        db.withTransaction {
            passDao.insertItem(fakeDayData)
            val dayList = passDao.getPass(PassType.TYPE_DAY)
            dayList.collect {
                assertEquals(it.size, 1)
            }

        }
    }

    @Test
    fun getAllPassByType() = runBlocking {
        passDao.insertItem(fakeDayData)
        passDao.insertItem(fakeHourData1)
        passDao.insertItem(fakeHourData2)

        passDao.getPass(PassType.TYPE_DAY).collect {
            assertEquals(it.filter { item -> item.type == PassType.TYPE_DAY }.size, 1)
        }

        passDao.getPass(PassType.TYPE_HOUR).collect {
            assertEquals(it.filter { item -> item.type == PassType.TYPE_HOUR }.size, 2)
        }
    }
}