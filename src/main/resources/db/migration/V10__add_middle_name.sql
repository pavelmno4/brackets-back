alter table participant
    rename column full_name to first_name;

alter table participant
    add column last_name varchar(255) not null default '-';

alter table participant
    add column middle_name varchar(255) not null default '-';

alter table participant
    alter column last_name drop default;

alter table participant
    alter column middle_name drop default;