# pw-manager
Password manager utility for linux



## Функциональные требования:

#### UI/UX:

1. Сохранение checksum для супер-пароля. Если в директории уже лежит пароль, выводит сообщение об ошибке.
    ```shell
    $ pwm init [OPTIONS]...
    "Enter your super password:"
    > *****
    "Repeat super password:"
    > *****
    "Checksum saved."
     ```
   Options:
   ```shell
   -d, --directory    Set directory to init utility.
   -v, --visible      Show password when you enter it.
   ```
2. Обновление супер-пароля.
    ```shell
    $ pwm chspw [OPTIONS]...
    "Enter your current super password:"
    > *****
    "Enter your new super password:"
    > *****
    "Repeat new super password:"
    > *****
    "Super password changed successfully"
    ```
   Options:
   ```shell
   -v, --visible      Show password when you enter it.
   ```
3. Установка директории для хранения паролей (по умолчанию ~/.passwords). Все старые пароли переносит в новую директорию:

   TODO: реализовать set

   ```shell
   $ pwm -l /new/directory/
   ```
4. Сохранение пароля к новому сервису:
    ```shell
   $ pwm save [OPTION]... [SERVICE]
   "Service already exists. Do you want to overwrite it? (y/n)"
   > y
   "Enter your password:"
   > my_p4ssw0rd
   "Enter you super password"
   > *****
   "Service saved successfully"
   ```
   Options:
   ```
   -g, --generate      Generate password.
   -l, --length        Specify the length of the password.
   -s, --special       Include special characters.
   -c, --clipboard     Save password to clipboard.
   ```
5. Генерация пароля:
   ```shell
   $ pwm gpw [OPTIONS]...
   Generated password: s3cr3t_p4ssw0rd
   ```
   Options:
   ```
   -c, --clipboard     Save password to clipboard.
   -l, --length        Specify the length of the password.
   -s, --special       Include special characters.
   ```
6. Вывод всех залогиненых сервисов.
   ```shell
   $ pwm list
   service1
   service2...
   ```
7. Вывод пароля и логина определенного сервиса.
   ```shell
   $ pwm show [SERVICE]
   "Enter your super password:"
   > *****
   Login: user1
   Password: p4ssw0rd  
   ```
8. Удаление сервиса.
9. Изменение пароля к сервису.

#### Хранение паролей:

Пароли лежат в директории (по умолчанию ~/.passwords). В директории лежит текстовый файл с checksum (.checksum). Далее для каждого серивиса создается отдельный файл с названием соответсующего названию серивиса. В каждом из таком файлов лежит два хэша зашифрованных при помощи двустороннего шифрования - логин и пароль.

TODO: добавить контроль доступа к файлам



