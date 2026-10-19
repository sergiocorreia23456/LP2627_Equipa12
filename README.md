# 🎓 Projeto — Objetos Achados e Perdidos | Equipa 12

## 📋 Descrição

Aplicação desenvolvida em Java para a gestão de objetos achados e perdidos.

O sistema permite aos utilizadores registar, consultar, pesquisar, editar e eliminar anúncios de objetos, disponibilizando funcionalidades específicas para utilizadores e administradores.

## 👥 Elementos da Equipa

- Afonso Dias
- Pedro Ferreira
- Rafael Viana
- Sérgio Correia
- Tiago Organista

## 🛠️ Tecnologias Utilizadas

- **Java** — Linguagem de programação
- **Hibernate** 
- **MySQL** — Sistema de gestão da base de dados
- **MySQL Workbench** — Administração da base de dados
- **Maven** — Gestão de dependências
- **Eclipse IDE** — Ambiente de desenvolvimento

## ⚙️ Funcionalidades

### Utilizador
- Registar objetos encontrados e perdidos
- Editar e eliminar objetos
- Pesquisar objetos
- Consultar anúncios
- Editar perfil

### Administrador
- Consultar todos os objetos
- Consultar estatísticas
- Eliminar anúncios
- Gerir marcas, categorias e subcategorias

### Base de Dados
- Criar, consultar, editar e eliminar marcas
- Criar, consultar, editar e eliminar categorias
- Criar, consultar, editar e eliminar subcategorias
- Associar subcategorias às respetivas categorias

## 🗄️ Configuração da Base de Dados

1. Instalar o MySQL Server e o MySQL Workbench.
2. Executar o ficheiro `BaseDados.sql` no MySQL Workbench.
3. Configurar as credenciais locais de ligação ao MySQL.
4. Confirmar que o servidor MySQL está em execução.
5. Executar a aplicação.

O Hibernate gere a criação e atualização das tabelas mapeadas através da propriedade `hibernate.hbm2ddl.auto=update`.

## ▶️ Executar o Projeto

1. Abrir o Eclipse IDE.
2. Importar o projeto como **Existing Maven Project**.
3. Atualizar as dependências Maven.
4. Confirmar a configuração do Hibernate.
5. Executar a classe `Main.java`.
6. Utilizar os menus apresentados.

## 🚧 Estado do Projeto

Projeto em desenvolvimento.

As operações de gestão de marcas, categorias e subcategorias utilizam Hibernate e MySQL. A integração das restantes funcionalidades com a base de dados encontra-se em desenvolvimento.

## 📚 Contexto Académico

Projeto desenvolvido no âmbito da unidade curricular de Laboratório de Programação, ano letivo 2026/2027.

