CREATE TABLE IF NOT EXISTS public.quizzes (
                                              id BIGSERIAL PRIMARY KEY,
                                              owner_id BIGINT,
                                              name VARCHAR(255) NOT NULL DEFAULT 'Quiz',
    description VARCHAR(1000) DEFAULT 'Quiz description',
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );
