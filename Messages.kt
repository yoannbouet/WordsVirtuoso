package wordsvirtuoso

enum class Messages(val msg: String) {
    InputFiveLetters("Input a 5-letter word:"),
    InputErrorNotFiveletters("The input isn't a 5-letter word."),
    InputErrorInvalidChars("One or more letters of the input aren't valid."),
    InputErrorDuplicate("The input has duplicate letters."),
    InputErrorNotInList("The input word isn't included in my words list."),
    InputErrorFileNotExist("Error: The %s file %s doesn't exist."),
    ArgsError("Error: Wrong number of arguments."),
    CandidatesAbsent("Error: %s candidate words are not included in the %s file."),
    InvalidWords("Error: %s invalid words were found in the %s file."),
    SecretWordFound("Correct!"),
    Score("The solution was found after %s tries in %s seconds."),
    Lucky("Amazing luck! The solution was found at once."),
    Virtuoso("Words Virtuoso"),
    GameOver("The game is over.")
}