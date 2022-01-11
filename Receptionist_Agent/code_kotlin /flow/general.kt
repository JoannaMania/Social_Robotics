package furhatos.app.newskill.flow

import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.Voice
import furhatos.gestures.BasicParams
import furhatos.gestures.defineGesture
import furhatos.util.Gender
import furhatos.util.Language
val Idle: State = state {

    init {
        furhat.setVoice(Language.ENGLISH_US, Gender.MALE)
        furhat.voice = Voice(gender = Gender.MALE, language = Language.ENGLISH_US, rate = 1.2)
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        }

    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }
}

val Interaction: State = state {

    onUserLeave(instant = true) {
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }

}