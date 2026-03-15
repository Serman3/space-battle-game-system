DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'game') THEN
       CREATE DATABASE game;
END IF;
END $$;