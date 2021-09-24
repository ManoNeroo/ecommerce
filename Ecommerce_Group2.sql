USE master

GO
-- Tạo DATABASE Ecommerce_Group2
IF(EXISTS (SELECT name FROM sys.databases WHERE name = 'Ecommerce_Group2'))
BEGIN
DROP DATABASE Ecommerce_Group2
END
GO
CREATE DATABASE Ecommerce_Group2

GO
-- Tạo login name 
IF(EXISTS (SELECT name FROM sys.server_principals WHERE name = 'GROUP2_SQL_LOGIN'))
BEGIN
DROP LOGIN GROUP2_SQL_LOGIN
END

GO
CREATE LOGIN GROUP2_SQL_LOGIN WITH PASSWORD = 'Complex!PW@1433',
DEFAULT_DATABASE = Ecommerce_Group2

GO
USE Ecommerce_Group2

GO
--Tạo User 
CREATE USER GROUP2_SQL_USER FOR LOGIN GROUP2_SQL_LOGIN

GO
-- Phân quyền cho User
EXEC sp_addrolemember 'db_owner', 'GROUP2_SQL_USER'

GO

-------------------------------------------------------- TẠO CÁC BẢNG --------------------------------------------------------------

CREATE TABLE user_acc(
	user_id INT IDENTITY PRIMARY KEY,
	user_name VARCHAR(30) NOT NULL UNIQUE,
	user_gender BIT NOT NULL,
	user_avatar NVARCHAR(200) NOT NULL,
	user_password VARCHAR(100) NOT NULL,
	user_first_name NVARCHAR(50) NOT NULL,
	user_last_name NVARCHAR(50) NOT NULL,
	user_phone_number VARCHAR(11) NOT NULL UNIQUE,
	user_status BIT DEFAULT 1
)

GO

CREATE TABLE user_role(
	role_id INT IDENTITY PRIMARY KEY,
	role_name VARCHAR(30) NOT NULL UNIQUE
)

GO

CREATE TABLE user_role_detail(
	role_id INT NOT NULL,
	user_id INT NOT NULL,
	CONSTRAINT user_role_detail_role_id_user_id_PK PRIMARY KEY(role_id, user_id),
	CONSTRAINT user_role_detail_role_id_FK FOREIGN KEY(role_id) REFERENCES user_role(role_id),
	CONSTRAINT user_role_detail_user_id_FK FOREIGN KEY(user_id) REFERENCES user_acc(user_id)
)

GO

CREATE TABLE brand(
	brand_id INT IDENTITY PRIMARY KEY,
	brand_name NVARCHAR(50) NOT NULL UNIQUE,
	brand_logo NVARCHAR(500) NOT NULL,
	brand_status BIT DEFAULT 1,
	created_at DATE DEFAULT GETDATE(),
	updated_at DATE DEFAULT GETDATE()
)

GO

CREATE TABLE category(
	category_id INT IDENTITY PRIMARY KEY,
	category_name NVARCHAR(50) NOT NULL UNIQUE,
	category_logo NVARCHAR(500) NOT NULL,
	category_status BIT DEFAULT 1,
	created_at DATE DEFAULT GETDATE(),
	updated_at DATE DEFAULT GETDATE()
)

GO

CREATE TABLE brand_category(
	brand_id INT NOT NULL,
	category_id INT NOT NULL,
	CONSTRAINT brand_category_brand_id_cate_id_PK PRIMARY KEY(brand_id, category_id),
	CONSTRAINT brand_category_brand_id_FK FOREIGN KEY(brand_id) REFERENCES brand(brand_id),
	CONSTRAINT brand_category_category_id_FK FOREIGN KEY(category_id) REFERENCES category(category_id)
)

GO

CREATE TABLE product(
	product_id VARCHAR(30) PRIMARY KEY,
	brand_id INT NOT NULL,
	category_id INT NOT NULL,
	product_name NVARCHAR(100) NOT NULL UNIQUE,
	product_quanlity INT NOT NULL,
	product_avg_star FLOAT DEFAULT 0,
	product_price INT NOT NULL, -- Giá gốc của sản phẩm
	product_price_off INT, -- Giá được giảm
	product_promo_price INT, -- Giá khuyến mãi (= product_price - product_price_off)
	product_avatar NVARCHAR(500) NOT NULL,
	product_number_vote INT DEFAULT 0,
	product_status BIT DEFAULT 1,
	created_at DATE DEFAULT GETDATE(),
	updated_at DATE DEFAULT GETDATE(),
	CONSTRAINT product_brand_id_FK FOREIGN KEY(brand_id) REFERENCES brand(brand_id),
	CONSTRAINT product_category_id_FK FOREIGN KEY(category_id) REFERENCES category(category_id)
)

GO

CREATE TABLE product_picture(
	picture_id INT IDENTITY PRIMARY KEY,
	picture_name NVARCHAR(500) NOT NULL,
	product_id VARCHAR(30) NOT NULL,
	CONSTRAINT product_picture_product_id_FK FOREIGN KEY(product_id) REFERENCES product(product_id)
)

GO

CREATE TABLE product_article(
	article_id INT IDENTITY PRIMARY KEY,
	article_content NTEXT NOT NULL,
	product_id VARCHAR(30) NOT NULL UNIQUE,
	CONSTRAINT product_article_product_id_FK FOREIGN KEY(product_id) REFERENCES product(product_id)
)

GO

CREATE TABLE product_technical_data(
	technical_data_id INT IDENTITY PRIMARY KEY,
	technical_data_content NTEXT NOT NULL,
	product_id VARCHAR(30) NOT NULL UNIQUE,
	CONSTRAINT product_technical_data_product_id_FK FOREIGN KEY(product_id) REFERENCES product(product_id)
)

GO

CREATE TABLE cart(
	cart_id VARCHAR(30) PRIMARY KEY,
	number_item INT NOT NULL DEFAULT 0,
	user_id INT NOT NULL UNIQUE,
	CONSTRAINT cart_user_id_FK FOREIGN KEY(user_id) REFERENCES user_acc(user_id)
)

GO

CREATE TABLE cart_item(
	cart_id VARCHAR(30) NOT NULL,
	product_id VARCHAR(30) NOT NULL,
	quanlity INT NOT NULL,
	checked BIT DEFAULT 0,
	CONSTRAINT cart_item_cart_id_product_id_PK PRIMARY KEY(cart_id, product_id),
	CONSTRAINT cart_item_cart_id_FK FOREIGN KEY(cart_id) REFERENCES cart(cart_id),
	CONSTRAINT cart_item_product_id_FK FOREIGN KEY(product_id) REFERENCES product(product_id)
)

GO

CREATE TABLE user_order(
	order_id VARCHAR(30) PRIMARY KEY,
	user_id INT NOT NULL,
	order_address NVARCHAR(500) NOT NULL,
	order_description NVARCHAR(500),
	order_status TINYINT NOT NULL DEFAULT 0,
	order_total INT NOT NULL,
	buyer_full_name NVARCHAR(100) NOT NULL,
	buyer_phone_number VARCHAR(11) NOT NULL,
	buyer_gender BIT NOT NULL,
	created_at DATE DEFAULT GETDATE(),
	updated_at DATE DEFAULT GETDATE(),
	CONSTRAINT user_order_user_id_FK FOREIGN KEY(user_id) REFERENCES user_acc(user_id)
)

GO

CREATE TABLE order_item(
	order_id VARCHAR(30) NOT NULL,
	product_id VARCHAR(30) NOT NULL,
	product_name NVARCHAR(100) NOT NULL,
	product_promo_price INT NOT NULL,
	product_price INT NOT NULL,
	product_avatar NVARCHAR(500) NOT NULL,
	quanlity INT NOT NULL,
	created_at DATE DEFAULT GETDATE(),
	CONSTRAINT order_item_order_id_product_id_PK PRIMARY KEY(order_id, product_id),
	CONSTRAINT order_item_product_id_FK FOREIGN KEY(product_id) REFERENCES product(product_id),
	CONSTRAINT order_item_order_id_FK FOREIGN KEY(order_id) REFERENCES user_order(order_id)
)

GO


----------------------------------------------- TẠO TRIGGER ----------------------------------------------------

CREATE TRIGGER utg_afterInsertOrderItem
ON order_item
AFTER INSERT
AS
BEGIN
	DECLARE @quanlity INT
	DECLARE @promoPrice INT
	SELECT @quanlity = quanlity FROM inserted
	SELECT @promoPrice = product_promo_price FROM inserted
	IF @quanlity < 1
	BEGIN
		RAISERROR(N'Số lượng không hợp lệ!', 16, 1)
		ROLLBACK TRANSACTION
	END
	ELSE 
	BEGIN
		UPDATE user_order SET order_total = order_total + (@promoPrice * @quanlity) FROM user_order INNER JOIN inserted ON user_order.order_id = inserted.order_id
	END
END

GO

CREATE TRIGGER utg_afterInsertUser
ON user_acc
AFTER INSERT
AS
BEGIN
	INSERT INTO cart(cart_id, user_id) SELECT user_name, user_id FROM inserted
END

GO

CREATE TRIGGER utg_forInsertCartItem
ON cart_item
FOR INSERT 
AS
BEGIN
	DECLARE @quanlity INT
	SELECT @quanlity = quanlity FROM inserted
	IF @quanlity < 1
	BEGIN
		RAISERROR(N'Số lượng không hợp lệ!', 16, 1)
		ROLLBACK TRANSACTION
	END
	ELSE 
	BEGIN
		UPDATE cart SET number_item = number_item + 1 FROM cart INNER JOIN inserted ON inserted.cart_id = cart.cart_id 
	END
END

GO

CREATE TRIGGER utg_forUpdateCartItem
ON cart_item
FOR UPDATE 
AS
BEGIN
	DECLARE @quanlity INT
	SELECT @quanlity = quanlity FROM inserted
	IF @quanlity < 1
	BEGIN
		RAISERROR(N'Số lượng không hợp lệ!', 16, 1)
		ROLLBACK TRANSACTION
	END
END

GO

CREATE TRIGGER utg_afterDeleteCartItem
ON cart_item
AFTER DELETE
AS
BEGIN
	UPDATE cart SET number_item = number_item - 1 FROM cart INNER JOIN deleted ON deleted.cart_id = cart.cart_id 
END

GO

CREATE TRIGGER utg_forInsertProduct
ON product
FOR INSERT
AS
BEGIN
	DECLARE @productPrice INT
	DECLARE @productPriceOff INT
	DECLARE @productQuanlity INT
	SELECT @productPrice=product_price FROM inserted
	SELECT @productPriceOff=product_price_off FROM inserted
	SELECT @productQuanlity=product_quanlity FROM inserted
	IF @productPrice < 0 OR @productPriceOff < 0
	BEGIN
		RAISERROR(N'Giá sản phẩm không hợp lệ!', 16, 1)
		ROLLBACK TRANSACTION
	END
	ELSE IF @productPrice < @productPriceOff
	BEGIN
		RAISERROR(N'Giá được giảm không hợp lệ!', 16, 1)
		ROLLBACK TRANSACTION
	END
	ELSE IF @productQuanlity < 0
	BEGIN
		RAISERROR(N'Số lượng sản phẩm không hợp lệ!', 16, 1)
		ROLLBACK TRANSACTION
	END
END

GO

CREATE TRIGGER utg_afterInsertProduct
ON product
AFTER INSERT
AS
BEGIN
	DECLARE @productPrice INT
	DECLARE @productPriceOff INT
	SELECT @productPrice=product_price FROM inserted
	SELECT @productPriceOff=product_price_off FROM inserted
	UPDATE product SET product_promo_price = @productPrice - @productPriceOff FROM product INNER JOIN inserted ON inserted.product_id = product.product_id 
END

GO

CREATE TRIGGER utg_forUpdateProduct
ON product
FOR UPDATE
AS
BEGIN
	DECLARE @productPrice INT
	DECLARE @productPriceOff INT
	DECLARE @productQuanlity INT
	SELECT @productPrice=product_price FROM inserted
	SELECT @productPriceOff=product_price_off FROM inserted
	SELECT @productQuanlity=product_quanlity FROM inserted
	IF @productPrice < 0 OR @productPriceOff < 0
	BEGIN
		RAISERROR(N'Giá sản phẩm không hợp lệ!', 16, 1)
		ROLLBACK TRANSACTION
	END
	ELSE IF @productPrice < @productPriceOff
	BEGIN
		RAISERROR(N'Giá được giảm không hợp lệ!', 16, 1)
		ROLLBACK TRANSACTION
	END
	ELSE IF @productQuanlity < 0
	BEGIN
		RAISERROR(N'Số lượng sản phẩm không hợp lệ!', 16, 1)
		ROLLBACK TRANSACTION
	END
END

GO

CREATE TRIGGER utg_afterUpdateProduct
ON product
AFTER UPDATE
AS
BEGIN
	DECLARE @productPrice INT
	DECLARE @productPriceOff INT
	SELECT @productPrice=product_price FROM inserted
	SELECT @productPriceOff=product_price_off FROM inserted
	UPDATE product SET product_promo_price = @productPrice - @productPriceOff FROM product INNER JOIN inserted ON inserted.product_id = product.product_id 
END

GO

------------------------------------------------ INSERT DỮ LIỆU ------------------------------------------------

----- Mật khẩu mặc định: fun123
----- Insert bằng từng lệnh insert, để nhận trigger --------
INSERT INTO user_acc (user_name, user_first_name, user_last_name, user_phone_number, user_password, user_gender, user_avatar) VALUES 
('admin', 'Admin', 'Account', '0986252125', '$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K', 0, '/admin/img/boy.png')
INSERT INTO user_acc (user_name, user_first_name, user_last_name, user_phone_number, user_password, user_gender, user_avatar) VALUES
('manager','Manager','Account','0942532125','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K', 0, '/admin/img/boy.png')
INSERT INTO user_acc (user_name, user_first_name, user_last_name, user_phone_number, user_password, user_gender, user_avatar) VALUES
('employee','Employee','Account','0978254251','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K', 1, '/admin/img/girl.png')
INSERT INTO user_acc (user_name, user_first_name, user_last_name, user_phone_number, user_password, user_gender, user_avatar) VALUES
('guest','Guest','Account','0397458212','$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K', 0, '/admin/img/boy.png')

GO

INSERT INTO user_role (role_name)
VALUES 
('ROLE_ADMIN'),('ROLE_MANAGER'),('ROLE_EMPLOYEE'),('ROLE_CUSTOMER')

GO

INSERT INTO user_role_detail(user_id,role_id)
VALUES 
(1, 1),(2, 2),(3, 3),(4, 4)

INSERT INTO brand(brand_name, brand_logo) VALUES
('Samsung', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340490904217021_Samsung@2x.jpg'),
('Apple (iPhone)', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340490193124614_iPhone-Apple@2x.jpg'),
('OPPO', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340491304997311_Oppo@2x.jpg'),
('Xiaomi', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/6/2/637582325361253577_Xiaomi@2x.jpg'),
('Vivo', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340491898745716_Vivo@2x.jpg'),
('Nokia', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340493755614653_Nokia@2x.jpg'),
('Apple (Macbook)', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340494668267616_Macbook-Apple@2x.jpg'),
('Asus', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340494666861275_Asus.jpg'),
('Dell', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340494666861275_Dell@2x.jpg'),
('Acer', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340494666704995_Acer@2x.jpg'),
('Apple (iPad)', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340494667486283_iPad-Apple@2x.jpg'),
('Masstel', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/8/26/637340491898901930_Masstel@2x.jpg'),
('Lenovo', 'https://images.fpt.shop/unsafe/fit-in/108x40/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/7/14/637618612858518848_Lenovo@2x.jpg')

GO

INSERT INTO category(category_name, category_logo) VALUES
(N'Điện thoại', 'M15.5 1h-8C6.12 1 5 2.12 5 3.5v17C5 21.88 6.12 23 7.5 23h8c1.38 0 2.5-1.12 2.5-2.5v-17C18 2.12 16.88 1 15.5 1zm-4 21c-.83 0-1.5-.67-1.5-1.5s.67-1.5 1.5-1.5 1.5.67 1.5 1.5-.67 1.5-1.5 1.5zm4.5-4H7V4h9v14z'),
(N'Laptop', 'M20 18c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2H4c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2H0v2h24v-2h-4zM4 6h16v10H4V6z'),
(N'Tablet', 'M21 4H3c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h18c1.1 0 1.99-.9 1.99-2L23 6c0-1.1-.9-2-2-2zm-2 14H5V6h14v12z'),
(N'Đồng hồ', 'M20 12c0-2.54-1.19-4.81-3.04-6.27L16 0H8l-.95 5.73C5.19 7.19 4 9.45 4 12s1.19 4.81 3.05 6.27L8 24h8l.96-5.73C18.81 16.81 20 14.54 20 12zM6 12c0-3.31 2.69-6 6-6s6 2.69 6 6-2.69 6-6 6-6-2.69-6-6z'),
(N'Máy ảnh', 'M9 2L7.17 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2h-3.17L15 2H9zm3 15c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5z'),
(N'Tủ lạnh', 'M18 2.01L6 2c-1.1 0-2 .89-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.11-.9-1.99-2-1.99zM18 20H6v-9.02h12V20zm0-11H6V4h12v5zM8 5h2v3H8zm0 7h2v5H8z'),
(N'Máy giặt', 'M9.17 16.83c1.56 1.56 4.1 1.56 5.66 0 1.56-1.56 1.56-4.1 0-5.66l-5.66 5.66zM18 2.01L6 2c-1.11 0-2 .89-2 2v16c0 1.11.89 2 2 2h12c1.11 0 2-.89 2-2V4c0-1.11-.89-1.99-2-1.99zM10 4c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zM7 4c.55 0 1 .45 1 1s-.45 1-1 1-1-.45-1-1 .45-1 1-1zm5 16c-3.31 0-6-2.69-6-6s2.69-6 6-6 6 2.69 6 6-2.69 6-6 6z'),
(N'TV', 'M21 3H3c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h5v2h8v-2h5c1.1 0 1.99-.9 1.99-2L23 5c0-1.1-.9-2-2-2zm0 14H3V5h18v12z')

GO

INSERT INTO brand_category(brand_id, category_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 2), (8, 2), (9, 2), (10, 2),
(11, 3), (12, 1), (12, 3), (13, 2), (13, 3)

GO

----- Insert bằng từng lệnh insert, để nhận trigger --------
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('Eu8778hdKL', 1, 1, 'Samsung Galaxy S21 Ultra 5G 128GB', 100, 30990000, 5000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/1/14/637462639794639518_ss-s21-ultra-128-dd.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('c6Yf78hdKL', 1, 1, 'Samsung Galaxy Z Fold3 5G 256GB', 100, 41990000, 0, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/8/13/637644576020854740_samsung-galaxy-z-fold3-xanh-dd-bh2nam.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('May774RWKL', 1, 1, 'Samsung Galaxy Z Flip3 5G 128GB', 100, 24990000, 0, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/8/13/637644576020542222_samsung-galaxy-z-flip3-5g-vang-dd-bh2nam.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('NPdf78hdKL', 2, 1, 'iPhone 12 Pro Max 128GB', 100, 32999000, 2000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/2/3/637479614485943996_ip-12-pro-max-dd-2nam.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('NPdf3WQbKL', 2, 1, 'iPhone 12 Mini 64GB', 100, 18999000, 2000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/4/22/637546879735318274_ip-12-mini-dd-a2.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5cH78hdKL', 3, 1, 'OPPO Find X3 Pro 5G', 100, 26990000, 5000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/5/14/637565961688766276_oppo-find-x3-pro-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('K8YTB8hdKL', 3, 1, 'OPPO A94 8GB-128GB', 100, 7690000, 700000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/5/6/637558899292415149_oppo-a94-bac-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5df78hdKL', 4, 1, 'Xiaomi Mi 11 5G 8GB-256GB', 100, 21990000, 5000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/4/16/637541698147579202_xiaomi-mi-11-xanh-dd-1.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5dkT64dKL', 4, 1, 'Xiaomi Mi 10T Pro 5G 8GB-128GB', 100, 11990000, 1000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/10/1/637371441443932640_xiaomi-mi-10t-pro-bac-dd-bh.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('MuY5OLhdKL', 4, 1, 'Xiaomi Redmi Note 10 Pro 8GB-128GB', 100, 7490000, 400000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/4/16/637541693612681073_xiaomi-redmi-note-10-pro-xam-dd-1.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('8olf78hdKL', 5, 1, 'Vivo Y72 5G 8GB-128GB', 100, 7990000, 500000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/4/8/637534872939311754_vivo-y72-xanh-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5df79Ut4L', 5, 1, 'Vivo Y53s 8GB+3GB - 128GB', 100, 6990000, 700000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/6/9/637588529466974447_vivo-y53s-xanhden-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5df78hdO0', 5, 1, 'Vivo V20 SE 8GB-128GB', 100, 7190000, 1190000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/11/2/637399139025892576_vivo-v20-se-xanh-DD.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5dki8hdKL', 6, 1, 'Nokia 5.4 4GB -128GB', 100, 4190000, 1000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/12/25/637445014345646352_nokia-5-4-dd.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('Eoif78hdKL', 7, 2, 'MacBook Pro 16" 2019 Touch Bar 2.6GHz Core i7', 100, 59999000, 0, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2019/11/18/637096956683492064_MBP-16-touch-dd.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5dfT9hdKL', 7, 2, 'MacBook Pro 13" 2020 Touch Bar 2.0GHz Core i5', 100, 47990000, 1000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/6/5/637269501975415139_mb-pro-13-2020-xam-dd.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5dfCChdKL', 7, 2, 'MacBook Air 13" 2020 M1 256GB', 100, 28999000, 1500000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/11/12/637407970062806725_mba-2020-gold-dd.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5df78CsKL', 8, 2, 'ASUS Gaming Zephyrus GA401QEC-K2064T/R9-5900HS', 100, 49990000, 2000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/9/8/637667072098789248_asus-gaming-zephyrus-ga401qec-xam-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5df78CRKL', 8, 2, 'Asus FLow Gaming GV301QC-K6052T/R9-5900HX', 100, 39990000, 2000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/9/8/637667013001484566_asus-flow-gaming-gv301-den-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5dfKMhdKL', 8, 2, 'Asus Zenbook Flip UX363EA-HP163T/i7-1165G7', 100, 33990000, 2000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/2/18/637492353319956785_asus-ux363-xam-dd-oled.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5df78NMKL', 9, 2, 'Dell XPS 13 9310 i5 1135G7', 100, 41890000, 2100000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/3/22/637520284129451058_dell-xps-13-9310-i5-evo-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5df68TdKL', 9, 2, 'Dell Gaming G5 15 Core i7 10750H', 100, 39990000, 2000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/2/19/637493278502148750_dell-g5-15-5500-den-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5CC78hdKL', 9, 2, 'Dell Gaming G7 15 7500 i7 10750H', 100, 38990000, 1000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/6/30/637606696162332195_dell-gaming-g7-15-den-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5LY78hdKL', 10, 2, 'ACER Predator Gaming PH315-54-78W5/i7-11800H', 100, 32990000, 2000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/7/23/637626626877470399_acer-predator-gaming-ph315-54-den-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5RE78hdKL', 10, 2, 'Acer Nitro Gaming AN515-57-5831/i5-11400H', 100, 32990000, 2000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/7/5/637610884162706659_acer-nitro-gaming-an515-57-den-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E5df7TEdKL', 10, 2, 'Acer Nitro Gaming AN515-45-R0B6/R7-5800H', 100, 32990000, 2200000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/240x215/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/7/20/637624042770963425_acer-nitro-gaming-an515-45-dd-rtx-3060.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E98loTEdKL', 11, 3, 'iPad 10.2 2020 Wi-Fi + Cellular 32GB', 100, 13999000, 200000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/9/20/637361593919986930_ipad-2020-10-2-wi-fi-cel-dd.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('N6kUSTEdKL', 11, 3, 'iPad Pro 11 2021 M1 Wi-Fi 5G 1TB', 100, 46990000, 0, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/4/21/637546036307685719_ipad-pro-11-cell-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('UR3fdvEdKL', 11, 3, 'iPad Pro 12.9 2021 M1 Wi-Fi 5G 128GB', 100, 34999000, 0, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/4/21/637546068952545209_ipad-pro-12-9-cell-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('Y4lNEdKL3N', 1, 3, 'Samsung Galaxy Tab S7 FE', 100, 13990000, 2000000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/7/1/637607328871408633_samsung-galaxy-tab-s7-fe-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('E68Y6EdKL3', 1, 3, 'Samsung Galaxy Tab A7 (2020)', 100, 7990000, 800000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/9/11/637354307486615605_ss-tab-a7-dd.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('KT45dTEdKL', 1, 3, 'Samsung Galaxy Tab A7 Lite', 100, 4490000, 0, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/5/27/637577199955105403_ss-tab-a7-lite-bac-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('YTr56TEdKL', 12, 3, 'Masstel Tab 10 Ultra', 100, 3790000, 300000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2020/10/28/637394760405298535_masstel-tab-10-ultra-vang-dd.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('Nmb7oTEdKL', 12, 3, 'Masstel Tab 10 4G', 100, 3490000, 400000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/1/30/637476240646298069_masstel-tab-10-4g-den-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('rEb8dTEdKL', 12, 3, 'Masstel Tab 8 Plus', 100, 1890000, 300000, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2019/2/19/636861635846520694_masstel-tab-8-plus-daidien.png')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('AN65oTEdKL', 13, 3, 'Lenovo Tab M10 64GB (Gen2)', 100, 5690000, 0, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/7/14/637618697974134752_lenovo-tab-m10-gen-2-xam-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('CMhloTEdKL', 13, 3, 'Lenovo Tab M8-Gen 2', 100, 3790000, 0, 0, 0, 'https://images.fpt.shop/unsafe/fit-in/214x214/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2021/7/14/637618700326132189_lenovo-tab-m8-xam-dd.jpg')
INSERT INTO product(product_id, brand_id, category_id, product_name, product_quanlity, product_price, product_price_off, product_avg_star, product_number_vote, product_avatar) VALUES
('BuYt0TEdKL', 13, 3, 'Lenovo Tab M7', 100, 2990000, 0, 0, 0, 'https://cdn.tgdd.vn/Products/Images/522/235363/lenovo-tab-m7-600x600-1-600x600.jpg')

GO

---------------------------------------------- TẠO FUNCTION -----------------------------------------------------

CREATE FUNCTION [dbo].[uf_nonUnicode](@inputVar NVARCHAR(MAX) )
RETURNS NVARCHAR(MAX)
AS
BEGIN    
    IF (@inputVar IS NULL OR @inputVar = '')  RETURN ''
   
    DECLARE @RT NVARCHAR(MAX)
    DECLARE @SIGN_CHARS NCHAR(256)
    DECLARE @UNSIGN_CHARS NCHAR (256)
 
    SET @SIGN_CHARS = N'ăâđêôơưàảãạáằẳẵặắầẩẫậấèẻẽẹéềểễệếìỉĩịíòỏõọóồổỗộốờởỡợớùủũụúừửữựứỳỷỹỵýĂÂĐÊÔƠƯÀẢÃẠÁẰẲẴẶẮẦẨẪẬẤÈẺẼẸÉỀỂỄỆẾÌỈĨỊÍÒỎÕỌÓỒỔỖỘỐỜỞỠỢỚÙỦŨỤÚỪỬỮỰỨỲỶỸỴÝ' + NCHAR(272) + NCHAR(208)
    SET @UNSIGN_CHARS = N'aadeoouaaaaaaaaaaaaaaaeeeeeeeeeeiiiiiooooooooooooooouuuuuuuuuuyyyyyAADEOOUAAAAAAAAAAAAAAAEEEEEEEEEEIIIIIOOOOOOOOOOOOOOOUUUUUUUUUUYYYYYDD'
 
    DECLARE @COUNTER int
    DECLARE @COUNTER1 int
   
    SET @COUNTER = 1
    WHILE (@COUNTER <= LEN(@inputVar))
    BEGIN  
        SET @COUNTER1 = 1
        WHILE (@COUNTER1 <= LEN(@SIGN_CHARS) + 1)
        BEGIN
            IF UNICODE(SUBSTRING(@SIGN_CHARS, @COUNTER1,1)) = UNICODE(SUBSTRING(@inputVar,@COUNTER ,1))
            BEGIN          
                IF @COUNTER = 1
                    SET @inputVar = SUBSTRING(@UNSIGN_CHARS, @COUNTER1,1) + SUBSTRING(@inputVar, @COUNTER+1,LEN(@inputVar)-1)      
                ELSE
                    SET @inputVar = SUBSTRING(@inputVar, 1, @COUNTER-1) +SUBSTRING(@UNSIGN_CHARS, @COUNTER1,1) + SUBSTRING(@inputVar, @COUNTER+1,LEN(@inputVar)- @COUNTER)
                BREAK
            END
            SET @COUNTER1 = @COUNTER1 +1
        END
        SET @COUNTER = @COUNTER +1
    END
    -- SET @inputVar = replace(@inputVar,' ','-')
    RETURN @inputVar
END

GO

---------------------------------------------- TẠO THỦ TỤC -------------------------------------------------------

CREATE PROC usp_selectCategoryByOffsetLimit(@offset INT, @limit INT, @name NVARCHAR(100), @status BIT)
AS
BEGIN
IF @name IS NULL AND @status IS NULL
BEGIN
	WITH category_cte AS 
	(SELECT *, ROW_NUMBER() OVER (ORDER BY created_at DESC) AS rowNum FROM category)
	SELECT category_id, category_name, category_logo, category_status, created_at, updated_at FROM category_cte WHERE rowNum >= @offset AND rowNum < @offset + @limit
END
ELSE IF @name IS NOT NULL AND @status IS NULL
BEGIN
	WITH category_cte AS 
	(SELECT *, ROW_NUMBER() OVER (ORDER BY created_at DESC) AS rowNum FROM category WHERE [dbo].[uf_nonUnicode](category_name) LIKE '%' + [dbo].[uf_nonUnicode](@name) +'%')
	SELECT category_id, category_name, category_logo, category_status, created_at, updated_at FROM category_cte WHERE rowNum >= @offset AND rowNum < @offset + @limit
END
ELSE IF @name IS NULL AND @status IS NOT NULL
BEGIN
	WITH category_cte AS 
	(SELECT *, ROW_NUMBER() OVER (ORDER BY created_at DESC) AS rowNum FROM category WHERE category_status = @status)
	SELECT category_id, category_name, category_logo, category_status, created_at, updated_at FROM category_cte WHERE rowNum >= @offset AND rowNum < @offset + @limit
END
ELSE IF @name IS NOT NULL AND @status IS NOT NULL
BEGIN
	WITH category_cte AS 
	(SELECT *, ROW_NUMBER() OVER (ORDER BY created_at DESC) AS rowNum FROM category WHERE [dbo].[uf_nonUnicode](category_name) LIKE '%' + [dbo].[uf_nonUnicode](@name) +'%' AND category_status=@status)
	SELECT category_id, category_name, category_logo, category_status, created_at, updated_at FROM category_cte WHERE rowNum >= @offset AND rowNum < @offset + @limit
END
END

GO

CREATE PROC usp_selectCountCategory(@name NVARCHAR(100), @status BIT, @count INT OUTPUT)
AS
BEGIN
IF @name IS NULL AND @status IS NULL
BEGIN
	SELECT @count = COUNT(category_id) FROM category
END
ELSE IF @name IS NOT NULL AND @status IS NULL
BEGIN
	SELECT @count = COUNT(category_id) FROM category WHERE [dbo].[uf_nonUnicode](category_name) LIKE '%' + [dbo].[uf_nonUnicode](@name) +'%'
END
ELSE IF @name IS NULL AND @status IS NOT NULL
BEGIN
	SELECT @count = COUNT(category_id) FROM category WHERE category_status = @status
END
ELSE IF @name IS NOT NULL AND @status IS NOT NULL
BEGIN
	SELECT @count = COUNT(category_id) FROM category WHERE [dbo].[uf_nonUnicode](category_name) LIKE '%' + [dbo].[uf_nonUnicode](@name) +'%' AND category_status=@status
END
END

GO

CREATE PROC usp_insertCategory(@name NVARCHAR(100), @status BIT, @logo NVARCHAR(500))
AS
BEGIN
INSERT INTO category(category_name, category_status, category_logo) VALUES
(@name, @status, @logo)
END

GO

CREATE PROC usp_updateCategory(@id INT, @name NVARCHAR(100), @status BIT, @logo NVARCHAR(500))
AS
BEGIN
UPDATE category SET category_name=@name, category_status=@status, category_logo=@logo, updated_at = GETDATE()
WHERE category_id=@id
END

GO

CREATE PROC usp_toggleCategoryStatus(@id INT, @status BIT)
AS
UPDATE category SET category_status=@status, updated_at = GETDATE()
WHERE category_id=@id

GO

CREATE PROC usp_selectBrandByOffsetLimit(@offset INT, @limit INT, @name NVARCHAR(100), @status BIT)
AS
BEGIN
IF @name IS NULL AND @status IS NULL
BEGIN
	WITH brand_cte AS 
	(SELECT *, ROW_NUMBER() OVER (ORDER BY created_at DESC) AS rowNum FROM brand)
	SELECT brand_id, brand_name, brand_logo, brand_status, created_at, updated_at FROM brand_cte WHERE rowNum >= @offset AND rowNum < @offset + @limit
END
ELSE IF @name IS NOT NULL AND @status IS NULL
BEGIN
	WITH brand_cte AS 
	(SELECT *, ROW_NUMBER() OVER (ORDER BY created_at DESC) AS rowNum FROM brand WHERE [dbo].[uf_nonUnicode](brand_name) LIKE '%' + [dbo].[uf_nonUnicode](@name) +'%')
	SELECT brand_id, brand_name, brand_logo, brand_status, created_at, updated_at FROM brand_cte WHERE rowNum >= @offset AND rowNum < @offset + @limit
END
ELSE IF @name IS NULL AND @status IS NOT NULL
BEGIN
	WITH brand_cte AS 
	(SELECT *, ROW_NUMBER() OVER (ORDER BY created_at DESC) AS rowNum FROM brand WHERE brand_status = @status)
	SELECT brand_id, brand_name, brand_logo, brand_status, created_at, updated_at FROM brand_cte WHERE rowNum >= @offset AND rowNum < @offset + @limit
END
ELSE IF @name IS NOT NULL AND @status IS NOT NULL
BEGIN
	WITH brand_cte AS 
	(SELECT *, ROW_NUMBER() OVER (ORDER BY created_at DESC) AS rowNum FROM brand WHERE [dbo].[uf_nonUnicode](brand_name) LIKE '%' + [dbo].[uf_nonUnicode](@name) +'%' AND brand_status=@status)
	SELECT brand_id, brand_name, brand_logo, brand_status, created_at, updated_at FROM brand_cte WHERE rowNum >= @offset AND rowNum < @offset + @limit
END
END

GO

CREATE PROC usp_selectCountBrand(@name NVARCHAR(100), @status BIT, @count INT OUTPUT)
AS
BEGIN
IF @name IS NULL AND @status IS NULL
BEGIN
	SELECT @count = COUNT(brand_id) FROM brand
END
ELSE IF @name IS NOT NULL AND @status IS NULL
BEGIN
	SELECT @count = COUNT(brand_id) FROM brand WHERE [dbo].[uf_nonUnicode](brand_name) LIKE '%' + [dbo].[uf_nonUnicode](@name) +'%'
END
ELSE IF @name IS NULL AND @status IS NOT NULL
BEGIN
	SELECT @count = COUNT(brand_id) FROM brand WHERE brand_status = @status
END
ELSE IF @name IS NOT NULL AND @status IS NOT NULL
BEGIN
	SELECT @count = COUNT(brand_id) FROM brand WHERE [dbo].[uf_nonUnicode](brand_name) LIKE '%' + [dbo].[uf_nonUnicode](@name) +'%' AND brand_status=@status
END
END

GO

CREATE PROC usp_toggleBrandStatus(@id INT, @status BIT)
AS
UPDATE brand SET brand_status=@status, updated_at = GETDATE()
WHERE brand_id=@id

GO

CREATE PROC usp_selectNumberCartItem(@cartId VARCHAR(30), @numberItem INT OUTPUT)
AS
SELECT @numberItem = number_item FROM cart WHERE cart_id = @cartId

GO

CREATE PROC usp_insertCartItem(@cartId VARCHAR(30), @productId VARCHAR(30), @quanlity INT, @checked BIT)
AS
INSERT INTO cart_item(cart_id, product_id, quanlity, checked) VALUES(@cartId, @productId, @quanlity, @checked);

GO

CREATE PROC usp_updateCartItem(@cartId VARCHAR(30), @productId VARCHAR(30), @quanlity INT, @checked BIT)
AS
UPDATE cart_item SET quanlity = @quanlity, checked = @checked WHERE cart_id = @cartId AND product_id = @productId

GO

CREATE PROC usp_deleteCartItem(@cartId VARCHAR(30), @productId VARCHAR(30))
AS
DELETE FROM cart_item WHERE cart_id = @cartId AND product_id = @productId

GO

CREATE PROC usp_updateOrderStatus(@orderId VARCHAR(30), @status TINYINT)
AS
UPDATE user_order SET order_status=@status WHERE order_id=@orderId


GO

CREATE PROC usp_selectByCartIdAndProductId(@cartId VARCHAR(30), @productId VARCHAR(30))
AS
SELECT * FROM cart_item WHERE cart_id=@cartId AND product_id=@productId

GO