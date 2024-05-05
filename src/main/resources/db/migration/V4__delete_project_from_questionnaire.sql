ALTER TABLE t_user_questionnaire
    DROP CONSTRAINT t_user_questionnaire_project_id_fkey;
ALTER TABLE t_user_questionnaire
    DROP COLUMN project_id;