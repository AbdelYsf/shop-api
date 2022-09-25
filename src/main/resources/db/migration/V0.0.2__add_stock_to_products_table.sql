ALTER TABLE products
    ADD COLUMN stock decimal
        CONSTRAINT CH__PRODUCTS__STOCK CHECK (stock >= 0);
