package Modeles.Class

import Modeles.Interface.Combattant

class Adversaire(
    override val nom: String,
    override val equipe: MutableList<Pokemon>
) : Combattant