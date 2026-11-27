# Руководство программиста

## 1. Структура проекта
- `app/src/main/java/com/example/kalkulator/`
  - `MainActivity.kt` — логика интерфейса, обработка нажатий, переключение тем.
  - `CalculatorEngine.kt` — бизнес-логика, математический парсер (рекурсивный спуск).
  - `HistoryRepository.kt` — сохранение и загрузка истории в файл `history.txt`.
- `app/src/main/res/`
  - `layout/activity_main.xml` — разметка экрана калькулятора.
  - `values/themes.xml` — стили двух тем.

## 2. Зависимости
- Язык: Kotlin.
- Минимальная версия SDK: 24.
- Используемые библиотеки: стандартные AndroidX, Material Components.

## 3. Сборка и запуск
1. Откройте проект в Android Studio.
2. Нажмите `Build -> Make Project` (или иконка Молотка).
3. Для запуска нажмите `Run -> Run 'app'`.
4. Для сборки APK: `Build -> Build Bundle(s) / APK(s) -> Build APK(s)`.

## 4. Тестирование
- Для запуска unit-тестов: откройте `CalculatorEngineTest.kt` и нажмите зеленую стрелочку рядом с классом.
- В проекте реализовано 4 unit-теста (сложение, приоритет операций, деление на ноль, вычитание).