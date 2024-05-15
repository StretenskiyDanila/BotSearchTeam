ALTER TABLE t_user ADD COLUMN team_id integer;

ALTER TABLE t_user ADD CONSTRAINT fk_team_id
    FOREIGN KEY (team_id)
    REFERENCES t_team(id);
