package ru.pkozlov.brackets.participant.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.pkozlov.brackets.participant.repository.ParticipantRepository
import ru.pkozlov.brackets.participant.repository.ParticipantRepositoryImpl
import ru.pkozlov.brackets.participant.repository.TeamRepository
import ru.pkozlov.brackets.participant.repository.TeamRepositoryImpl
import ru.pkozlov.brackets.participant.service.ParticipantService
import ru.pkozlov.brackets.participant.service.TeamComponent

val participantModule: Module = module {
    singleOf(::ParticipantRepositoryImpl) bind ParticipantRepository::class
    singleOf(::ParticipantService)
    singleOf(::TeamRepositoryImpl) bind TeamRepository::class
    singleOf(::TeamComponent)
}