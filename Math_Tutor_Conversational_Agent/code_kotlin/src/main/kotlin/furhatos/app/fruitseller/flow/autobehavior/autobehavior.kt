package furhatos.app.fruitseller.flow.autobehavior


import furhatos.app.fruitseller.flow.autobehavior.attendingLocation
import furhatos.app.fruitseller.opinion_neg
import furhatos.app.fruitseller.opinion_pos
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.state
import furhatos.app.fruitseller.util.*
import furhatos.event.senses.SenseSpeechStart
import furhatos.event.monitors.MonitorSpeechStart
import java.io.File

fun autoBehavior(shouldReset : Boolean = true) : State = state {

    init{
        // Read positive and negative opinion words
        var file_name = "opinion_positive.txt"
        File(file_name).readLines().forEach {
            opinion_pos.add(it.replace("\n",""))
        }
        file_name = "opinion_negative.txt"
        File(file_name).readLines().forEach {
            opinion_neg.add(it.replace("\n",""))
        }
    }

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