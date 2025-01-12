
CREATE SEQUENCE IF NOT EXISTS audit_event_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE AUDIT_EVENT
(
    id         BIGINT                      NOT NULL DEFAULT nextval('audit_event_id_seq'),
    type       VARCHAR(32)                 NOT NULL,
    action     VARCHAR(32)                 NOT NULL,
    reference  TEXT                        NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_audit_event PRIMARY KEY (id)
);
