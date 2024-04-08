-- Create store table
CREATE TABLE store (
                       store_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       store_uuid VARCHAR(255) UNIQUE,
                       store_name VARCHAR(255),
                       store_address VARCHAR(255)
);

-- Create user table
CREATE TABLE user (
                      user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      user_uuid VARCHAR(255) UNIQUE,
                      store_id BIGINT,
                      first_name VARCHAR(255),
                      last_name VARCHAR(255),
                      email VARCHAR(255),
                      username VARCHAR(255),
                      password VARCHAR(255),
                      created_at TIMESTAMP,
                      created_by VARCHAR(255),
                      FOREIGN KEY (store_id) REFERENCES store(store_id)
);

-- Create product_category table
CREATE TABLE product_category (
                                  category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  category_name VARCHAR(255)
);

-- Create product table
CREATE TABLE product (
                         product_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         product_uuid VARCHAR(255) UNIQUE,
                         product_name VARCHAR(255),
                         category_id BIGINT,
                         brand VARCHAR(255),
                         hsn_code VARCHAR(255),
                         created_by VARCHAR(255),
                         updated_by VARCHAR(255),
                         created_at TIMESTAMP,
                         updated_at TIMESTAMP,
                         FOREIGN KEY (category_id) REFERENCES product_category(category_id)
);

-- Create store_inventory table
CREATE TABLE store_inventory (
                                 inventory_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 store_uuid VARCHAR(255),
                                 product_id BIGINT,
                                 available_qty INT,
                                 price DOUBLE,
                                 status VARCHAR(255),
                                 minimum_maintain_qty INT,
                                 FOREIGN KEY (store_uuid) REFERENCES store(store_uuid),
                                 FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- Create order table
CREATE TABLE order_table (
                             order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             order_uuid VARCHAR(255) UNIQUE,
                             store_uuid VARCHAR(255),
                             total_amount DOUBLE,
                             customer_name VARCHAR(255),
                             customer_contact VARCHAR(255),
                             created_at TIMESTAMP,
                             created_by VARCHAR(255),
                             FOREIGN KEY (store_uuid) REFERENCES store(store_uuid)
);

-- Create order_product table
CREATE TABLE order_product (
                               order_id BIGINT,
                               product_uuid VARCHAR(255),
                               quantity INT,
                               price DOUBLE,
                               FOREIGN KEY (order_id) REFERENCES order_table(order_id),
                               FOREIGN KEY (product_uuid) REFERENCES product(product_uuid),
                               PRIMARY KEY (order_id, product_uuid)
);