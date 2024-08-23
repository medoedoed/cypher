#  Cypher

Cypher is a simple CLI password manager for Linux systems. 

-------

## Introduction

#### Cypher is a simple password manager written in Java, so you will need the JDK to make it work. 
#### It securely stores all your encrypted logins and passwords in a SQLite database located in a specified directory and uses a specified passphrase to decrypt them. 
#### The application uses SHA-256 to verify the passphrase and AES-256 to encrypt the logins and passwords.
-----
## Installation

#### To install Cypher, simply clone the repository and run the install script as root. It will automatically build the project and copy the executable to /usr/local/bin.

```sh
$ git clone https://github.com/medoedoed/cypher.git
$ cd cypher
$ sudo install.sh
```

----
## Configuration

#### You can find config file in ~/.config/cypher. t's a standard TOML file.

----

#### Cypher is an open-source project. Contributions are welcome!
