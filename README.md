📚 Projeto: Integração com HubSpot (Meetime)

Este projeto implementa integração com a API do HubSpot utilizando OAuth 2.0, além de realizar a criação de contatos e receber notificações via Webhook.

🚀 Funcionalidades

- Geração da URL de Autorização OAuth2
- Callback OAuth2 para troca do código pelo Access Token
- Criação de contatos no HubSpot CRM (via camada de serviço)
- Recebimento e processamento de Webhook de criação de contatos

🔧 Tecnologias Utilizadas

Java 17+
Spring Boot
Spring Web
Spring Validation
WebClient (Spring Reactive)
Lombok
Jackson (JSON)

⚙️ Como Executar o Projeto

Pré-requisitos
Java 17 ou superior
Maven
Conta de Desenvolvedor HubSpot
Passos
Clone o repositório:



git clone https://github.com/seu-usuario/meetime-hubspot-integration.git

🚩 Primeiros Passos: Configuração do Ambiente HubSpot CLI
Os aplicativos do HubSpot usam JavaScript. Node.js é um ambiente de servidor de tempo de execução que executa JavaScript. npm é um gerenciador de pacotes JavaScript que funciona com Node. Recomendamos usar um gerenciador de pacotes como o Homebrew para instalar o Node.js (ou baixe em https://nodejs.org/)..)

O HubSpot CLI permite que você conecte suas ferramentas de desenvolvimento local ao HubSpot. Instale-o executando:

bash
CopyInsert in Terminal
npm install -g @hubspot/cli@latest
No terminal, execute a inicialização:

bash
CopyInsert in Terminal
hs init --account=SEU_ID_DA_CONTA
Substitua SEU_ID_DA_CONTA pelo seu ID de conta HubSpot (exemplo: 497xx241).

Siga as instruções para criar uma chave de acesso pessoal. Isso é necessário para que a CLI possa interagir com sua conta da HubSpot:

Você será redirecionado ao HubSpot em seu navegador web.
Gere sua chave de acesso pessoal e, em seguida, copie e cole sua chave no terminal.
Forneça um nome para a conta que você está conectando para referência ao usar a CLI. Isso criará automaticamente seu arquivo de configuração (hubspot.config.yml) e definirá a conta como padrão.
Dicas de comandos úteis:

Execute hs auth para conectar outra conta à CLI.
Execute hs accounts para alternar entre contas e selecionar outra conta para usar como padrão.
Execute o comando hs auth e siga o prompt no seu terminal para gerar uma chave de acesso pessoal na sua conta de desenvolvedor. Copie-a de volta para o seu terminal para autenticar a conta.
🔗 Como Ajustar a URL de Redirecionamento no HubSpot
Antes de executar o fluxo de autenticação, é necessário configurar a URL de redirecionamento no painel de desenvolvedor da HubSpot:

Acesse: https://app.hubspot.com/developer
Entre na seção de aplicativos (Apps).
Preencha as informações necessárias do seu aplicativo.
Acesse a aba "Autenticação".
Em "URLs de redirecionamento", adicione:
CopyInsert
http://localhost:8080/auth/callback
Essa configuração é obrigatória para que o fluxo OAuth2 funcione corretamente após executar o projeto.

2.Configure as variáveis no application.properties:

server.port=8080 hubspot.clientId=SEU_CLIENT_ID hubspot.clientSecret=SEU_CLIENT_SECRET hubspot.redirectUri=http://localhost:8080/auth/callback hubspot.webhook.secret=SEU_WEBHOOK_SECRET

1. Compile e execute:
   ```bash
mvn spring-boot:run


5. Acesse http://localhost:8080/swagger-ui/index.html

📝 Observações Importantes

As variáveis do application.properties devem ser preenchidas com os valores da sua conta HubSpot.

📚 Referências

Documentação da API HubSpot

Título do e-mail: "Processo Seletivo Meetime - Case Técnico"

📋 Recursos Útéis

OAuth Quickstart HubSpot

HubSpot CLI Docs

---

## 📋 Progresso do Case Técnico

### O que já foi feito:

✅ **1. Geração da Authorization URL**
- Implementado e funcional.

✅ **2. Processamento do Callback OAuth**
- Implementado e funcional (realiza troca pelo `access_token` e `refresh_token`).

### O que ainda falta implementar:

📋 O que fazer:

✅ 1. Gere uma nova URL de autorização.
✅ 2. Acesse imediatamente a URL no navegador.
✅ 3. Autorize o app (faça o login ou clique em "Allow" no HubSpot).
✅ 4. Quando o HubSpot redirecionar para `http://localhost:8080/auth/callback?code=XXXX`, copie e use esse novo code imediatamente.

> **IMPORTANTE:** Cada vez que você clicar no link de autorização você gera um code novo, e ele não pode ser reutilizado.

✍ Como seria o fluxo correto:

```plaintext
[1] Gero a URL
[2] Abro a URL no navegador -> HubSpot mostra tela de autorização
[3] Autorizo -> Redireciona para http://localhost:8080/auth/callback?code=123456
[4] Capturo esse código imediatamente
[5] Faço uso do código
```

---

## 💡 Análise das Tecnologias Utilizadas

**Java 17+**  
Optei por utilizar o Java 17 por ser uma versão LTS (Long Term Support), garantindo estabilidade, performance e acesso aos recursos mais modernos da linguagem. Além disso, o ecossistema Java é robusto e amplamente utilizado em integrações corporativas.

**Spring Boot**  
Escolhi o Spring Boot para acelerar o desenvolvimento, já que ele simplifica a configuração e o bootstrap de aplicações Java. Ele também oferece uma estrutura modular, facilitando a criação de APIs REST e integração com outros serviços.

**Spring Web**  
O módulo Spring Web foi utilizado para expor endpoints REST de forma prática e padronizada, aproveitando todo o poder do Spring MVC para lidar com requisições HTTP e roteamento.

**Spring Validation**  
A validação dos dados de entrada é fundamental em qualquer API. O Spring Validation permite aplicar regras de validação de forma declarativa, garantindo que apenas dados corretos sejam processados, reduzindo bugs e facilitando a manutenção.

**WebClient (Spring Reactive)**  
Para consumir a API do HubSpot, utilizei o WebClient, que é o cliente HTTP reativo do Spring. Ele oferece uma abordagem moderna, eficiente e não bloqueante para chamadas externas, sendo ideal para integrações com APIs de terceiros.

**Lombok**  
O Lombok foi escolhido para reduzir a verbosidade do código Java, gerando automaticamente getters, setters, construtores e outros métodos comuns. Isso deixa o código mais limpo, legível e fácil de manter.

**Jackson (JSON)**  
Para serialização e desserialização de objetos JSON, usei o Jackson, que é a biblioteca padrão do Spring Boot para manipulação de JSON. Ele facilita o mapeamento entre objetos Java e JSON, essencial para integração com APIs REST.

---

### 🛠 Melhorando o fluxo de autenticação

Para deixar o processo mais fluido, também é possível automatizar a troca do code pelo token e exibir diretamente no navegador uma mensagem como "Token gerado com sucesso", sem precisar copiar e colar o código manualmente. Isso torna a experiência mais prática para o usuário.

---

⚠️ **Atenção sobre o Token OAuth2 HubSpot**

O token de acesso OAuth2 da HubSpot expira em aproximadamente 6 horas após ser emitido. Após esse período, será necessário obter um novo token seguindo o fluxo de autenticação novamente, ou implementar o uso do refresh token para renovar automaticamente. Sempre verifique se está utilizando um token válido ao fazer chamadas para a API do HubSpot.


---
## ✅ Checklist de Entrega — Case Técnico Integração HubSpot

### Endpoints Obrigatórios

- [X] **Geração da Authorization URL**  
  Endpoint que retorna a URL de autorização para iniciar o fluxo OAuth com o HubSpot.

- [X] **Processamento do Callback OAuth**  
  Endpoint que recebe o código de autorização do HubSpot e realiza a troca pelo token de acesso.

- [X] **Criação de Contatos**  
  Endpoint para criar um contato no CRM via API do HubSpot, respeitando as políticas de rate limit.

- [X] **Recebimento de Webhook para Criação de Contatos**  
  Endpoint que escuta/processa eventos do tipo "contact.creation" enviados pelo webhook do HubSpot.

---

### Requisitos Técnicos

- [X] API REST desenvolvida em Java usando Spring Boot (ou Play Framework).
- [X] Boas práticas de segurança, conforme recomendações da documentação do HubSpot.
- [X] Boas práticas de código: separação de responsabilidades, tratamento de erros, estrutura clara.
- [X] README.md com instruções detalhadas para execução do projeto.
- [X] Documentação técnica explicando decisões, motivação para uso de libs e possíveis melhorias futuras.

---

### Entregáveis

- [X] Código-fonte disponível em repositório GitHub.
- [X] README.md com instruções detalhadas.
- [X] Documentação técnica (pode estar no README, explicando decisões e possíveis melhorias).
- [] E-mail enviado com o link do repositório para:  
  - thais.dias@meetime.com.br  
  - joao@meetime.com.br  
  - william.willers@meetime.com.br  
  - victor@meetime.com.br  
  **Título:** “Processo seletivo Meetime - Case técnico”

---

### Observações

- [X] Todas as bibliotecas/libs utilizadas estão descritas e justificadas no README.md.
- [X] O projeto pode ser executado seguindo as instruções fornecidas.
- [X] Possíveis melhorias futuras estão listadas/documentadas.

---

> Construído com amor para a Meetime ❤️