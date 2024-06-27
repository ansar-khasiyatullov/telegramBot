-- liquibase formatted sql

-- changeset developer1:1
CREATE TABLE notification_task (
    id BIGSERIAL primary key,
    chatId BIGINT,
    noteText TEXT,
    noteTime TIMESTAMP
)