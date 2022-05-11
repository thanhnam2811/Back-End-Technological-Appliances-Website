-- DROP DB
USE master
GO

DROP DATABASE IF EXISTS db_technological_appliances
GO

-- CREATE DB
CREATE DATABASE db_technological_appliances
GO

USE db_technological_appliances
GO
-------------------- Table --------------------
CREATE TABLE Users
(
    Username    VARCHAR(40) PRIMARY KEY,
    Name        NVARCHAR(100),
    Email       VARCHAR(100) UNIQUE,
    PhoneNumber VARCHAR(25) UNIQUE,
    DateOfBirth DATE,
    Address     NVARCHAR(200) NOT NULL,
    Gender      BIT
)
GO
CREATE TABLE Account
(
    Username VARCHAR(40) FOREIGN KEY REFERENCES dbo.Users (Username)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    Password VARCHAR(60) NOT NULL,
    PRIMARY KEY (Username),
    Role     INTEGER NOT NULL DEFAULT 0
)
GO
CREATE TABLE Categories
(
    CategoryId VARCHAR(20) PRIMARY KEY,
    Name       NVARCHAR(100) NOT NULL
)
GO
CREATE TABLE Brands
(
    BrandId  VARCHAR(20) PRIMARY KEY,
    Name     NVARCHAR(100) NOT NULL,
    Email    VARCHAR(100)  NOT NULL,
    Logo     NVARCHAR(200),
    Location NVARCHAR(200)
)
GO
CREATE TABLE Products
(
    ProductId   VARCHAR(20) PRIMARY KEY,
    Name        NVARCHAR(100),
    CategoryId  VARCHAR(20) FOREIGN KEY REFERENCES dbo.Categories (CategoryId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    BrandId     VARCHAR(20) FOREIGN KEY REFERENCES dbo.Brands (BrandId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    Image       NVARCHAR(MAX),
    Quantity    INTEGER,
    SaleDate    DATETIME DEFAULT GETDATE(),
    RAM         INTEGER,
    ROM         VARCHAR(20),
    FrontCam    NVARCHAR(200),
    BackCam     NVARCHAR(200),
    OS          NVARCHAR(100),
    Screen      NVARCHAR(200),
    CPU         VARCHAR(50),
    Battery     VARCHAR(100),
    Weight      VARCHAR(20),
    VGA         NVARCHAR(100),
    Description nvarchar(max),
    Price       FLOAT NOT NULL
)
GO
CREATE TABLE Reviews
(
    ReviewId  VARCHAR(20) PRIMARY KEY,
    Username  VARCHAR(40) FOREIGN KEY REFERENCES dbo.Users (Username)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    ProductId VARCHAR(20) FOREIGN KEY REFERENCES dbo.Products (ProductId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    Content   NVARCHAR(max),
    Rate      INTEGER,
    Time      DATETIME
)
GO
CREATE TABLE Deliveries
(
    DeliveryId  VARCHAR(20) PRIMARY KEY,
    Name        NVARCHAR(100) NOT NULL,
    Email       VARCHAR(100)  NOT NULL,
    PhoneNumber VARCHAR(20)   NOT NULL,
    Location    NVARCHAR(200)
)
GO
CREATE TABLE Coupons
(
    CouponId      VARCHAR(40) PRIMARY KEY,
    Discount      FLOAT NOT NULL,
    ExpiredTime   DATETIME,
    EffectiveTime DATETIME,
    Description   NVARCHAR(300)
)
GO
CREATE TABLE Orders
(
    OrderId       VARCHAR(20) PRIMARY KEY,
    Username      VARCHAR(40) FOREIGN KEY REFERENCES dbo.Users (Username)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    Name          NVARCHAR(100) NOT NULL,
    Address       NVARCHAR(200) NOT NULL,
    PhoneNumber   VARCHAR(20)   NOT NULL,
    PurchaseDate  DATETIME,
    TotalPrices   FLOAT,
    DeliveryId    VARCHAR(20) FOREIGN KEY REFERENCES dbo.Deliveries (DeliveryId)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    Status        NVARCHAR(200),
    CouponId      VARCHAR(40) FOREIGN KEY REFERENCES dbo.Coupons (CouponId)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    DiscountPrice FLOAT
)
GO
CREATE TABLE OrderDetails
(
    OrderId    VARCHAR(20) FOREIGN KEY REFERENCES dbo.Orders (OrderId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    ProductId  VARCHAR(20) FOREIGN KEY REFERENCES dbo.Products (ProductId)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    Quantity   INTEGER NOT NULL,
    Price      FLOAT,
    TotalPrice FLOAT,
    PRIMARY KEY (OrderId, ProductId)
)
GO
CREATE TABLE CartDetails
(
    Username  VARCHAR(40) FOREIGN KEY REFERENCES dbo.Users (Username)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    ProductId VARCHAR(20) FOREIGN KEY REFERENCES dbo.Products (ProductId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    Quantity  INTEGER NOT NULL,
    PRIMARY KEY (Username, ProductId)
)
GO
-------------------- Stored Procedure --------------------

-- 1 User
-- 1.1 GetByUserName
DROP PROC IF EXISTS sp_getByUserName
GO
CREATE PROC sp_getByUserName(@Username VARCHAR(40))
AS
SELECT *
FROM dbo.Users
WHERE Username = @Username
GO
-- 1.2 InsertUser
DROP PROC IF EXISTS sp_insertUser
GO
CREATE PROC sp_insertUser(@Username VARCHAR(40), @Name NVARCHAR(100), @Email VARCHAR(100), @PhoneNumber VARCHAR(25),
                          @DateOfBirth DATE, @Address NVARCHAR(200), @Gender BIT)
AS
INSERT INTO dbo.Users(Username, Name, Email, PhoneNumber, DateOfBirth, Address, Gender)
VALUES (@Username, @Name, @Email, @PhoneNumber, @DateOfBirth, @Address, @Gender)
GO
-- 1.3 UpdateRole
-- 1.4 UpdateUser
DROP PROC IF EXISTS sp_updateUser
GO
CREATE PROC sp_updateUser(@Username VARCHAR(40), @Name NVARCHAR(100), @Email VARCHAR(100), @PhoneNumber VARCHAR(25),
                          @DateOfBirth DATE, @Address NVARCHAR(200), @Gender BIT)
AS
UPDATE dbo.Users
SET Name=@Name,
    Email=@Email,
    PhoneNumber=@PhoneNumber,
    DateOfBirth=@DateOfBirth,
    Address=@Address,
    Gender=@Gender
WHERE Username = @Username
GO
-- 1.5 DeleteUser
DROP PROC IF EXISTS sp_deleteUser
GO
CREATE PROC sp_deleteUser(@Username VARCHAR(40))
AS
DELETE dbo.Users
WHERE Username = @Username
GO

-- 2 Account
-- 2.1 GetByUserName
DROP PROC IF EXISTS sp_getAccount
GO
CREATE PROC sp_getAccount(@Username VARCHAR(40), @Password VARCHAR(40))
AS
SELECT Username
FROM dbo.Account
WHERE Username = @Username
  AND Password = @Password
GO
-- 2.2 InsertAccount
DROP PROC IF EXISTS sp_insertAccount
GO
CREATE PROC sp_insertAccount(@Username VARCHAR(40), @Password VARCHAR(40))
AS
INSERT INTO dbo.Account(Username, Password)
VALUES (@Username, @Password)
GO
-- 2.3 UpdateAccount
DROP PROC IF EXISTS sp_updateAccount
GO
CREATE PROC sp_updateAccount(@Username VARCHAR(40), @Password VARCHAR(40))
AS
UPDATE dbo.Account
SET Password=@Password
WHERE Username = @Username
GO
-- 2.4 DeleteAccount
DROP PROC IF EXISTS sp_deleteAccount
GO
CREATE PROC sp_deleteAccount(@Username VARCHAR(40))
AS
DELETE dbo.Account
WHERE Username = @Username
GO
-- 3 Categories
-- 3.1 GetAll
DROP PROC IF EXISTS sp_getAllCategories
GO
CREATE PROC sp_getAllCategories
AS
SELECT *
FROM dbo.Categories
GO
-- 3.2 GetById
DROP PROC IF EXISTS sp_getByIdCategoty
GO
CREATE PROC sp_getByIdCategoty(@CategoryId VARCHAR(20))
AS
SELECT *
FROM dbo.Categories
WHERE CategoryId = @CategoryId
GO
-- 3.3 InsertCategory
DROP PROC IF EXISTS sp_insertCategory
GO
CREATE PROC sp_insertCategory(@CategoryId VARCHAR(20), @Name NVARCHAR(100))
AS
INSERT INTO dbo.Categories(CategoryId, Name)
VALUES (@CategoryId, @Name)
GO
-- 3.4 UpdateCategory
DROP PROC IF EXISTS sp_updateCategory
GO
CREATE PROC sp_updateCategory(@CategoryId VARCHAR(20), @Name NVARCHAR(100))
AS
UPDATE dbo.Categories
SET Name = @Name
WHERE CategoryId = @CategoryId
GO
-- 3.5 DeleteCategory
DROP PROC IF EXISTS sp_deleteCategory
GO
CREATE PROC sp_deleteCategory(@CategoryId VARCHAR(20))
AS
DELETE dbo.Categories
WHERE CategoryId = @CategoryId
GO

-- 4 Brands
-- 4.1 GetAll
DROP PROC IF EXISTS sp_getAllBrands
GO
CREATE PROC sp_getAllBrands
AS
SELECT *
FROM dbo.Brands
GO
-- 4.2 GetById
DROP PROC IF EXISTS sp_getByIdBrand
GO
CREATE PROC sp_getByIdBrand(@BrandId VARCHAR(20))
AS
SELECT *
FROM dbo.Brands
WHERE BrandId = @BrandId
GO
-- 4.3 InsertBrand
DROP PROC IF EXISTS sp_insertBrand
GO
CREATE PROC sp_insertBrand(@BrandId VARCHAR(20), @Name NVARCHAR(100), @Email VARCHAR(100), @Logo NVARCHAR(200),
                           @Location NVARCHAR(200))
AS
INSERT INTO dbo.Brands(BrandId, Name, Email, Logo, Location)
VALUES (@BrandId, @Name, @Email, @Logo, @Location)
GO
-- 4.4 UpdateBrand
DROP PROC IF EXISTS sp_updateBrand
GO
CREATE PROC sp_updateBrand(@BrandId VARCHAR(20), @Name NVARCHAR(100), @Email VARCHAR(100), @Logo NVARCHAR(200),
                           @Location NVARCHAR(200))
AS
UPDATE dbo.Brands
SET Name=@Name,
    Email=@Email,
    Logo=@Logo,
    Location=@Location
WHERE BrandId = @BrandId
GO
-- 4.5 DeleteBrand
DROP PROC IF EXISTS sp_deleteBrand
GO
CREATE PROC sp_deleteBrand(@BrandId VARCHAR(20))
AS
DELETE dbo.Brands
WHERE BrandId = @BrandId
GO

-- 5 Products
-- 5.1 GetAll
DROP PROC IF EXISTS sp_getAllProducts
GO
CREATE PROC sp_getAllProducts
AS
SELECT *
FROM dbo.Products
GO
-- 5.2 GetById
DROP PROC IF EXISTS sp_getByIdProduct
GO
CREATE PROC sp_getByIdProduct(@ProductId VARCHAR(20))
AS
SELECT*
FROM dbo.Products
WHERE ProductId = @ProductId
GO
-- 5.3 InsertProduct
DROP PROC IF EXISTS sp_insertProduct
GO
CREATE PROC sp_insertProduct @Name NVARCHAR(100),
                             @CategoryId VARCHAR(20),
                             @BrandId VARCHAR(20),
                             @Image NVARCHAR(MAX),
                             @Quantity INTEGER,
                             @SaleDate DATETIME,
                             @RAM INTEGER,
                             @ROM VARCHAR(20),
                             @FrontCam NVARCHAR(200),
                             @BackCam NVARCHAR(200),
                             @OS NVARCHAR(100),
                             @Screen NVARCHAR(200),
                             @CPU VARCHAR(50),
                             @Battery VARCHAR(100),
                             @Weight VARCHAR(20),
                             @VGA NVARCHAR(100),
                             @Description nvarchar(max),
                             @Price FLOAT
AS
INSERT INTO dbo.Products(Name, -- Name - nvarchar(100)
                         CategoryId, -- CategoryId - varchar(20)
                         BrandId, -- BrandId - varchar(20)
                         Image, -- Image - nvarchar(max)
                         Quantity, -- Quantity - integer
                         SaleDate, -- SaleDate - datetime
                         RAM, -- RAM - integer
                         ROM, -- ROM - varchar(20)
                         FrontCam, -- FrontCam - nvarchar(200)
                         BackCam, -- BackCam - nvarchar(200)
                         OS, -- OS - nvarchar(100)
                         Screen, -- Screen - nvarchar(200)
                         CPU, -- CPU - varchar(50)
                         Battery, -- Battery - varchar(100)
                         Weight, -- Weight - varchar(20)
                         VGA, -- VGA - nvarchar(100)
                         Description, -- Description - text
                         Price)
VALUES (@Name, -- Name - nvarchar(100)
        @CategoryId, -- CategoryId - varchar(20)
        @BrandId, -- BrandId - varchar(20)
        @Image, -- Image - nvarchar(max)
        @Quantity, -- Quantity - integer
        @SaleDate, -- SaleDate - datetime
        @RAM, -- RAM - integer
        @ROM, -- ROM - varchar(20)
        @FrontCam, -- FrontCam - nvarchar(200)
        @BackCam, -- BackCam - nvarchar(200)
        @OS, -- OS - nvarchar(100)
        @Screen, -- Screen - nvarchar(200)
        @CPU, -- CPU - varchar(50)
        @Battery, -- Battery - varchar(100)
        @Weight, -- Weight - varchar(20)
        @VGA, -- VGA - nvarchar(100)
        @Description, -- Description - text
        @Price -- Price - float
       )
GO
-- 5.4 UpdateProduct
DROP PROC IF EXISTS sp_updateProduct
GO
CREATE PROC sp_updateProduct @ProductId VARCHAR(20),
                             @Name NVARCHAR(100),
                             @CategoryId VARCHAR(20),
                             @BrandId VARCHAR(20),
                             @Image NVARCHAR(MAX),
                             @Quantity INTEGER,
                             @SaleDate DATETIME,
                             @RAM INTEGER,
                             @ROM VARCHAR(20),
                             @FrontCam NVARCHAR(200),
                             @BackCam NVARCHAR(200),
                             @OS NVARCHAR(100),
                             @Screen NVARCHAR(200),
                             @CPU VARCHAR(50),
                             @Battery VARCHAR(100),
                             @Weight VARCHAR(20),
                             @VGA NVARCHAR(100),
                             @Description nvarchar(max),
                             @Price FLOAT
AS
UPDATE dbo.Products
SET Name        = @Name,
    CategoryId  = @CategoryId,
    BrandId     = @BrandId,
    Image       = @Image,
    Quantity    = @Quantity,
    SaleDate    = @SaleDate,
    RAM         = @RAM,
    ROM         = @ROM,
    FrontCam    = @FrontCam,
    BackCam     = @BackCam,
    OS          = @OS,
    Screen      = @Screen,
    CPU         = @CPU,
    Battery     = @Battery,
    Weight      = @Weight,
    VGA         = @VGA,
    Description = @Description,
    Price       = @Price
WHERE ProductId = @ProductId
GO
-- 5.5 DeleteProduct
DROP PROC IF EXISTS sp_deleteProduct
GO
CREATE PROC sp_deleteProduct(@ProductId VARCHAR(20))
AS
DELETE dbo.Products
WHERE ProductId = @ProductId
GO

-- 6 Review
-- 6.1 GetAllByProductId
DROP PROC IF EXISTS sp_getByProductId
GO
CREATE PROC sp_getByProductId(@ProductId VARCHAR(20))
AS
SELECT *
FROM dbo.Reviews
WHERE ProductId = @ProductId
GO
-- 6.2 GetById
DROP PROC IF EXISTS sp_getById
GO
CREATE PROC sp_getById(@ReviewId VARCHAR(20))
AS
SELECT *
FROM dbo.Reviews
WHERE ReviewId = @ReviewId
GO
-- 6.3 InsertReviews
DROP PROC IF EXISTS sp_insertReview
GO
CREATE PROC sp_insertReview @Username VARCHAR(40),
                            @ProductId VARCHAR(20),
                            @Content NVARCHAR(max),
                            @Rate INTEGER,
                            @Time DATETIME
AS
INSERT INTO dbo.Reviews(Username, ProductId, Content, Rate, Time)
VALUES (@Username, -- Username - varchar(40)
        @ProductId, -- ProductId - varchar(20)
        @Content, -- Content - nvarchar(max)
        @Rate, -- Rate - integer
        @Time -- Time - datetime
       )
GO
-- 6.4 UpdateReviews
DROP PROC IF EXISTS sp_updateReview
GO
CREATE PROC sp_updateReview @ReviewId VARCHAR(20),
                            @Username VARCHAR(40),
                            @ProductId VARCHAR(20),
                            @Content NVARCHAR(max),
                            @Rate INTEGER,
                            @Time DATETIME
AS

UPDATE dbo.Reviews
SET Username  = @Username,
    ProductId = @ProductId,
    Content   = @Content,
    Rate      = @Rate,
    Time      = @Time
WHERE ReviewId = @ReviewId
GO
-- 6.5 DeleteReview
DROP PROC IF EXISTS sp_deleteReview
GO
CREATE PROC sp_deleteReview(@ReviewId VARCHAR(20))
AS
DELETE dbo.Reviews
WHERE ReviewId = @ReviewId
GO

-- 7 Deliveries
-- 7.1 Get
DROP PROC IF EXISTS sp_getAllDeliveries
GO
CREATE PROC sp_getAllDeliveries
AS
SELECT *
FROM Deliveries
GO
-- 7.2 Get by id
DROP PROC IF EXISTS sp_getDeliveryById
GO
CREATE PROC sp_getDeliveryById @Id VARCHAR(20)
AS
SELECT *
FROM Deliveries
WHERE DeliveryId = @Id
GO
-- 7.3 Insert
DROP PROC IF EXISTS sp_insertDelivery
GO
CREATE PROC sp_insertDelivery @DeliveryId VARCHAR(20),
                              @Name NVARCHAR(100),
                              @Email VARCHAR(100),
                              @PhoneNumber VARCHAR(20),
                              @Location NVARCHAR(200)
AS
INSERT INTO Deliveries
VALUES (@DeliveryId, @Name, @Email, @PhoneNumber, @Location)
GO
-- 7.4 Update
DROP PROC IF EXISTS sp_updateDelivery
GO
CREATE PROC sp_updateDelivery @DeliveryId VARCHAR(20),
                              @Name NVARCHAR(100),
                              @Email VARCHAR(100),
                              @PhoneNumber VARCHAR(20),
                              @Location NVARCHAR(200)
AS
UPDATE Deliveries
SET Name        = @Name,
    Email       = @Email,
    PhoneNumber = @PhoneNumber,
    Location    = @Location
WHERE DeliveryId = @DeliveryId
GO
-- 7.5 Delete
DROP PROC IF EXISTS sp_deleteDelivery
GO
CREATE PROC sp_deleteDelivery @DeliveryId VARCHAR(20)
AS
DELETE Deliveries
WHERE DeliveryId = @DeliveryId
GO

-- 8 Coupons
-- 8.1 Get
DROP PROC IF EXISTS sp_getAllCoupons
GO
CREATE PROC sp_getAllCoupons
AS
SELECT *
FROM Coupons
GO
-- 8.2 Get by id
DROP PROC IF EXISTS sp_getCouponById
GO
CREATE PROC sp_getCouponById @CouponId VARCHAR(40)
AS
SELECT *
FROM Coupons
WHERE CouponId = @CouponId
GO
-- 8.3 Insert
DROP PROC IF EXISTS sp_insertCoupon
GO
CREATE PROC sp_insertCoupon @CouponId VARCHAR(40),
                            @Discount FLOAT,
                            @ExpiredTime DATETIME,
                            @EffectiveTime DATETIME,
                            @Description NVARCHAR(300)
AS
INSERT INTO Coupons
VALUES (@CouponId, @Discount, @ExpiredTime, @EffectiveTime, @Description)
GO
-- 8.4 Update
DROP PROC IF EXISTS sp_updateCoupon
GO
CREATE PROC sp_updateCoupon @CouponId VARCHAR(40),
                            @Discount FLOAT,
                            @ExpiredTime DATETIME,
                            @EffectiveTime DATETIME,
                            @Description NVARCHAR(300)
AS
UPDATE Coupons
SET Discount      = @Discount,
    ExpiredTime   = @ExpiredTime,
    EffectiveTime = @EffectiveTime,
    Description   = @Description
WHERE CouponId = @CouponId
GO
-- 8.5 Delete
DROP PROC IF EXISTS sp_deleteCoupon
GO
CREATE PROC sp_deleteCoupon @CouponId VARCHAR(40)
AS
DELETE Coupons
WHERE CouponId = @CouponId
GO

-- 9 Orders
-- 9.1 Get
DROP PROC IF EXISTS sp_getAllOrders
GO
CREATE PROC sp_getAllOrders
AS
SELECT *
FROM Orders
GO
-- 9.2 Get by id
DROP PROC IF EXISTS sp_getOrderById
GO
CREATE PROC sp_getOrderById @OrderId INTEGER
AS
SELECT *
FROM Orders
WHERE OrderId = @OrderId
GO
-- 9.3 Insert
DROP PROC IF EXISTS sp_insertOrder
GO
CREATE PROC sp_insertOrder @OrderId VARCHAR(20),
                           @Username VARCHAR(40),
                           @Name NVARCHAR(100),
                           @Address NVARCHAR(200),
                           @PhoneNumber VARCHAR(20),
                           @PurchaseDate DATETIME,
                           @TotalPrices FLOAT,
                           @DeliveryId VARCHAR(20),
                           @Status NVARCHAR(200),
                           @CouponId INTEGER,
                           @DiscountPrice FLOAT
AS
INSERT INTO Orders
VALUES (@OrderId, @Username, @Name, @Address, @PhoneNumber, @PurchaseDate,
        @TotalPrices, @DeliveryId, @Status, @CouponId, @DiscountPrice)
GO
-- 9.4 Update
DROP PROC IF EXISTS sp_updateOrder
GO
CREATE PROC sp_updateOrder @OrderId VARCHAR(20),
                           @Username VARCHAR(40),
                           @Name NVARCHAR(100),
                           @Address NVARCHAR(200),
                           @PhoneNumber VARCHAR(20),
                           @PurchaseDate DATETIME,
                           @TotalPrices FLOAT,
                           @DeliveryId VARCHAR(20),
                           @Status NVARCHAR(200),
                           @CouponId INTEGER,
                           @DiscountPrice FLOAT
AS
UPDATE Orders
SET Username      = @Username,
    Name          = @Name,
    Address       = @Address,
    PhoneNumber   = @PhoneNumber,
    PurchaseDate  = @PurchaseDate,
    TotalPrices   = @TotalPrices,
    DeliveryId    = @DeliveryId,
    Status        = @Status,
    CouponId      = @CouponId,
    DiscountPrice = @DiscountPrice
WHERE OrderId = @OrderId
GO
-- 9.5 Delete
DROP PROC IF EXISTS sp_deleteOrder
GO
CREATE PROC sp_deleteOrder @OrderId VARCHAR(20)
AS
DELETE Orders
WHERE OrderId = @OrderId
GO

-- 10 OrderDetails
-- 10.1 Get
DROP PROC IF EXISTS sp_getAllOrderDetails
GO
CREATE PROC sp_getAllOrderDetails
AS
SELECT *
FROM OrderDetails
GO
-- 10.2 Get by OrderId
DROP PROC IF EXISTS sp_getOrderDetailsByOrderId
GO
CREATE PROC sp_getOrderDetailsByOrderId @OrderId VARCHAR(20)
AS
SELECT *
FROM OrderDetails
WHERE OrderId = @OrderId
GO
-- 10.3 Insert
DROP PROC IF EXISTS sp_insertOrderDetails
GO
CREATE PROC sp_insertOrderDetails @OrderId VARCHAR(20),
                                  @ProductId VARCHAR(20),
                                  @Quantity INTEGER
AS
INSERT INTO OrderDetails
VALUES (@OrderId, @ProductId, @Quantity, 0.0, 0.0)
GO
-- 10.4 Update
DROP PROC IF EXISTS sp_updateOrderDetails
GO
CREATE PROC sp_updateOrderDetails @OrderId VARCHAR(20),
                                  @ProductId VARCHAR(20),
                                  @Quantity INTEGER
AS
UPDATE OrderDetails
SET Quantity = @Quantity
WHERE OrderId = @OrderId
  and ProductId = @ProductId
GO
-- 10.5 Delete
DROP PROC IF EXISTS sp_deleteOrderDetails
GO
CREATE PROC sp_deleteOrderDetails @OrderId VARCHAR(20),
                                  @ProductId VARCHAR(20)
AS
DELETE OrderDetails
WHERE OrderId = @OrderId
  and ProductId = @ProductId
GO

-- 11 CartDetails
-- 11.1 Get
DROP PROC IF EXISTS sp_getAllCartDetails
GO
CREATE PROC sp_getAllCartDetails
AS
SELECT *
FROM CartDetails
GO
-- 11.2 Get by OrderId
DROP PROC IF EXISTS sp_getCartDetailsByUsername
GO
CREATE PROC sp_getCartDetailsByUsername @Username VARCHAR(40)
AS
SELECT *
FROM CartDetails
WHERE Username = @Username
GO
-- 11.3 Insert
DROP PROC IF EXISTS sp_insertCartDetails
GO
CREATE PROC sp_insertCartDetails @Username VARCHAR(40),
                                 @ProductId VARCHAR(20),
                                 @Quantity INTEGER
AS
INSERT INTO CartDetails
VALUES (@Username, @ProductId, @Quantity)
GO
-- 11.4 Update
DROP PROC IF EXISTS sp_updateCartDetails
GO
CREATE PROC sp_updateCartDetails @Username VARCHAR(40),
                                 @ProductId VARCHAR(20),
                                 @Quantity INTEGER
AS
UPDATE CartDetails
SET Quantity = @Quantity
WHERE Username = @Username
  and ProductId = @ProductId
GO
-- 11.5 Delete
DROP PROC IF EXISTS sp_deleteCartDetails
GO
CREATE PROC sp_deleteCartDetails @Username VARCHAR(40),
                                 @ProductId VARCHAR(20)
AS
DELETE CartDetails
WHERE Username = @Username
  and ProductId = @ProductId
GO

--------- TRIGGER ----------
-- Insert, update OrderDetails
drop trigger if exists after_insert_update_OrderDetails
go
create trigger after_insert_update_OrderDetails
    on OrderDetails
    after insert,
    update as
    declare
        @OrderId varchar(20), @TotalPrice float, @Username varchar(40), @ProductID varchar(20), @Quantity int, @Price float, @QuantityInStock int
    select @OrderId = new.OrderId,
           @TotalPrice = new.Quantity * p.Price,
           @Price = p.Price,
           @QuantityInStock = p.Quantity,
           @Username = ord.username,
           @ProductID = new.ProductId,
           @Quantity = new.quantity
    from inserted new,
         Products p,
         Orders ord
    where new.ProductId = p.ProductId
      and new.OrderId = ord.OrderId
begin
    if (@Quantity > @QuantityInStock)
        begin
            raiserror (N'Quantity not valid! ProductId: %s, Quantity: %d, QuantityInStock: %d', 16, 1, @ProductID, @Quantity, @QuantityInStock)
            rollback;
        end
    else
        begin
            update OrderDetails
            set Price      = @Price,
                TotalPrice = @TotalPrice
            where OrderId = @OrderId
              and ProductId = @ProductID

            update Orders
            set TotalPrices = (select sum(TotalPrice) from OrderDetails where OrderDetails.OrderId = @OrderId)
            where OrderId = @OrderId

            delete CartDetails
            where CartDetails.ProductId = @ProductID
              and CartDetails.Username = @Username

            update Products
            set Quantity -= @Quantity
            where ProductId = @ProductID
        end
end
go

-- Insert, update CartDetails
drop trigger if exists after_insert_update_CartDetails
go
create trigger after_insert_update_CartDetails
    on CartDetails
    after insert,
    update as
    declare
        @ProductId varchar(20), @Quantity int, @QuantityInStock int
    select @ProductId = new.ProductId, @Quantity = new.Quantity, @QuantityInStock = p.Quantity
    from inserted new,
         Products p
    where new.ProductId = p.ProductId
    if (@Quantity > @QuantityInStock)
        begin
            raiserror (N'Quantity not valid! ProductId: %s, Quantity: %d, QuantityInStock: %d', 16, 1, @ProductId, @Quantity, @QuantityInStock)
            rollback;
        end
go

drop trigger if exists Trigger_Delete_OrderDetail
go
CREATE TRIGGER Trigger_Delete_OrderDetail
    on OrderDetails
    for delete
    as
    DECLARE
        @Product_ID VARCHAR(20), @Order_ID VARCHAR(20), @Total int, @CouponId VARCHAR(40)
begin
    --Lấy ra product Id và Order Id từ cột vừa xóa
    SELECT @Product_ID = Deleted.ProductId, @Order_ID = Deleted.OrderId FROM Deleted
    --Cập nhật lại số lượng product trong kho
    UPDATE dbo.Products
    SET Quantity = Quantity +
                   (SELECT Deleted.Quantity FROM Deleted)
    WHERE ProductId = @Product_ID
    --Tính tổng tiền các sản phẩm còn lại
    SELECT @Total = SUM(TotalPrice)
    FROM dbo.OrderDetails
    WHERE OrderId = @Order_ID
    --Lấy mã coupon
    SELECT @CouponId = CouponId FROM dbo.Orders
    --Set lại tổng tiền
    UPDATE dbo.Orders
    SET TotalPrices   = @Total,
        DiscountPrice = @Total - (SELECT Discount from Coupons WHERE CouponId = @CouponId)
    WHERE OrderId = @Order_ID
end
go

-- SAMPLE DATAS --
USE [db_technological_appliances]
GO
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'admin', N'admin', N'admin@email.com', N'0999999999', CAST(N'2001-01-01' AS Date), N'TP.HCM', 1)
GO
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'nam', N'Thái Thành Nam', N'nam@email.com', N'0981771024', CAST(N'2001-11-28' AS Date), N'Bến Tre', 1)
GO
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'phuong', N'Trịnh Xuân Phương', N'phuong@email.com', N'0999999995', CAST(N'2001-01-01' AS Date), N'Bến Tre', 1)
GO
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'tan', N'Cao Hoài Tấn', N'tan@email.com', N'0999999998', CAST(N'2001-01-01' AS Date), N'Bến Tre', 1)
GO
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'toan', N'Nguyễn Phúc Thanh Toàn', N'toan@email.com', N'0999999996', CAST(N'2001-01-01' AS Date), N'Tây Ninh', 1)
GO
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'trung', N'Nguyễn Ngọc Trung', N'trung@email.com', N'0999999997', CAST(N'2001-01-01' AS Date), N'Phú Yên', 1)
GO
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'admin', N'$2a$10$EIJuGNzAYSpm0neUemi.3.eWA44XqltUMszWRqZo64my/IF3tCZI.', 1)
GO
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'nam', N'$2a$10$j5aLIfYSOIbU2Ihkpti2KOh9o4b2J64zvFIRpvUwpy7d/h0bXxO7q', 0)
GO
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'phuong', N'$2a$10$77/xC5K89B3z7SecIu1Wm.PSDnw0qu0z6y6poYQjXTgsLqNeVVCbm', 0)
GO
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'tan', N'$2a$10$FGRitRqmuYpQq8KuGAA4quTRVgBt9LvnEUsF7CyScwa4efCRspXaW', 0)
GO
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'toan', N'$2a$10$dk8yjCKXGgQ.rCKjoGkizetL52Ay..w6IB80Kx7A/Lk161DrXykqy', 0)
GO
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'trung', N'$2a$10$/osmVYwaRKMrNydGUcmRa.Ho1NScl3JuVuT4YTSTHlaYoP.xsUdke', 0)
GO
INSERT [dbo].[Categories] ([CategoryId], [Name]) VALUES (N'C01', N'Laptop')
GO
INSERT [dbo].[Categories] ([CategoryId], [Name]) VALUES (N'C02', N'Smart Phone')
GO
INSERT [dbo].[Brands] ([BrandId], [Name], [Email], [Logo], [Location]) VALUES (N'B01', N'Samsung', N'samsung@email.com', N'link', N'Korea')
GO
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00001', N'Galaxy S21', N'C02', N'B01', N'link', 3, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 2500000)
GO
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00002', N'Galaxy S21', NULL, NULL, N'link', 5, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 25000000)
GO
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00003', N'Galaxy S21', N'C02', N'B01', N'link', 1, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 25000000)
GO
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00004', N'Galaxy S21', N'C02', N'B01', N'link', 1, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 26000000)
GO
INSERT [dbo].[Reviews] ([ReviewId], [Username], [ProductId], [Content], [Rate], [Time]) VALUES (N'R00001', N'nam', N'P00001', N'Sản phẩm tẹt zời', 5, CAST(N'2022-03-26T07:28:32.000' AS DateTime))
GO
INSERT [dbo].[Deliveries] ([DeliveryId], [Name], [Email], [PhoneNumber], [Location]) VALUES (N'D01', N'GiaoHanhNhanh', N'GHN@emai.com', N'0998877666', N'TP.HCM')
GO
INSERT [dbo].[Orders] ([OrderId], [Username], [Name], [Address], [PhoneNumber], [PurchaseDate], [TotalPrices], [DeliveryId], [Status], [CouponId], [DiscountPrice]) VALUES (N'O00001', N'nam', N'Nam', N'Bến Tre', N'0981771024', CAST(N'2022-03-24T00:00:00.000' AS DateTime), 50000000, N'D01', N'waiting', NULL, NULL)
GO
INSERT [dbo].[CartDetails] ([Username], [ProductId], [Quantity]) VALUES (N'nam', N'P00002', 5)
GO
INSERT [dbo].[OrderDetails] ([OrderId], [ProductId], [Quantity], [Price], [TotalPrice]) VALUES (N'O00001', N'P00001', 2, 25000000, 50000000)
GO
