
-- Criar a base de dados, caso ainda não exista
CREATE DATABASE IF NOT EXISTS objetos_encontrados_perdidos;

-- Selecionar a base de dados
USE objetos_encontrados_perdidos;

-- Mostrar todas as tabelas
SHOW TABLES;

-- Consultar os utilizadores
SELECT * FROM utilizadores;

-- Consultar os objetos
SELECT * FROM objetos;

-- Consultar as marcas
SELECT * FROM Marca;

-- Consultar as categorias
SELECT * FROM Categoria;

-- Consultar as subcategorias
SELECT * FROM SubCategoria;

-- Consultar os objetos e respetivas marcas e categorias
SELECT
    o.id,
    o.titulo,
    o.descricao,
    o.localizacao,
    o.dataRegisto,
    o.tipo,
    o.estado,
    m.nome AS marca,
    c.nome AS categoria
FROM objetos o
LEFT JOIN Marca m ON o.marca_id = m.id
LEFT JOIN Categoria c ON o.categoria_id = c.id;

-- Contar o total de objetos
SELECT COUNT(*) AS total_objetos
FROM objetos;

-- Contar objetos perdidos e encontrados
SELECT tipo, COUNT(*) AS quantidade
FROM objetos
GROUP BY tipo;
