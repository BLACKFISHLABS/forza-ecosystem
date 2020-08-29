SELECT *
FROM app_company
WHERE id NOT IN (1, 994);

SELECT *
FROM app_address
WHERE id NOT IN (1, 995);

SELECT *
FROM app_phone
WHERE id NOT IN (1, 997);

SELECT *
FROM app_contact
WHERE id NOT IN (1, 996);

SELECT *
FROM app_customer
WHERE company_id NOT IN (1, 994);

SELECT *
FROM app_order
WHERE company_id NOT IN (1, 994);

SELECT *
FROM app_order_item
WHERE order_id IN (
  SELECT id
  FROM app_order
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_payment
WHERE company_id NOT IN (1, 994);

SELECT *
FROM app_price_table
WHERE company_id NOT IN (1, 994);

SELECT *
FROM app_price_table_item
WHERE pricetable_id IN (
  SELECT id
  FROM app_price_table
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_product
WHERE company_id NOT IN (1, 994);

SELECT *
FROM app_route
WHERE company_id NOT IN (1, 994);

SELECT *
FROM app_route_customer
WHERE route_id IN (
  SELECT id
  FROM app_route
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_salesman_company
WHERE company_id NOT IN (1, 994);

SELECT *
FROM app_salesman
WHERE id IN (
  SELECT salesman_id
  FROM app_salesman_company
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_user
WHERE company_id NOT IN (1, 994);

SELECT *
FROM app_user_permission
WHERE user_id IN (
  SELECT id
  FROM app_user
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_vehicle
WHERE salesman_id IN (
  SELECT salesman_id
  FROM app_salesman_company
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_charge
WHERE company_id NOT IN (1, 994);

SELECT *
FROM app_charge_vehicle
WHERE id IN (
  SELECT vehicle_id
  FROM app_charge
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_charge_route
WHERE charge_id IN (
  SELECT id
  FROM app_charge
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_charge_customer
WHERE route_id IN (
  SELECT id
  FROM app_charge_route
  WHERE charge_id IN (
    SELECT id
    FROM app_charge
    WHERE company_id NOT IN (1, 994)));

SELECT *
FROM app_charge_block
WHERE id IN (
  SELECT charge_block_id
  FROM app_charge
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_charge_table_price
WHERE charge_block_id IN (
  SELECT charge_block_id
  FROM app_charge
  WHERE company_id NOT IN (1, 994));

SELECT *
FROM app_charge_item
WHERE charge_table_price_id IN (
  SELECT id
  FROM app_charge_table_price
  WHERE charge_block_id IN (
    SELECT charge_block_id
    FROM app_charge
    WHERE company_id NOT IN (1, 994)));

SELECT *
FROM app_charge_product
WHERE id IN (
  SELECT id
  FROM app_charge_item
  WHERE charge_table_price_id IN (
    SELECT id
    FROM app_charge_table_price
    WHERE charge_block_id IN (
      SELECT charge_block_id
      FROM app_charge
      WHERE company_id NOT IN (1, 994))));