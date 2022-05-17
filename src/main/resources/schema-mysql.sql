create table account (
    account_id bigint not null auto_increment,
    login_id varchar(30) not null,
    password varchar(30) not null,
    nickname varchar(30) not null,
    email varchar(30) not null,
    email_verified bit not null,
    email_check_token varchar(255),
    email_check_token_generated_at datetime(6),
    profile_image longtext,
    created_at datetime(6),
    updated_at datetime(6),
    primary key (account_id)
) engine=InnoDB;

alter table account add constraint UK_email unique (email);
alter table account add constraint UK_login_id unique (login_id);
alter table account add constraint UK_nickname unique (nickname);