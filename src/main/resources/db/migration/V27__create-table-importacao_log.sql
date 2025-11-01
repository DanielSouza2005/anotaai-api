CREATE TABLE importacao_log (
    cod_importacaolog SERIAL PRIMARY KEY,
    tipo int NOT NULL,
    status int NOT NULL,
    dt_inicio TIMESTAMP NOT NULL,
    dt_fim TIMESTAMP,
    total_registros INT DEFAULT 0,
    registros_sucesso INT DEFAULT 0,
    registros_erro INT DEFAULT 0,
	mensagem_erro text
);