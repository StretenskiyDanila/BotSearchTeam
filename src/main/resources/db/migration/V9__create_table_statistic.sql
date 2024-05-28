CREATE TABLE t_statistic
(
    id                 serial       not null primary key,
    count_found_users  integer      not null default 0,
    team_title         varchar(255) not null
);