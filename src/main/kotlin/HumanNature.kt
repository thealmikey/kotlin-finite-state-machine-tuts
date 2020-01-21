import com.tinder.StateMachine

sealed class HumanState {
    object PureBliss : HumanState()
    object Happy : HumanState()
    object Bored : HumanState()
    object Sad : HumanState()
}

sealed class HumanAction {
    object Entertain : HumanAction()
    object GiveLecture : HumanAction()
    object Annoy : HumanAction()
    object GiveTherapy : HumanAction()
}

sealed class UseResult {
    object ReportHappiness : UseResult()
    object ReportSadness : UseResult()
    object ReportBoredness : UseResult()
    object ReportGoodWork : UseResult()
    object ReportVibeKiller : UseResult()
}

class Human(var name: String) {
    val emotionalState = StateMachine.create<HumanState, HumanAction, UseResult> {
        initialState(HumanState.Bored)
        state<HumanState.Bored> {
            on<HumanAction.Entertain> {
                transitionTo(HumanState.Happy, UseResult.ReportHappiness)
            }
            on<HumanAction.GiveTherapy> {
                transitionTo(HumanState.Happy, UseResult.ReportGoodWork)
            }
            on<HumanAction.Annoy> {
                transitionTo(HumanState.Sad, UseResult.ReportSadness)
            }
        }
        state<HumanState.PureBliss> {
            on<HumanAction.Annoy> {
                transitionTo(HumanState.Happy, UseResult.ReportHappiness)
            }
            on<HumanAction.GiveLecture> {
                transitionTo(HumanState.Happy, UseResult.ReportGoodWork)
            }
        }

        state<HumanState.Happy> {
            on<HumanAction.GiveTherapy> {
                transitionTo(HumanState.PureBliss, UseResult.ReportGoodWork)
            }
            on<HumanAction.GiveLecture> {
                transitionTo(HumanState.Bored, UseResult.ReportBoredness)
            }
            on<HumanAction.Annoy> {
                transitionTo(HumanState.Sad, UseResult.ReportSadness)
            }
        }
        state<HumanState.Sad> {
            on<HumanAction.Entertain> {
                transitionTo(HumanState.Happy, UseResult.ReportGoodWork)
            }
            on<HumanAction.GiveTherapy> {
                transitionTo(HumanState.Bored, UseResult.ReportGoodWork)
            }
            on<HumanAction.Annoy> {

                transitionTo(HumanState.Bored, UseResult.ReportGoodWork)
            }
        }
        onTransition {
            val validTransition = it as? StateMachine.Transition.Valid ?: return@onTransition
            when (validTransition.sideEffect) {
                UseResult.ReportHappiness -> println("Hapiness message") //ourService.log("Hapiness message")
                UseResult.ReportBoredness -> println("BoredNess message") //anotherService.log("BoredNess message")
                UseResult.ReportSadness -> println("Bad vibes message") //ourService.log("Bad vibes message")
                UseResult.ReportGoodWork -> println("Goodwork report") //anotherService.log("Goodwork report")
                UseResult.ReportVibeKiller -> println("Please don't kill my vibe")
            }
        }
    }
}

fun main() {

    var regularMike = Human("Mike Regular")

    regularMike.emotionalState.transition(HumanAction.Entertain)
    println(regularMike.emotionalState.state)
    // Hapiness message
    // HumanState$Happy@5b37e0d2

    regularMike.emotionalState.transition(HumanAction.GiveLecture)
    println(regularMike.emotionalState.state)
    //BoredNess message
    // HumanState$Bored@28c97a5

    regularMike.emotionalState.transition(HumanAction.Annoy)
    println(regularMike.emotionalState.state)
    //Bad vibes message
    //HumanState$Sad@2328c243

    regularMike.emotionalState.transition(HumanAction.GiveTherapy)
    println(regularMike.emotionalState.state)
    //Goodwork report
    // HumanState$Bored@28c97a5

    regularMike.emotionalState.transition(HumanAction.GiveTherapy)
    println(regularMike.emotionalState.state)
    //Goodwork report
    // HumanState$Happy@736e9adb

    regularMike.emotionalState.transition(HumanAction.GiveTherapy)
    println(regularMike.emotionalState.state)
    // Goodwork report
    //  HumanState$PureBliss@23ab930d

}





