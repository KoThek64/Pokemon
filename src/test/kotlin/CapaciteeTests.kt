import modeles.classes.CapaciteeDex
import modeles.classes.EspecePokemon
import modeles.classes.Pokedex
import modeles.classes.Pokemon
import modeles.exceptions.CapaciteeException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CapaciteeTests {

    lateinit var pokedex: Pokedex
    lateinit var salamecheE: EspecePokemon
    lateinit var salameche: Pokemon
    lateinit var capaciteeDex: CapaciteeDex

    @BeforeEach
    fun init(){
        capaciteeDex = CapaciteeDex.chargerDepuisFichier("data/capacitee.json")
        pokedex = Pokedex.chargerDepuisFichier("data/pokedex.json")
        salamecheE = pokedex.especes.first { it.nom == "Salamèche" }
        salameche = Pokemon.creer(salamecheE, 1, capaciteeDex)
    }

    @Test
    fun apprendreCapaciteeOK(){
        assertEquals(salamecheE.capacitesDeBase, salameche.competences.map { it.id })
        assertTrue { salameche.apprendreCapacitee(53, capaciteeDex) }
    }

    @Test
    fun apprendreCapaciteeTrop(){
        assertEquals(salamecheE.capacitesDeBase, salameche.competences.map { it.id })

        assertTrue { salameche.apprendreCapacitee(53, capaciteeDex) }
        assertEquals(2, salameche.competences.size)
        assertTrue { salameche.competences.any { it.id == 53 } }

        assertTrue { salameche.apprendreCapacitee(10, capaciteeDex) } // Griffe
        assertEquals(3, salameche.competences.size)
        assertTrue { salameche.competences.any { it.id == 10 } }

        assertTrue { salameche.apprendreCapacitee(98, capaciteeDex) } // Vive-Attaque
        assertEquals(4, salameche.competences.size)
        assertTrue { salameche.competences.any { it.id == 98 } }

        assertThrows<CapaciteeException> { salameche.apprendreCapacitee(22, capaciteeDex) } // Fouet Lianes
        assertEquals(4, salameche.competences.size)
        assertFalse { salameche.competences.any { it.id == 22 } }

        assertTrue {
            salameche.competences.any { it.id == 53 } &&
            salameche.competences.any { it.id == 33 } &&
            salameche.competences.any { it.id == 98 } &&
            salameche.competences.any { it.id == 10 }
        }
    }

    @Test
    fun apprendreCapaciteeDoublon() {
        assertEquals(salamecheE.capacitesDeBase, salameche.competences.map { it.id })

        assertThrows<CapaciteeException> {
            salameche.apprendreCapacitee(salamecheE.capacitesDeBase[0], capaciteeDex)
        }
    }

    @Test
    fun oublierCapaciteeOK(){
        salameche.apprendreCapacitee(53, capaciteeDex) // Lance-Flammes
        assertTrue {
            salameche.competences.size == 2 &&
            salameche.competences.any { it.id == 33 } && // Charge
            salameche.competences.any { it.id == 53 } // Lance-Flammes
        }

        assertTrue {
            salameche.oublierCapacitee(53)
        }

        assertTrue {
            salameche.competences.size == 1 &&
            salameche.competences.any { it.id == 33 } // Charge
        }
    }

    @Test
    fun testOublieCapaciteeErreurUneSeuleCapacitee(){
        val salameche = Pokemon.creer(salamecheE, 1, capaciteeDex)
        assertEquals(salamecheE.capacitesDeBase.size, salameche.competences.size)
        assertThrows<CapaciteeException> {
            salameche.oublierCapacitee(salameche.competences[0].id)
        }
    }

}