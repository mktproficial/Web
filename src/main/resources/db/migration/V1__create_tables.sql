CREATE TABLE clientes
(
    id            INT          NOT NULL AUTO_INCREMENT,
    nome_cliente  VARCHAR(255) NOT NULL,
    cpf           VARCHAR(11)  NOT NULL,
    telefone      VARCHAR(12)  NOT NULL,
    email         VARCHAR(100) NOT NULL,
    dt_nascimento DATE         NOT NULL,

    PRIMARY KEY (id)
);


CREATE TABLE imoveis
(
    id                INT               NOT NULL AUTO_INCREMENT,
    tipo_imovel       VARCHAR(50)       NOT NULL,
    endereco          VARCHAR(255)      NOT NULL,
    cep               VARCHAR(10)       NOT NULL,
    dormitorios       INT               NOT NULL,
    banheiros         INT               NOT NULL,
    suites            INT               NOT NULL,
    metragem          INT               NOT NULL,
    valor_aluguel_sug DECIMAL(10, 2)    NOT NULL,
    obs               TEXT,

    PRIMARY KEY (id)
);


CREATE TABLE locacao
(
    id              INT             NOT NULL AUTO_INCREMENT,
    id_imovel       INT             NOT NULL,
    id_cliente      INT             NOT NULL,
    ativo           BIT             NOT NULL,
    data_fim        DATE            NOT NULL,
    data_inicio     DATE            NOT NULL,
    data_vencimento INT             NOT NULL,
    perc_multa      DECIMAL(5, 2)   NOT NULL,
    valor_aluguel   DECIMAL(10, 2)  NOT NULL,
    obs             TEXT,

    PRIMARY KEY (id),

    CONSTRAINT fk_locacao_imovel
        FOREIGN KEY (id_imovel)
            REFERENCES imoveis (id),

    CONSTRAINT fk_locacao_cliente
        FOREIGN KEY (id_cliente)
            REFERENCES clientes (id)
);


CREATE TABLE alugueis
(
    id            INT               NOT NULL AUTO_INCREMENT,
    id_locacao    INT               NOT NULL,
    dt_vencimento DATE              NOT NULL,
    valor_pago    DECIMAL(10, 2)    NOT NULL,
    obs           TEXT,

    PRIMARY KEY (id),

    CONSTRAINT fk_alugueis_locacao
        FOREIGN KEY (id_locacao)
            REFERENCES locacao (id)
);