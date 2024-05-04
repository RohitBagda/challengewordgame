package com.rohitbagda.challenge

import android.content.ContentValues
import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader


class WordSearchService {
    companion object {
        private val ALPHABETS_REGEX = "[a-z]+".toRegex()
    }
    val words: MutableList<String> = mutableListOf()

    fun load(context: Context) {
        // Specify the resource ID of the raw file containing words (assuming it's named words.txt)
        val resourceId = R.raw.words

        // Read words from the raw resource file and insert them into a list
        context.resources.openRawResource(resourceId).use { inputStream ->
            val reader = BufferedReader(InputStreamReader(inputStream))
            var word: String? = reader.readLine()
            while (!word.isNullOrBlank()) {
                if (word.matches(ALPHABETS_REGEX)) {
                    words.add(word.trim())
                }
                word = reader.readLine()
            }
        }
    }

    fun wordCanBeContinued(word: String): WordSearchResult {
        val lowercaseWord = word.lowercase()
        Log.i(ContentValues.TAG, "Word count: ${words.size}")
        val result = words.filter { it.startsWith(lowercaseWord) }

        if (result.isEmpty()) {
            return WordSearchResult(
                result = SearchResult.DOES_NOT_EXIST,
                children = search(word.dropLast(1))
            )
        }

        if (result.distinct().size == 1 && result.first() == lowercaseWord) {
            return WordSearchResult(result = SearchResult.HAS_NO_CHILDREN)
        }

        if (result.distinct().size == 1 && result.first() != lowercaseWord) {
            return WordSearchResult(result = SearchResult.HAS_NO_CHILDREN_NOT_YET_COMPLETE)
        }

       return WordSearchResult(
           result = SearchResult.HAS_CHILDREN,
           children = result
       )
    }

    fun search(word: String): List<String> {
        return words.filter { it.startsWith(word) }
    }
}
