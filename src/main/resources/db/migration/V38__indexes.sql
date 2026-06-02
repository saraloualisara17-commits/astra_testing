-- Performance and composite indexes.
-- Replaces the failed V36/V37 migrations (which used DELIMITER syntax
-- incompatible with Flyway's JDBC executor).
-- All statements use IF NOT EXISTS so re-running is always safe.

ALTER TABLE commandes
    ADD INDEX IF NOT EXISTS idx_commandes_status (status);

ALTER TABLE commandes
    ADD INDEX IF NOT EXISTS idx_commandes_date_creation (date_creation);

ALTER TABLE commandes
    ADD INDEX IF NOT EXISTS idx_commandes_scheduled_pickup (scheduled_pickup_date);

ALTER TABLE commandes
    ADD INDEX IF NOT EXISTS idx_commandes_scheduled_delivery (scheduled_delivery_date);

ALTER TABLE commandes
    ADD INDEX IF NOT EXISTS idx_commandes_delivery_driver (delivery_driver_id);

ALTER TABLE historique_statuts
    ADD INDEX IF NOT EXISTS idx_historique_statut_date (nouveau_statut, created_at);

ALTER TABLE clients
    ADD INDEX IF NOT EXISTS idx_clients_created_at (created_at);

ALTER TABLE commandes
    ADD INDEX IF NOT EXISTS idx_commandes_livreur_date (pickup_driver_id, date_creation);

ALTER TABLE commandes
    ADD INDEX IF NOT EXISTS idx_commandes_delivery_driver_status (delivery_driver_id, status);

ALTER TABLE commandes
    ADD INDEX IF NOT EXISTS idx_commandes_status_montants (status, montant_total, montant_paye);

ALTER TABLE commandes
    ADD INDEX IF NOT EXISTS idx_commandes_status_date_creation (status, date_creation);

ALTER TABLE notifications
    ADD INDEX IF NOT EXISTS idx_notifications_recipient_read (recipient_id, is_read);
