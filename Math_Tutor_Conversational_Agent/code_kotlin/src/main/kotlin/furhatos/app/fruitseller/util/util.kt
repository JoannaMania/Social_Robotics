package furhatos.app.fruitseller.util


import furhatos.gestures.BasicParams
import furhatos.gestures.Gesture
import furhatos.gestures.Gestures
import furhatos.gestures.defineGesture
import furhatos.records.Location
import furhatos.util.Language
import java.util.*
import furhatos.app.fruitseller.*

fun getPosWords(utterance: String): Int{
    var words = utterance.split("\\s+".toRegex())
    var list_of_matches = words.filter { it in opinion_pos}
    return list_of_matches.size
}

fun getNegWords(utterance: String): Int{
    var words = utterance.split("\\s+".toRegex())
    var list_of_matches = words.filter { it in opinion_neg}
    return list_of_matches.size
}

fun Location.getRandomNearbyLocation(amplitude : Double) : Location {
    val locations = mutableListOf<Location>()
    for (x in 0..2) {
        for (y in 0..2) {
            val _x = x * amplitude * z // scale with z
            val _y = y * amplitude / 3 * z // scale with z, and smaller changes on Y-axis (1/3)
            locations.add(this.add(Location(_x, _y, 0.0)))
            locations.add(this.subtract(Location(_x, -_y, 0.0)))
        }
    }
    return locations.shuffled().first()
}

fun gazeAway(duration: Double = 1.0) =
        defineGesture("GazeAway", duration) {
            val pan = r.nextInt(180) - 90
            val tilt = r.nextInt(180) - 90
            frame(0.5 * duration) {
                BasicParams.GAZE_PAN to pan
                BasicParams.GAZE_TILT to tilt
            }
            reset(duration)
        }
/*
fun getProblems(): mapOf{
    var words = utterance.split("\\s+".toRegex())
    var list_of_matches = words.filter { it in opinion_neg}
    return list_of_matches.size
}
*/
/*
// Was trying to get Vader to work.
fun sent(args: Array<String>) {
    val sentences: ArrayList<String?> = object : ArrayList<String?>() {
        init {
            add("VADER is smart, handsome, and funny.")
            add("VADER is smart, handsome, and funny!")
            add("VADER is very smart, handsome, and funny.")
            add("VADER is VERY SMART, handsome, and FUNNY.")
            add("VADER is VERY SMART, handsome, and FUNNY!!!")
            add("VADER is VERY SMART, really handsome, and INCREDIBLY FUNNY!!!")
            add("The book was good.")
            add("The book was kind of good.")
            add("The plot was good, but the characters are uncompelling and the dialog is not great.")
            add("A really bad, horrible book.")
            add("At least it isn't a horrible book.")
            add(":) and :D")
            add("")
            add("Today sux")
            add("Today sux!")
            add("Today SUX!")
            add("Today kinda sux! But I'll get by, lol")
        }
    }
    for (sentence in sentences) {
        println(sentence)
        val sentimentAnalyzer = SentimentAnalyzer(sentence)
        sentimentAnalyzer.analyse()
        System.out.println(sentimentAnalyzer.getPolarity())
    }
}
 */