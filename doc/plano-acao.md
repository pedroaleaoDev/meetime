🎯 Plano de Ação: API REST Java - Integração HubSpot
1. Ambiente de Desenvolvimento
Spring Boot 3.x (última versão estável)

Java 17 ou superior

Maven para build

Lombok para reduzir boilerplate

OpenFeign ou RestTemplate para chamadas HTTP

H2 (memória) ou Postgres (opcional) para simular persistência local dos tokens

Spring Security para futuras proteções

2. Arquitetura (Estilo MVC)
Model:

OAuthToken: representa os dados de access token e refresh token.

Contact: representa os dados de um contato (nome, e-mail, etc).

Controller:

OAuthController

GET /oauth/authorize-url → gera URL de autorização HubSpot.

GET /oauth/callback → recebe o código e troca por tokens.

ContactController

POST /contacts → cria um contato via API do HubSpot.

POST /webhooks/contact → escuta notificações de criação de contatos.

Service:

OAuthService

Gera URL de OAuth.

Troca código por token.

Armazena e gerencia access tokens.

ContactService

Faz requisições para criar contatos na API do HubSpot.

Trata limits (rate limit) com retry/backoff simples.

Processa eventos recebidos via Webhook.


OAuthTokenRepository

Armazena os tokens para reuso.

Configuração:

HubSpotProperties (carrega dados sensíveis do application.yml — clientId, clientSecret, redirectUri, etc).

HubSpotFeignClient (caso use Feign para chamadas externas).

1. Endpoints que serão entregues

Recurso	Método	Path	Função
Authorization URL	GET	/oauth/authorize-url	Gera URL para consentimento OAuth.
Callback OAuth	GET	/oauth/callback	Recebe código e troca por access token.
Criar Contato	POST	/contacts	Cria contato na HubSpot via API.

4. Fluxos importantes
Fluxo OAuth:
/oauth/authorize-url → redireciona usuário para consentimento → /oauth/callback troca o code pelo access_token.

Fluxo de criação de contato: /contacts → recebe dados no corpo → usa token para chamar API HubSpot → cria contato.

Fluxo de webhook: HubSpot envia evento → /webhooks/contact processa o JSON recebido → salva log, processa ou responde 200 OK.

5. Boas práticas que serão aplicadas
Tratamento de erros (@ControllerAdvice).

Validação dos dados de entrada (@Valid, DTOs de request/response).

Retry em falhas HTTP (ex.: erro 429 de rate limit).

Organização do código em pacotes limpos: controller, service, model, config, repository.

Segregação de responsabilidades clara (SRP - Princípio da Responsabilidade Única).

6. Libs sugeridas (e que você pode justificar no README)
spring-boot-starter-web

spring-boot-starter-validation

spring-boot-starter-security (opcional para proteger endpoints webhook)

spring-cloud-starter-openfeign (para integração HTTP)

spring-retry (para retry automático no rate limit)

lombok

H2 Database (para armazenamento simples dos tokens)

spring-boot-starter-data-jpa (se usar repositórios)

mapstruct (opcional para mapper entre DTOs e entidades)

📦 Organização do Projeto
src/main/java
 └── com.meetime
     ├── controller
     │    ├── OAuthController.java
     │    └── ContactController.java
     ├── service
     │    ├── OAuthService.java
     │    └── ContactService.java
     ├── model
     │    ├── OAuthToken.java
     │    └── Contact.java
     ├── config
     │    ├── HubSpotProperties.java
     │    └── FeignConfig.java
     └── exception
          └── GlobalExceptionHandler.java
📋 Próximos passos
[] Configurar projeto Spring Boot (spring-boot-starter-web).

[] Criar application.yml com configs de OAuth (clientId, clientSecret, redirectUri).

[] Implementar OAuthController e OAuthService.

[] Implementar ContactController e ContactService.

[] Implementar WebhookController.

[] Testar manualmente todos os fluxos com ferramentas como Postman ou Insomnia.

✅ Criar README.md com:

Setup do projeto

Explicação dos endpoints

Instruções de execução

Decisões de arquitetura

[] Subir para o GitHub e enviar o link.
