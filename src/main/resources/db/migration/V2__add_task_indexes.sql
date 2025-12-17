-- V2__Add_task_indexes.sql
CREATE INDEX IF NOT EXISTS idx_task_status ON task_time.task(status);
