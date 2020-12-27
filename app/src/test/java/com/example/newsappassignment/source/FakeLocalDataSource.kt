package com.example.newsappassignment.source

import androidx.lifecycle.MutableLiveData
import com.example.newsappassignment.models.Fact
import com.example.newsappassignment.models.FactCategory
import com.example.newsappassignment.models.Model
import com.example.newsappassignment.source.local.ILocalDataSource
import java.security.MessageDigest
import java.util.*
import kotlin.collections.ArrayList

class FakeLocalDataSource(
    override val factCategory: MutableLiveData<FactCategory>,
    override val allFacts: MutableLiveData<List<Fact>>
) : ILocalDataSource{
    override suspend fun syncDB(data: Model.Result?): Boolean {
        if(data == null){
            return false
        }
        val factCategoryId = UUID.randomUUID().toString()
        factCategory.value = FactCategory(UUID.randomUUID().toString(), data.title)
        for (row in data.rows) {
            if (row.title != null) {
                val fact = Fact(
                    generateHash(row.title!!),
                    row.title!!,
                    row.description,
                    row.imageHref,
                    factCategoryId
                )
            }
        }
        val list = ArrayList<Fact>()
        allFacts.value = list
        return true
    }

    override fun generateHash(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-1").digest(input.toByteArray())
        val result = StringBuilder(bytes.size)
        bytes.forEach { byte ->
            val i = byte.toInt()
            result.append(ILocalDataSource.HEX_CHARS[i shr 4 and 0xF])
            result.append(ILocalDataSource.HEX_CHARS[i and 0XF])
        }

        return bytes.decodeToString()
    }

}