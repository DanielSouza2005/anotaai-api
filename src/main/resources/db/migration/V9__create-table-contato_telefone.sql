CREATE TABLE IF NOT EXISTS "contato_telefone" (
"cod_telefone" serial NOT NULL UNIQUE,
"cod_contato" bigint NOT NULL,
"telefone" varchar(30) NOT NULL,
"tipo" varchar(50),
PRIMARY KEY ("cod_telefone")
);

ALTER TABLE "contato_telefone" ADD CONSTRAINT
"contato_telefone_fk1" FOREIGN KEY ("cod_contato")
REFERENCES "contato"("cod_contato");