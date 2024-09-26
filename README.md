
# API de Gestão de Contas

Esta API permite o gerenciamento de contas, oferecendo funcionalidades como registrar, atualizar, obter detalhes, alterar a situação, remover contas, além de calcular o valor total pago em um período e importar contas via arquivo CSV.

## Funcionalidades Principais

- **Cadastro de contas**: Registre novas contas com informações como valor, data de vencimento, data de pagamento, descrição e situação.
- **Consulta de contas**: Obtenha as contas cadastradas com paginação e filtros opcionais (data de vencimento e descrição).
- **Atualização de contas**: Atualize as informações de uma conta existente.
- **Remoção de contas**: Remova contas cadastradas.
- **Alteração da situação da conta**: Atualize o status de uma conta (Paga, Pendente, etc).
- **Cálculo de valor total pago**: Obtenha o valor total pago em um determinado período.
- **Importação de contas via CSV**: Importe múltiplas contas de uma só vez a partir de um arquivo CSV.

## Requisitos Gerais

1. **Java 21**: O projeto utiliza a linguagem Java na versão 21.
2. **Spring Boot**: Framework para construção da aplicação.
3. **PostgreSQL**: Banco de dados utilizado no projeto.
4. **Docker**: A aplicação roda em containers Docker.
5. **Docker Compose**: Utilizado para orquestrar a aplicação e seus serviços como o banco de dados.
6. **GitHub**: O projeto está hospedado em um repositório GitHub.
7. **Autenticação**: Mecanismo de autenticação implementado Spring Security e JWT.
8. **Domain Driven Design**: Organização do projeto com o padrão DDD.
9. **Flyway**: Ferramenta para versionamento e criação de estrutura de banco de dados.
10. **JPA**: Utilizado para persistência de dados.
11. **Paginação**: Todas as APIs de consulta são paginadas.

## **Endpoints**

### 1. **Obter Contas - Filtragem e Paginação**
   - **URL**: `/contas`
   - **Método**: `GET`
   - **Descrição**: Retorna uma lista paginada de contas, com a possibilidade de filtrar por data de vencimento e descrição.
   - **Parâmetros de Query**:
     - `dataVencimento` (opcional): Data de vencimento da conta no formato `dd/MM/yyyy`.
     - `descricao` (opcional): Texto que será buscado na descrição das contas.
     - `pageable`: Parâmetro para controle de paginação.
   - **Exemplo de Requisição**:
     ```bash
     GET /api/v1/contas?dataVencimento=01/01/2023&descricao=Internet&page=0&size=10
     ```
   - **Resposta**:
     - `200 OK`: Página de contas filtradas.
     ```json
     {
       "content": [
         {
           "id": 1,
           "dataVencimento": "2023-01-01",
           "valor": 200.50,
           "descricao": "Conta de Internet",
           "situacao": "PENDENTE"
         }
       ],
       "totalElements": 1,
       "totalPages": 1
     }
     ```

### 2. **Obter Conta por ID**
   - **URL**: `/api/v1/contas/{id}`
   - **Método**: `GET`
   - **Descrição**: Retorna os detalhes de uma conta específica.
   - **Parâmetros de Rota**:
     - `id` (obrigatório): O ID da conta.
   - **Exemplo de Requisição**:
     ```bash
     GET /api/v1/contas/1
     ```
   - **Resposta**:
     - `200 OK`: Detalhes da conta.
     ```json
     {
       "id": 1,
       "dataVencimento": "2023-01-01",
       "valor": 200.50,
       "descricao": "Conta de Internet",
       "situacao": "PENDENTE"
     }
     ```
   - **Erros**:
     - `404 NOT FOUND`: Conta não encontrada.

### 3. **Registrar Conta**
   - **URL**: `/api/v1/contas`
   - **Método**: `POST`
   - **Descrição**: Cria uma nova conta.
   - **Corpo da Requisição** (JSON):
     ```json
     {
       "dataVencimento": "2023-01-01",
       "valor": 200.50,
       "descricao": "Conta de Internet",
       "situacao": "PENDENTE"
     }
     ```
   - **Resposta**:
     - `201 CREATED`: Conta criada com sucesso.
     ```json
     {
       "id": 1,
       "dataVencimento": "2023-01-01",
       "valor": 200.50,
       "descricao": "Conta de Internet",
       "situacao": "PENDENTE"
     }
     ```

### 4. **Atualizar Conta**
   - **URL**: `/api/v1/contas/{id}`
   - **Método**: `PUT`
   - **Descrição**: Atualiza uma conta existente.
   - **Parâmetros de Rota**:
     - `id` (obrigatório): O ID da conta.
   - **Corpo da Requisição** (JSON):
     ```json
     {
       "dataVencimento": "2023-01-01",
       "valor": 200.50,
       "descricao": "Conta de Internet Atualizada",
       "situacao": "PAGA"
     }
     ```
   - **Resposta**:
     - `201 CREATED`: Conta atualizada com sucesso.
     ```json
     {
       "id": 1,
       "dataVencimento": "2023-01-01",
       "valor": 200.50,
       "descricao": "Conta de Internet Atualizada",
       "situacao": "PAGA"
     }
     ```

### 5. **Alterar Situação da Conta**
   - **URL**: `/api/v1/contas/{id}/situacao`
   - **Método**: `PATCH`
   - **Descrição**: Altera a situação de uma conta.
   - **Parâmetros de Rota**:
     - `id` (obrigatório): O ID da conta.
   - **Corpo da Requisição** (JSON):
     ```json
     {
       "situacao": "PAGA"
     }
     ```
   - **Resposta**:
     - `201 CREATED`: Situação alterada com sucesso.
     ```json
     {
       "id": 1,
       "dataVencimento": "2023-01-01",
       "valor": 200.50,
       "descricao": "Conta de Internet",
       "situacao": "PAGA"
     }
     ```

### 6. **Remover Conta**
   - **URL**: `/api/v1/contas/{id}`
   - **Método**: `DELETE`
   - **Descrição**: Remove uma conta existente.
   - **Parâmetros de Rota**:
     - `id` (obrigatório): O ID da conta.
   - **Resposta**:
     - `204 NO CONTENT`: Conta removida com sucesso.
     
### 7. **Obter Valor Total Pago por Período**
   - **URL**: `/api/v1/contas/valor-total-pago`
   - **Método**: `GET`
   - **Descrição**: Retorna o valor total pago de contas dentro de um período.
   - **Parâmetros de Query**:
     - `inicio`: Data de início no formato `dd/MM/yyyy`.
     - `fim`: Data de fim no formato `dd/MM/yyyy`.
   - **Exemplo de Requisição**:
     ```bash
     GET /api/v1/contas/valor-total-pago?inicio=01/01/2023&fim=31/01/2023
     ```
   - **Resposta**:
     - `200 OK`: Valor total pago dentro do período.
     ```json
     {
       "valorTotalPago": 1000.00
     }
     ```

### 8. **Importar Contas via CSV**
   - **URL**: `/api/v1/contas/importar`
   - **Método**: `POST`
   - **Descrição**: Importa um arquivo CSV de contas.
   - **Parâmetros de Form-Data**:
     - `file`: Arquivo CSV contendo as contas.
   - **Exemplo de Requisição** (via cURL):
     ```bash
     curl -X POST "http://localhost:8080/contas/api/v1/importar" -F "file=@contas.csv"
     ```
   - **Resposta**:
     - `201 CREATED`: Contas importadas com sucesso.
     ```json
     {
       "mensagem": "Contas importadas com sucesso."
     }
     ```
