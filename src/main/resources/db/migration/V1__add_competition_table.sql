create table if not exists competition
(
    id uuid not null,
	title varchar(255) not null,
	start_date date not null,
	end_sate date not null,
	address varchar(255) not null,
	image_path varchar(255) not null,
	categories jsonb not null,
	deleted boolean not null,
	created_at timestamp not null,
	updated_at timestamp not null,
	primary key(id)
);