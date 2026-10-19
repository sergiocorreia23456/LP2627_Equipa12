
-- ============================================================
-- PROJETO: Sistema de Gestão de Objetos Achados e Perdidos
-- EQUIPA: 12
-- BASE DE DADOS: MySQL 
-- ORM: Hibernate
-- ============================================================

-- 1. CRIAÇÃO DA BASE DE DADOS
-- Cria a base de dados apenas se ainda não existir.

CREATE DATABASE IF NOT EXISTS objetos_encontrados_perdidos
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;


-- 2. SELEÇÃO DA BASE DE DADOS
-- Define a base de dados a utilizar.

USE objetos_encontrados_perdidos;


-- 3. VERIFICAÇÃO DAS TABELAS
-- Apresenta todas as tabelas existentes.

SHOW TABLES;


-- 4. CONSULTA DOS REGISTOS
-- Permite verificar os dados guardados nas principais tabelas.
-- Executar após o Hibernate criar as tabelas.

SELECT * FROM Utilizador;
SELECT * FROM Objeto;
SELECT * FROM Marca;
SELECT * FROM Categoria;
SELECT * FROM SubCategoria;


-- ============================================================
-- NOTAS SOBRE A GESTÃO DA BASE DE DADOS
-- ============================================================

-- 1. A criação e atualização das tabelas é realizada
--    automaticamente pelo Hibernate, através da propriedade:
--
--    hibernate.hbm2ddl.auto = update
--
-- 2. As entidades mapeadas no projeto são:
--    - Utilizador
--    - Administrador (herda de Utilizador)
--    - Objeto
--    - Marca
--    - Categoria
--    - SubCategoria
--
-- 3. Como é utilizada a estratégia de herança SINGLE_TABLE,
--    Administrador e Utilizador partilham a mesma tabela.
--
-- 4. O Hibernate gere as relações entre as entidades,
--    incluindo as respetivas chaves estrangeiras.
--
-- 5. Este script não elimina tabelas nem registos existentes.
--
-- ============================================================
