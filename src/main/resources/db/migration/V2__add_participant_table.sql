create table if not exists participant
(
    id uuid not null,
    full_name varchar(255) not null,
    birth_year integer not null,
    gender varchar(6) not null,
    age_category varchar(255) not null,
    weight_category varchar(255) not null,
    weight decimal(5, 2),
    competition_id uuid not null,
    primary key (id)
);

alter table participant
    add constraint participant_competition_id_fk foreign key (competition_id) references competition(id) on delete restrict on update restrict;

create index participant_competition_id_idx on participant (competition_id);