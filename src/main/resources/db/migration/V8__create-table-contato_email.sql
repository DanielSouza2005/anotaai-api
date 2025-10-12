CREATE TABLE IF NOT EXISTS "contato_email" (
"cod_email" serial NOT NULL UNIQUE,
"cod_contato" bigint NOT NULL,
"email" varchar(200) NOT NULL,
"tipo" varchar(50),
PRIMARY KEY ("cod_email")
);

ALTER TABLE "contato_email" ADD CONSTRAINT
"contato_email_fk1" FOREIGN KEY ("cod_contato") REFERENCES
"contato"("cod_contato");