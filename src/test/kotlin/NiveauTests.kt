import modeles.classes.EspecePokemon
import modeles.classes.Pokedex
import modeles.classes.Pokemon
import modeles.exceptions.NiveauException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue

class NiveauTests {

    lateinit var pokedex: Pokedex
    lateinit var salamecheE: EspecePokemon
    lateinit var salameche: Pokemon

    @BeforeEach
    fun init(){
        pokedex = Pokedex.chargerDepuisFichier("data/pokedex.json")
        salamecheE = pokedex.especes.first { it.nom == "Salamèche" }
    }

    @Test
    fun monterDeNiveauOK(){
        salameche = Pokemon.creer(salamecheE, 1)
        assertTrue { salameche.monterDeNiveau() }
        assertTrue { salameche.niveau == 2 }
    }

    @Test
    fun monterDeNiveauJusqua100(){
        salameche = Pokemon.creer(salamecheE, 1)
        for(i in 1 until 100){
            assertTrue { salameche.monterDeNiveau() }
        }
        assertTrue { salameche.niveau == 100 }
    }

    @Test
    fun monterDeNiveauAuDessusDe100(){
        salameche = Pokemon.creer(salamecheE, 100)
        assertThrows<NiveauException> { salameche.monterDeNiveau() }
        assertTrue { salameche.niveau == 100 }
    }
}