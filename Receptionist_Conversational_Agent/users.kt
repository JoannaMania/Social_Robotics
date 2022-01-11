package furhatos.app.newskill

import furhatos.app.newskill.nlu.RoomType
import furhatos.nlu.common.Number
import furhatos.nlu.common.PersonName
import furhatos.records.User

class GuestInformation {
    var guestNumber : Number? = Number(0)
        get() = field
        set(value){
            field = value
        }
    var name : PersonName? = PersonName()
        get() = field
        set(value){
            field = value
        }
    var timeNum : Number? = Number(0)
        get() = field
        set(value) {
            field = value
        }

    var roomType : RoomType? = RoomType()
        get() = field
        set(value){
            field = value
        }
}

val User.information : GuestInformation
    get() = data.getOrPut(GuestInformation::class.qualifiedName, GuestInformation())