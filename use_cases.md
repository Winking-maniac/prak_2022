# Сценарии использования
Сценарии подразделяются на группы в зависимости от прав пользователя на те или иные действия.
## Список сценариев
### Гость
- Поиск учеников, учителей по их ФИО, компаний и курсов по названию
- Создание аккаунта
- Вход в аккаунт
### Зарегистрированный пользователь(ученик,учитель или компания)
- Выход из аккаунта
- Удаление аккаунта
- Изменение личной информации
Далее следуют специфичные для конкретных групп пользователей сценарии работы
#### Ученик
- Запись на курс
- Отписывание от курса
- Просмотр курсов, на которые записан
#### Учитель
- Прием/отклонение приглашений от компаний
- Выход из компании
- Создание уроков(доступно для администраторов курса)
- Выставление оценок ученикам за уроки, проведенные этим учителем
#### Компания
- Приглашение учителей в компанию
- Удаление преподавателей из компании
- Создание курса
- Назначение учителей на курс
- Назначение администраторов курса
- Удаление учителей с курса
## Перечень страниц
Каждая страница имеет колонтитул, содержащий либо приглашение к авторизации/созданию аккаунта(страницы 2,3), либо ссылку на личный кабинет(страница 4) и выход из аккаунта.
Также, при нажатии на логотип, происходит переход на страницу 
1) **Стартовая страница**
Поисковая строка, кнопка "найти", по нажатию переносящая на страницу 5.
Кнопки для перехода на страницы 11, 12 и 13.
2) **Страница входа**
Имеет поля логин/пароль и кнопку отправить для авторизации. Перемещает на страницу 1.
4) **Страница создания аккаунта**
Позволяет выбрать тип аккаунта: учитель, ученик или компания. При выборе появляются поля, необходимые для создания соответствующего аккаунта.
6) **Страница личного кабинета**
Отражает личную информацию об аккаунте. Имеет поля для ее изменения и кнопку "Сохранить". Кнопка "Удалить аккаунт".
В зависимости от типа аккаунта, показывает информацию о прослушиваемых курсах(ученик), местах работы(учитель), преподаваемых курсах(учитель), курсах компании и работниках компании в виде таблицы гиперссылок. Каждой строчке соответствует кнопка отписки от курса/увольнения сотрудника по желанию сотрудника или компании.
Компании и учителя имеют кнопку для перевода на страницу 14. Компании имеют кнопку для перевода на страницу создания курса(страницу 16).
7) **Страница результатов поиска**
Выводит результат поискового запроса в виде таблицы гиперссылок на публичные профили. Имеется поисковая строка для повторного запроса.
9) **Публичный профиль учителя**
Соответствует содержимому личного кабинета без какой-либо возможности редактирования
11) **Публичный профиль ученика**
Соответствует содержимому личного кабинета без какой-либо возможности редактирования
13) **Публичный профиль компании**
Соответствует содержимому личного кабинета без какой-либо возможности редактирования
14) **Публичный профиль курса**
Предоставляет информацию о курсе и список его уроков. Для учеников присутствует кнопка записаться/отписаться, для учителей, назначенных администраторами курса, и компаний присутствует кнопка для перехода на страницу управления курсом(страница 15)
15) **Страница урока**
Предоставляет информацию об уроке, список учеников и полученных за урок баллов. Учителю, проводившему урок, доступны поля и кнопки для выставления ученикам баллов.
17) **Список курсов**
Список гиперссылок на курсы
19) **Список учителей**
Список гиперссылок на профили учителей
20) **Список компаний**
Список гиперссылок на профили компаний
14) **Страница приглашений**
Список отправленных/полученный приглашений с кнопками соответтвенно для отзыва и приема/отклонения приглашений.
15) **Страница управления курсом**
Позволяет компаниям назначать учителей(из списка своих учителей) и администраторов(из числа назначенных учителей), а администраторам создавать уроки путем заполнения полей формы.
16) **Страница создания курса**
Содержит поля для передачи информации, неообходимой для создания курса. После перемещает на страницу управления курсом.
## Описание сценариев
Предполагается старт со стартовой страницы, будучи авторизованным соответcтвующим аккаунтом для соответствующего сценария работы
1) **Поиск учеников, учителей по их ФИО, компаний и курсов по названию**
Ввод искомого имени/названия в поиссковую строку, нажатие кнопки "Найти", далее выбор необходимого профиля из списка на странице 5.
ИЛИ
аналогичный поиск с предварительным переходом к списку курсов, учителей или компаний
2) **Создание аккаунта**
Переход по ссылке из колонтитула, заполнение формы.
3) **Вход в аккаунт**
Переход по ссылке из колонтитула, заполнение формы входа.
4) **Выход из аккаунта**
Переход по ссылке из колонтитула
5) **Удаление аккаунта**
Переход по ссылке из колонтитула в личный кабинет, нажатие на кнопку "Удалить аккаунт", подтверждение.
6) **Изменение личной информации**
Переход по ссылке из колонтитула в личный кабинет, ввод новой информации, нажатие на кнопку "Сохранить", подтверждение.
7) **Запись на курс**
Переход на публичный профиль интересующего курса, нажатие на кнопку "Записаться/Отписаться"
8) **Просмотр прослушиваемых курсов**
Переход по ссылке из колонтитула в личный кабинет, просмотр необходимой информации.
9) **Прием/отклонение приглашений от компаний**
Переход по ссылке из колонтитула в личный кабинет, нажатие на кнопку "Приглашения", просмотр необходимой информации, нажатие на соответствующие кнопки.
10) **Выход из компании**
Переход по ссылке из колонтитула в личный кабинет, нажатие на кнопку "Выйти" в строчке соответсвующей компании в списке компаний, подтверждение.
11) **Создание урока**
Переход на публичный профиль интересующего курса, нажатие на кнопку "Управление"(переход на страницу 15), нажатие на кнопку "Добавить урок", заполнение формы, нажатие на кнопку "Сохранить".
12) **Выставление оценок студентам**
Переход на публичный профиль интересующего курса, переход на публичный профиль интересующего урока, заполнение поля в строчке с нужным студентом, нажатие на кнопку "Сохранить".
13) **Приглашение учителей в компанию**
Переход на публичный профиль интересующего учителя, нажатие на кнопку "Пригласить".
14) **Удаление преподавателей из компании**
Переход по ссылке из колонтитула в личный кабинет, нажатие на кнопку "Удалить" в строчке соответсвующего учителя в списке учителей, подтверждение.
15) **Создание курса**
Переход по ссылке из колонтитула в личный кабинет, нажатие на кнопку "Создать курс", заполнение формы на отдельной странице, подтверждение.
16) **Назначение учителей/администраторов на курс**
Переход на публичный профиль курса, переход в управление курсом, нажатие на кнопку "Добавить учителя", выбор из списка учителей компании. Для выдачи прав администратора учителю необходимо отметить галочку в строке с этим учителем.
17) **Удаление учителей с курса**
Переход на публичный профиль курса, переход в управление курсом, нажатие на кнопку "Удалить" в строке с нужным учителем.