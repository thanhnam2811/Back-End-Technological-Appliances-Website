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
    Role     INTEGER     NOT NULL DEFAULT 0
)
CREATE TABLE PasswordResetToken
(
    Id       INTEGER PRIMARY KEY IDENTITY,
    Username VARCHAR(40) FOREIGN KEY REFERENCES dbo.Users (Username)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    Token    VARCHAR(60) NOT NULL,
    Expiry   DATETIME    NOT NULL
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
    CategoryId  VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES dbo.Categories (CategoryId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    BrandId     VARCHAR(20) NOT NULL FOREIGN KEY REFERENCES dbo.Brands (BrandId)
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
        ON UPDATE CASCADE
        ON DELETE CASCADE ,
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
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'admin', N'admin', N'admin@email.com', N'0999999999', CAST(N'2001-01-01' AS Date), N'TP.HCM', 1)
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'nam', N'Thái Thành Nam', N'nam@email.com', N'0981771024', CAST(N'2001-11-28' AS Date), N'Bến Tre', 1)
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'phuong', N'Trịnh Xuân Phương', N'phuong@email.com', N'0999999995', CAST(N'2001-01-01' AS Date), N'Bến Tre', 1)
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'tan', N'Cao Hoài Tấn', N'tan@email.com', N'0999999998', CAST(N'2001-01-01' AS Date), N'Bến Tre', 1)
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'toan', N'Nguyễn Phúc Thanh Toàn', N'toan@email.com', N'0999999996', CAST(N'2001-01-01' AS Date), N'Tây Ninh', 1)
INSERT [dbo].[Users] ([Username], [Name], [Email], [PhoneNumber], [DateOfBirth], [Address], [Gender]) VALUES (N'trung', N'Nguyễn Ngọc Trung', N'trung@email.com', N'0999999997', CAST(N'2001-01-01' AS Date), N'Phú Yên', 1)
GO
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'admin', N'$2a$10$EIJuGNzAYSpm0neUemi.3.eWA44XqltUMszWRqZo64my/IF3tCZI.', 1)
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'nam', N'$2a$10$j5aLIfYSOIbU2Ihkpti2KOh9o4b2J64zvFIRpvUwpy7d/h0bXxO7q', 0)
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'phuong', N'$2a$10$77/xC5K89B3z7SecIu1Wm.PSDnw0qu0z6y6poYQjXTgsLqNeVVCbm', 0)
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'tan', N'$2a$10$FGRitRqmuYpQq8KuGAA4quTRVgBt9LvnEUsF7CyScwa4efCRspXaW', 0)
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'toan', N'$2a$10$dk8yjCKXGgQ.rCKjoGkizetL52Ay..w6IB80Kx7A/Lk161DrXykqy', 0)
INSERT [dbo].[Account] ([Username], [Password], [Role]) VALUES (N'trung', N'$2a$10$/osmVYwaRKMrNydGUcmRa.Ho1NScl3JuVuT4YTSTHlaYoP.xsUdke', 0)
GO
INSERT [dbo].[Brands] ([BrandId], [Name], [Email], [Logo], [Location]) VALUES (N'B01', N'Samsung', N'samsung@email.com', N'link', N'Korea')
GO
INSERT [dbo].[Categories] ([CategoryId], [Name]) VALUES (N'C01', N'Laptop')
INSERT [dbo].[Categories] ([CategoryId], [Name]) VALUES (N'C02', N'SmartPhone')
GO
INSERT [dbo].[Deliveries] ([DeliveryId], [Name], [Email], [PhoneNumber], [Location]) VALUES (N'D01', N'GiaoHanhNhanh', N'GHN@emai.com', N'0998877666', N'TP.HCM')
GO
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00001', N'Galaxy S21', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-a73-1-600x600.jpg', 10, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 2500000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00002', N'Galaxy S21', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-a73-1-600x600.jpg', 5, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 25000000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00003', N'Galaxy S21', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-a73-1-600x600.jpg', 1, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 25000000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00004', N'Galaxy S21', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-a73-1-600x600.jpg', 1, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 26000000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00005', N'Dell XPS', N'C01', N'B01', N'http://img.websosanh.vn/v2/users/root_product/images/laptop-dell-xps-13-9305-/ync0weuk0ussi.jpg', 1, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 26000000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00006', N'Dell XPS', N'C01', N'B01', N'http://img.websosanh.vn/v2/users/root_product/images/laptop-dell-xps-13-9305-/ync0weuk0ussi.jpg', 1, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 26000000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00007', N'Dell XPS', N'C01', N'B01', N'http://img.websosanh.vn/v2/users/root_product/images/laptop-dell-xps-13-9305-/ync0weuk0ussi.jpg', 1, NULL, 6, N'512', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 26000000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00018', N'iPhone 13 Pro Max 128GB | Chính hãng VN/A', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone_13-_pro-5_4.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'6.8', N'Snapdragon 8 gen 1', N'5000mAh', N'188g', NULL, NULL, 34990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00019', N'iPhone 11 64GB I Chính hãng VN/A ', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone_11_white_4_.png', 46, NULL, NULL, N'64 GB', N'16', N'108', N'OneUI', N'7.8', N'Snapdragon 8 gen 2', N'5000mAh', N'188g', NULL, NULL, 18000000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00020', N'iPhone 12 Pro Max 128GB I Chính hãng VN/A ', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone_12_pro_max_white_1.png', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'8.8', N'Snapdragon 8 gen 3', N'5000mAh', N'188g', NULL, NULL, 32990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00021', N'Xiaomi Mi 11 Lite 5G', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/x/i/xiaomi-mi-11-lite-5g-2_10.png', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'9.8', N'Snapdragon 8 gen 4', N'5000mAh', N'188g', NULL, NULL, 10490000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00022', N'iPhone 13 128GB | Chính hãng VN/A', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/ip13-pro_2.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'10.8', N'Snapdragon 8 gen 5', N'5000mAh', N'188g', NULL, NULL, 24990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00023', N'iPhone 12 64GB I Chính hãng VN/A ', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone-12_2__3.jpg', 46, NULL, NULL, N'64 GB', N'16', N'108', N'OneUI', N'11.8', N'Snapdragon 8 gen 6', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00024', N'Xiaomi POCO X3 Pro', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/x/i/xiaomi-poco-x3-pro-2.jpg', 56, NULL, NULL, N'256 GB', N'16', N'108', N'OneUI', N'12.8', N'Snapdragon 8 gen 7', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00025', N'iPhone 11 128GB I Chính hãng VN/A ', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone_11_white.png', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'13.8', N'Snapdragon 8 gen 8', N'5000mAh', N'188g', NULL, NULL, 19900000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00026', N'OPPO Reno6 Z 5G', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/o/p/oppo_reno6.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'14.8', N'Snapdragon 8 gen 9', N'5000mAh', N'188g', NULL, NULL, 9490000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00027', N'Samsung Galaxy A73', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-a73-1-600x600.jpg', 46, NULL, NULL, N'256 GB', N'16', N'108', N'OneUI', N'15.8', N'Snapdragon 8 gen 10', N'5000mAh', N'188g', NULL, NULL, 12990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00028', N'OPPO Reno6 5G', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/r/e/reno6-1_1.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'16.8', N'Snapdragon 8 gen 11', N'5000mAh', N'188g', NULL, NULL, 12990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00029', N'OPPO A95', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/c/o/combo_a95_-_en_-_cmyk.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'17.8', N'Snapdragon 8 gen 12', N'5000mAh', N'188g', NULL, NULL, 6990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00030', N'Tecno Pova 2', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/t/e/tecno-pova-2-3.jpg', 46, NULL, NULL, N'64 GB', N'16', N'108', N'OneUI', N'18.8', N'Snapdragon 8 gen 13', N'5000mAh', N'188g', NULL, NULL, 3990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00031', N'Samsung Galaxy A32', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-a32-20.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'19.8', N'Snapdragon 8 gen 14', N'5000mAh', N'188g', NULL, NULL, 6490000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00032', N'Samsung Galaxy A13 4G 128GB', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/g/a/galaxy_a13.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'20.8', N'Snapdragon 8 gen 15', N'5000mAh', N'188g', NULL, NULL, 4690000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00033', N'Realme Narzo 50A', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/r/e/realme-narzo-50a-600x600.jpg', 56, NULL, NULL, N'64 GB', N'16', N'108', N'OneUI', N'21.8', N'Snapdragon 8 gen 16', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00034', N'iPhone 13 Pro 128GB | Chính hãng VN/A', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone_13-_pro-5.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'22.8', N'Snapdragon 8 gen 17', N'5000mAh', N'188g', NULL, NULL, 31990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00035', N'ASUS ROG Phone 5 chính hãng', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/a/s/asus-rog-phone-5_0002_gsmarena_001.jpg', 46, NULL, NULL, N'256 GB', N'16', N'108', N'OneUI', N'23.8', N'Snapdragon 8 gen 18', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00036', N'OPPO Reno7 Z (5G)', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/c/o/combo_product_-_rainbow_spectrum_-_reno7_z.png', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'24.8', N'Snapdragon 8 gen 19', N'5000mAh', N'188g', NULL, NULL, 10490000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00037', N'Tecno Spark 8C', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/4/0/40_1_23.jpg', 46, NULL, NULL, N'64 GB', N'16', N'108', N'OneUI', N'25.8', N'Snapdragon 8 gen 20', N'5000mAh', N'188g', NULL, NULL, 2990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00038', N'Vsmart Aris 8GB 128GB', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/1/1/11_3_1_1.png', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'26.8', N'Snapdragon 8 gen 21', N'5000mAh', N'188g', NULL, NULL, 6690000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00039', N'iPhone 13 Pro Max 256GB I Chính hãng VN/A', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone_13-_pro-5_4_1.jpg', 46, NULL, NULL, N'256 GB', N'16', N'108', N'OneUI', N'27.8', N'Snapdragon 8 gen 22', N'5000mAh', N'188g', NULL, NULL, 37990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00040', N'ASUS ROG Phone 5S 16GB 256GB', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/a/s/asus-rog-phone-5_0002_gsmarena_001_3_1.jpg', 46, NULL, NULL, N'256 GB', N'16', N'108', N'OneUI', N'28.8', N'Snapdragon 8 gen 23', N'5000mAh', N'188g', NULL, NULL, 20990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00041', N'iPhone SE 2022 | Chính hãng VN/A', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone-se-red-select-20220322.jpg', 46, NULL, NULL, N'64 GB', N'16', N'108', N'OneUI', N'29.8', N'Snapdragon 8 gen 24', N'5000mAh', N'188g', NULL, NULL, 12990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00042', N'Samsung Galaxy A52s 5G', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/0/4/04_2_4.png', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'30.8', N'Snapdragon 8 gen 25', N'5000mAh', N'188g', NULL, NULL, 10990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00043', N'Xiaomi 12 Pro (5G)', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/x/i/xiaomi-12-pro_arenamobiles.jpg', 46, NULL, NULL, N'256 GB', N'16', N'108', N'OneUI', N'31.8', N'Snapdragon 8 gen 26', N'5000mAh', N'188g', NULL, NULL, 27990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00044', N'Realme C35', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/f/r/frame3935-640x640.png', 56, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'32.8', N'Snapdragon 8 gen 27', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00045', N'ASUS ROG Phone 5S 16GB 512GB', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/h/7/h732_1.png', 46, NULL, NULL, N'512 GB', N'16', N'108', N'OneUI', N'33.8', N'Snapdragon 8 gen 28', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00046', N'Realme GT Neo 3', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/r/e/realme-gt-neo3-600x600.jpg', 56, NULL, NULL, N'FALSE', N'16', N'108', N'OneUI', N'34.8', N'Snapdragon 8 gen 29', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00047', N'Samsung Galaxy A12', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-a12_2_.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'35.8', N'Snapdragon 8 gen 30', N'5000mAh', N'188g', NULL, NULL, 4290000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00048', N'Nubia Red Magic 6s Pro Cyborg 12GB 128GB', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/n/u/nubia-red-magic-6s-pro-black.png', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'36.8', N'Snapdragon 8 gen 31', N'5000mAh', N'188g', NULL, NULL, 17990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00049', N'Redmi K40', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/r/e/redmi-k40.jpg', 56, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'37.8', N'Snapdragon 8 gen 32', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00050', N'Nokia G21', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/t/h/thumb_602966_default_big.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'38.8', N'Snapdragon 8 gen 33', N'5000mAh', N'188g', NULL, NULL, 4290000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00051', N'Nubia Red Magic 7', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/z/t/zte-nubia-red-magic-7-600x600.jpg', 56, NULL, NULL, N'FALSE', N'16', N'108', N'OneUI', N'39.8', N'Snapdragon 8 gen 34', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00052', N'iPhone XR 64GB I Chính hãng VN/A ', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone_xr_red.png', 56, NULL, NULL, N'64 GB', N'16', N'108', N'OneUI', N'40.8', N'Snapdragon 8 gen 35', N'5000mAh', N'188g', NULL, NULL, 14990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00053', N'Realme Q3 Pro ', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/r/e/realme-q3-pro-1.jpg', 56, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'41.8', N'Snapdragon 8 gen 36', N'5000mAh', N'188g', NULL, NULL, 22990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00054', N'Samsung Galaxy A72', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-a72-30.jpg', 56, NULL, NULL, N'256 GB', N'16', N'108', N'OneUI', N'42.8', N'Snapdragon 8 gen 37', N'5000mAh', N'188g', NULL, NULL, 11490000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00055', N'Realme 9 Pro', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/r/e/real_me_pro_002.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'43.8', N'Snapdragon 8 gen 38', N'5000mAh', N'188g', NULL, NULL, 7990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00056', N'Xiaomi Redmi 10 (4GB - 128GB)', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/0/0/001_2.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'44.8', N'Snapdragon 8 gen 39', N'5000mAh', N'188g', NULL, NULL, 4290000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00057', N'Tecno Pova 2 6G 128GB', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/t/e/tecno-pova-2-2_2.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'45.8', N'Snapdragon 8 gen 40', N'5000mAh', N'188g', NULL, NULL, 4490000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00058', N'Samsung Galaxy S21 Ultra 5G', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-s21-ultra-1_1.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'46.8', N'Snapdragon 8 gen 41', N'5000mAh', N'188g', NULL, NULL, 30990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00059', N'Nokia G50 (5G)', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/n/o/nokia-g50-4_1.jpeg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'47.8', N'Snapdragon 8 gen 42', N'5000mAh', N'188g', NULL, NULL, 6490000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00060', N'Vivo Y15a', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/d/i/dien-thoai-vivo-y15a-2021.png', 46, NULL, NULL, N'64 GB', N'16', N'108', N'OneUI', N'48.8', N'Snapdragon 8 gen 43', N'5000mAh', N'188g', NULL, NULL, 3790000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00061', N'iPhone 12 Pro 128GB I Chính hãng VN/A ', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone_12_pro_black.png', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'49.8', N'Snapdragon 8 gen 44', N'5000mAh', N'188g', NULL, NULL, 30990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00062', N'Samsung Galaxy S20 FE 256GB (Fan Edition)', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-20-fe_4_.jpg', 46, NULL, NULL, N'256 GB', N'16', N'108', N'OneUI', N'50.8', N'Snapdragon 8 gen 45', N'5000mAh', N'188g', NULL, NULL, 15490000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00063', N'Samsung Galaxy A23', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/a/samsung-galaxy-a23-cam-thumb-600x600_1.jpg', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'51.8', N'Snapdragon 8 gen 46', N'5000mAh', N'188g', NULL, NULL, 5690000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00064', N'iPhone 12 mini 64GB I Chính hãng VN/A ', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/i/p/iphone_12_mini_blue.png', 56, NULL, NULL, N'64 GB', N'16', N'108', N'OneUI', N'52.8', N'Snapdragon 8 gen 47', N'5000mAh', N'188g', NULL, NULL, 18990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00065', N'Xiaomi Redmi Note 10', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/x/i/xiaomi-redmi-note-10_1.jpg', 56, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'53.8', N'Snapdragon 8 gen 48', N'5000mAh', N'188g', NULL, NULL, 5490000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00066', N'Samsung Galaxy A03 (3GB - 32GB)', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/s/c/screenshot_2_39.png', 46, NULL, NULL, N'32 GB', N'16', N'108', N'OneUI', N'54.8', N'Snapdragon 8 gen 49', N'5000mAh', N'188g', NULL, NULL, 2990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00067', N'OPPO Reno7 4G (8GB - 128GB)', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/c/o/combo_product_-_black_-_reno7_4g.png', 46, NULL, NULL, N'128 GB', N'16', N'108', N'OneUI', N'55.8', N'Snapdragon 8 gen 50', N'5000mAh', N'188g', NULL, NULL, 8990000)
INSERT [dbo].[Products] ([ProductId], [Name], [CategoryId], [BrandId], [Image], [Quantity], [SaleDate], [RAM], [ROM], [FrontCam], [BackCam], [OS], [Screen], [CPU], [Battery], [Weight], [VGA], [Description], [Price]) VALUES (N'P00068', N'Xiaomi Redmi 9A', N'C02', N'B01', N'https://cdn.cellphones.com.vn/media/catalog/product/cache/7/small_image/9df78eab33525d08d6e5fb8d27136e95/r/e/redmi_9a_0005_background.jpg', 46, NULL, NULL, N'32 GB', N'16', N'108', N'OneUI', N'56.8', N'Snapdragon 8 gen 51', N'5000mAh', N'188g', NULL, NULL, 2490000)
GO
INSERT [dbo].[Reviews] ([ReviewId], [Username], [ProductId], [Content], [Rate], [Time]) VALUES (N'R00001', N'nam', N'P00001', N'Sản phẩm tẹt zời', 5, CAST(N'2022-03-26T07:28:32.000' AS DateTime))
GO
INSERT [dbo].[Orders] ([OrderId], [Username], [Name], [Address], [PhoneNumber], [PurchaseDate], [TotalPrices], [DeliveryId], [Status], [CouponId], [DiscountPrice]) VALUES (N'O00001', N'nam', N'Nam', N'Bến Tre', N'0981771024', CAST(N'2022-03-24T00:00:00.000' AS DateTime), 5000000, N'D01', N'waiting', NULL, NULL)
GO
INSERT [dbo].[OrderDetails] ([OrderId], [ProductId], [Quantity], [Price], [TotalPrice]) VALUES (N'O00001', N'P00001', 2, 2500000, 5000000)
GO
INSERT [dbo].[CartDetails] ([Username], [ProductId], [Quantity]) VALUES (N'nam', N'P00002', 5)
GO
