package furhatos.app.newskill.nlu

import furhatos.nlu.*
import furhatos.nlu.grammar.Grammar
import furhatos.nlu.kotlin.grammar
import furhatos.nlu.common.Number
import furhatos.nlu.common.PersonName
import furhatos.util.Language

class CheckIn() : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I would like to check in", "I want to check in please", "I want to check in")
    }
}

class Intro() : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("Who are you",
                      "Where am I",
                      "What is this",
                      "Hello",
                      "What",
                      "Hi")
    }
}

class WishItem : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("get breakfast served to my room",
                      "have Mexican food for dinner",
                      "be woken up at 7 every morning")
    }
}

class Wish(var wishItem : WishItem? = WishItem()) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I want to @wishItem",
                      "I would like to @wishItem",
                      "Yes, please. I want to @wishItem")
    }
}

class OtherWish(var wishItem : WishItem? = WishItem()) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I want to play @wishItem",
                      "I would like to try @wishItem",
                      "Yes, please. I want to play @wishItem",
                      "I want to @wishItem",
                      "I would like to @wishItem",
                      "Yes, please. I want to @wishItem")
    }
}

class Activity: EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("ski",
                "tennis",
                "badminton",
                "zombie survival")
    }
}

class ListActivities(var activity : Activity? = Activity()) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("I also want to @activity",
                "I also would like to @activity",
                "Yes, please. I also want to @activity",
                "I want to @activity",
                "I would like to @activity",
                "Yes, please. I want to @activity")
    }
}

class GuestNumber(var count : Number? = Number(1)) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@count people",
                      "I want to check in @count people",
                      "@count persons",
                      "@count please")
    }
}

class FurtherDetails(
        var name : PersonName? = PersonName(),
        var timeLengthNum : Number? = Number(1),
        var roomType: RoomType? = RoomType()) : ComplexEnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("My name is @name and I want to stay for @timeLengthNum days in @roomType rooms.",
                      "I am @name and I want to stay for @timeLengthNum days in @roomType rooms.",
                      "I am @name. I want to stay for @timeLengthNum days in @roomType rooms.",
                      "My name is @name. I would like to stay for @timeLengthNum days in @roomType rooms.")
    }
}

class RoomType : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("suite", "citizen")
    }
}