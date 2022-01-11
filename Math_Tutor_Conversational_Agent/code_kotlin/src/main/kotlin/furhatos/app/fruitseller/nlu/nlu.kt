package furhatos.app.fruitseller.nlu

import furhatos.nlu.*
import furhatos.nlu.common.Number
import furhatos.util.Language

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

class RequestOptions: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("What options do you have?",
                "What can I do?",
                "What are the alternatives?",
                "What do you have?")
    }
}

class FeelingWell: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(" I am okay", "I am feeling well", "I am doing great")
    }
}

class FeelingBad: Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(" Not well", "I am feeling unwell", "I am doing bad")
    }
}


class MathOptions : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("addition", "multiplication")
    }
}


class ExercisePick(var exercises : FruitList? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@exercises", "I want @exercises", "I would like @exercises", "I want to practise @exercises")
    }
}


class FruitList : ListEntity<QuantifiedFruit>()

class QuantifiedFruit(
        var count : Number? = Number(1),
        var fruit : MathOptions ? = null) : ComplexEnumEntity() {
    override fun getEnum(lang: Language): List<String> {
        return listOf("@count @fruit", "@fruit")
    }

    override fun toText(): String {
        return generate("$count " + if (count?.value == 1) fruit?.value else "${fruit?.value}" + "s")
    }
}



