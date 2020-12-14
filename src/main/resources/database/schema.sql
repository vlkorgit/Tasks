create table if not exists tasks
(
    id serial primary key,
    name varchar(50) not null,
    description varchar(200),
    status bool not null
);