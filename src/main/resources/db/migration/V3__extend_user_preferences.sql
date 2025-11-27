-- Extend users table with preference flags and staff role metadata
ALTER TABLE users
    ADD COLUMN customer_type VARCHAR(40) NULL AFTER promo_code,
    ADD COLUMN default_shipping_cost DECIMAL(12,2) NOT NULL DEFAULT 3000 AFTER customer_type,
    ADD COLUMN newsletter BIT NOT NULL DEFAULT 0 AFTER default_shipping_cost,
    ADD COLUMN save_address BIT NOT NULL DEFAULT 0 AFTER newsletter,
    ADD COLUMN staff_role VARCHAR(60) NULL AFTER save_address;

-- Ensure existing addresses keep a single primary flag
UPDATE user_addresses SET is_primary = b'0' WHERE is_primary IS NULL;
