package ru.pkozlov.brackets.file.service

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import ru.pkozlov.brackets.app.utils.bfs
import ru.pkozlov.brackets.app.utils.flat
import ru.pkozlov.brackets.app.utils.toDeque
import ru.pkozlov.brackets.competition.dto.CompetitionDto
import ru.pkozlov.brackets.competition.enumeration.Stage.COMPLETED
import ru.pkozlov.brackets.grid.dto.GridDto
import ru.pkozlov.brackets.grid.dto.Node
import ru.pkozlov.brackets.grid.dto.Participant
import java.util.*

object GridHtmlComponent {
    fun createHtml(competition: CompetitionDto, grid: GridDto): String = createHTML().html {
        lang = "ru"

        head {
            meta(charset = "utf-8")
            title("Протокол поединков: ${grid.gender.rusName}, ${grid.ageCategory.value} г.р., вес ${grid.weightCategory.value} кг.")

            style {
                unsafe {
                    +"""
                    body {
                        font-family: helvetica, arial, sans-serif;
                        display: flex;
                        flex-direction: column;
                    }
            
                    .three-columns {
                        display: flex;
                        justify-content: space-between;
                    }
            
                    .column {
                        flex: 1;
                    }
            
                    .indent {
                        margin-bottom: 50px;
                    }
            
                    .title {
                        text-align: center;
                        font-size: 24px;
                        font-weight: bold;
                    }
            
                    .info {
                        text-align: left;
                        font-size: 14px;
                    }
            
                    .third-place-title {
                        text-align: center;
                    }
            
                    /* brackets */
                    .bracket {
                        display: flex;
                    }
            
                    .round {
                        flex: 1;
                        display: flex;
                        margin-right: 10px;
                        flex-direction: column;
                        justify-content: space-around;
                    }
            
                    .match {
                        margin: 5px 0;
                        overflow: hidden;
                        border-radius: 5px;
                    }
            
                    .participant {
                        height: 12px;
                        padding: 10px 8px;
                        border: 1px solid;
                        font-size: 9px;
                    }
                    """.trimIndent()
                }
            }
        }
        body {
            // Title
            h1(classes = "title") { +competition.title }

            // Info
            div(classes = "three-columns") {
                div(classes = "column")
                div(classes = "column")
                div(classes = "column info") {
                    +"Пол: "
                    b { +grid.gender.rusName }
                    br()
                    +"Год рождения: "
                    b { +grid.ageCategory.value }
                    +" г.р."
                    br()
                    +"Весовая категория: "
                    b { +grid.weightCategory.value }
                    +" кг"
                }
            }

            // Main bracket
            for (dendrogram in grid.dendrogram) {
                val flatDendrogram: Map<Int, List<Node>> = dendrogram.flat()
                val lastLevel: Int = flatDendrogram.size - 1

                div(classes = "bracket") {
                    for (round in lastLevel downTo 2) {
                        div(classes = "round") {
                            val participants: Queue<Node> = flatDendrogram[round]?.toDeque() ?: LinkedList()
                            val matches: Int = participants.size / 2

                            for (match in 1..matches) {
                                div(classes = "match") {
                                    val participantOne: Participant? = participants.poll()?.participant
                                    val participantTwo: Participant? = participants.poll()?.participant

                                    div(classes = "participant") {
                                        participantOne?.run { +"$lastName $firstName ($team)" } ?: ""
                                    }
                                    div(classes = "participant") {
                                        participantTwo?.run { +"$lastName $firstName ($team)" } ?: ""
                                    }
                                }
                            }
                        }
                    }

                    div(classes = "round") {
                        val participants: Queue<Node> = flatDendrogram[1]?.toDeque() ?: LinkedList()

                        div(classes = "match") {
                            val participantOne: Participant? = participants.poll()?.participant
                            val participantTwo: Participant? = participants.poll()?.participant

                            div(classes = "participant") {
                                participantOne?.run { +"$lastName $firstName ($team)" } ?: ""
                            }
                            div(classes = "participant") {
                                participantTwo?.run { +"$lastName $firstName ($team)" } ?: ""
                            }
                        }
                    }

                    div(classes = "round") {
                        val participants: Queue<Node> = flatDendrogram[0]?.toDeque() ?: LinkedList()

                        div(classes = "match") {
                            val participant: Participant? = participants.poll()?.participant

                            div(classes = "participant") {
                                participant?.run { +"$lastName $firstName ($team)" } ?: ""
                            }
                        }
                    }
                }

                // The third place fight
                val semiFinal: Queue<Node> = flatDendrogram[2]?.toDeque() ?: LinkedList()

                if (semiFinal.isNotEmpty()) {
                    val thirdPlaceFightParticipants: List<Participant?> =
                        if (competition.stage != COMPLETED) emptyList()
                        else semiFinal.filterNot { it?.participant?.winner ?: true }.map { it?.participant }
                    val participantOne: Participant? = thirdPlaceFightParticipants.getOrNull(0)
                    val participantTwo: Participant? = thirdPlaceFightParticipants.getOrNull(1)
                    val thirdPlaceWinner: Participant? = dendrogram
                        .bfs { result, node -> if (node.participant?.id == grid.thirdPlaceParticipantId) result.add(node.participant) }
                        .firstOrNull()

                    div(classes = "three-columns") {
                        div(classes = "column")
                        div(classes = "column")
                        div(classes = "column third-place-title") { +"Бой за 3-е место" }
                    }
                    div(classes = "three-columns") {
                        div(classes = "column")
                        div(classes = "column")
                        div(classes = "column") {
                            div(classes = "bracket") {
                                div(classes = "round") {
                                    div(classes = "match") {
                                        div(classes = "participant") {
                                            participantOne?.run { +"$lastName $firstName ($team)" }
                                        }
                                        div(classes = "participant") {
                                            participantTwo?.run { +"$lastName $firstName ($team)" }
                                        }
                                    }
                                }
                                div(classes = "round") {
                                    div(classes = "match") {
                                        div(classes = "participant") {
                                            thirdPlaceWinner?.run { +"$lastName $firstName ($team)" }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}