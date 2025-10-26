CREATE TABLE IF NOT EXISTS exportacao_log (
    cod_exportacaolog SERIAL PRIMARY KEY,
    tipo int NOT NULL,
    status int NOT NULL,
    caminho_arquivo varchar(255),
    mensagem_erro TEXT,
    dt_inclusao TIMESTAMP DEFAULT now(),
    dt_termino TIMESTAMP DEFAULT now()
);