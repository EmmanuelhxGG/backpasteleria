-- Extend order_items to persist benefit labels and item notes for reporting
ALTER TABLE order_items
    ADD COLUMN benefit_labels JSON NULL AFTER original_subtotal,
    ADD COLUMN note TEXT NULL AFTER benefit_labels;
