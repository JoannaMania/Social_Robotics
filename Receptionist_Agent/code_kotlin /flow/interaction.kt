package furhatos.app.newskill.flow

import furhatos.app.newskill.flow.autobehavior.gazeSilent
import furhatos.app.newskill.information
import furhatos.app.newskill.nlu.*
import furhatos.app.newskill.util.GazeSilent
import furhatos.autobehavior.defineMicroexpression
import furhatos.flow.kotlin.*
import furhatos.gestures.BasicParams
import furhatos.gestures.Gestures
import furhatos.gestures.defineGesture
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import java.util.*
import furhatos.app.newskill.util.LookAround
import furhatos.autobehavior.*

val SUITE_ROOMS_FREE = 10
val CITIZEN_ROOMS_FREE = 10
val RANDOM_GAZE = 0 /* Set to 1 to get random gaze behavior */

val Start = state(Interaction) {
    onEntry {

        send(GazeSilent())

        random(
                {   furhat.say("Hello.") },
                {   furhat.say("Oh, hello there.") },
                {   furhat.say("Welcome. It is great to see you!")},
                {   furhat.say("Hi!")}
        )

        /* Random gaze behavior*/
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

        goto(TakingOrder)
    }
}

val TakingOrder : State = state(Interaction) {

    onEntry {

        random(
                { furhat.ask("How can I help you?") },
                { furhat.ask("How may I be of assistance to you?") }
        )
    }

    onResponse<CheckIn> {
        goto(CheckInIntro)
    }

    onResponse<Intro>{
        goto(RobotIntro)
    }

}

val RobotIntro : State = state(Interaction) {
    onEntry {
        furhat.say("Welcome to Starship Enterprise. We are currently leaving for a 12-day voyage from planet Earth to planet Vulkan.")
        furhat.say("My name is Data and I am your check-in assistant for today.")
        furhat.ask({
            random {
                +"Would you like to check in?"
                +"Do you want to check in?"
                +"Can I check you in?"
            }
            })
        }

    onResponse<Yes> {
        goto(CheckInIntro)
    }

    onResponse<CheckIn>{
        goto(CheckInIntro)
    }

    onResponse<No> {
        furhat.say({
            random{
                + "Goodbye."
                +"Byebye."
                +"Bye."
                 }
            random{
                + "Hope to see you again soon."
                + "Have a good day."
                + "Have a nice day."
            }
        })
        goto(Idle)
    }
}

val CheckInIntro : State = state(Interaction) {
    onEntry {
        furhat.say("Great! As the travel is longer than two days on our journey to Vulkan, regulation requires we ask a few questions.")
        random(
                { furhat.ask("Is that okay with you?") },
                { furhat.ask("Are you okay with this?") }
        )
    }

    onResponse<Yes> {
        furhat.say({
            random {
                +"Let's get started then."
                +"Cool. Let's start then."
                +"Great! Let's begin."
            }
        })
        goto(AskGuests)
    }

    onResponse<No> {
        furhat.say("Without your information I cannot book you in.")
        goto(VerifyNoInformation)
    }
}

val VerifyNoInformation : State = state(Interaction){
    onEntry {
        random(
                { furhat.ask("Are you sure?") },
                { furhat.ask("Are you sure about this?") }
        )
    }

    onResponse<Yes> {
        furhat.say({
            random {
                +"Goodbye then."
                +"Bye. Hope to see you again soon."
            }
        })
            goto(Idle)
        }

    onResponse<No> {
        furhat.say("Let's get started then.")
        goto(AskGuests)
    }
}

val AskGuests : State = state(Interaction) {
    onEntry {
        random(
                { furhat.ask("How many people would you like to check-in?") },
                { furhat.ask("For how many people do you want to check-in?") }
        )
    }

    onResponse<GuestNumber> {
        val num = it.intent.count
        furhat.say("$num people, great.")
        users.current.information.guestNumber = num
        goto(askAmenities)
    }
}

var askAmenities : State = state(Interaction) {
    onEntry {
        random(
                { furhat.ask("By the way, would you like to know about the available amenities in our rooms?") },
                { furhat.ask("Should I tell you about the available amenities in our rooms?") }
        )
    }

    onResponse<Yes> {
        furhat.say("You are provided a bed, a table, a chair, and a Replicator, which allows you to instantly create any dish you've ever wanted to eat, in the comfort of your own room.")
        goto(FurtherDetailsReceived)
    }

    onResponse<No> {
        goto(FurtherDetailsReceived)
    }
}

var FurtherDetailsReceived : State = state(Interaction){
    onEntry {
        furhat.ask("Perfect. Now, could you give me your name, how long you intend to stay on Starship Enterprise, and whether you would like to stay in our Suite-class rooms or the Citizen-class rooms? (suite class have 2 beds, citizen-class have 1 bed)")
    }

    onInterimResponse(endSil = 500){
        random(
                {furhat.gesture(Gestures.Smile)},
                {furhat.gesture(Gestures.Nod)}
        )
    }

    onResponse<FurtherDetails> {
        users.current.information.timeNum = it.intent.timeLengthNum
        users.current.information.roomType = it.intent.roomType
        users.current.information.name = it.intent.name
        if("${users.current.information.roomType}" == "citizen"){
            if (users.current.information.guestNumber?.value?.compareTo(CITIZEN_ROOMS_FREE) == 1){
                goto(StarshipOverload)
            }
            else{
                goto(SpecificWishes)
            }
        }
        else {
            if (users.current.information.guestNumber?.value?.compareTo(SUITE_ROOMS_FREE*2) == 1){
                goto(StarshipOverload)
            }
            else{
                goto(SpecificWishes)
            }
        }
    }
}

var StarshipOverload : State = state(Interaction) {

    onEntry {
        var numFreeRooms = 0
        if("${users.current.information.roomType}" == "citizen"){
            numFreeRooms = CITIZEN_ROOMS_FREE
        }
        else if("${users.current.information.roomType}" == "suite"){
            numFreeRooms = SUITE_ROOMS_FREE
        }
        furhat.say("Unfortunately there are not sufficiently many rooms left of this kind.")
        furhat.say("We only have $numFreeRooms rooms of this kind left.")
        furhat.ask("Would you like to change the number of people you are checking in?")
    }

    onReentry {
        furhat.ask{"We do not have enough space at this time. Would you like to change the number of people?"}
    }

    onResponse<Yes> {
        furhat.say("Wonderful.")
        goto(NumberOfPeopleChange)
    }

    onResponse<No> {
        furhat.say("Alright then.")
        goto(CheckInCancel)
    }

    onTime(delay=100000) {
        goto(CheckInCancel)
    }
}

var CheckInCancel : State = state(Interaction){
    onEntry {
        furhat.say("Please tell me if you'd like to start over. Otherwise I wish you a good day.")
        goto(Idle)
    }
}

var NumberOfPeopleChange : State = state(Interaction) {

    onEntry {
        furhat.ask("Please tell me how many guests you would like to check in.")
    }

    onResponse<GuestNumber> {
        users.current.information.guestNumber = it.intent.count
        if("${users.current.information.roomType}" == "citizen"){
            if (users.current.information.guestNumber?.value?.compareTo(CITIZEN_ROOMS_FREE) == 1){
                goto(StarshipOverload)
            }
            else{
                goto(SpecificWishes)
            }
        }
        else {
            if (users.current.information.guestNumber?.value?.compareTo(SUITE_ROOMS_FREE*2) == 1){
                goto(StarshipOverload)
            }
            else{
                goto(SpecificWishes)
            }
        }
    }

    onResponse<No> {
        furhat.say("Alright then.")
        goto(CheckInCancel)
    }
}

var SpecificWishes : State = state(Interaction){
    onEntry {
        furhat.say("Amazing. The data has been entered to your name ${users.current.information.name}.")
        furhat.ask("Now, before asking you about the different activities we offer on board, I would like to ask you if you have any specific wishes for your stay here?")
    }

    onResponse<No> {
        goto(ThereAreNoWishes)
    }

    onResponse<Wish>{
        var wish = it.intent.wishItem
        goto(WishesLoop(wish))
    }
}

var ThereAreNoWishes : State = state(Interaction){
    onEntry {
        furhat.say("Alright, then let's move on.")
        goto(StarshipActivities)
        }
}

fun WishesLoop(wishItem: WishItem?) : State = state(Interaction){
    onEntry {
        random(
                { furhat.ask("Understood. Anything else?") },
                { furhat.ask("No problem. Do you have any further wishes?")},
                { furhat.ask("Of course. Can I do anything else for you?") }
        )
    }

    onResponse<OtherWish> {
        var wish = it.intent.wishItem
        goto(WishesLoop(wish))
    }

    onResponse<No> {
        goto(EndOfWishes)
    }
}

var EndOfWishes : State = state(Interaction) {
    onEntry {
        furhat.say("Alright, your demands have been noted and will be read by the crew. Let's move on then.")
        goto(StarshipActivities)
    }
}

var StarshipActivities : State = state(Interaction) {
    onEntry {
        furhat.say("On Starship Enterprise we offer numerous simulated activities, namely: Skiing, Tennis, Badminton and Zombie Survival.")
        furhat.ask("Please tell me which ones of those activities you would like to sign up for today.")
    }

    onResponse<No>{
        goto(EndState)
    }

    onResponse<ListActivities>{
        goto(EndState)
    }
}

var EndState : State = state(Interaction) {
    onEntry {
        furhat.say("Understood. You have now successfully checked in. You will soon be teleported to your room, and your luggage will be delivered by our staff.")
        furhat.say("We hope your stay at Starship Enterprise will be a fun and relaxing one.")
        goto(Idle)
    }
}




