CREATE TABLE IF NOT EXISTS "order"
(
    id         BIGSERIAL PRIMARY KEY,
    status     VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255),
    cost_in_cents   INTEGER,
    inventory_count INTEGER,
    status          VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS product_by_order
(
    order_id      BIGINT,
    product_id    BIGINT,
    count         INTEGER,
    cost_in_cents INTEGER,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES "order" (id),
    FOREIGN KEY (product_id) REFERENCES product (id)
);

