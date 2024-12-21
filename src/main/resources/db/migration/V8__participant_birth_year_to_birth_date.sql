alter table participant
    drop column birth_year;

alter table participant
    add column birth_date date not null;