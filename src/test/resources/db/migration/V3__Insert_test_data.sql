-- V3__Insert_test_data.sql
INSERT INTO task_time.task (id, title, description, status)
VALUES 
(1, 'Prepare slides for the Spring meetup', 'Prepare slides for the Spring meetup', 'TO_DO'),
(2, 'Go to the theater', 'Go to the theater', 'TO_DO');

-- Reset the sequence to start after the highest ID
SELECT setval('task_time.task_seq', (SELECT MAX(id) FROM task_time.task), true);