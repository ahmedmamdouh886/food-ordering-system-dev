DROP SCHEMA IF EXISTS customer CASCADE;

CREATE SCHEMA customer;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE customer.customers
(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);

DROP MATERIALIZED VIEW IF EXISTS customer.order_customer_m_view;

CREATE MATERIALIZED VIEW customer.order_customer_m_view
TABLESPACE pg_default
AS
 SELECT id,
    username,
    first_name,
    last_name
   FROM customer.customers
WITH DATA;

refresh materialized VIEW customer.order_customer_m_view; -- To create the data from customers table in the materialized view.

DROP function IF EXISTS customer.refresh_order_customer_m_view;

-- Define a function to call it for each data manipulation(Insert, delete, update, truncate) on the customers table.
-- This function will be used to refresh the materialized view of the customers table, while Inserting, updating, deleting, truncating.
CREATE OR replace function customer.refresh_order_customer_m_view()
returns trigger
AS '
BEGIN
    refresh materialized VIEW customer.order_customer_m_view;
    return null;
END;
'  LANGUAGE plpgsql;

DROP trigger IF EXISTS refresh_order_customer_m_view ON customer.customers;

-- Run the predefined function for each data manipulation(Insert, delete, update, truncate) on the customers table.
-- Refresh the materialized view of the customers table, while Inserting, updating, deleting, truncating.
CREATE trigger refresh_order_customer_m_view
after INSERT OR UPDATE OR DELETE OR truncate
ON customer.customers FOR each statement
EXECUTE PROCEDURE customer.refresh_order_customer_m_view();