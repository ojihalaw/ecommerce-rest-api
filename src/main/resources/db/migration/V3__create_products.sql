CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    name VARCHAR(150) NOT NULL,
    description TEXT,

    price NUMERIC(18,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,

    category_id UUID NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_products_category
      FOREIGN KEY (category_id)
          REFERENCES categories(id),

    CONSTRAINT chk_products_price
      CHECK (price >= 0),

    CONSTRAINT chk_products_stock
      CHECK (stock >= 0)
);