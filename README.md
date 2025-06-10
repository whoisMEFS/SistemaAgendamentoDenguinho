# 🐾 Sistema de Agendamento para Petshop: **Denguinho para Cachorro**

Projeto acadêmico desenvolvido para a disciplina de **Projeto em Desenvolvimento de Sistemas** da **Universidade Nove de Julho (UNINOVE)**.  
O sistema visa facilitar o gerenciamento de agendamentos em petshops, promovendo **organização, agilidade** e uma **melhor experiência** para clientes e colaboradores.

---

- **Professor Orientador:** Leandro Fernandes Da Mota  
- **Empresa:** Denguinho para Cachorro

---

## 🎯 Objetivos

### Objetivo Geral
Desenvolver um sistema desktop em **Java**, com interface gráfica amigável, para agendamento e controle de serviços em petshops, destinado a uma microempresa real.

### Objetivos Específicos
- Interface para cadastro de **clientes**, **pets** e **serviços**
- Agendamento com verificação de **disponibilidade**
- **Banco de dados local** seguro
- **Relatórios** de agendamentos
- **Facilidade de uso** para todos os perfis de usuários

---

## 🧩 Modelagem de Processos

Utilizamos **BPMN 2.0**, modelado em [bpmn.io](https://bpmn.io), para representar o fluxo _"Realizar agendamento de um serviço"_:

1. Verificar se o cliente está cadastrado  
2. Cadastrar cliente, se necessário  
3. Verificar/cadastrar o pet  
4. Selecionar o serviço desejado  
5. Verificar horários disponíveis  
6. Cliente escolhe o horário  
7. Confirmar e salvar o agendamento  
8. Enviar confirmação ao cliente  

---

## 🚀 Desenvolvimento Ágil com Scrum

O projeto foi desenvolvido com base na metodologia **Scrum**, com papéis definidos e **sprints semanais**.

- **Duração:** 4 sprints (1 semana cada)
- **Product Backlog (Resumo):**

| ID   | Funcionalidade                      | Prioridade |
|------|-------------------------------------|------------|
| PB01 | Cadastro de cliente                 | Alta       |
| PB02 | Cadastro de pet                     | Alta       |
| PB03 | Cadastro de serviços                | Média      |
| PB04 | Agendamento de serviços             | Alta       |
| PB05 | Consulta de agendamentos            | Alta       |
| PB06 | Edição e exclusão de agendamentos   | Média      |
| PB07 | Relatórios                          | Baixa      |
| PB08 | Interface gráfica amigável          | Média      |
| PB09 | Banco de dados integrado            | Alta       |
| PB10 | Tela de login                       | Baixa      |

---

## 🛠️ Tecnologias Utilizadas

- **Java** – Linguagem principal  
- **NetBeans** – IDE utilizada  
- **Swing / AWT** – Criação da interface gráfica  
- **SQLite** – Banco de dados local  
- **BPMN 2.0** – Modelagem de processos  
- **SCRUM** – Metodologia ágil

---

## 🧱 Modelagem Orientada a Objetos

### Principais Classes
- `Cliente`
- `Pet`
- `Serviço`
- `Agendamento`
- `Usuário`

### Casos de Uso
- Cadastrar cliente/pet/serviço  
- Realizar e consultar agendamentos  
- Editar/cancelar agendamentos  
- Gerar relatórios

---

## 💻 Telas do Sistema

- **Login:** segurança e controle de acesso  
- **Menu Principal:** acesso às funcionalidades  
- **Cadastro de Cliente/Pet/Serviço:** entrada de dados  
- **Agendamento:** seleção de cliente, pet, serviço e horário  
- **Consulta:** listagem com filtros  
- **Relatórios:** exportação simples de dados (.xls)

---

## 📚 Bibliotecas Utilizadas

| Biblioteca             | Função                                 |
|------------------------|----------------------------------------|
| `jcalendar-1.4.jar`    | Seleção de datas no agendamento        |
| `jxl.jar`              | Leitura/escrita de arquivos Excel (.xls) |
| `sqlite-jdbc-3.49.1.0.jar` | Integração com banco SQLite         |

---

## 📈 Conclusão

Este projeto proporcionou uma **experiência prática completa** de desenvolvimento de software, desde a modelagem até a implementação final. Destacamos:

- Aplicação de **POO** com boas práticas  
- **Modelagem clara** com BPMN e UML  
- Uso eficaz do **Scrum** para organização do projeto  
- **Interface simples e funcional** para o usuário final  

---

## 📄 Referências

- LAKATOS, Eva Maria; MARCONI, Marina de Andrade. *Metodologia do trabalho científico*. 5. ed. São Paulo: Atlas, 2001.  
- **ABNT NBR 6023**: Informação e documentação: referências: elaboração, 2002.  
- **Manual ABNT UNINOVE:** [Manual UNINOVE](http://docs.uninove.br/arte/pdfs/Manual-Elaboracao-de-Trabahos-ABNT.pdf)  
- **jCalendar:** [https://toedter.com/jcalendar/](https://toedter.com/jcalendar/)  
- **Java Excel API (JXL):** [https://sourceforge.net/projects/jexcelapi/](https://sourceforge.net/projects/jexcelapi/)  
- **SQLite JDBC:** [https://github.com/xerial/sqlite-jdbc](https://github.com/xerial/sqlite-jdbc)

---

> Projeto desenvolvido para fins acadêmicos – UNINOVE 2025.
