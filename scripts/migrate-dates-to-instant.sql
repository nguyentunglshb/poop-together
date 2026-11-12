-- Run in the application's PostgreSQL database before starting the new version.
-- Set the search path to the application schema and use the zone of the old local timestamps.
BEGIN;

SET LOCAL TIME ZONE 'Asia/Bangkok';

DO $$
DECLARE
    app_schema text := current_schema();
    legacy_zone text := current_setting('TimeZone');
    table_change record;
BEGIN
    FOR table_change IN
        SELECT mapping.table_name,
               string_agg(
                   format('ALTER COLUMN %I TYPE timestamp(6) with time zone USING (%I::timestamp AT TIME ZONE %L)',
                          mapping.column_name, mapping.column_name, mapping.source_zone),
                   ', ' ORDER BY mapping.column_name
               ) AS alterations
        FROM (VALUES
            ('conversation_members', 'joined_at', legacy_zone),
            ('conversation_members', 'last_read_at', legacy_zone),
            ('conversation_members', 'left_at', legacy_zone),
            ('conversations', 'created_at', legacy_zone),
            ('conversations', 'last_message_at', legacy_zone),
            ('conversations', 'updated_at', legacy_zone),
            ('friendships', 'created_at', legacy_zone),
            ('friendships', 'responded_at', legacy_zone),
            ('leaderboard_snapshots', 'created_at', legacy_zone),
            ('leaderboard_snapshots', 'period_start', 'Asia/Bangkok'),
            ('messages', 'created_at', legacy_zone),
            ('messages', 'recalled_at', legacy_zone),
            ('notifications', 'created_at', legacy_zone),
            ('poop_sessions', 'started_at', legacy_zone),
            ('poop_sessions', 'updated_at', legacy_zone),
            ('refresh_tokens', 'created_at', legacy_zone),
            ('refresh_tokens', 'expires_at', legacy_zone),
            ('refresh_tokens', 'revoked_at', legacy_zone),
            ('user_daily_stats', 'stat_date', 'Asia/Ho_Chi_Minh'),
            ('user_daily_stats', 'updated_at', legacy_zone),
            ('users', 'created_at', legacy_zone),
            ('users', 'last_seen_at', legacy_zone),
            ('users', 'updated_at', legacy_zone)
        ) AS mapping(table_name, column_name, source_zone)
        JOIN information_schema.columns AS actual
          ON actual.table_schema = app_schema
         AND actual.table_name = mapping.table_name
         AND actual.column_name = mapping.column_name
        WHERE actual.data_type IN ('date', 'timestamp without time zone')
        GROUP BY mapping.table_name
        ORDER BY mapping.table_name
    LOOP
        EXECUTE format('ALTER TABLE %I.%I %s', app_schema, table_change.table_name, table_change.alterations);
    END LOOP;
END;
$$;

COMMIT;
