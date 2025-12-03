package modeles.interfaces

import modeles.classes.Pokemon
import modeles.exceptions.EquipePokemonException

interface Combattant {
    val nom : String
    val equipe : MutableList<Pokemon>

    fun getPokemonActif(): Pokemon{
        return equipe[0]
    }

    fun changerPokemonActif(index : Int){
        if (index == 0){
            throw EquipePokemonException("On ne peut pas echanger un pokemon avec lui même")
        } else if (index >= equipe.size){
            throw EquipePokemonException("l'index choisi est trop grand")
        }
        val pok = equipe[0]
        equipe[0] = equipe[index]
        equipe[index] = pok
    }

    fun aEncoreDesPokemon() : Boolean{
        if (equipe.isEmpty()){
            throw EquipePokemonException("Pas de pokémon dans son equipe")
        }
        for (i in 0 until equipe.size){
            if (!equipe[i].estKo){
                return true
            }
        }
        return false
    }
}