create table if not exists users
(
    id          bigserial primary key,
    created_at  timestamp(6),
    email       varchar(255) not null unique,
    login       varchar(255) not null unique,
    profile_pic uuid default 'e34838b9-e85b-4ebc-b5e7-28c1a6899e70' not null unique
);

create table if not exists projects
(
    project_id  bigserial primary key,
    description varchar(250),
    likes       bigint  default 0                                            not null,
    main_image  uuid    default '21e22474-d31f-4119-8478-d9d448727cfe'       not null,
    title       varchar(50)                                                  not null unique,
    views       bigint  default 0                                            not null,
    visibility  boolean default false                                        not null,
    owner_id    bigint                                                       not null,
    foreign key (owner_id) references users(id)
);

create table if not exists tags
(
    id    bigserial primary key,
    title varchar(255) not null unique
);

create table if not exists projects_tags
(
    projects_project_id bigint not null,
    tags_id             bigint not null,
    foreign key (tags_id) references tags(id),
    foreign key (projects_project_id) references projects(project_id)
);

create table if not exists files
(
    id         uuid         not null primary key,
    name       varchar(255) not null unique,
    project_id bigint,
    foreign key (project_id) references projects(project_id),
);

create table if not exists refresh_tokens
(
    expired_time timestamp(6) not null,
    token        varchar(255) not null unique,
    user_id      bigint       not null,
    foreign key(user_id) references users(id)
);

create table if not exists roles
(
    id   bigint       not null primary key,
    role varchar(255) not null unique
);

create table if not exists user_details
(
    is_active boolean      not null,
    password  varchar(255) not null,
    user_id   bigint       not null,
    foreign key(user_id) references users(id)
);

create table if not exists user_details_roles
(
    role_id         bigint not null,
    user_details_id bigint not null,
    foreign key(role_id) references roles(id),
    foreign key(user_details_id) references user_details(user_id)
);

create table if not exists verification_tokens
(
    delete_user  boolean      not null,
    expired_time timestamp(6) not null,
    token        varchar(255) not null unique,
    user_id      bigint       not null,
    foreign key(user_id) references users(id)
);

INSERT INTO emp_tab(emp_id, emp_name, emp_email)
SELECT 3, 'Seth', 'seth@abc.com'
    WHERE
NOT EXISTS (
SELECT emp_id FROM emp_tab WHERE emp_id = 3
);

insert into roles (id, role)
select 1, 'USER_ROLE'
    where not exists (select id from roles where role = 1);

insert into files (id, name)
select 'e34838b9-e85b-4ebc-b5e7-28c1a6899e70', 'default_icon.jpg'
    where not exists (select id from files where id = 'e34838b9-e85b-4ebc-b5e7-28c1a6899e70');

insert into files (id, name)
select '21e22474-d31f-4119-8478-d9d448727cfe', 'default_project.jpg'
    where not exists (select id from files where id = '21e22474-d31f-4119-8478-d9d448727cfe');



