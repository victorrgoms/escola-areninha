-- Inserindo as areninhas do seu JSON
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Escola Areninha Lagoa do Catão', 'R. Monte Líbano, S/N - Mondubim, Fortaleza - CE, 60810-670', -3.8262407283547275, -38.56361806084558);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha Papicu', 'Rua Valdetário Mota, s/n - Papicu, Fortaleza - CE', -3.7356, -38.4839);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha Pirambu', 'Av. Leste Oeste, s/n - Pirambu, Fortaleza - CE', -3.7065, -38.5433);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha Bom Jardim', 'Rua Oscar Araripe, s/n - Bom Jardim, Fortaleza - CE', -3.7916, -38.6025);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha Conjunto Esperança', 'Av. Contorno Norte, s/n - Conjunto Esperança, Fortaleza - CE', -3.8097, -38.5867);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha José Walter', 'Av. I, s/n - Prefeito José Walter, Fortaleza - CE', -3.8221, -38.5404);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha Serrinha', 'Rua Rosinha Sampaio, s/n - Serrinha, Fortaleza - CE', -3.7847, -38.5236);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha Benfica', 'Av. da Universidade, s/n - Benfica, Fortaleza - CE', -3.7449, -38.5367);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha Parangaba', 'Av. Osório de Paiva, s/n - Parangaba, Fortaleza - CE', -3.7722, -38.5660);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha Valdir Bezerra', 'Av. Paulino Rocha, 1343 - Cajazeiras, Fortaleza - CE', -3.8102, -38.5113);
INSERT INTO areninha (nome, endereco, latitude, longitude) VALUES ('Areninha Alvorada', 'Av. Conselheiro Gomes de Freitas, s/n - Sapiranga, Fortaleza - CE, 60833-104', -3.7898, -38.4558);

-- Criando seu usuário admin pra você poder deletar o pessoal pelo postman sem dor de cabeça
-- A senha aqui é '123' já criptografada em BCrypt
INSERT INTO usuario (nome, email, senha, tipo_usuario, turno_lotado, area_conhecimento, areninha_id) VALUES ('Victor Admin', 'admin@areninha.com', '$2a$10$76/yO/gJ6DInB8X.f6hT6.wO8mB5zN.vJjC2Y7S2qFv7Kj8r/Y2L.', 'ADMIN', 'Ambos', 'Gestão', 1);