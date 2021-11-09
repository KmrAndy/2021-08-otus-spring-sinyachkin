# 2021-08-otus-spring-sinyachkin

Домашнее задание №3
Перенести приложение для тестирования студентов на Spring Boot

Цель:
Цель: использовать возможности Spring Boot, чтобы разрабатывать современные приложения, так, как их сейчас и разрабатывают.
Результат: Production-ready приложение на Spring Boot

Это домашнее задание выполняется на основе предыдущего.

Создать проект, используя Spring Boot Initializr (https://start.spring.io)
Перенести приложение проведения опросов из прошлого домашнего задания.
Перенести все свойства в application.yml
Локализовать выводимые сообщения и вопросы (в CSV-файле). MesageSource должен быть из автоконфигурации Spring Boot.
Сделать собственный баннер для приложения.
Перенести тесты и использовать spring-boot-test-starter для тестирования
*Опционально:

использовать ANSI-цвета для баннера.
если Ваш язык отличается от русского и английского - локализовать в нём.
Коммитить wrapper или нет в репозиторий - решать Вам.

Задание сдаётся в виде ссылки на pull-request в чат с преподавателем.
Вопросы можно задавать в чате, но для оперативности рекомендуем Slack.

Написанное приложение будет использоваться в ДЗ №4 (к занятию №5).
Данное задание засчитывает ДЗ №1 (к занятию №1) и ДЗ №2 (к занятию №2).
Если Вы хотите засчитать, то обязательно пришлите ссылку в чат соответствующего предыдущего занятия.

Критерии оценки:
Факт сдачи:

0 - задание не сдано
1 - задание сдано Степень выполнения (количество работающего функционала, что примет заказчик, что будет проверять тестировщик):
0 - ничего не работает или отсутствует основной функционал
1 - не работает или отсутствует большая часть критического функционала
2 - основной функционал есть, возможны небольшие косяки
3 - основной функционал есть, всё хорошо работает
4 - основной функционал есть, всё хорошо работает, тесты и/или задание перевыполнено Способ выполнения (качество выполнения, стиль кода, как ревью перед мержем):
0 - нужно править, мержить нельзя (нарушение соглашений, публичные поля)
1 - лучше исправить в рамках этого ДЗ для повышения оценки
2 - можно мержить, но в следующих ДЗ нужно поправить.
3 - можно мержить, мелкие недочёты
4 - отличная работа!
5 - экстра балл за особо красивый кусочек кода/решение целиком (ставится только после отличной работы, отдельно не ставится)
Статус "Принято" ставится от 6 и выше баллов.
Ниже 6, задание не принимается.

Идеальное, но не работающее, решение = 1 + 0 + 4 (+4 а не 5) = 5 - не принимается.
Если всё работает, но стилю не соответствует (публичные поля, классы капсом) = 1 + 4 + 0 = 5 - не принимается