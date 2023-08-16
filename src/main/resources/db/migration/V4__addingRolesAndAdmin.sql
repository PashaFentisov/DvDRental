insert into role(name) values ('ROLE_ADMIN');

insert into role(name) values ('ROLE_USER');

insert into users(email, password, is_verified, role_id) values ('pasha.ua@gmail.com','$2a$12$KHJu28QJhlkQ04ryhxtPeOxmCbDh1AjVqpFyxcpwvrtrHPxTTlziu', true, 1);