CREATE TABLE estado (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ESTADOS EM ORDEM ALFABÉTICA
INSERT INTO estado (codigo, nome) VALUES(1, 'Acre');
INSERT INTO estado (codigo, nome) VALUES(2, 'Alagoas');
INSERT INTO estado (codigo, nome) VALUES(3, 'Amazonas');
INSERT INTO estado (codigo, nome) VALUES(4, 'Amapá');
INSERT INTO estado (codigo, nome) VALUES(5, 'Bahia');
INSERT INTO estado (codigo, nome) VALUES(6, 'Ceará');
INSERT INTO estado (codigo, nome) VALUES(7, 'Distrito Federal');
INSERT INTO estado (codigo, nome) VALUES(8, 'Espírito Santo');
INSERT INTO estado (codigo, nome) VALUES(9, 'Goiás');
INSERT INTO estado (codigo, nome) VALUES(10, 'Maranhão');
INSERT INTO estado (codigo, nome) VALUES(11, 'Minas Gerais');
INSERT INTO estado (codigo, nome) VALUES(12, 'Mato Grosso do Sul');
INSERT INTO estado (codigo, nome) VALUES(13, 'Mato Grosso');
INSERT INTO estado (codigo, nome) VALUES(14, 'Pará');
INSERT INTO estado (codigo, nome) VALUES(15, 'Paraíba');
INSERT INTO estado (codigo, nome) VALUES(16, 'Pernambuco');
INSERT INTO estado (codigo, nome) VALUES(17, 'Piauí');
INSERT INTO estado (codigo, nome) VALUES(18, 'Paraná');
INSERT INTO estado (codigo, nome) VALUES(19, 'Rio de Janeiro');
INSERT INTO estado (codigo, nome) VALUES(20, 'Rio Grande do Norte');
INSERT INTO estado (codigo, nome) VALUES(21, 'Rondônia');
INSERT INTO estado (codigo, nome) VALUES(22, 'Roraima');
INSERT INTO estado (codigo, nome) VALUES(23, 'Rio Grande do Sul');
INSERT INTO estado (codigo, nome) VALUES(24, 'Santa Catarina');
INSERT INTO estado (codigo, nome) VALUES(25, 'Sergipe');
INSERT INTO estado (codigo, nome) VALUES(26, 'São Paulo');
INSERT INTO estado (codigo, nome) VALUES(27, 'Tocantins');

CREATE TABLE cidade (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
  codigo_estado BIGINT(20) NOT NULL,
  FOREIGN KEY (codigo_estado) REFERENCES estado(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- CIDADE/MUNICÍPIOS DO ACRE EM ORDEM ALFABÉTICA
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (1, 'Acrelândia', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (2, 'Assis Brasil', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (3, 'Brasiléia', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (4, 'Bujari', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (5, 'Capixaba', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (6, 'Cruzeiro do Sul', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (7, 'Epitaciolândia', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (8, 'Feijó', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (9, 'Jordão', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (10, 'Mâncio Lima', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (11, 'Manoel Urbano', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (12, 'Marechal Thaumaturgo', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (13, 'Plácido de Castro', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (14, 'Porto Acre', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (15, 'Porto Walter', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (16, 'Rio Branco', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (17, 'Rodrigues Alves', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (18, 'Santa Rosa do Purus', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (19, 'Senador Guiomard', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (20, 'Sena Madureira', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (21, 'Tarauacá', 1);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (22, 'Xapuri', 1);

-- CIDADE/MUNICÍPIOS DE ALAGOAS
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (26, 'Água Branca', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (27, 'Anadia', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (28, 'Arapiraca', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (29, 'Atalaia', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (30, 'Barra de Santo Antônio', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (31, 'Barra de São Miguel', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (32, 'Batalha', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (33, 'Belém', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (34, 'Belo Monte', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (35, 'Boca da Mata', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (36, 'Branquinha', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (37, 'Cacimbinhas', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (38, 'Cajueiro', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (39, 'Campestre', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (40, 'Campo Alegre', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (41, 'Campo Grande', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (42, 'Canapi', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (43, 'Capela', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (44, 'Carneiros', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (45, 'Chã Preta', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (46, 'Coité do Nóia', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (47, 'Colônia Leopoldina', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (48, 'Coqueiro Seco', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (49, 'Coruripe', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (50, 'Craíbas', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (51, 'Delmiro Gouveia', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (52, 'Dois Riachos', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (53, 'Estrela de Alagoas', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (54, 'Feira Grande', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (55, 'Feliz Deserto', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (56, 'Flexeiras', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (57, 'Girau do Ponciano', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (58, 'Ibateguara', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (59, 'Igreja Nova', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (60, 'Jacaré dos Homens', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (61, 'Jacuípe', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (62, 'Japaratinga', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (63, 'Jaramataia', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (64, 'Jequiá da Praia', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (65, 'Joaquim Gomes', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (66, 'Jundiá', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (67, 'Junqueiro', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (68, 'Lagoa da Canoa', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (69, 'Limoeiro de Anadia', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (70, 'Maceió', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (71, 'Major Isidoro', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (72, 'Mar Vermelho', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (73, 'Maragogi', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (74, 'Maravilha', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (75, 'Marechal Deodoro', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (76, 'Maribondo', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (77, 'Mata Grande', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (78, 'Matriz de Camaragibe', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (79, 'Messias', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (80, 'Minador do Negrão', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (81, 'Monteirópolis', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (82, 'Murici', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (83, 'Novo Lino', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (84, 'Olho D''Água das Flores', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (85, 'Olho D''Água do Casado', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (86, 'Olho D''Água Grande', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (87, 'Olivença', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (88, 'Ouro Branco', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (89, 'Palestina', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (90, 'Palmeira dos Índios', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (91, 'Pariconha', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (92, 'Paripueira', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (93, 'Passo de Camaragibe', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (94, 'Paulo Jacinto', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (95, 'Penedo', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (96, 'Piaçabuçu', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (97, 'Pilar', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (98, 'Pindoba', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (99, 'Piranhas', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (100, 'Poço das Trincheiras', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (101, 'Porto Calvo', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (102, 'Porto Real do Colegio', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (103, 'Porto de Pedras', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (104, 'Quebrangulo', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (105, 'Rio Largo', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (106, 'Roteiro', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (107, 'Santa Luzia do Norte', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (108, 'Santana do Ipanema', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (109, 'Santana do Mundau', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (110, 'São Brás', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (111, 'São José da Laje', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (112, 'São José da Tapera', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (113, 'São Luis do Quitunde', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (114, 'São Miguel dos Campos', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (1206, 'São Miguel dos Milagres', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (115, 'São Sebastião', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (116, 'Satuba', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (117, 'Senador Rui Palmeira', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (118, 'Tanque D''Arca', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (119, 'Taquarana', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (120, 'Teotônio Vilela', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (121, 'Traipu', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (122, 'União dos Palmares', 2);
INSERT INTO cidade (codigo, nome, codigo_estado) VALUES (123, 'Viçosa', 2);

-- OBS: O RESTANTE DOS MUNICÍPIOS INSERIMOS DIRETAMENTE NO MYSQL WORKBENCH

ALTER TABLE pessoa DROP COLUMN cidade;
ALTER TABLE pessoa DROP COLUMN estado;
ALTER TABLE pessoa ADD COLUMN codigo_cidade BIGINT(20);
ALTER TABLE pessoa ADD CONSTRAINT fk_pessoa_cidade FOREIGN KEY (codigo_cidade) REFERENCES cidade(codigo);

UPDATE pessoa SET codigo_cidade = 2;
