package com.dd.company.dailyplanner.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "plans")
data class PlanEntity(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var userId: Long = 0L,
    var icon: String = "",
    var name: String = "",
    var content: String = "",
    var startTime: Long = 0L,
    var endTime: Long = 0L,
    var type: Int = 0
) : Serializable {

    constructor(
        userId: Long,
        icon: String,
        name: String,
        content: String,
        startTime: Long,
        endTime: Long,
        type: Int
    ) : this(null, userId, icon, name, content, startTime, endTime, type)

}