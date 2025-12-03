package modeles.classes

import modeles.exceptions.EquipePokemonException
import modeles.exceptions.JoueurException
import modeles.interfaces.Combattant

class Joueur private constructor(
    override val nom : String,
    override val equipe: MutableList<Pokemon>,
    var argent : Double = 0.0
) : Combattant {

    companion object{
        private var instance: Joueur? = null

        fun creer(nom: String): Joueur {
            if (instance != null) {
                throw JoueurException("Il y a déjà un joueur créé pour cette partie")
            }
            val joueur = Joueur(nom, mutableListOf(), 0.0)
            instance = joueur
            return joueur
        }
    }

    fun ajouterPokemon(pokemon: Pokemon) : Boolean{
        if (this.equipe.size == 6){
            throw EquipePokemonException("L'équipe est déjà au complet")
        } else if (this.equipe.contains(pokemon)){
            throw EquipePokemonException("Le joueur possède déjà ce pokémon")
        }
        this.equipe.add(pokemon)
        return true
    }

    fun supprimerPokemon(pokemon: Pokemon) : Boolean{
        if (this.equipe.isEmpty()){
            throw EquipePokemonException("L'équipe pokémon du joueur est vide")
        } else if (!this.equipe.contains(pokemon)){
            throw EquipePokemonException("Le joueur ne possède pas ce pokémon")
        }
        this.equipe.remove(pokemon)
        return true
    }

    fun gagnerArgent(argent : Double) : Double{
        this.argent+= argent
        return this.argent
    }

    fun perdreArgent(argent : Double) : Double{
        if (this.argent == 0.0){
            throw JoueurException("Le joueur n'a plus d'argent")
        } else if (this.argent <= argent){
            this.argent = 0.0
            return 0.0
        }
        this.argent-=argent
        return this.argent
    }
}