package furhatos.app.fruitseller.flow.autobehavior

import furhatos.app.fruitseller.*
import furhatos.app.fruitseller.util.getRandomNearbyLocation
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users
import java.util.*
import furhatos.event.monitors.MonitorSpeechStart
import furhatos.event.senses.SenseSpeechEnd
import furhatos.event.senses.SenseSpeechStart
import kotlin.math.roundToInt

fun gazeSilent() : State = state(autoBehavior()) {
    onEntry {
        if (users.count > 0) {
            furhat.attend(users.current)
        }
    }

    /* Choose random interval for length of gazing at user
    val duration_g_a = (r.nextGaussian() * STD_NOB_G + MU_NOB_G).roundToInt()
    val duration_g_b = (r.nextGaussian() * STD_NOB_G + MU_NOB_G).roundToInt()
    var duration_g_min = minOf(duration_g_a, duration_g_b) * 10
    var duration_g_max = maxOf(duration_g_a, duration_g_b) * 10
     */

    // To prevent intervals being to short, just use mean +- 1
    var duration_g_min = (MU_NOB_G - 1).roundToInt()
    var duration_g_max = (MU_NOB_G + 1).roundToInt()

    onTime(repeat = duration_g_min..duration_g_max, instant = true) {
        // This will make sure Furhat does some random micromovements with his gaze while attending
        // val duration_x = (r.nextGaussian() * STD_NOB_X + MU_NOB_X).roundToInt()
        // furhat.glance(users.current.head.location.getRandomNearbyLocation(100.0), duration = duration_x, async = true)
        furhat.attend(users.current.head.location.getRandomNearbyLocation(0.03))
    }

    /* Monitors when user starts to speak */
    onEvent<SenseSpeechStart> {
        goto(gazeUserSpeaking())
    }

    // When Furhat starts speaking
    onEvent<MonitorSpeechStart> {
        goto(gazeFurhatSpeaking())
    }

}