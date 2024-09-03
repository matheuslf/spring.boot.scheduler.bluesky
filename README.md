# Bluesky Post Scheduler

Este projeto é um agendador de postagens para o [Bluesky](https://bsky.app), desenvolvido em Spring Boot. Ele permite que os usuários agendem postagens para serem publicadas em horários específicos utilizando a API do Bluesky.

## Funcionalidades

- **Agendamento de Postagens**: Agende postagens para serem publicadas no futuro em horários específicos.
- **Publicação Automática**: Um processo em segundo plano verifica periodicamente as postagens agendadas e as publica automaticamente.
- **Histórico de Postagens**: Todas as postagens agendadas e publicadas são armazenadas em um banco de dados.

## Requisitos

- Java 17 ou superior
- Spring Boot 2.7.x ou superior
- Banco de Dados H2 (ou outro de sua escolha)
- Conta no [Bluesky](https://bsky.app) com credenciais válidas

## Configuração

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/bluesky-post-scheduler.git
cd bluesky-post-scheduler

### 2. Usuário e Senha da API

- Deixei duas váriaveis que precisam ser modificadas dentro da classe BlueskyApiService. A ideia era buscar de dentro do application.properties, mas não tive tempo.
