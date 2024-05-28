ALTER TABLE t_user
DROP COLUMN team_id;

DO $$
    DECLARE
        constraint_name text;
    BEGIN
        SELECT conname INTO constraint_name
        FROM pg_constraint
        WHERE conrelid = 't_request'::regclass AND confrelid = 't_team'::regclass;

        EXECUTE format('ALTER TABLE t_request DROP CONSTRAINT %I', constraint_name);

        EXECUTE 'ALTER TABLE t_request ADD CONSTRAINT fk_team FOREIGN KEY (team_id) REFERENCES t_team (id) ON DELETE CASCADE';
END $$;