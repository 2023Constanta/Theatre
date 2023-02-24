package com.example.theatre.features.favourite.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.theatre.core.domain.model.common.performance.Performance

/**
 * Сущность представления. Для домайна используется [Performance]
 */
@Entity(
    tableName = "favourites"
)
data class FavouritePerformanceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "publication_date")
    val publicationDate: Long? = 0L,
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "short_title")
    val shortTitle: String? = "",
    @ColumnInfo(name = "slug")
    val slug: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "body_text")
    val bodyText: String = "",
    @ColumnInfo(name = "categories")
    val categories: List<String> = listOf(),
    @ColumnInfo(name = "tagline")
    val tagline: String = "",
    @ColumnInfo(name = "age_restriction")
    val ageRestriction: String = "",
    @ColumnInfo(name = "price")
    val price: String = "",
    @ColumnInfo(name = "is_free")
    val isFree: Boolean = false,
    @ColumnInfo(name = "favorites_count")
    val favoritesCount: Int = 0,
    @ColumnInfo(name = "comments_count")
    val commentsCount: Int = 0,
    @ColumnInfo(name = "site_url")
    val siteUrl: String = "",
    @ColumnInfo(name = "tags")
    val tags: List<String> = listOf(),
)
