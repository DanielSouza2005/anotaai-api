CREATE TABLE IF NOT EXISTS "empresa" (
	"cod_empresa" bigserial NOT NULL UNIQUE,
	"razao" varchar(200) NOT NULL,
	"fantasia" varchar(200) NOT NULL,
	"cnpj" varchar(20) NOT NULL UNIQUE,
	"ie" varchar(20),
	"pais" varchar(200) NOT NULL,
	"uf" varchar(2) NOT NULL,
	"cidade" varchar(200) NOT NULL,
	"bairro" varchar(200) NOT NULL,
	"rua" varchar(200) NOT NULL,
	"numero" varchar(10),
	"complemento" varchar(200),
	"cep" varchar(9) NOT NULL,
	"ativo" smallint NOT NULL,
	"dt_inclusao" timestamp without time zone NOT NULL,
	"dt_alteracao" timestamp without time zone NOT NULL,
	PRIMARY KEY ("cod_empresa")
);