# ProPython — Android-приложение для изучения Python

## О проекте
**ProPython** — офлайн Android‑приложение для изучения языка программирования Python через курсы и уроки. Приложение поддерживает гостевой режим, профиль пользователя, избранные курсы, статистику прогресса, экспорт статистики в CSV, а также админ‑панель для управления контентом (курсами и уроками) и пользователями.

## Основные возможности

### Для гостя
- Просмотр списка курсов.
- Доступ к вкладкам **Профиль** и **Статистика** (в виде информационных экранов).
- При попытке открыть курс/уроки отображается сообщение о необходимости регистрации.

### Для пользователя
- Регистрация и вход.
- Каталог курсов:
  - поиск по курсам;
  - фильтр по уровню сложности (Все / Начальный / Средний / Продвинутый);
  - сортировка по алфавиту.
- Курс → список уроков → страница урока:
  - отметка урока как завершённого;
  - отображение прогресса по курсу.
- Тест по курсу (после прохождения всех уроков).
- Избранное:
  - добавление/удаление курсов в избранное;
  - просмотр списка избранных курсов.
- Профиль:
  - редактирование имени/фамилии/email/логина/пароля;
  - загрузка аватара из галереи.
- Статистика:
  - сводка и визуализация прогресса;
  - экспорт статистики в CSV (и отправка файла через стандартное "Поделиться").
- Блокнот кода (Snippets):
  - создание/редактирование/удаление сниппетов;
  - поиск по сниппетам.

### Для администратора
- Вход в админ‑панель.
- Управление курсами (создание/редактирование/удаление).
- Управление уроками внутри курса (CRUD).
- Управление пользователями (просмотр/редактирование/удаление).
- Просмотр статистики пользователей и экспорт CSV.

## Технологии
- **Язык:** Kotlin  
- **UI:** Jetpack Compose + Material 3  
- **Хранилище:** SQLite (`SQLiteOpenHelper`)  
- **Архитектура UI:** Single Activity + Composable screens  
- **Экспорт:** CSV (локальный файл + Share Intent)  
- **Работа с файлами:** `FileProvider`

## Требования
- **Android Studio:** актуальная версия (рекомендуется Stable)
- **Минимальная версия Android:** **Android 7.0 (API 24)**  
- **Target SDK:** **36**
- **JDK:** 11 (используется в Gradle настройках проекта)

## Установка и запуск

### 1) Клонирование репозитория
```bash
git clone <URL_вашего_репозитория>
cd ProPython
```

### 2) Открытие в Android Studio
- `File → Open…` → выбрать папку проекта

### 3) Синхронизация Gradle
- Android Studio предложит выполнить Sync автоматически
- либо: `File → Sync Project with Gradle Files`

### 4) Запуск
- Выбрать эмулятор/устройство (Android 7.0+)
- Нажать `Run` (Shift+F10)

## Доступы по умолчанию

### Администратор
- **Логин:** `admin`
- **Пароль:** `admin123`

> Примечание: админ‑логин/пароль в текущей версии проверяются напрямую в коде (без отдельной записи в БД).

## Структура базы данных (SQLite)

### Таблица `users`
| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| id | INTEGER | PK, AUTOINCREMENT | ID пользователя |
| first_name | TEXT | NOT NULL | Имя |
| last_name | TEXT | NOT NULL | Фамилия |
| login | TEXT | NOT NULL, UNIQUE | Логин |
| email | TEXT | NOT NULL, UNIQUE | Email |
| password | TEXT | NOT NULL | Пароль |
| avatar | TEXT | DEFAULT '' | Путь к файлу аватара |
| secret_word | TEXT | DEFAULT '' | Секретное слово |

### Таблица `courses`
| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| id | INTEGER | PK, AUTOINCREMENT | ID курса |
| title | TEXT | NOT NULL | Название |
| description | TEXT | NOT NULL | Описание |
| level | TEXT | NOT NULL | Уровень (Начальный/Средний/Продвинутый) |
| icon | TEXT | DEFAULT '' | Иконка/эмодзи |
| lessons_count | INTEGER | DEFAULT 5 | Количество уроков (кэш) |

### Таблица `lessons`
| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| id | INTEGER | PK, AUTOINCREMENT | ID урока |
| course_id | INTEGER | NOT NULL | ID курса |
| title | TEXT | NOT NULL | Название урока |
| content | TEXT | NOT NULL | Контент |
| order_num | INTEGER | NOT NULL | Порядок урока |
| duration_minutes | INTEGER | DEFAULT 10 | Длительность |

### Другие таблицы
- `user_stats` — статистика пользователя
- `user_lesson_progress` — прогресс прохождения уроков
- `favorites` — избранные курсы
- `test_questions` — вопросы для тестов
- `snippets` — блокнот кода

## Разрешения приложения (AndroidManifest)
- `android.permission.CAMERA`
- `android.permission.WRITE_EXTERNAL_STORAGE` (только до API 28)

## Известные ограничения
- Пароль хранится в базе в открытом виде
- Проект офлайн: нет серверной синхронизации
- Админ‑логин/пароль заданы в коде

## Лицензия
MIT License

2. **Откройте проект в Android Studio:**
   - File → Open → выберите папку проекта

3. **Синхронизируйте Gradle:**
   - Android Studio автоматически предложит синхронизацию
   - Или: File → Sync Project with Gradle Files

4. **Запустите приложение:**
   - Выберите эмулятор или подключённое устройство
   - Нажмите Run (▶️) или Shift+F10

### Учётные данные по умолчанию:

**Администратор:**
- Логин: `admin`
- Пароль: `admin123`

## Структура проекта

```
ProPython/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/myapplication12345678/
│   │   │   ├── MainActivity.kt
│   │   │   ├── data/
│   │   │   │   ├── LocalDatabase.kt
│   │   │   │   ├── ExcelExporter.kt
│   │   │   │   └── (models: Course, Lesson, UserStats, CodeSnippet, ...)
│   │   │   └── ui/
│   │   │       ├── auth/
│   │   │       │   ├── AuthScreens.kt
│   │   │       │   ├── SharedUi.kt
│   │   │       │   ├── StatsScreens.kt
│   │   │       │   ├── AdminScreens.kt
│   │   │       │   └── UserScreens.kt
│   │   │       └── theme/
│   │   │           ├── Color.kt
│   │   │           ├── Theme.kt
│   │   │           └── Type.kt
│   └── build.gradle.kts
├── build.gradle.kts
└── README.md

## Архитектура

### Технологический стек:
- **UI Framework:** Jetpack Compose (Material 3)
- **База данных:** SQLite (через SQLiteOpenHelper)
- **Язык:** Kotlin
- **Архитектура:** Single Activity + Composable Functions

### Паттерны:
- State Hoisting для управления состоянием
- Remember + MutableState для локального состояния
- Callback-based навигация между экранами

## Схема базы данных

### Таблица `users`
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER | Первичный ключ |
| first_name | TEXT | Имя пользователя |
| last_name | TEXT | Фамилия пользователя |
| login | TEXT | Уникальный логин |
| email | TEXT | Email адрес |
| password | TEXT | Пароль |
| avatar | TEXT | Эмодзи аватара (по умолчанию 🐍) |
| is_admin | INTEGER | Флаг администратора (0/1) |

### Таблица `courses`
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER | Первичный ключ |
| title | TEXT | Название курса |
| description | TEXT | Описание курса |
| level | TEXT | Уровень (Начальный/Средний/Продвинутый) |
| icon | TEXT | Эмодзи иконка |
| lessons_count | INTEGER | Количество уроков |

### Таблица `lessons`
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER | Первичный ключ |
| course_id | INTEGER | FK на courses.id |
| title | TEXT | Название урока |
| content | TEXT | Содержимое урока |
| order_num | INTEGER | Порядковый номер |
| duration_minutes | INTEGER | Длительность в минутах |

### Таблица `user_stats`
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER | Первичный ключ |
| user_login | TEXT | FK на users.login |
| completed_courses | INTEGER | Количество завершённых курсов |
| total_time_minutes | INTEGER | Общее время обучения |

### Таблица `lesson_progress`
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER | Первичный ключ |
| user_login | TEXT | FK на users.login |
| lesson_id | INTEGER | FK на lessons.id |
| completed | INTEGER | Флаг завершения (0/1) |
| completed_at | TEXT | Дата завершения |

### Таблица `favorites`
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER | Первичный ключ |
| user_login | TEXT | FK на users.login |
| course_id | INTEGER | FK на courses.id |

### Таблица `support_messages`
| Поле | Тип | Описание |
|------|-----|----------|
| id | INTEGER | Первичный ключ |
| user_login | TEXT | Логин пользователя |
| message | TEXT | Текст сообщения |
| is_from_admin | INTEGER | Флаг сообщения от админа (0/1) |
| created_at | TEXT | Дата создания |

## ER-диаграмма

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│   users     │       │   courses   │       │   lessons   │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id (PK)     │       │ id (PK)     │◄──────│ course_id   │
│ login       │       │ title       │       │ id (PK)     │
│ first_name  │       │ description │       │ title       │
│ last_name   │       │ level       │       │ content     │
│ email       │       │ icon        │       │ order_num   │
│ password    │       │ lessons_cnt │       │ duration    │
│ avatar      │       └─────────────┘       └─────────────┘
│ is_admin    │              │                     │
└─────────────┘              │                     │
       │                     │                     │
       │              ┌──────┴──────┐              │
       │              │             │              │
       ▼              ▼             ▼              ▼
┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐
│ user_stats  │ │  favorites  │ │   lesson_progress   │
├─────────────┤ ├─────────────┤ ├─────────────────────┤
│ user_login  │ │ user_login  │ │ user_login          │
│ completed   │ │ course_id   │ │ lesson_id           │
│ total_time  │ └─────────────┘ │ completed           │
└─────────────┘                 │ completed_at        │
       │                        └─────────────────────┘
       │
       ▼
┌──────────────────┐
│ support_messages │
├──────────────────┤
│ user_login       │
│ message          │
│ is_from_admin    │
│ created_at       │
└──────────────────┘
```

## Версии базы данных

| Версия | Изменения |
|--------|-----------|
| 1 | Начальная схема (users, courses, lessons) |
| 2 | Добавлена таблица user_stats |
| 3 | Добавлена таблица lesson_progress |
| 4 | Добавлено поле avatar в users |
| 5 | Добавлена таблица favorites |
| 6 | Добавлена таблица support_messages |

## Лицензия

MIT License

## Автор

Разработано с использованием Jetpack Compose и Material Design 3.
