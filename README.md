# Challenge FIAP × TOTVS — Inteligência Conversacional

Projeto acadêmico desenvolvido no **Challenge FIAP × TOTVS**, com o objetivo de transformar transcrições de reuniões comerciais em informações úteis para análise e tomada de decisão.

A solução permite registrar reuniões e transcrições, processar os textos e apresentar **resumos, indicadores de sentimento, palavras-chave e alertas** sobre possíveis riscos e oportunidades comerciais.

> **Nota técnica:** a análise textual implementada utiliza processamento de palavras e regras definidas no código (abordagem heurística). Não se trata de integração com um modelo de IA generativa.

## Problema e proposta

Reuniões de vendas e relacionamento com clientes geram transcrições extensas. A leitura manual pode dificultar a identificação de informações importantes, como insatisfação, menções a concorrentes e oportunidades de expansão.

O projeto propõe organizar essas informações e facilitar sua consulta por meio de uma aplicação web com backend Java.

## Funcionalidades

- Cadastro, consulta, atualização e exclusão de reuniões por API REST.
- Registro e associação de transcrições às reuniões.
- Análise automatizada do conteúdo textual.
- Classificação de sentimento em **positivo, negativo, misto ou neutro**, com base em termos identificados.
- Extração de palavras-chave e geração de resumos.
- Identificação de menções a concorrentes e sinais de risco de perda do cliente (*churn*).
- Sinalização de oportunidades comerciais e indícios de *upsell*.
- Armazenamento de reuniões, transcrições, resultados e alertas em banco de dados.
- Interface web em React para interação com os dados da aplicação.

## Tecnologias

| Camada | Tecnologias |
| --- | --- |
| Backend | Java 21, Spring Boot, Spring Web MVC |
| Persistência | Spring Data JPA, Oracle Database, JDBC |
| Validações | Jakarta Bean Validation |
| Frontend | React, JavaScript, Vite, CSS |
| Gerenciamento | Maven e Git/GitHub |

## Organização do projeto

```text
challengeTOTVS/
├── src/main/java/br/com/fiap/
│   ├── controller/       # Endpoints HTTP
│   ├── service/          # Regras de negócio e análise textual
│   ├── model/
│   │   ├── dto/          # Objetos de transferência
│   │   ├── entity/       # Entidades
│   │   └── repository/   # Persistência JPA
│   ├── dao/              # Implementações de acesso a dados
│   └── main/             # Classes auxiliares de execução
├── src/main/resources/   # Configuração da aplicação
├── frontend/
│   └── src/              # Interface React e consumo da API
└── pom.xml               # Dependências Maven
```

## Fluxo simplificado

1. Uma reunião é cadastrada e recebe uma transcrição.
2. O serviço de análise processa o texto, filtra termos comuns e identifica palavras relevantes.
3. Regras de classificação geram sentimento, resumo e possíveis alertas comerciais.
4. Os resultados são persistidos e podem ser consultados pelos recursos da aplicação.

## Executando localmente

**Pré-requisitos:** JDK 21, acesso a uma instância Oracle compatível, Node.js e npm para o frontend.

1. Clone o repositório:
   ```bash
   git clone https://github.com/hvicente21/challengeTOTVS.git
   cd challengeTOTVS
   ```
2. Configure a conexão Oracle em `src/main/resources/application.properties` para seu ambiente, preferencialmente por variáveis de ambiente e **sem publicar credenciais**. O arquivo atualmente versionado contém uma configuração acadêmica antiga e pode exigir ajustes antes da execução.
3. Inicie o backend com o Maven Wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
   No Windows: `mvnw.cmd spring-boot:run`.
4. Em outro terminal, inicie o frontend:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
5. Confira a configuração da URL da API em `frontend/src/services/api.js`.

> A execução depende de um banco de dados configurado com o esquema esperado. Não há garantia de execução imediata sem o ambiente Oracle e os dados necessários.

## Contexto acadêmico

Projeto desenvolvido para o **Challenge FIAP × TOTVS — Inteligência Conversacional**, com foco em orientação a objetos, desenvolvimento web, regras de negócio, persistência e análise de transcrições comerciais.

**Repositório:** [hvicente21/challengeTOTVS](https://github.com/hvicente21/challengeTOTVS)

---
Projeto acadêmico para fins de aprendizado e portfólio. Não é um produto oficial da TOTVS.
