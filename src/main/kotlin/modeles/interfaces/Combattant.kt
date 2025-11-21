package modeles.interfaces

import modeles.classes.Pokemon

interface Combattant {
    val nom : String
    val equipe : MutableList<Pokemon>
}