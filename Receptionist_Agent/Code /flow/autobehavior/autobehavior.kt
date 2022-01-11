package furhatos.app.newskill.flow.autobehavior

import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.app.newskill.util.*
import furhatos.event.senses.SenseSpeechStart
import furhatos.event.monitors.MonitorSpeechStart

fun autoBehavior(shouldReset : Boolean = true) : State = state {
    onEntry {
        if (shouldReset) {
            furhat.attendNobody()
        }
    }

    onEvent<SenseSpeechStart> {
        goto(gazeUserSpeaking())
    }

    onEvent<MonitorSpeechStart> {
        goto(gazeFurhatSpeaking())
    }

    onEvent<StopAutoBehavior> {
        goto(autoBehavior(shouldReset = false))
    }

    onEvent<LookAround> {
        goto(lookingAround())
    }

    onEvent<AttendLocation> {
        goto(attendingLocation(location = it.location))
    }
}