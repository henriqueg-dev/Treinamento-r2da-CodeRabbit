# Configuração CodeRabbit - Padrões R2DA Tecnologia
language: "pt-BR"

reviews:
  # 1. chill:     Foca apenas em bugs críticos e erros fatais. Mais silencioso.
  # 2. standard:  Equilíbrio entre bugs e boas práticas (Padrão).
  # 3. assertive: Perfil rigoroso. Questiona design, sugere refatorações e garante o cumprimento estrito das 'instructions' abaixo.
  profile: "assertive" # Define o rigor e o volume de comentários da IA
  
  # Foca as revisões nas branches conforme padrão da empresa
  branches:
    - "master"
    - "develop"
    - "feature/*"
    - "feat/*"
    - "fix/*"
    - "hotfix/"
    
  path_filters:
    # Filtros de arquivos e pastas para ignorar (conforme sua lista)
    - '!**/build/**'
    - '!**/.gradle/**'
    - '!**/build.gradle**'
    - '!**/out/**'
    - '!**/bin/**'
    - '!**/dist/**'
    - '!**/.idea/**'
    - '!**/*.iml'
    - '!**/.vscode/**'
    - "!**/gradlew**"
    - "!**/gradlew.bat**"
    - "!**/gradle/**"

  instructions: |
    Atue como um Tech Lead da R2DA. Suas revisões devem garantir:
    
    1. **Estrutura de Métodos:**
       - Métodos curtos: máximo 15 linhas.
       - Responsabilidade Única (SRP) em cada método.
       - Nomes de classes, métodos e atributos devem ser descritivos e em PORTUGUÊS.
    
    2. **Persistência e Banco de Dados:**
       - Padrão de carregamento OBRIGATÓRIO: `@ManyToOne(fetch = FetchType.LAZY)`.
       - Uso de `CascadeType.ALL` como padrão (verifique segurança em operações de DELETE).
       - Operações de escrita (INSERT, UPDATE, DELETE) devem estar em transação única.
    
    3. **Backend Java & Spring:**
       - Proiba o uso de `java.util.Date`; exija sempre `java.time` (LocalDate/LocalDateTime).
       - Verifique se as Regras de Negócio estão isoladas na camada Service/BO.
       - Garanta o uso de VO's ou Responses para retorno ao Frontend (não exponha Entidades).
       - Logger: Uso de Log4j2 (`LogManager.getLogger`). Verifique se erros incluem a exceção: `logger.error("mensagem", e)`.
    
    4. **Frontend & Qualidade:**
       - Projetos Vue.js devem seguir o ESLint.
       - Não permita código duplicado ou comitado com erros de compilação aparentes.
       - Exija tratamento de exceções (de preferência automático) e comentários em códigos complexos (ex: Regex).
