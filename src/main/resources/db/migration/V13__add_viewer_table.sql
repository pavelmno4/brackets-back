CREATE TABLE IF NOT EXISTS viewer
(
    id             uuid PRIMARY KEY,
    first_name     VARCHAR(255) NOT NULL,
    last_name      VARCHAR(255) NOT NULL,
    middle_name    VARCHAR(255) NOT NULL,
    phone          VARCHAR(255) NOT NULL,
    competition_id uuid         NOT NULL,
    CONSTRAINT viewer_competition_id_fk FOREIGN KEY (competition_id) REFERENCES competition (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE INDEX viewer_competition_id_idx ON viewer (competition_id);