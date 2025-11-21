package modeles.classes

import modeles.interfaces.Combattant

class Adversaire(
    override val nom: String,
    override val equipe: MutableList<Pokemon>
) : Combattant