internal fun enumToString(colour: Colour) : String {
    return colour.toString().lowercase().replaceFirstChar { it.uppercase() }
}

internal fun stringToEnum(colour: String) : Colour {
    return Colour.values().asList().stream()
        .filter { it.name == colour.uppercase() }
        .findFirst()
        .orElseThrow { throw UnsupportedOperationException("Wrong colour! Please, choose: RED, BLUE or GREEN") }
}