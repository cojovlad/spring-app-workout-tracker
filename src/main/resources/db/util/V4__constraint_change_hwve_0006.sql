ALTER TABLE exercise_sets
    DROP CHECK chk_weight;

ALTER TABLE exercise_sets
    ADD CONSTRAINT chk_weight CHECK (weight_kg >= 0.00 AND weight_kg <= 500);
