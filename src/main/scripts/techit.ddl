
    create table assignments (
       ticket_id bigint not null,
        technician_id bigint not null,
        primary key (ticket_id, technician_id)
    ) engine=InnoDB;

    create table hibernate_sequence (
       next_val bigint
    ) engine=InnoDB;

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    insert into hibernate_sequence values ( 1 );

    create table tickets (
       id bigint not null,
        completion_details varchar(255),
        details varchar(255),
        end_date datetime,
        update_date datetime,
        location varchar(255),
        priority varchar(255),
        progress varchar(255),
        start_date datetime,
        subject varchar(255),
        requester_id bigint,
        unit_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table units (
       id bigint not null,
        description varchar(255),
        email varchar(255),
        location varchar(255),
        name varchar(255) not null,
        phone varchar(255),
        primary key (id)
    ) engine=InnoDB;

    create table updates (
       id bigint not null,
        date datetime,
        details varchar(255),
        modifier_id bigint,
        ticket_id bigint,
        primary key (id)
    ) engine=InnoDB;

    create table users (
       id bigint not null,
        department varchar(255),
        email varchar(255),
        enabled bit not null,
        first_name varchar(255) not null,
        hash varchar(255) not null,
        last_name varchar(255) not null,
        phone varchar(255),
        post varchar(255),
        username varchar(255) not null,
        unit_id bigint,
        primary key (id)
    ) engine=InnoDB;

    alter table units 
       add constraint UK_etw07nfppovq9p7ov8hcb38wy unique (name);

    alter table users 
       add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);

    alter table assignments 
       add constraint FK8mhcham1p9eg34kjjukrysfec 
       foreign key (technician_id) 
       references users (id);

    alter table assignments 
       add constraint FKo9wcactgruf2l6m8tw49vm7kv 
       foreign key (ticket_id) 
       references tickets (id);

    alter table tickets 
       add constraint FKdp5i1hou98n2co3e49fffh9fp 
       foreign key (requester_id) 
       references users (id);

    alter table tickets 
       add constraint FKmj126vcy9uobxd6rfu269wjc2 
       foreign key (unit_id) 
       references units (id);

    alter table updates 
       add constraint FKoi4ep6c36gikkxrtwny69hm0k 
       foreign key (modifier_id) 
       references users (id);

    alter table updates 
       add constraint FK3fnl74oyd1raon25v5lo3hyag 
       foreign key (ticket_id) 
       references tickets (id);

    alter table users 
       add constraint FKp2hfld4bhbwtakwrmt4xq6een 
       foreign key (unit_id) 
       references units (id);
