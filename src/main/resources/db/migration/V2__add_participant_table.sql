create table if not exists participant
(
    id uuid not null,
    name varchar(255) not null,
    birth_year integer not null,
    gender varchar(6) not null,
    age_category varchar(255) not null,
    weight_category varchar(255) not null,
    competition_id uuid not null,
    primary key (id)
);

create index participant_competition_id_idx on participant (competition_id);