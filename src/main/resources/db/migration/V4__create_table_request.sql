CREATE TABLE requests
(
  id          BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  request_id  VARCHAR(36) NOT NULL,
  transfer_id BIGINT      NOT NULL,
  CONSTRAINT requests_transfer_id_fk FOREIGN KEY (transfer_id) REFERENCES transfers (id)
);
CREATE UNIQUE INDEX requests_id_uindex
  ON requests (id);

CREATE UNIQUE INDEX requests_request_id_uindex
  ON requests (request_id);
