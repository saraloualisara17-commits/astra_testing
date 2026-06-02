-- Composite indexes for common multi-column query patterns.
-- Uses ADD INDEX IF NOT EXISTS (supported on MySQL 8.0.29+ / MySQL 9.x).

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
