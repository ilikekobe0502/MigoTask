package com.migo.task.model.repository

import com.migo.task.model.db.dao.PassDao
import com.migo.task.model.enums.PassType
import com.migo.task.model.vo.Pass
import javax.inject.Inject

class MigoDbRepository @Inject constructor(private val passDao: PassDao) {
    fun fetchPass(passType: PassType) = passDao.getPass(passType)
    fun updateData(pass: Pass) = passDao.updateItem(pass)
    fun insertData(pass: Pass) = passDao.insertItem(pass)
}
