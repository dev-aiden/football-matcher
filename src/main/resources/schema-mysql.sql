create table account (
    account_id bigint not null auto_increment,
    login_id varchar(20) not null,
    password varchar(20) not null,
    name varchar(10) not null,
    nickname varchar(10) not null,
    phone_number varchar(11) not null,
    email varchar(30) not null,
    email_verified bit default 0 not null,
    email_check_token varchar(255),
    email_check_token_generated_at datetime(6),
    profile_image longtext,
    created_at datetime default current_timestamp not null,
    updated_at datetime default current_timestamp not null,
    primary key (account_id)
) engine=InnoDB;

alter table account add constraint UK_email unique (email);
alter table account add constraint UK_login_id unique (login_id);
alter table account add constraint UK_nickname unique (nickname);
alter table account add constraint UK_phone_number unique (phone_number);

create table team (
    team_id bigint not null auto_increment,
    name varchar(255) not null,
    secret bit default 0 not null,
    emblem longtext,
    description longtext,
    created_at datetime default current_timestamp not null,
    updated_at datetime default current_timestamp not null,
    primary key (team_id)
) engine=InnoDB;

create table team_member (
    team_member_id bigint not null auto_increment,
    account_id bigint not null,
    team_id bigint not null,
    created_at datetime default current_timestamp not null,
    updated_at datetime default current_timestamp not null,
    primary key (team_member_id)
) engine=InnoDB;