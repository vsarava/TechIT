create table hibernate_sequence (
	next_val bigint
);

insert into hibernate_sequence values ( 10000 );

create table tickets (
	id					bigint primary key,
	completion_details	varchar(8000),
	details				varchar(500),
	end_date			datetime,
	update_date			datetime,
	location			varchar(255),
	priority			varchar(255) default 'MEDIUM',
	progress			varchar(255) default 'OPEN',
	start_date			datetime default now(),
	subject				varchar(255),
	requester_id		bigint not null references users(id),
	unit_id				bigint references units(id)
);

create table units (
	id				bigint primary key,
	description		varchar(500),
	email			varchar(255),
	location		varchar(255),
	name			varchar(255) not null,
	phone			varchar(255)
);

create table updates (
	id				bigint primary key,
	date			datetime,
	details			varchar(8000),
	modifier_id		bigint not null references users(id),
	ticket_id		bigint not null references tickets(id)
);

create table users (
	id				bigint primary key,
	department		varchar(255),
	email			varchar(255),
	enabled			boolean default true,
	first_name		varchar(255) not null,
	hash			varchar(255) not null,
	last_name		varchar(255) not null,
	phone			varchar(255),
	post			varchar(255) default 'USER',
	username		varchar(255) not null,
	unit_id			bigint references units(id)
);

create table assignments(
	ticket_id		bigint not null references tickets(id),
	technician_id	bigint not null references users(id)
);


insert into techit2.users(id, username, hash, first_name, last_name, unit_id, post)
	values (1,'admin', '$2a$10$4Mss6qmmc8FLwLe8sIXrP.1Y1B41Hgagi4nKDmeqk3kT1POnbzmI6', 'Admin', 'admin', 7, 'SYS_ADMIN');
insert into techit2.users( id,username, hash, first_name, last_name, unit_id, post) 
	values (2,'ammar', '$2a$10$4Mss6qmmc8FLwLe8sIXrP.1Y1B41Hgagi4nKDmeqk3kT1POnbzmI6', 'ammar', 'barafwala', 7, 'SUPERVISING_TECHNICIAN');
insert into techit2.users( id,username, hash, first_name, last_name, unit_id, post) 
	values (3,'kenny', '$2a$10$4Mss6qmmc8FLwLe8sIXrP.1Y1B41Hgagi4nKDmeqk3kT1POnbzmI6', 'Adekola', 'Togunloju', 7, 'SUPERVISING_TECHNICIAN');
insert into techit2.users( id,username, hash, first_name, last_name, unit_id, post) 
	values (4,'viccena', '$2a$10$4Mss6qmmc8FLwLe8sIXrP.1Y1B41Hgagi4nKDmeqk3kT1POnbzmI6', 'Vicky', 'Saravanan', 7,'TECHNICIAN');
insert into techit2.users( id,username, hash, first_name, last_name) 
	values (6,'vic', '$2a$10$4Mss6qmmc8FLwLe8sIXrP.1Y1B41Hgagi4nKDmeqk3kT1POnbzmI6', 'Vicky', 'Saravanan');


insert into units (id, description, email, location, name,phone) 
	values (7, 'Testing','a@g.com', 'KH', 'Testing', '123456');
insert into units (id, description, email, location, name,phone) 
	values (1, 'Testing2','a@g.com', 'KH', 'Testing2', '123456');

insert into tickets (id, subject, requester_id, unit_id) 
	values(1, 'Ac Repair', 4, 7);
insert into tickets (id, subject, requester_id, unit_id) 
	values(2, 'Projector Failure', 2, 1);
insert into tickets (id, subject, requester_id) 
	values(3, 'Ac Repair',3);
insert into tickets (id, subject, requester_id) 
	values(4, 'Ac Repair',3);

insert into assignments values(1, 1);
insert into assignments values(1, 4);
insert into assignments values(3,4);

insert into updates (id, details,ticket_id,modifier_id) 
	value(1,'Demo',1,2);
insert into updates (id, details,ticket_id,modifier_id) 
	value(2,'Demo2',1,2);