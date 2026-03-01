# API Escola Areninha 

Esta é a API RESTful desenvolvida para o sistema de gestão do projeto **Escola Areninha**, uma iniciativa voltada para o esporte e educação em tempo integral, em parceria com a **Universidade Estadual do Ceará (UECE)** e a **Prefeitura de Fortaleza**.

O sistema fornece toda a infraestrutura de backend para o aplicativo mobile, gerenciando autenticação de usuários, controle de permissões, agendamento de atividades, registro de frequência e geração automatizada de relatórios oficiais.

##  Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot 3**
- **Spring Security & JWT** (Autenticação e Autorização)
- **Spring Data JPA** (Persistência)
- **PostgreSQL** (Banco de dados relacional)
- **OpenPDF** (Geração de relatórios em memória)
- **Lombok** (Redução de boilerplate)
- **Maven** (Gerenciamento de dependências)

##  Funcionalidades Principais

- **Segurança e Perfis de Acesso:** Login via token JWT com Role-Based Access Control (RBAC) para Administradores, Supervisores e Monitores.
- **Gestão de Areninhas:** Cadastro e listagem de unidades, incluindo mapeamento de coordenadas (Latitude/Longitude) para integração com mapas no mobile.
- **Grade de Horários e Eventos:** Gerenciamento da grade de aulas semanal e calendário de eventos institucionais de cada unidade.
- **Registros de Frequência:** Lançamento diário das aulas ministradas pelos monitores.
- **Geração de Relatórios (PDF):** Geração automatizada do relatório mensal de atividades (padrão Decofin). O sistema formata os dados, calcula a carga horária e injeta as assinaturas via URL diretamente no PDF, retornando o arquivo pronto para download sem onerar o storage do servidor.
- **Galeria de Imagens:** Registro de URLs das fotos das atividades vinculadas a cada areninha e usuário.
- **Gestão de Equipes:** Listagem de profissionais alocados por unidade de atuação.

## Como rodar o projeto localmente

1. Certifique-se de ter o **Java**, **Maven** e o **PostgreSQL** instalados.
2. Clone o repositório:
   ```bash
   git clone [https://github.com/seu-usuario/escola-areninha-api.git](https://github.com/seu-usuario/escola-areninha-api.git)
   ```

3. Crie um banco de dados no PostgreSQL chamado `areninha_db`.
4. Configure as variáveis de ambiente ou altere o arquivo `src/main/resources/application.properties` com as suas credenciais do banco e a chave secreta do JWT:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/areninha_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

api.security.token.secret=sua_chave_secreta_aqui
```


5. Execute o projeto usando a sua IDE de preferência ou via terminal:
```bash
./mvnw spring-boot:run

```


6. A API estará disponível em `http://localhost:8080`.

##  Desenvolvedor

**Victor Gomes** Desenvolvedor Full Stack & Estudante de Ciência da Computação (UECE)

[LinkedIn](https://www.linkedin.com/in/paulo-victor-desousa/) | [GitHub](https://github.com/victorrgoms)