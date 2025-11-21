import Modeles.Capacitee
import Modeles.Pokemon
import Modeles.Type

fun main(){
    val pikachu = Pokemon("Pikachu",mutableListOf(Type.ELECTRIQUE), 5, mutableListOf(Capacitee.CHARGE, Capacitee.ATTAQUE_ECLAIR), mutableListOf(1,2,3,4), "description")

    println(pikachu.toString())
}