package furhatos.app.fruitseller.util


import furhatos.event.Event
import furhatos.records.Location

/*
    Automatic behavior
 */
class AttendLocation(val location: Location) : Event()
class LookAround : Event()
class LookStraight(val randomMovements: Boolean = true) : Event()
class StopAutoBehavior : Event()
class GazeFurhatSpeaking : Event()
class GazeUserSpeaking : Event()
class GazeSilent : Event()