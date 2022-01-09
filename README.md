[![Java CI with Maven](https://github.com/Quertte/job4j_grabber/actions/workflows/maven.yml/badge.svg)](https://github.com/Quertte/job4j_grabber/actions/workflows/maven.yml)

# Агрегатор Java вакансий.

### Техническое задание

Приложение парсит сайты с вакансиями. Первый сайт будет sql.ru. В разделе job
приложение должно считывать все вакансии относящиеся к Java и записывать их в базу.

- В проекте используется Maven, Jacoco, checkstyle.
- Приложение собирается в .jar
- Система запускается по расписанию. Период запуска указан в настройках - app.properties

####Расширение.

- В проект можно добавить новый сайты без изменения кода.
- В проекте можно сделать параллельный парсинг сайтов.
