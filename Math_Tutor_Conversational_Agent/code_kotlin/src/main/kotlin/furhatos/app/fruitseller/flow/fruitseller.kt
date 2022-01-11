@file:Suppress("UNREACHABLE_CODE")

package furhatos.app.fruitseller.flow


import furhatos.app.fruitseller.flow.autobehavior.gazeSilent
//import furhatos.app.fruitseller.information
import furhatos.app.fruitseller.nlu.*
import furhatos.app.fruitseller.util.GazeSilent
import furhatos.app.fruitseller.util.getNegWords
import furhatos.app.fruitseller.util.getPosWords
import furhatos.autobehavior.defineMicroexpression
import furhatos.flow.kotlin.*
import furhatos.gestures.BasicParams
import furhatos.gestures.Gestures
import furhatos.gestures.defineGesture
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import java.util.*
import furhatos.autobehavior.*
import furhatos.flow.kotlin.voice.CereprocVoice

import furhatos.app.fruitseller.nlu.*
import furhatos.flow.kotlin.*
import furhatos.nlu.common.*


val SUITE_ROOMS_FREE = 10


val CITIZEN_ROOMS_FREE = 10


val RANDOM_GAZE = 0 /* Set to 1 to get random gaze behavior
*/


val Start = state(Interaction) {
    onEntry {
        random(
            {   furhat.say("Welcome to your math class") },
            {   furhat.say("Good morning, welcome to the math class") }
        )


        if (RANDOM_GAZE == 1) {
            furhat.setMicroexpression(
                    defineMicroexpression {
                        // Adjust eye gaze randomly: default: (between -3 and 3 degrees) with a random interval of 200-400 ms.
                        repeat(200..2000) {
                            adjust(-90.0..90.0, BasicParams.GAZE_PAN)
                            adjust(-90.0..90.0, BasicParams.GAZE_TILT)
                        }
                    }
            )
        }
        goto(emotionalState)
    }
}




val emotionalState = state(Interaction) {

    //onResponse<FeelingWell> {
    //    random(
    //            { furhat.say("Glad to hear that") },
    //            { furhat.say("Happy to hear that") }
    //    )

    //    goto(ChooseClass)
    //}

    //onResponse<FeelingBad> {
    //    random(
    //            { furhat.say("Sorry to hear that") },
    //            { furhat.say("Do not worry, it will get better soon") }
    //    )

    //    goto(ChooseClass)
    //}
    onEntry {
        random(
                { furhat.ask("How are you feeling today?") },
                { furhat.ask("How are you?") }
        )
    }

    onResponse{
        // Get number of positive and negative opinion words
        //println(it.text)
        val pos = getPosWords(it.text.toLowerCase())
        val neg = getNegWords(it.text.toLowerCase())
        println(pos)
        println(neg)
        if (neg>pos){goto(FeelBadMotivation)}
        else {random(
                { furhat.say("Glad to hear that") },
                { furhat.say("Happy to hear that")}
                    )
            goto(ChooseClass)}
        //goto(ChooseClass)
    }
}



val FeelBadMotivation=state(Interaction){
    onEntry {random(
            { furhat.ask("Oh no... Why do you feel this way?") },
            { furhat.ask("Sorry to hear that.. What made you feel that way") }
    )
    }

    onResponse{
        furhat.say("Oh, I see... Do you know what will make you feel better? Some maths!")
        goto(ChooseClass)
    }
}

val ChooseClass = state(Interaction) {
    onEntry {
        random(
                { furhat.ask("Would you like to practise today?") },
                { furhat.ask("Do you feel like doing some math exercises?") }
        )
    }

    onResponse<Yes> {
        random(
                { furhat.ask("What would you like to practise?") },
                { furhat.ask("What math problems do you struggle with?") }
        )
    }

    onResponse<No> {
        furhat.say("Okay, that's a shame. Have a nice day!")
        goto(Idle)
    }

    onResponse<RequestOptions> {
        furhat.say("We have ${MathOptions().optionsToText()}")
        furhat.ask("What are you interested in?")
    }

    onResponse<ExercisePick> {
        val exercises = it.intent.exercises
        if (exercises != null) goto(OrderReceived(exercises))
        else {
            propagate()
        }
    }

}
/*
val AdditionExercise = state(Interaction) {
    onEntry {
        random(
                { furhat.ask("2 plus 2. How much is it?") },
                { furhat.ask("20 plus 2. How much is it?") }
        )
    }
}

val MultiplicationExercise = state(Interaction) {
    onEntry {
        random(
                { furhat.ask("2 times 2. How much is it?") },
                { furhat.ask("20 times 2. How much is it?") }
        )
    }
}
*/

fun OrderReceived(exercises: FruitList) : State {
    return state(Interaction) {
        onEntry {
            furhat.say("${exercises.text}, good choice! Let's start with something simple. ")
            if (exercises.text == "addition")
                run { goto(doExercise("What is 2+2?", "4", "Try to count on your fingers"))}//goto(AdditionExercise) }
            else {
                furhat.say("Here is one problem for you")
                run { goto(doExercise("What is 3 multiplied by 2?", "6", "What is 3 plus 3?"))}}
        }
    }
}



fun doExercise(exercise: String, correct: String, tips: String) : State {
    return state(Interaction) {
        onEntry{
            furhat.ask(exercise)
        }

        onResponse{
            //val answer = it.text.toLowerCase()

            if (it.text != null) {
                furhat.say("${it.text}, hmmmmmm...")


                if (it.text != correct) {

                    if (tips != ""){
                        if (it.text != correct) {

                            furhat.ask(tips)
                            run{goto(doExercise("Let's again, "+exercise, correct, ""))}
                        }
                        else {
                            furhat.say("Sorry, I don't have more tips...")}
                    }
                    }
                }
                if (it.text == correct)
                    random(
                            {   furhat.say("Good job!") },
                            {   furhat.say("Yes! You're correct!") }
                    )


            }
    }
}