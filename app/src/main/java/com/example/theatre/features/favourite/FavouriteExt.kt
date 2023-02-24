package com.example.theatre.features.favourite

import com.example.theatre.core.domain.model.common.performance.Performance
import com.example.theatre.features.favourite.data.room.entity.FavouritePerformanceEntity

fun FavouritePerformanceEntity.toPerformance(): Performance =
    Performance(
        id = id,
        publicationDate = publicationDate,
        dates = listOf(),
        title = title,
        shortTitle = shortTitle,
        slug = slug,
        place = null,
        description = description,
        bodyText = bodyText,
        location = null,
        categories = categories,
        tagline = tagline,
        ageRestriction = ageRestriction,
        price = price,
        isFree = isFree,
        images = listOf(),
        favoritesCount = favoritesCount,
        commentsCount = commentsCount,
        siteUrl = siteUrl,
        tags = tags,
        participants = listOf()
    )

fun Performance.toEntity(): FavouritePerformanceEntity =
    FavouritePerformanceEntity(
        id = id,
        publicationDate = publicationDate,
        title = title,
        shortTitle = shortTitle,
        slug = slug!!,
        description = description!!,
        bodyText = bodyText!!,
        categories = categories!!,
        tagline = tagline!!,
        ageRestriction = ageRestriction!!,
        price = price!!,
        isFree = isFree!!,
        favoritesCount = favoritesCount!!,
        commentsCount = commentsCount!!,
        siteUrl = siteUrl!!,
        tags = tags!!,
    )