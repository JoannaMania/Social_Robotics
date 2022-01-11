package furhatos.app.newskill.flow.autobehavior

import furhatos.app.newskill.util.getRandomNearbyLocation
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users
import java.util.*
import furhatos.app.newskill.*
import furhatos.app.newskill.util.StopAutoBehavior
import furhatos.app.newskill.util.gazeAway
import furhatos.event.monitors.MonitorSpeechEnd
import kotlin.math.roundToInt

fun gazeFurhatSpeaking() : State = state(autoBehavior()) {

   onEntry {
       if (users.count > 0) {
           furhat.attend(users.current)
       }
   }

    // Determine every how many milliseconds Furhat should look away
    var duration_g_min = (MU_AGENT_SPEAKS_G - 1).roundToInt()
    var duration_g_max = (MU_AGENT_SPEAKS_G + 1).roundToInt()

    onTime(repeat = duration_g_min..duration_g_max, instant = true) {
        // This will make sure Furhat does some random micromovements with his gaze while attending
        // val duration_x = r.nextGaussian() * STD_AGENT_SPEAKS_X + MU_AGENT_SPEAKS_X
        furhat.attend(users.current.head.location.getRandomNearbyLocation(0.03))

    }

    // Furhat stopped speaking
    onEvent<MonitorSpeechEnd> {
        goto(gazeSilent())
    }

}