package com.example.theatre.features.spectacles.presentation.ui.list

object SearchQueryConstructor {

    fun create(
        searchQuery: String,
    ): String {
        val params = StringBuilder()
        params.apply {
            append(searchQuery)
            append(other)
        }
        return params.toString()
    }

    private const val other = "&categories=theater&ctype=event"

}