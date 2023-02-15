package wordsvirtuoso

import java.io.File
import java.io.IOException
import kotlin.random.Random
import kotlin.system.exitProcess

class WordsVirtuoso (args: Array<String>) {
    private lateinit var words: String
    private lateinit var candidates: String
    private var wordsSet = setOf<String>()
    private var candidatesSet = setOf<String>()
    private val inputRejectsSet = mutableSetOf<Char>()
    private val inputCluesList = mutableListOf<String>()

    init {
        if (args.size != 2) Messages.ArgsError.show()
        gameStartup(args)
        val secretWord = candidatesSet.elementAt(Random.nextInt(candidatesSet.size)).lowercase()
        gameOngoing(secretWord)
    }

    private fun gameOngoing(secret: String, attempts: Int = 1, timeUsed: Int = 0) {
        Messages.InputFiveLetters.show()
        val start = System.currentTimeMillis()
        val input = readln().lowercase().also { str ->
            if (str == EXIT) Messages.GameOver.show()
            else if (!isWordValid(str, true)) {
                gameOngoing(secret, attempts + 1,
                    timeUsed + (System.currentTimeMillis() - start).toInt())
            } else if (str !in wordsSet) {
                Messages.InputErrorNotInList.show()
                gameOngoing(secret, attempts + 1,
                    timeUsed + (System.currentTimeMillis() - start).toInt())
            }
        }

        inputCluesList.add(input.lowercase())
        println()
        inputCluesList.forEach { str ->
            str.forEachIndexed { i, ch ->
                if (ch == secret[i]) print("\u001b[48:5:10m${ch.uppercase()}\u001B[0m")
                else if (ch in secret) print("\u001b[48:5:11m${ch.uppercase()}\u001B[0m")
                else {
                    print("\u001b[48:5:7m${ch.uppercase()}\u001B[0m")
                    inputRejectsSet.add(ch)
                }
            }
        }
        println()

        if (input == secret) {
            Messages.SecretWordFound.show()
            if (attempts == 1) Messages.Lucky.show()
            else Messages.Score.show(attempts.toString(), timeUsed.toString())
        } else {
            println("\u001B[48:5:14m${inputRejectsSet.sorted().joinToString(EMPTY).uppercase()}\u001B[0m\n")
            gameOngoing(secret, attempts + 1, timeUsed + (System.currentTimeMillis() - start).toInt())
        }
    }

    private fun gameStartup(args: Array<String>) {
        words = fileCheck(args[0], "words")
        candidates = fileCheck(args[1], "candidate words")
        wordsSet = File(words).readLines().map { it.lowercase() }.toSet()
        candidatesSet = File(candidates).readLines().map { it.lowercase() }.toSet()

        invalidsCheck(words, wordsSet)
        invalidsCheck(candidates, candidatesSet)

        candidatesSet.minus(wordsSet).toSet().let { set ->
            if (set.isNotEmpty()) Messages.CandidatesAbsent.show(set.size.toString(), words)
        }

        Messages.Virtuoso.show()
    }

    private fun invalidsCheck(file: String, set: Set<String>) {
        val invalids = set.count { !isWordValid(it, false) }
        if (invalids > 0) Messages.InvalidWords.show(invalids.toString(), file)
    }

    private fun isWordValid(str: String, print: Boolean): Boolean {
        return if (str.length != 5) {
            if (print) Messages.InputErrorNotFiveletters.show()
            false
        } else if (!Regex("^[a-z]+$").matches(str)) {
            if (print) Messages.InputErrorInvalidChars.show()
            false
        } else if (Regex("([a-z])([a-z]+)?\\1").find(str) != null) {
            if (print) Messages.InputErrorDuplicate.show()
            false
        } else true
    }

    private fun fileCheck(input: String, name: String = EMPTY): String {
        try {
            File(input).readLines()
        } catch (e: IOException) {
            Messages.InputErrorFileNotExist.show(name, input)
        }
        return input
    }

    private fun Messages.show(str1: String = EMPTY, str2: String = EMPTY) {
        println(this.msg.format(str1, str2))
        val exitMessages = listOf(
            Messages.CandidatesAbsent, Messages.InputErrorFileNotExist, Messages.InvalidWords,
            Messages.ArgsError, Messages.GameOver, Messages.Score, Messages.Lucky
        )
        if (this in exitMessages) exitProcess(0)
    }

    companion object {
        const val UNDERSCORE = '_'
        const val EMPTY = ""
        const val EXIT = "exit"
    }
}

fun main(args: Array<String>) {
    WordsVirtuoso(args)
}