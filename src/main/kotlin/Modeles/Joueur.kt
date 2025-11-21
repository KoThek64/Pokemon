package Modeles

class Joueur(
    override val nom : String,
    override val equipe: MutableList<Pokemon>,
    var argent : Double = 0.0
) : Combattant