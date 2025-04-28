# meetime 🚀

Case Técnico: Integração com HubSpot

---

## 🌐 Link do Projeto HubSpot
[HubSpot Developer Projects](https://app.hubspot.com/developer-projects)

---

## 📝 Introdução aos Projetos e Extensões de IU
Explore os princípios básicos de um aplicativo público criado com a ferramenta Projetos, incluindo extensões de IU personalizadas para potencializar sua integração com o HubSpot.

- Documentação de componente: [Docs HubSpot](https://developers.hubspot.com/docs/api/crm/extensions)
- Biblioteca de aplicativos de amostra: [Sample Apps](https://github.com/HubSpot/sample-apps)
- Referência CLI: [HubSpot CLI Docs](https://developers.hubspot.com/docs/cli)

---

## ⚡ Passo a passo para rodar o projeto

1. **Instale o Node.js e npm**
   - Recomendado: use um gerenciador de pacotes como o Homebrew (Mac) ou o instalador oficial do Node.js ([nodejs.org](https://nodejs.org/)).
2. **Instale o HubSpot CLI**
   ```bash
   npm install -g @hubspot/cli@latest
   ```
3. **Inicialize o projeto com o HubSpot CLI**
   ```bash
   hs init --account=XXXXXXX
   ```
4. **Autentique sua conta HubSpot**
   - Siga as instruções no terminal para criar uma chave de acesso pessoal.
   - Você será redirecionado ao HubSpot no navegador. Gere sua chave, copie e cole no terminal.
   - Dê um nome para a conta conectada (para referência futura).

5. **Arquivo de configuração**
   - O processo criará automaticamente o arquivo `hubspot.config.yml` e definirá a conta como padrão.

---

## 💡 Dicas úteis de CLI

- Conectar outra conta:
  ```bash
  hs auth
  ```
- Alternar entre contas:
  ```bash
  hs accounts
  ```
- Selecionar outra conta como padrão:
  ```bash
  hs accounts use <nome_da_conta>
  ```

---

## 🛠️ Integração Backend

Para integrar o backend com o HubSpot:
1. Siga o passo a passo acima para autenticação e configuração do CLI.
2. Utilize as APIs do HubSpot para criar, ler, atualizar ou deletar dados da sua conta diretamente do backend.
3. Consulte a [documentação oficial de APIs](https://developers.hubspot.com/docs/api/overview) para exemplos e endpoints disponíveis.

---

## 📈 Progresso

- 5% concluído

---

## 🤝 Contribuições
Sinta-se à vontade para sugerir melhorias ou abrir issues!

---

## 📞 Suporte
Dúvidas? Entre em contato com o responsável pelo projeto.

---

> README otimizado com ❤️ para facilitar sua integração com HubSpot!
