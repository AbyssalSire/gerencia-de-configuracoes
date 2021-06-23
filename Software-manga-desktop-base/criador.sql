DROP SEQUENCE IF EXISTS usuarios_id_seq CASCADE;

CREATE SEQUENCE usuarios_id_seq
START 1
INCREMENT 1;

-- Table: usuarios

DROP TABLE IF EXISTS usuarios CASCADE;

CREATE TABLE usuarios
(
    id integer NOT NULL DEFAULT nextval('usuarios_id_seq'::regclass),
    nome character varying(50) COLLATE pg_catalog."default" NOT NULL,
    nome_usuario character varying(10) COLLATE pg_catalog."default" NOT NULL,
    senha character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT usuarios_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE usuarios
    OWNER to postgres;

-- Table: lista_mangas

DROP TABLE IF EXISTS lista_mangas CASCADE;

CREATE TABLE lista_mangas
(
    id_usuario integer NOT NULL,
    nome_manga character varying(50) COLLATE pg_catalog."default" NOT NULL,
    descricao_manga character varying(100) COLLATE pg_catalog."default",
    lista_volumes character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT lista_mangas_pkey PRIMARY KEY (nome_manga, id_usuario),
    CONSTRAINT id_usuario FOREIGN KEY (id_usuario)
        REFERENCES public.usuarios (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE lista_mangas
    OWNER to postgres;
INSERT INTO usuarios(
	nome, nome_usuario, senha)
	VALUES ('Lucas', 'luc', 'senha'),('Teste', 'test', 'tes'),('Professor', 'Prof', 'senhaT');
INSERT INTO lista_mangas(
	nome_manga, id_usuario, descricao_manga, lista_volumes)
	VALUES ('Berserk', 1, 'Excelente manga', '2 5 10 15 40'),('Ajin', 1, 'Thriller', '15'),('One Punch Man', '3', 'Comédia', '3'),('Blade', 2, 'Ação', null);

