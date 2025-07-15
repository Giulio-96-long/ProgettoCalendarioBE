-- === UTENTI ===
INSERT INTO users (id, email, password, username, lastname, photo, photo_content_type, deleted, role)
VALUES 
  (1, 'admin@email.com', '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'Admin', 'Root', NULL, NULL, FALSE, 'ADMIN'),
  (2, 'user1@email.com', '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'Mario', 'Rossi', NULL, NULL, FALSE, 'USER'),
  (3, 'user2@email.com', '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'Giulia', 'Verdi', NULL, NULL, FALSE, 'USER'),
  (4, 'user3@email.com', '$2a$10$4EQxZUJPgVScZEOfHlDKhOfGExqXWQy.3Hjz3SsoaM854zpzNmhMC', 'Luca', 'Bianchi', NULL, NULL, FALSE, 'USER');

-- === DATENOTE ===
INSERT INTO date_note (id, event_date, deleted)
VALUES 
  (1, '2025-07-14T10:00:00', FALSE),
  (2, '2025-07-15T10:00:00', FALSE),
  (3, '2025-08-10T09:00:00', FALSE),
  (4, '2025-09-21T14:00:00', FALSE);

-- === NOTE ===
INSERT INTO note (id, title, description, date_creation, date_modification, is_important, archived, deleted, user_id, date_note_id)
VALUES 
  (1, 'Nota di esempio', 'Questa è una descrizione di test.', '2025-07-14T10:05:00', NULL, TRUE, FALSE, FALSE, 2, 1),
  (2, 'Nota Importante', 'Da discutere con il team.', '2025-07-15T11:00:00', NULL, TRUE, FALSE, FALSE, 3, 2),
  (3, 'Scadenza mensile', 'Controllare i dati.', '2025-08-10T09:15:00', NULL, TRUE, FALSE, FALSE, 2, 3),
  (4, 'Progetto segreto', 'Non condividere.', '2025-09-21T14:10:00', NULL, FALSE, FALSE, FALSE, 4, 4);

-- === PERSONALIZED NOTE ===
INSERT INTO personalized_note (id, note_id, color, custom_message)
VALUES 
  (1, 1, '#FF0000', 'Messaggio personalizzato'),
  (2, 2, '#0000FF', 'Urgente!'),
  (3, 3, '#00AA00', 'Scadenza vicina'),
  (4, 4, '#888888', 'Riservata');

-- === NOTE CHANGE HISTORY ===
INSERT INTO note_change_history (id, change_type, note_id, modified_by, modification_date)
VALUES 
  (1, 'CREAZIONE', 1, 2, '2025-07-14T10:05:00'),
  (2, 'CREAZIONE', 2, 3, '2025-07-15T11:00:00'),
  (3, 'MODIFICA', 2, 3, '2025-07-15T11:30:00'),
  (4, 'CREAZIONE', 3, 2, '2025-08-10T09:15:00'),
  (5, 'CREAZIONE', 4, 4, '2025-09-21T14:10:00');

-- === FEEDBACK ===
INSERT INTO feedback (id, sender_id, receiver_id, subject, body, parent_id, created_at, is_read, read_at)
VALUES 
  (1, 2, 1, 'Problema con l\'app', 'Ho trovato un bug nel calendario.', NULL, '2025-07-14T11:00:00', FALSE, NULL),
  (2, 3, 1, 'Suggerimento', 'Aggiungerei notifiche giornaliere.', NULL, '2025-07-15T12:00:00', TRUE, '2025-07-15T15:00:00');

-- === SHARE ===
INSERT INTO note_share (id, note_id, shared_by, shared_at)
VALUES 
  (1, 2, 3, '2025-07-15T12:10:00'),   -- user2 condivide la nota 2
  (2, 3, 2, '2025-08-10T10:00:00');   -- user1 condivide la nota 3

-- === SHARE MEMBER ===
INSERT INTO share_member (id, share_id, user_id, removed_for_me)
VALUES 
  (1, 1, 2, FALSE),  -- nota 2 condivisa con user1
  (2, 1, 4, FALSE),  -- nota 2 condivisa con user3
  (3, 2, 3, FALSE);  -- nota 3 condivisa con user2
