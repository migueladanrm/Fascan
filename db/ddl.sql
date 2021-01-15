begin;

create extension if not exists "uuid-ossp";

create table face
(
    id                uuid         not null primary key default uuid_generate_v4(),
    encodings         jsonb        not null,
    resource          varchar(256) not null,
    custom_attributes jsonb        not null             default '{}'::jsonb,
    created_at        timestamp    not null             default now()
);

end;