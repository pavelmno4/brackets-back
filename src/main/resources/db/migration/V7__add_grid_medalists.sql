alter table grid add first_place_participant_id uuid;
alter table grid add second_place_participant_id uuid;
alter table grid add third_place_participant_id uuid;

alter table grid add constraint grid_first_place_participant_id_fk foreign key (first_place_participant_id) references participant(id) on delete restrict on update restrict;
alter table grid add constraint grid_second_place_participant_id_fk foreign key (second_place_participant_id) references participant(id) on delete restrict on update restrict;
alter table grid add constraint grid_third_place_participant_id_fk foreign key (third_place_participant_id) references participant(id) on delete restrict on update restrict;