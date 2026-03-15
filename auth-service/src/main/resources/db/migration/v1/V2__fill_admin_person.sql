INSERT INTO person.t_user(username, password, status)
SELECT 'j.jameson', '{noop}password', 'CREATED'
WHERE NOT EXISTS (
    SELECT 1
    FROM person.t_user
    WHERE username = 'j.jameson'
);

INSERT INTO person.t_user_authority(id_user, c_authority)
SELECT 
    (SELECT id FROM person.t_user WHERE username = 'j.jameson'),
    'ROLE_MANAGER'
WHERE NOT EXISTS (
    SELECT 1
    FROM person.t_user_authority tua
    JOIN person.t_user tu ON tua.id_user = tu.id
    WHERE tu.username = 'j.jameson' AND tua.c_authority = 'ROLE_MANAGER'
)
AND EXISTS (SELECT 1 FROM person.t_user WHERE username = 'j.jameson');