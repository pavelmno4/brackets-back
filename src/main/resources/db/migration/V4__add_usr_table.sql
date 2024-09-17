create table if not exists usr
(
    id uuid primary key,
    login varchar(255) not null,
    password varchar(255) not null,
    roles varchar(10)[] not null
);

alter table usr
    add constraint user_login_idx unique (login);