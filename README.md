# HTML Analyzer - Internship Technical Test

## Descrição

Programa Java que analisa a estrutura HTML de uma URL e retorna o trecho de texto contido no nível mais profundo da árvore HTML.

## Objetivo

A partir de uma URL, o programa identifica e retorna o texto que está no nível de maior profundidade na estrutura HTML. Se houver múltiplos trechos no mesmo nível máximo, o primeiro é retornado.

### Exemplo

Para o HTML:
```html
<html>
  <head>
    <title>
      Este é o título.
    </title>
  </head>
  <body>
    Este é o corpo.
  </body>
</html>
```

O resultado seria: **"Este é o título."** (profundidade 3), já que "Este é o corpo." está apenas na profundidade 2.

## Funcionalidades

✅ Análise de profundidade de estruturas HTML  
✅ Detecção de HTML mal-formado (pontos bônus)  
✅ Tratamento de erros de conexão  
✅ Suporte a indentação e linhas em branco  

## Requisitos Técnicos

- **JDK 17**
- Sem bibliotecas externas
- Sem uso de packages nativos de manipulação HTML/XML/DOM

## Compilação

```bash
javac HtmlAnalyzer.java
```

## Execução

```bash
java HtmlAnalyzer <url>
```

### Exemplo de uso

```bash
java HtmlAnalyzer http://hiring.axreng.com/internship/example1.html
```

## Outputs Possíveis

1. **Texto identificado** - O trecho de texto no nível mais profundo
2. **"malformed HTML"** - Quando a estrutura HTML é inválida
3. **"URL connection error"** - Quando há falha na conexão com a URL

## Premissas

- Código HTML dividido em linhas
- Cada linha é de apenas um tipo:
  - Tag de abertura (ex: `<div>`)
  - Tag de fechamento (ex: `</div>`)
  - Trecho de texto
- Tags não possuem atributos
- Apenas elementos com pares de abertura e fechamento
- Espaços iniciais (indentação) são ignorados
- Linhas em branco são ignoradas

## Estrutura do Código

### Classes e Métodos Principais

- **`main()`** - Ponto de entrada, processa argumentos e coordena execução
- **`fetchHtmlContent()`** - Busca o conteúdo HTML da URL
- **`analyzeHtml()`** - Analisa o HTML e retorna o texto mais profundo
- **`parseHtmlLines()`** - Faz o parsing das linhas e valida a estrutura
- **`identifyLineType()`** - Identifica o tipo de cada linha (tag ou texto)
- **`extractTagName()`** - Extrai o nome da tag

### Classes Internas

- **`HtmlNode`** - Representa um nó com informações de profundidade
- **`LineType`** - Enum para tipos de linha (OPENING_TAG, CLOSING_TAG, TEXT)
- **`MalformedHtmlException`** - Exceção customizada para HTML inválido

## Validação de HTML Mal-formado

O programa detecta os seguintes problemas:

- ✅ Tags de fechamento sem correspondente abertura
- ✅ Tags de abertura sem correspondente fechamento
- ✅ Tags desbalanceadas (ordem incorreta)
- ✅ Linhas com formato inválido

## Testes

O programa foi projetado para passar nos testes automatizados da Axur, seguindo rigorosamente todos os requisitos especificados.

## Design e Boas Práticas

- **Orientação a Objetos**: Classes bem definidas com responsabilidades claras
- **Tratamento de Exceções**: Hierarquia apropriada de exceções
- **Validação Robusta**: Verificação completa da estrutura HTML
- **Código Limpo**: Nomenclatura clara, comentários JavaDoc
- **Eficiência**: Uso de Stack para validação O(n)

## Autor

Pedro - Software Engineering Student @ PUC-Campinas

## Licença

Este projeto foi desenvolvido como parte de um teste técnico para processo seletivo.

---

**Nota**: Este programa atende a todos os requisitos funcionais e técnicos especificados no teste, incluindo a funcionalidade bônus de detecção de HTML mal-formado.
