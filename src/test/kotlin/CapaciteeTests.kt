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

    @BeforeEach
    fun init(){
        pokedex = Pokedex.chargerDepuisFichier("data/pokedex.json")
        salamecheE = pokedex.especes.first { it.nom == "Salamèche" }
        salameche = Pokemon.creer(salamecheE, 1)
    }

    @Test
    fun apprendreCapaciteeOK(){
        assertEquals(salamecheE.capacitesDeBase, salameche.competences)
        assertTrue { salameche.apprendreCapacitee(Capacitee.LANCE_FLAMMES) }
    }

    @Test
    fun apprendreCapaciteeTrop(){
        assertEquals(salamecheE.capacitesDeBase, salameche.competences)

        assertTrue { salameche.apprendreCapacitee(Capacitee.LANCE_FLAMMES) }
        assertEquals(2, salameche.competences.size)
        assertTrue { salameche.competences.contains(Capacitee.LANCE_FLAMMES) }

        assertTrue { salameche.apprendreCapacitee(Capacitee.GRIFFE) }
        assertEquals(3, salameche.competences.size)
        assertTrue { salameche.competences.contains(Capacitee.GRIFFE) }

        assertTrue { salameche.apprendreCapacitee(Capacitee.ATTAQUE_ECLAIR) }
        assertEquals(4, salameche.competences.size)
        assertTrue { salameche.competences.contains(Capacitee.ATTAQUE_ECLAIR) }

        assertThrows<CapaciteeException> { salameche.apprendreCapacitee(Capacitee.FOUET_LIANES) }
        assertEquals(4, salameche.competences.size)
        assertFalse { salameche.competences.contains(Capacitee.FOUET_LIANES) }

        assertTrue {
            salameche.competences.contains(Capacitee.LANCE_FLAMMES)
            salameche.competences.contains(Capacitee.CHARGE)
            salameche.competences.contains(Capacitee.ATTAQUE_ECLAIR)
            salameche.competences.contains(Capacitee.GRIFFE)
        }
    }

    @Test
    fun apprendreCapaciteeDoublon() {
        assertEquals(salamecheE.capacitesDeBase, salameche.competences)

        assertThrows<CapaciteeException> {
            salameche.apprendreCapacitee(salamecheE.capacitesDeBase[0])
        }
    }

    @Test
    fun oublierCapaciteeOK(){
        salameche.apprendreCapacitee(Capacitee.LANCE_FLAMMES)
        assertTrue {
            salameche.competences.size == 2
            salameche.competences.contains(Capacitee.CHARGE)
            salameche.competences.contains(Capacitee.LANCE_FLAMMES)
        }

        assertTrue {
            salameche.oublierCapacitee(Capacitee.LANCE_FLAMMES)
        }

        assertTrue {
            salameche.competences.size == 1
            salameche.competences.contains(Capacitee.CHARGE)
        }
    }

    @Test
    fun testOublieCapaciteeErreurUneSeuleCapacitee(){
        val salameche = Pokemon.creer(salamecheE, 1)
        assertEquals(salamecheE.capacitesDeBase.size, salameche.competences.size)
        assertThrows<CapaciteeException> {
            salameche.oublierCapacitee(salameche.competences[0])
        }
    }

}