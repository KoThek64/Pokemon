import modeles.classes.CapaciteeDex
import modeles.classes.EspecePokemon
import modeles.classes.Pokedex
import modeles.classes.Pokemon
import modeles.exceptions.PVException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class SoinsDegatsTest {
    lateinit var pokedex: Pokedex
    lateinit var salamecheE: EspecePokemon
    lateinit var salameche: Pokemon
    lateinit var capaciteeDex: CapaciteeDex

    @BeforeEach
    fun init(){
        capaciteeDex = CapaciteeDex.chargerDepuisFichier("data/capacitee.json")
        pokedex = Pokedex.chargerDepuisFichier("data/pokedex.json")
        salamecheE = pokedex.especes.first { it.nom == "Salamèche" }
        salameche = Pokemon.creer(salamecheE, 50, capaciteeDex)
    }

    @Test
    fun degats(){
        salameche.subirDegats(50)
        assertEquals(salameche.stats.pv - 50, salameche.pvActuels)
    }

    @Test
    fun degatsPasMoinsDe0(){
        salameche.subirDegats(salameche.pvActuels)
        assertEquals(0, salameche.pvActuels)
        assertEquals(true, salameche.estKO())

        assertThrows<PVException> {
            salameche.subirDegats(1)
        }
    }

    @Test
    fun soin(){
        salameche.subirDegats(50)
        assertEquals(salameche.stats.pv-50,salameche.pvActuels)

        salameche.soigner(25)
        assertEquals(salameche.stats.pv-25, salameche.pvActuels)
    }

    @Test
    fun soinKO(){
        salameche.subirDegats(salameche.pvActuels)
        assertEquals(0, salameche.pvActuels)
        assertEquals(true, salameche.estKO())

        salameche.soigner(50)
        assertEquals(50, salameche.pvActuels)
        assertEquals(false, salameche.estKO())
    }

    @Test
    fun soinTotal(){
        salameche.subirDegats(75)
        salameche.soinTotal()
        assertEquals(salameche.stats.pv, salameche.pvActuels)
    }

    @Test
    fun soinTotalKO(){
        salameche.subirDegats(salameche.pvActuels)
        assertEquals(0, salameche.pvActuels)
        assertEquals(true, salameche.estKO())

        salameche.soinTotal()
        assertEquals(salameche.stats.pv, salameche.pvActuels)
        assertEquals(false, salameche.estKO())
    }

    @Test
    fun soinException(){
        assertThrows<PVException> {
            salameche.soinTotal()
        }
        assertThrows<PVException> {
            salameche.soigner(1)
        }
    }
}