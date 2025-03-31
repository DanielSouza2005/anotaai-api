CREATE TABLE IF NOT EXISTS "usuario" (
	"cod_usuario" serial NOT NULL UNIQUE,
	"nome" varchar(255) NOT NULL,
	"email" varchar(255) NOT NULL UNIQUE,
	"senha" varchar(255) NOT NULL,
	"ativo" smallint NOT NULL,
	"dt_inclusao" timestamp without time zone NOT NULL,
	"dt_alteracao" timestamp without time zone NOT NULL,
	PRIMARY KEY ("cod_usuario")
);