package furhatos.app.newskill

import furhatos.records.Location
import java.util.*

var LOOK_AROUND_ALLOWED : Boolean = true
val LOOKAROUND_INTERVAL = 4000..8000
val MICROMOVEMENTS_INTERVAL = 2000..4000
val DEFAULT_LOCATION = Location(0.0, 0.0, 1.0)

val r = Random()
val MU_AGENT_SPEAKS_X = 0.6587
val STD_AGENT_SPEAKS_X = 0.8673755
val MU_USER_SPEAKS_X = 0.6006
val STD_USER_SPEAKS_X = 0.25979
val MU_AGENT_SPEAKS_G = 8.81
val STD_AGENT_SPEAKS_G = 10.9
val MU_USER_SPEAKS_G = 13.379
val STD_USER_SPEAKS_G = 15.97
val MU_NOB_X = 1.06
val STD_NOB_X = 0.78
val MU_NOB_G = 8.63
val STD_NOB_G = 2.16