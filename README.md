# pw-manager
Password manager utility for linux



## Функциональные требования:

#### UI/UX:

1. Сохранение checksum для супер-пароля. Если в директории уже лежит пароль, выводит сообщение об ошибке.
    ```shell
    $ pwm init 
    "Enter your super password:"
    > *****
    "Repeat super password:"
    > *****
    "Checksum saved."
     ```
2. Обновление супер-пароля.
    ```shell
    $ pwm chspw 
    "Enter your previous checksum"
    > *****
    "Super password changed successfully"
    ```

3. Установка директории для хранения паролей (по умолчанию ~/.passwords). Все старые пароли переносит в новую директорию:
    ```shell
    $ pwm -l /new/directory/
    ```
4. Сохранение пароля к новому сервису:
    ```shell
    $ pwm save [OPTION]... [SERVICE]
    "Enter your password:"
    > my_p4ssw0rd
    "Enter you super password"
    > *****
    "Service saved successfully"
    ```
    Options:
    ```
    -g, --generate      Generate password.
    -h, --hide          Don't show password in terminal after saving.
    -c, --clipboard     Save password to clipboard.
    ```
5. Генерация пароля:
    ```shell
    $ pwm gpw [OPTIONS]...
    s3cr3t_p4ssw0rd
    ```
    Options:
    ```
    -c, --clipboard     Save password to clipboard.
    ```
6. Вывод всех залогиненых сервисов.
7. Вывод пароля и логина определенного сервиса.
8. Удаление сервиса.
9. Изменение пароля к сервису.

#### Хранение паролей:

Пароли лежат в директории (по умолчанию ~/.passwords). В директории лежит текстовый файл с checksum (.checksum). Далее для каждого серивиса создается отдельный файл с названием соответсующего названию серивиса. В каждом из таком файлов лежит два хэша зашифрованных при помощи двустороннего шифрования - логин и пароль.

