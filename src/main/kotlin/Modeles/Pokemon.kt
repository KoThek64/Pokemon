package Modeles

class Pokemon(
    val nom: String,
    val types: MutableList<Type>,
    val niveau: Int,
    val competences: MutableList<Capacitee>,
    val statistiques: MutableList<Int>,
    val description: String
){



    override fun toString(): String {
        return "$nom est de niveau : $niveau, il possède les capacités : $competences, il est de type : $types"
    }
}