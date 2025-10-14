CREATE TABLE backup_config (
    COD_BACKUPCONFIG SERIAL PRIMARY KEY,
    COD_USUARIO INTEGER NULL,
    frequencia INTEGER NOT NULL,
    formato INTEGER NOT NULL,
    dt_proximo_backup TIMESTAMP,
    dt_inclusao TIMESTAMP DEFAULT NOW(),
    dt_alteracao TIMESTAMP DEFAULT NOW()
);