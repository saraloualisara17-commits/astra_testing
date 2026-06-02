-- Performance indexes: eliminates full-table scans on the most-queried columns.
-- Uses ADD INDEX IF NOT EXISTS (supported on MySQL 8.0.29+ / MySQL 9.x).

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
