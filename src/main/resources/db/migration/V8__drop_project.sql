DROP TABLE t_project CASCADE;

ALTER TABLE t_team DROP COLUMN project_id;

ALTER TABLE t_team ADD COLUMN project_id integer not null;;