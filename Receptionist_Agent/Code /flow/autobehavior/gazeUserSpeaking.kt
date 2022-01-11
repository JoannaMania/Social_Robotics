package furhatos.app.newskill.flow.autobehavior

import furhatos.app.newskill.*
import furhatos.app.newskill.util.gazeAway
import furhatos.app.newskill.util.getRandomNearbyLocation
import furhatos.event.monitors.MonitorSpeechEnd
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users
import java.util.*
import furhatos.event.senses.SenseSpeechEnd
import kotlin.math.roundToInt

fun gazeUserSpeaking() : State = state(autoBehavior()) {

   onEntry {
       if (users.count > 0) {
           furhat.attend(users.current)
       }
   }

    // Determine every how many milliseconds Furhat should look away while user is speaking
    var duration_g_min = (MU_USER_SPEAKS_G - 1).roundToInt()
    var duration_g_max = (MU_USER_SPEAKS_G + 1).roundToInt()

    onTime(repeat = duration_g_min..duration_g_max, instant = true) {
        // This will make sure Furhat does some random micromovements with his gaze while attending
        val duration_x = r.nextGaussian() * STD_USER_SPEAKS_X + MU_USER_SPEAKS_X
        /*
        furhat.glance(users.current.head.location.getRandomNearbyLocation(100.0), duration = duration_x,
                async = true)
        furhat.attend(users.current.head.location.getRandomNearbyLocation(0.03))
         */
        furhat.gesture(gazeAway(duration_x), async = true)

    }

    // User stopped speaking
    onEvent<SenseSpeechEnd> {
        goto(gazeSilent())
    }

}