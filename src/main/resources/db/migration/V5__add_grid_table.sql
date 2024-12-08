create table if not exists grid (
    id uuid not null,
    gender varchar(6) not null,
    age_category varchar(255) not null,
    weight_category varchar(255) not null,
    dendrogram jsonb not null,
    competition_id uuid not null,
     primary key (id)
);

alter table grid
    add constraint grid_competition_id_fk foreign key (competition_id) references competition(id) on delete restrict on update restrict;

create index grid_gender_idx on grid (gender);
create index grid_age_category_idx on grid (age_category);
create index grid_weight_category_idx on grid (weight_category);
create index grid_competition_id_idx on grid (competition_id);