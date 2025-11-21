package Modeles

class Adversaire(
    override val nom: String,
    override val equipe: MutableList<Pokemon>
) : Combattant