alter table grid
    add column show boolean;

update grid
set show = true;

alter table grid
    alter column show set not null;