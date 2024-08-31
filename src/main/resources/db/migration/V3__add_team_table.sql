create table if not exists team
(
    id uuid not null,
    name varchar(255) not null,
    primary key (id)
);

create unique index if not exists team_name_idx on team(name);

alter table participant add column team_id uuid not null;
alter table participant add constraint participant_team_id_fk foreign key (team_id) references team(id) on delete restrict on update restrict;

create index if not exists participant_team_id_idx on participant(team_id);
