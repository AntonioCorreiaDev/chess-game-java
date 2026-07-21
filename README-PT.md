# chess-game-java

Aplicação de xadrez desenvolvida em Java no âmbito da unidade curricular de Programação Avançada / Programação Orientada a Objetos, com interface gráfica em JavaFX e arquitetura baseada no padrão MVC.

## Objetivo

Este projeto foi criado para desenvolver uma aplicação de xadrez funcional, modular e organizada, aplicando conceitos fundamentais de programação orientada a objetos, design patterns e desenvolvimento de interfaces gráficas.

## Funcionalidades principais

- Jogo de xadrez com lógica principal completamente implementada
- Criação de partidas com introdução dos nomes dos jogadores
- Guardar e carregar jogos através de serialização em formato `.dat`
- Registo e visualização de logs da partida em tempo real
- Suporte para undo e redo de jogadas
- Promoção de peões
- Movimentos especiais implementados, incluindo en passant
- Interface gráfica desenvolvida com JavaFX
- Testes unitários

## Arquitetura

O projeto segue o padrão **MVC (Model-View-Controller)**, permitindo separar a lógica de negócio da interface gráfica e tornar o código mais modular, organizado e de fácil manutenção.

### Design patterns utilizados

- **MVC** para separação entre modelo, interface e controlo
- **Command** para encapsular ações do jogador, incluindo movimentos e undo/redo
- **Factory** para criação dinâmica de peças de xadrez através de `PieceType`
- **Singleton** para centralizar o registo de logs através de `ModelLog`
- **Classes abstratas** para definir comportamento comum às peças

## Estrutura principal

Algumas das classes mais relevantes do projeto:

- `ChessGameManager` — controla o estado e a lógica do jogo
- `ChessGame` — representa o estado interno da partida
- `Board` — gere o tabuleiro e as interações entre peças
- `Piece` e subclasses — representam as peças e respetivos movimentos
- `CommandManager` — gere o histórico para undo/redo
- `MovePieceCommand` — implementa comandos de movimento
- `ChessGameSerialization` — trata da serialização do jogo
- `ModelLog` — gere os logs do modelo
- `RootPane`, `ChessBoardJFX`, `PlayersInfoPane` e `LogsJFX` — componentes principais da interface

## O que foi implementado

- Lógica do xadrez
- Início de jogo com nomes dos jogadores
- Guardar e carregar jogo
- Fim de jogo por checkmate e empate
- Organização do código
- Testes unitários
- Movimentos especiais
- Áudio em alguns eventos do jogo
- Learning mode com jogadas possíveis, undo e redo

## Tecnologias utilizadas

- Java
- JavaFX
- Programação orientada a objetos
- Serialização
- MVC
- Design patterns

## Contexto académico

Unidade Curricular: **Programação Avançada (2024/2025)**.
