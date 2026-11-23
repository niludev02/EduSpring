CREATE TABLE professor (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    disciplina_id BIGINT NOT NULL REFERENCES disciplina(id)
);

CREATE TABLE professor_turma (
    professor_id BIGINT NOT NULL REFERENCES professor(id) ON DELETE CASCADE,
    turma_id BIGINT NOT NULL REFERENCES turma(id) ON DELETE CASCADE,
    PRIMARY KEY (professor_id, turma_id),
    UNIQUE (turma_id)
);
