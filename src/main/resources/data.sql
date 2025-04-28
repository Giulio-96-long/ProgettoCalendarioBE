INSERT INTO `calendar`.`user`
(`id`, `email`, `password`, `role`, `username`)
VALUES
(1, 'admin@example.com', 'password_admin', 'ADMIN', 'adminUser'),
(2, 'user@example.com', 'password_user', 'STELLINA', 'userStellina');

INSERT INTO `calendar`.`date_note`
(`id`, `event_date`)
VALUES
(1, '2025-05-01'),
(2, '2025-06-10');



INSERT INTO `calendar`.`note`
(`id`, `date_creation`, `date_modification`, `description`, `is_important`, `title`, `date_note_id`, `user_id`, `archived`)
VALUES
(1, '2025-04-28 12:00:00', '2025-04-28 12:00:00', 'Nota importante sul progetto', TRUE, 'Progetto A', 1, 1, FALSE),
(2, '2025-04-28 13:00:00', '2025-04-28 13:00:00', 'Nota per il team', FALSE, 'Team B', 2, 2, FALSE);


INSERT INTO `calendar`.`feedbacks`
(`id`, `comment`, `response`, `admin_id`)
VALUES
(1, 'Ottimo lavoro sul progetto!', 'Grazie! Continuerò a migliorare.', 1),
(2, 'Rispetto la scadenza, ma ho bisogno di aiuto.', 'Ci possiamo incontrare domani.', 1);

INSERT INTO `calendar`.`error_log`
(`id`, `endpoint`, `error_message`, `stack_trace`, `timestamp`, `account`, `user`)
VALUES
(1, '/api/note', 'Errore durante la creazione della nota', 'Stack trace completo...', '2025-04-28 12:30:00', 'adminUser', 'admin@example.com'),
(2, '/api/user', 'Errore di autenticazione', 'Stack trace completo...', '2025-04-28 13:15:00', 'userStellina', 'user@example.com');


INSERT INTO `calendar`.`change_history`
(`id`, `change_type`, `modification_date`, `modified_entity`, `new_data`, `previous_data`, `modified_by`, `note_id`)
VALUES
(1, 'UPDATE', '2025-04-28 12:05:00', 'note', 'Modifica al titolo', 'Vecchio titolo', 1, 1),
(2, 'DELETE', '2025-04-28 13:05:00', 'attachment', 'Allegato rimosso', 'N/A', 2, 2);


INSERT INTO `calendar`.`attachment`
(`id`, `base64`, `date_creation`, `nome`, `path`, `note_id`)
VALUES
(1, 'base64encodedstring', '2025-04-28 12:00:00', 'Allegato1', '/path/to/file1', 1),
(2, 'base64encodedstring', '2025-04-28 12:30:00', 'Allegato2', '/path/to/file2', 2);

INSERT INTO `calendar`.`personalized_note`
(`id`, `color`, `custom_message`, `note_id`)
VALUES
(1, 'red', 'Messaggio personalizzato per Progetto A', 1),
(2, 'blue', 'Messaggio personalizzato per Team B', 2);


