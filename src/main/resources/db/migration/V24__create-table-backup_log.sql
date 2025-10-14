CREATE TABLE IF NOT EXISTS backup_log (
    cod_backuplog SERIAL PRIMARY KEY,
    cod_backupconfig INTEGER REFERENCES backup_config(cod_backupconfig) NOT NULL,
    tipo_operacao INT NOT NULL DEFAULT 0,
    sucesso INT NOT NULL DEFAULT 1,
    mensagem TEXT,
    dt_inicio TIMESTAMP NOT NULL,
    dt_fim TIMESTAMP NOT NULL
);