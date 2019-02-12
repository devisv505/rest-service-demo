CREATE TABLE accounts
(
  id          BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  uuid        VARCHAR(36) NOT NULL,
  balance     DECIMAL     NOT NULL,
  description VARCHAR(500),
  currency    VARCHAR_IGNORECASE(3)
);
CREATE UNIQUE INDEX accounts_id_uindex
  ON accounts (id);
CREATE UNIQUE INDEX accounts_uuid_uindex
  ON accounts (uuid);