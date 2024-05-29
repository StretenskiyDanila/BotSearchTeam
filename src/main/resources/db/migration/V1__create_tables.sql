CREATE TABLE t_user
(
    id                serial       not null primary key,
    telegram_chat_id  bigint       not null unique,
    telegram_username varchar(255) not null unique,
    created_at        timestamp    not null default now()
);

CREATE TABLE t_project
(
    id                  serial  not null primary key,
    project_name        varchar not null unique,
    project_description varchar
);

CREATE TABLE t_team
(
    id           serial  not null primary key,
    project_id   integer not null references t_project (id),
    team_lead_id integer not null references t_user (id) unique,
    description  varchar not null,
    is_open      bool    not null

);

create TABLE t_user_questionnaire
(
    id                 serial  not null primary key,
    user_id            integer not null references t_user (id) unique,
    project_id         integer not null references t_project (id),
    questionnaire_text text    not null,
    is_open            bool    not null
);


CREATE TABLE t_request
(
    id                    serial  not null primary key,
    team_id               integer not null references t_team (id),
    user_questionnaire_id integer not null references t_user_questionnaire (id)
)