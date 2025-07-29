package at.nukular.aura

enum class Tischler(val string: String) {
    BERNHARD("Bernhard"),
    CERNE("Cerne"),
    ELLI("Elli"),
    FABI("Fabi"),
    KO("Ko"),
    KRISSI("Krissi"),
    MALE("Male"),
    MICHI("Michi"),
    NIKLAS("Niklas"),
    PETER("Peter"),
    SUSI("Susi"),
    TREVOR("Trevor"),
    ;

    companion object {
        private val map = entries.associateBy(Tischler::string)
        fun fromString(string: String): Tischler? = map[string]
    }
}