# Задача

Нужно написать генератор оглавления для markdown файлов.

Это должна быть command-line тула на Java, которая принимает на вход путь к markdown файлу, добавляет к нему в начало оглавление и выводит результат в standard output.

Нельзя пользоваться библиотеками для генерации и парсинга markdown.

### Input:
```md
# My Project
## Idea
content
## Implementation
### Step 1
content
### Step 2
content
```
### Output:
```md
1. [My Project](#my-project)
    1. [Idea](#idea)
    2. [Implementation](#implementation)
        1. [Step 1](#step-1)
        2. [Step 2](#step-2)

# My Project
## Idea
content
## Implementation
### Step 1
content
### Step 2
content
```

# How to run?
Запуск тестов:
```shell
./gradlew clean test
```

Запуск консольного приложения:
```shell
chmod +x run.sh
./run input_file [--file [output_file]]
```

Последние два аргумента являются опциональными.

- Если запустить приложение в формате:
```shell
java Main input_file
```
то результат будет выведен на стандартный вывод.

- Если запустить приложение в формате:
```shell
java Main input_file --file
```
то результат запишется в тот же файл.

- Если запустить приложение в формате:
```shell
java Main input_file --file output_file
```
то результат запишется в указанный файл.

Например, можно запустить приложение на файле `sample.md` из папки `tests`:
```shell
chmod +x run.sh

./run tests/sample.md # выведет результат в stdout

./run tests/sample.md --file # запишет результат в sample.md

./run tests/sample.md --file tests/sample_result.md # запишет результат в sample_result.md
```

# Преимущества
1. Рассмотрено и проверено на GitHub большое число различных кейсов (повторения в названиях заголовков, обработка специальных символов, учет количества пробелов в начале строки и многое другое)
2. Добавлена поддержка альтернативной записи заголовков первого и второго уровней (=== и ---)
3. Покрытие тестами базовых и специфичных сценариев
4. Заданный файл не хранится целиком в памяти, а обрабатывается построчно

Можно перейти в [src/test/java/actual](src/test/java/actual) и покликать на ссылки/результат работы приложения и убедиться, что все они действительно работают корректно.
