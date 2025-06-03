DO $$ BEGIN
CREATE TYPE role AS ENUM ('STUDENT', 'TEACHER');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS public.users (
                                            id BIGSERIAL PRIMARY KEY,
                                            username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role role NOT NULL
    );
