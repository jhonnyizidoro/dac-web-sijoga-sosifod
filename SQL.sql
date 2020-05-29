DROP TABLE IF EXISTS intimations;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	id INT PRIMARY KEY AUTO_INCREMENT,
	profile INT NOT NULL,
	name VARCHAR(255) NOT NULL,
	cpf VARCHAR(255) UNIQUE NOT NULL,
	email VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL
);

CREATE TABLE intimations (
	id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	cpf VARCHAR(255) NOT NULL,
	address VARCHAR(255) NOT NULL,
	lawsuit VARCHAR(255) NOT NULL,
	origin INT NOT NULL DEFAULT 0,
	created_at TIMESTAMP NOT NULL,
	concluded_at TIMESTAMP,
	officer_id INT NOT NULL,
	FOREIGN KEY (officer_id) REFERENCES users(id)
);

DROP TABLE IF EXISTS phases;
DROP TABLE IF EXISTS lawsuits;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	id INT PRIMARY KEY AUTO_INCREMENT,
	profile INT NOT NULL,
	name VARCHAR(255) NOT NULL,
	cpf VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL
);

CREATE TABLE lawsuits (
	id INT PRIMARY KEY AUTO_INCREMENT,
	created_at TIMESTAMP NOT NULL,
	status INT NOT NULL,
	promoted_id INT NOT NULL,
	promoting_id INT NOT NULL,
	promoted_lawier_id INT NOT NULL,
	promoting_lawier_id INT NOT NULL,
	judge_id INT NOT NULL,
	FOREIGN KEY (promoted_id) REFERENCES users(id),
	FOREIGN KEY (promoting_id) REFERENCES users(id),
	FOREIGN KEY (promoted_lawier_id) REFERENCES users(id),
	FOREIGN KEY (promoting_lawier_id) REFERENCES users(id),
	FOREIGN KEY (judge_id) REFERENCES users(id)
);

CREATE TABLE phases (
	id INT PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(255) NOT NULL,
	description MEDIUMTEXT NOT NULL,
	attachment MEDIUMTEXT NOT NULL,
	created_at TIMESTAMP NOT NULL,
	type INT NOT NULL,
	status INT NOT NULL,
	user_id INT NOT NULL,
	lawsuit_id INT NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id),
	FOREIGN KEY (lawsuit_id) REFERENCES users(id)
);