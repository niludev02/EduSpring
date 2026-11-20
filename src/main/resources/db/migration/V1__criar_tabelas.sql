CREATE TABLE turma (
    id BIGSERIAL PRIMARY KEY,
    identificador VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE disciplina (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE aluno (
    id BIGSERIAL PRIMARY KEY,
    matricula VARCHAR(30) NOT NULL UNIQUE,
    nome VARCHAR(150) NOT NULL,
    idade INTEGER NOT NULL,
    turma_id BIGINT REFERENCES turma(id)
);

CREATE TABLE nota (
    id BIGSERIAL PRIMARY KEY,
    valor NUMERIC(4,2) CHECK (valor BETWEEN 0 AND 10),
    aluno_id BIGINT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    disciplina_id BIGINT NOT NULL REFERENCES disciplina(id),
    UNIQUE (aluno_id, disciplina_id)
);
