CREATE TABLE transfers
(
  id                BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  uuid              VARCHAR(36) NOT NULL,
  source_account_id BIGINT      NOT NULL,
  target_account_id BIGINT      NOT NULL,
  amount            DECIMAL     NOT NULL,
  created_at        TIMESTAMP   NOT NULL,
  completed_at      TIMESTAMP,
  state             VARCHAR(50) NOT NULL,
  description       VARCHAR(500),
  currency          VARCHAR_IGNORECASE(3),
  CONSTRAINT transfers_source_accounts_id_fk FOREIGN KEY (source_account_id) REFERENCES accounts (id),
  CONSTRAINT transfers_target_accounts_id_fk FOREIGN KEY (target_account_id) REFERENCES accounts (id)
);
CREATE UNIQUE INDEX transfers_id_uindex
  ON transfers (id);
CREATE UNIQUE INDEX transfers_uuid_uindex
  ON transfers (uuid);