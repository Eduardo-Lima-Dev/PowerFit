# PowerFit - Sistema para Academia

**PowerFit** é um aplicativo móvel desenvolvido em **Kotlin** com **Kotlin Compose** para academias, com foco na gestão de treinos e acompanhamento de resultados dos alunos. O objetivo do PowerFit é proporcionar uma experiência completa e personalizada para os usuários, com funcionalidades de cadastro, planos de treino e monitoramento de desempenho físico.

## Funcionalidades

- **Cadastro e login de usuários com autenticação**.
- **Criação e personalização de planos de treino**.
- **Registro e controle de exercícios** com séries, repetições e cargas.
- **Acompanhamento de estatísticas** para monitorar o progresso.
- **Recuperação de senha**.

## Estrutura do Projeto - Padrão **MVC (Model-View-Controller)**

O PowerFit adota o padrão **MVC (Model-View-Controller)** para a organização do código. Esse padrão divide claramente as responsabilidades da aplicação, tornando o código mais modular, fácil de testar e com baixo acoplamento entre as camadas.

### O que é o MVC?

- **Model**: Representa a lógica de negócios e os dados do aplicativo. No caso do PowerFit, o Model manipula dados como cadastro de usuários, planos de treino, estatísticas de desempenho, etc.
  
- **View**: A interface gráfica do usuário. No PowerFit, a View será implementada usando **Kotlin Compose**, criando a interface do usuário com as telas do aplicativo.

- **Controller**: O controlador é responsável por intermediar a comunicação entre o Model e a View. Ele recebe as entradas da View e realiza as modificações necessárias no Model, além de atualizar a View conforme necessário.

### Exemplo de Interação MVC no PowerFit

- **Model**: A classe `UserModel` gerencia dados como o nome do usuário, email, histórico de treinos, etc.
  
- **View**: A `LoginScreen` exibirá a interface de login com campos para o email e senha do usuário. Ela se comunica com o Controller quando o usuário interage com os botões ou campos.

- **Controller**: O `LoginController` recebe os dados da View, interage com o `UserModel` para validar as credenciais e retorna o resultado (sucesso ou erro) de volta para a View.

## Estrutura do Código

A estrutura do código no PowerFit será organizada da seguinte forma:

### 1. **Model**

Responsável pela lógica de dados e regras de negócios. Exemplo de modelo:

```kotlin
data class User(val email: String, val password: String)

class UserModel {
    fun login(user: User): Boolean {
        // Lógica de autenticação
    }
}
```

### 2. **View**

A interface do usuário será construída com Kotlin Compose, exibindo as telas e interagindo com os controles de interface.

Exemplo de View (Tela de Login):

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    Column {
        TextField(value = email, onValueChange = { email = it })
        TextField(value = password, onValueChange = { password = it })
        Button(onClick = { viewModel.login(email, password) }) {
            Text("Login")
        }
    }
}
```

### 3. **Controller**

O Controller é responsável por coordenar as interações entre a View e o Model.

Exemplo de Controller (LoginController):

```kotlin
class LoginController(private val userModel: UserModel) {
    fun login(email: String, password: String): Boolean {
        val user = User(email, password)
        return userModel.login(user)
    }
}
```

## Instalação

### Pré-requisitos

- **Android Studio**: IDE oficial para desenvolvimento Android.
- **Dispositivo Android**: O aplicativo será testado diretamente em um dispositivo Android físico.

### Passos para Instalar

1. Clone o repositório:
   ```bash
   git clone https://github.com/Eduardo-Lima-Dev/PowerFit
   ```

2. Acesse a pasta do projeto:
   ```bash
   cd PowerFit
   ```

3. Abra o projeto no **Android Studio**.

4. Conecte seu dispositivo Android via USB.

5. Habilite a **Depuração USB** nas configurações do seu dispositivo Android:
   - Acesse **Configurações** → **Sobre o telefone** → toque 7 vezes em **Número da versão** para habilitar as **Opções de desenvolvedor**.
   - Em **Opções de desenvolvedor**, ative **Depuração USB**.

6. No Android Studio, clique em **Run** (ícone de play) para rodar o aplicativo no seu dispositivo Android.

## Padrão de Commits

Adotaremos os seguintes tipos de commits:

- **feat**: Nova funcionalidade ou recurso.
- **refac**: Refatoração de código.
- **fix**: Correção de erros.
- **docs**: Atualização ou adição de documentação.
- **style**: Ajustes de estilo (formatação de código, espaçamento).

### Exemplos de Commits

- **feat**: 
  ```bash
  git commit -m "feat: implementar funcionalidade de login de usuário"
  ```

- **refac**:
  ```bash
  git commit -m "refac: refatorar estrutura de controle de login"
  ```

- **fix**:
  ```bash
  git commit -m "fix: corrigir erro de login com e-mail inválido"
  ```

- **docs**:
  ```bash
  git commit -m "docs: atualizar README com novos requisitos"
  ```

- **style**:
  ```bash
  git commit -m "style: corrigir formatação de código no modelo de usuário"
  ```

## Estrutura de Pastas

Aqui está a estrutura sugerida para o projeto **PowerFit**:

```
PowerFit/
│
├── app/                            # Código do aplicativo
│   ├── ui/                         # Interface do usuário (Views)
│   │   ├── LoginScreen.kt          # Tela de Login
│   │   ├── HomeScreen.kt           # Tela Inicial
│   │   └── DashboardScreen.kt      # Tela de Acompanhamento
│   │
│   ├── model/                      # Modelos de dados
│   │   ├── UserModel.kt            # Modelo de Usuário
│   │   └── ExerciseModel.kt        # Modelo de Exercício
│   │
│   └── controller/                 # Controladores
│       ├── LoginController.kt      # Controlador de Login
│       └── DashboardController.kt  # Controlador de Dashboard
│
├── data/                           # Acesso aos dados (Repositórios, APIs)
│   ├── UserRepository.kt           # Repositório de Usuário
│   └── ExerciseRepository.kt       # Repositório de Exercício
│
└── utils/                          # Funções utilitárias
    └── Validators.kt               # Funções de validação
```

---
