package furhatos.app.newskill

import furhatos.app.newskill.flow.Idle
import furhatos.flow.kotlin.Flow
import furhatos.skills.Skill
import furhatos.app.newskill.flow.autobehavior.autoBehavior

class NewskillSkill : Skill() {
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
