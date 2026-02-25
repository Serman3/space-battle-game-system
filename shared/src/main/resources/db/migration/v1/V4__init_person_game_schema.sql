CREATE SCHEMA IF NOT EXISTS person_game;
CREATE SCHEMA IF NOT EXISTS person_game_history;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
SET search_path TO person_game,person_game_history,public;