CREATE TABLE clientes
(
    id            INT          NOT NULL AUTO_INCREMENT,
    nome_cliente  VARCHAR(255) NOT NULL,
    cpf           VARCHAR(11)  NOT NULL,
    telefone      VARCHAR(12),
    email         VARCHAR(100),
    dt_nascimento DATE,

    PRIMARY KEY (id)
);


CREATE TABLE imoveis
(
    id                INT          NOT NULL AUTO_INCREMENT,
    tipo_imovel       VARCHAR(50)  NOT NULL,
    endereco          VARCHAR(255) NOT NULL,
    cep               VARCHAR(10),
    dormitorios       INT,
    banheiros         INT,
    suites            INT,
    metragem          INT,
    valor_aluguel_sug DECIMAL(10, 2),
    obs               TEXT,

    PRIMARY KEY (id)
);


CREATE TABLE locacao
(
    id              INT     NOT NULL AUTO_INCREMENT,
    id_imovel       INT     NOT NULL,
    id_cliente      INT     NOT NULL,
    ativo           BIT NOT NULL,
    data_fim        DATE,
    data_inicio     DATE,
    data_vencimento INT,
    perc_multa      DECIMAL(5, 2),
    valor_aluguel   DECIMAL(10, 2),
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
    id            INT NOT NULL AUTO_INCREMENT,
    id_locacao    INT NOT NULL,
    dt_vencimento DATE,
    valor_pago    DECIMAL(10, 2),
    obs           TEXT,

    PRIMARY KEY (id),

    CONSTRAINT fk_alugueis_locacao
        FOREIGN KEY (id_locacao)
            REFERENCES locacao (id)
);