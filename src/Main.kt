package search

import java.io.File

fun main(args: Array<String>) {
    val lines = File(args[1]).readLines()

    InvertedIndexData.fill(lines)

    while (true) {
        println("\n=== Menu ===\n" +
                "1. Find\n" +
                "2. Print all\n" +
                "0. Exit")

        when (readLine()!!) {
            "0" -> {
                println("\nBye!")
                break
            }
            "1" -> {
                println("\nSelect a matching strategy: ALL, ANY, NONE")

                val keyword = readLine()!!

                if (keyword != "ALL") {
                    if (keyword != "ANY") {
                        if (keyword != "NONE") {
                            println("\nIncorrect option! Try again.")
                            continue
                        }
                    }
                }

                println("\nEnter data to search:")

                val dataForSearch = readLine()!!.toLowerCase().split(" ")
                val lineNumbers = when (keyword) {
                    "ALL" -> InvertedIndexData.findAllLines(dataForSearch)
                    "ANY" -> InvertedIndexData.findAnyLines(dataForSearch)
                    else -> InvertedIndexData.findNoneLines(lines, dataForSearch)
                }

                if (lineNumbers.isEmpty()) println("\nNot found.") else {
                    println("\n${lineNumbers.size} found:")
                    for (i in lineNumbers) println(lines[i])
                }
            }
            "2" -> {
                println("\n=== All Data ===\n" +
                        lines.joinToString("\n"))
            }
            else -> println("\nIncorrect option! Try again.")
        }
    }
}

object InvertedIndexData {
    private val map = mutableMapOf<String, MutableList<Int>>()

    fun fill(data: List<String>) {
        for (s in data.joinToString(" ").split(" ")) map[s.toLowerCase()] = mutableListOf()

        for (k in map.keys) {
            for (i in data.indices) if (data[i].contains(k, true)) map[k]!!.add(i)
        }
    }

    fun findAllLines(data: List<String>): Set<Int> {
        for (k in data) if (!map.contains(k)) return setOf()

        val lineNumbers = mutableSetOf<Int>()
        val removingLines = mutableSetOf<Int>()

        for (k in data) map[k]!!.forEach { lineNumbers.add(it) }

        for (i in lineNumbers) {
            var containData = true

            for (k in data) if (!map[k]!!.contains(i)) containData = false

            if (!containData) removingLines.add(i)
        }

        return (lineNumbers - removingLines).toSet()
    }

    fun findAnyLines(data: List<String>): Set<Int> {
        val lineNumbers = mutableSetOf<Int>()

        for (k in data) if(map.contains(k)) map[k]!!.forEach { lineNumbers.add(it) }

        return lineNumbers.toSet()
    }

    fun findNoneLines(lines: List<String>, dataForSearch: List<String>): Set<Int> {
        val lineNumbers = mutableSetOf<Int>()

        for (i in lines.indices) {
            var contain = false

            for (k in dataForSearch) {
                if (lines[i].contains(k, true)) contain = true
            }

            if (!contain) lineNumbers.add(i)
        }

        return lineNumbers.toSet()
    }
}
