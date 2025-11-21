package Modeles.Interface

import Modeles.Class.Pokemon

interface Combattant {
    val nom : String
    val equipe : MutableList<Pokemon>
}