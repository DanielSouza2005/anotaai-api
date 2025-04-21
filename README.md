<h1>🪪 AnotaAí API</h1> 
<img loading="lazy" src="https://img.shields.io/github/stars/DanielSouza2005/anotaai-api?style=social"/> 
<h2>💡 Sobre </h2> 
<p> RESTful API desenvolvida para o sistema de anotações e gerenciamento de contatos e empresas, com autenticação e boas práticas de desenvolvimento backend em Java. </p> 
<h2>🖥️ Tecnologias usadas </h2>
<div align="left" dir="auto"> 
  <a href="https://skillicons.dev" rel="nofollow"> 
  <img src="https://skillicons.dev/icons?i=java,spring,hibernate,postgres,maven" style="max-width: 100%;"> </a> <br>
</div> 
<h2>🛠️ Funcionalidades do Projeto </h2>

- 📇 `CRUD de Contatos`: Endpoints completos para criar, listar, atualizar e excluir contatos.
- 🏢 `CRUD de Empresas`: Cadastro e gerenciamento de empresas associadas aos contatos.
- 👤 `CRUD de Usuários`: Gestão dos usuários do sistema com autenticação segura.
- 📄 `Paginação e Ordenação`: Listagens otimizadas com suporte a paginação e ordenação dinâmica.
- ✅ `Validações`: Validação de dados com Bean Validation para garantir a integridade das requisições.
- ⚠️ `Tratamento de Erros`: Respostas padronizadas com mensagens claras para erros comuns.
- 🔐 `Criptografia`: Senhas criptografadas com segurança utilizando BCrypt.
- 🔑 `Autenticação JWT`: Autenticação segura baseada em token JWT.
- 📚 `Documentação com Spring Docs`: Interface automática e interativa para explorar os endpoints da API.

<h2>🧪 Tecnologias utilizadas nos testes </h2>

- ⚙️ `Spring Boot Test` – Estrutura principal de testes do Spring.
- 🌐 `MockMvc` – Simula requisições HTTP para os controllers.
- 🎭 `Mockito (@MockitoBean)` – Mocka dependências como o UsuarioRepository e BCryptPasswordEncoder.
- 📦 `JacksonTester` – Serializa e desserializa objetos JSON nos testes.
- 🧪 `JUnit 5` – Framework de testes usado com anotações como @Test e @DisplayName.

<h2>📚 Documentação dos Endpoints</h2> 
<h3>🔐 Autenticação</h3>

- <strong>POST</strong> <code>/login</code>

<strong>Descrição:</strong> Autentica um usuário e retorna um token JWT para acesso aos endpoints protegidos.
<strong>Requisição:</strong>
<pre>
  <code>{
    "email": "usuario@example.com", 
    "senha": "senha123" 
  }</code>
</pre>

<strong>Resposta:</strong>
<pre>
  <code>{
    "token": "jwt-token-aqui"
  }</code>
</pre>

<p>
  <strong>🔐 Nota:</strong> Todos os endpoints (exceto <code>/login</code> e <code>POST /usuarios</code>) requerem um token JWT válido no cabeçalho <code>Authorization</code> no formato <code>Bearer {token}</code>.
</p>

<h3>👤 Usuários</h3>

- <strong>POST</strong> <code>/usuarios</code>

<strong>Descrição:</strong> Cria um novo usuário no sistema.
<strong>Requisição:</strong>

<pre>
  <code>{
    "nome": "Nome do Usuário", 
    "email": "usuario@example.com", 
    "senha": "senha123" 
    }</code>
</pre>

<strong>Resposta:</strong> Detalhes do usuário criado.​

- <strong>GET</strong> <code>/usuarios</code>

<strong>Descrição:</strong> Lista todos os usuários com suporte a paginação e ordenação.

<strong>Parâmetros de Query:</strong>

<code>page</code>: Número da página (opcional).

<code>size</code>: Quantidade de registros por página (opcional).

<code>sort</code>: Campo para ordenação (opcional).

- <strong>GET</strong> <code>/usuarios/{id}</code>

<strong>Descrição:</strong> Retorna os detalhes de um usuário específico.​

- <strong>PUT</strong> <code>/usuarios/{id}</code>

<strong>Descrição:</strong> Atualiza as informações de um usuário existente.​

- <strong>DELETE</strong> <code>/usuarios/{id}</code>

<strong>Descrição:</strong> Remove um usuário do sistema.​

<h3>📇 Contatos</h3>

- <strong>POST</strong> <code>/contatos</code>

<strong>Descrição:</strong> Cria um novo contato associado a uma empresa (não obrigatório).

<strong>Requisição:</strong>

<pre>
  <code>{
      "nome": "Nome do Contato", 
      "email": "contato@example.com", 
      "telefone": "123456789", 
      "empresaId": 1 
  }</code>
</pre>

<strong>Resposta:</strong> Detalhes do contato criado.​

- <strong>GET</strong> <code>/contatos</code>

<strong>Descrição:</strong> Lista todos os contatos com suporte a paginação e ordenação.​

- <strong>GET</strong> <code>/contatos/{id}</code>

<strong>Descrição:</strong> Retorna os detalhes de um contato específico.​

- <strong>PUT</strong> <code>/contatos/{id}</code>

<strong>Descrição:</strong> Atualiza as informações de um contato existente.​

- <strong>DELETE</strong> <code>/contatos/{id}</code>

<strong>Descrição:</strong> Remove um contato do sistema.​

<h3>🏢 Empresas</h3>

- <strong>POST</strong> <code>/empresas</code>

<strong>Descrição:</strong> Cria uma nova empresa.

<strong>Requisição:</strong>

<pre>
  <code>{
      "nome": "Nome da Empresa",
      "cnpj": "12.345.678/0001-99" 
  }</code>
</pre>

<strong>Resposta:</strong> Detalhes da empresa criada.​

- <strong>GET</strong> <code>/empresas</code>

<strong>Descrição:</strong> Lista todas as empresas com suporte a paginação e ordenação.​

- <strong>GET</strong> <code>/empresas/{id}</code>

<strong>Descrição:</strong> Retorna os detalhes de uma empresa específica.​

- <strong>PUT</strong> <code>/empresas/{id}</code>

<strong>Descrição:</strong> Atualiza as informações de uma empresa existente.​

- <strong>DELETE</strong> <code>/empresas/{id}</code>

<strong>Descrição:</strong> Remove uma empresa do sistema.​

