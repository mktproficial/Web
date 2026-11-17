CREATE TABLE perfis (
                        id INT AUTO_INCREMENT,
                        nome VARCHAR(30) NOT NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT uk_perfis_nome UNIQUE (nome)
);

INSERT INTO perfis (nome)
VALUES ('ADMIN'), ('ATENDENTE'), ('CLIENTE');


CREATE TABLE usuarios (
                          id INT AUTO_INCREMENT,
                          login VARCHAR(100) NOT NULL,
                          senha VARCHAR(100) NOT NULL,
                          ativo BOOLEAN NOT NULL DEFAULT TRUE,
                          cliente_id INT,
                          PRIMARY KEY (id),
                          CONSTRAINT uk_usuarios_login UNIQUE (login),
                          CONSTRAINT uk_usuarios_cliente UNIQUE (cliente_id),
                          CONSTRAINT fk_usuarios_cliente
                              FOREIGN KEY (cliente_id) REFERENCES clientes (id)
);


CREATE TABLE usuario_perfis (
                                usuario_id INT NOT NULL,
                                perfil_id INT NOT NULL,
                                PRIMARY KEY (usuario_id, perfil_id),
                                CONSTRAINT fk_usuario_perfis_usuario
                                    FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
                                        ON DELETE CASCADE,
                                CONSTRAINT fk_usuario_perfis_perfil
                                    FOREIGN KEY (perfil_id) REFERENCES perfis (id)
);


INSERT INTO usuarios (login, senha, ativo)
VALUES (
           'admin',
           '$2b$10$AKr4fPfdtpslSIrUuA4Ft./KVBD2MLae/yoAyUTrXs6XKoXUjem6u',
           TRUE
       );


INSERT INTO usuario_perfis (usuario_id, perfil_id)
SELECT u.id, p.id
FROM usuarios u
         CROSS JOIN perfis p
WHERE u.login = 'admin'
  AND p.nome = 'ADMIN';