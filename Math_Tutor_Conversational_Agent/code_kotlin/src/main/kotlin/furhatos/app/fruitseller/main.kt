package furhatos.app.fruitseller
/*
import furhatos.app.fruitseller.flow.Idle
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class FruitsellerSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
*/

import furhatos.app.fruitseller.flow.Idle
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill
import furhatos.app.fruitseller.flow.autobehavior.autoBehavior

class FruitsellerSkill : Skill() {
    override fun start() {
        // Start a flow for automatic behavior (nonverbal)
        Flow().runAsync(autoBehavior())

        // Start flow for actual interaction
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}