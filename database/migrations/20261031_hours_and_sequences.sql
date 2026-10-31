-- One-time migration for the existing MariaDB schema.
-- Stop application writers and verify a backup before running this script.
-- Removing players discards that column's values; keep the pre-migration backup.
-- MariaDB DDL commits implicitly. If a later statement fails, inspect the
-- schema and sequences before proceeding; do not rerun this script blindly.

SET @ready = (
    SELECT COUNT(*) = 1 FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'videogames' AND column_name = 'players'
) AND (
    SELECT COUNT(*) = 0 FROM information_schema.columns
    WHERE table_schema = DATABASE() AND table_name = 'videogames' AND column_name = 'hours_played'
) AND (
    SELECT COUNT(*) = 4 FROM information_schema.tables
    WHERE table_schema = DATABASE() AND table_type = 'SEQUENCE'
      AND table_name IN ('videogames_seq', 'platform_seq', 'digital_platform_seq', 'hibernate_sequence')
);
SET @sql = IF(@ready, 'DO 0',
    'SIGNAL SQLSTATE ''45000'' SET MESSAGE_TEXT = ''Migration preconditions not met; no changes made''');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

ALTER TABLE videogames
    DROP COLUMN players,
    ADD COLUMN hours_played INT NULL DEFAULT NULL;

-- Hibernate allocates blocks of 50 identifiers. Keep each next block above
-- both the highest existing ID and the current sequence position.
SET @next_id = (SELECT GREATEST(COALESCE(MAX(id), 0) + 51,
    (SELECT next_not_cached_value FROM videogames_seq)) FROM videogames);
SET @sql = CONCAT('ALTER SEQUENCE videogames_seq RESTART WITH ', @next_id);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @next_id = (SELECT GREATEST(COALESCE(MAX(id), 0) + 51,
    (SELECT next_not_cached_value FROM platform_seq)) FROM platform);
SET @sql = CONCAT('ALTER SEQUENCE platform_seq RESTART WITH ', @next_id);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @next_id = (SELECT GREATEST(COALESCE(MAX(id), 0) + 51,
    (SELECT next_not_cached_value FROM digital_platform_seq)) FROM digital_platform);
SET @sql = CONCAT('ALTER SEQUENCE digital_platform_seq RESTART WITH ', @next_id);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @next_id = (SELECT GREATEST(
    (SELECT COALESCE(MAX(id), 0) FROM videogames),
    (SELECT COALESCE(MAX(id), 0) FROM platform),
    (SELECT COALESCE(MAX(id), 0) FROM digital_platform)
) + 1);
SET @next_id = GREATEST(@next_id, (SELECT next_not_cached_value FROM hibernate_sequence));
SET @sql = CONCAT('ALTER SEQUENCE hibernate_sequence RESTART WITH ', @next_id);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
