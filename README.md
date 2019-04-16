# uds-test
Repository for Test

Como testar a aplicação:
Execute o jar UDSProject-jar-with-dependencies.jar com o comando 
java -jar UDSProject-jar-with-dependencies.jar

Execute os comandos abaixo em um Postgresql local.

create database projetouds;
create table pessoa(id bigint primary key, nome varchar(80) not null, email varchar(80), data_cadastro timestamp default current_timestamp);
create sequence pessoa_id_seq start with 1;

Instale o Postman (https://www.getpostman.com/)
Importe o json UDS Tecnologia.postman_collection.json para o Postman
Envie para o serviço as 4 operações ou crie seus próprios testes.
