USE [master]
GO
/****** Object:  Database [MDS]    Script Date: 18.09.2016 12:07:43 ******/
CREATE DATABASE [MDS] ON  PRIMARY 
( NAME = N'MDS', FILENAME = N'D:\SQLBASE\MDS.mdf' , SIZE = 941696KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'MDS_log', FILENAME = N'D:\SQLBASE\MDS_log.ldf' , SIZE = 9024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [MDS] SET COMPATIBILITY_LEVEL = 100
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [MDS].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [MDS] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [MDS] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [MDS] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [MDS] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [MDS] SET ARITHABORT OFF 
GO
ALTER DATABASE [MDS] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [MDS] SET AUTO_SHRINK ON 
GO
ALTER DATABASE [MDS] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [MDS] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [MDS] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [MDS] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [MDS] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [MDS] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [MDS] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [MDS] SET  DISABLE_BROKER 
GO
ALTER DATABASE [MDS] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [MDS] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [MDS] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [MDS] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [MDS] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [MDS] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [MDS] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [MDS] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [MDS] SET  MULTI_USER 
GO
ALTER DATABASE [MDS] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [MDS] SET DB_CHAINING OFF 
GO
EXEC sys.sp_db_vardecimal_storage_format N'MDS', N'ON'
GO
USE [MDS]
GO
/****** Object:  User [EKFGROUP\A.Svirin]    Script Date: 18.09.2016 12:07:44 ******/
CREATE USER [EKFGROUP\A.Svirin] FOR LOGIN [EKFGROUP\A.Svirin] WITH DEFAULT_SCHEMA=[dbo]
GO
ALTER ROLE [db_datareader] ADD MEMBER [EKFGROUP\A.Svirin]
GO
/****** Object:  UserDefinedFunction [dbo].[getLastEkfgrExchngDate]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date, ,>
-- Description:	<Description, ,>
-- =============================================
CREATE FUNCTION [dbo].[getLastEkfgrExchngDate]() 
RETURNS datetime
AS
BEGIN
	-- Declare the return variable here
	DECLARE @res datetime;

	-- Add the T-SQL statements to compute the return value here
	SET @res=GETDATE();

	SELECT TOP(1) @res=ISNULL(dt_val, GETDATE()) FROM cb_settings WHERE name='last_ekfgr_exchng_date';

	SET @res = ISNULL(@res, GETDATE());

	RETURN @res;

END



GO
/****** Object:  UserDefinedFunction [dbo].[getPPropsCount]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date, ,>
-- Description:	<Description, ,>
-- =============================================
CREATE FUNCTION [dbo].[getPPropsCount](@product_id varchar(255)) 
RETURNS int
AS
BEGIN
	-- Declare the return variable here
	DECLARE @res int;

	-- Add the T-SQL statements to compute the return value here
	SET @res=0;

	SELECT @res=COUNT(*) FROM product_properties_values WHERE product_id=@product_id;

	RETURN @res;

END


GO
/****** Object:  UserDefinedFunction [dbo].[getPPropsJSONArray]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date, ,>
-- Description:	<Description, ,>
-- =============================================
CREATE FUNCTION [dbo].[getPPropsJSONArray](@product_id varchar(255)) 
RETURNS varchar(max)
AS
BEGIN
	-- Declare the return variable here
	DECLARE @res varchar(max), @property_code varchar(1000), @pval varchar(2000),  @cnt int;

	-- Add the T-SQL statements to compute the return value here
	SET @res='{';
	SET @cnt=0;
	DECLARE product_props CURSOR FOR 
	SELECT ppv.product_property_code, ppv.pvalue 
	FROM product_properties_values ppv JOIN products_properties_permissions ppp ON ppv.product_id=ppp.product_id AND ppv.product_property_code=ppp.properties_id 
	WHERE ppv.product_id=@product_id AND ppp.used=1 AND ppp.information_system=3;
	OPEN product_props

	FETCH NEXT FROM product_props INTO @property_code, @pval;
	WHILE @@FETCH_STATUS=0 BEGIN
	  IF @res<>'{'
		SET @res=@res+',';
		SET @res=@res+'"c'+CAST(@cnt as varchar(20))+'":"'+REPLACE(REPLACE(REPLACE(@property_code,'"',''''), CHAR(13), '<br/>'), CHAR(10), '')+'","v'+CAST(@cnt as varchar(20))+'":"'+REPLACE(REPLACE(REPLACE(@pval,'"',''''), CHAR(13), '<br/>'), CHAR(10), '')+'"';
	  FETCH NEXT FROM product_props INTO @property_code, @pval;
	  SET @cnt = (@cnt+1);
	END

	IF @cnt>0
		SET @res=@res+',';
	SET @res=@res+'"dc":"'+CAST(@cnt as varchar(20))+'"';
	
	SET @res=@res+'}';

	-- Return the result of the function
	RETURN @res;

END

GO
/****** Object:  UserDefinedFunction [dbo].[getProdImgFilePath]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date, ,>
-- Description:	<Description, ,>
-- =============================================
CREATE FUNCTION [dbo].[getProdImgFilePath](@product_id varchar(255), @forder int, @fupd int) 
RETURNS varchar(1000)
AS
BEGIN
	-- Declare the return variable here
	DECLARE @res varchar(1000);

	-- Add the T-SQL statements to compute the return value here
	SET @res='';

	if(@forder=0) begin
		SELECT @res=paf.file_path FROM products_attached_files paf WHERE paf.file_type='main_image' and paf.product_id=@product_id and 
		(paf.file_extension='jpg') and (paf.file_updated=1 or @fupd=0)
	end
	if(@forder>0) begin --
		SELECT @res=paf.file_path FROM products_attached_files paf WHERE paf.file_type='additional_image' and paf.product_id=@product_id and 
		(paf.file_extension='jpg') and paf.sort_order=(@forder-1)  and (paf.file_updated=1 or @fupd=0)
	end

	SET @res=ISNULL(@res,'');

	-- Return the result of the function
	RETURN @res;

END


GO
/****** Object:  UserDefinedFunction [dbo].[getProductEkfGroupQuantity]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date, ,>
-- Description:	<Description, ,>
-- =============================================
CREATE FUNCTION [dbo].[getProductEkfGroupQuantity](@product_id varchar(255)) 
RETURNS decimal(28,10)
AS
BEGIN
	-- Declare the return variable here
	DECLARE @res decimal(28,10);

	-- Add the T-SQL statements to compute the return value here
	SET @res=0;

	SELECT        @res=SUM(quantity) FROM	products_amounts
	WHERE        (warehouse_id = 'e5e08b47-90d9-11e4-ba37-005056b80040') AND product_id=@product_id;

	RETURN @res;

END



GO
/****** Object:  UserDefinedFunction [dbo].[sectionIsParented]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date, ,>
-- Description:	<Description, ,>
-- =============================================
CREATE FUNCTION [dbo].[sectionIsParented](@sect_id varchar(50), @parent_id varchar(50)) 
RETURNS smallint
AS
BEGIN
	DECLARE @top_sect smallint, @is_parented smallint, @cparent_id varchar(50);
	SET @is_parented=0;
	SET @top_sect=0;
	IF LEN(@sect_id)=36 AND LEN(@parent_id)=36 BEGIN
	    --SET @is_parented=2;
		WHILE @top_sect=0 AND @is_parented=0 BEGIN
			SET @cparent_id='';
			SELECT TOP 1 @cparent_id=ISNULL(pg.parent_id,'') FROM product_groups pg WHERE pg.id=@sect_id;
			IF LEN(@cparent_id)>0 BEGIN
				IF(@cparent_id=@parent_id) BEGIN
					SET @is_parented=1;
				END
				SET @sect_id = @cparent_id;
			END
			IF LEN(@cparent_id)=0 BEGIN
				SET @top_sect=1;
			END
		END
	END

	-- Return the result of the function
	RETURN @is_parented;

END

GO
/****** Object:  Table [dbo].[bitrix_catalog_trees]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[bitrix_catalog_trees](
	[id] [nvarchar](36) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_bitrix_catalog_trees] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[bitrix_groups_links]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[bitrix_groups_links](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[catalog_tree_id] [nvarchar](36) NOT NULL,
	[catalog_group_id] [nvarchar](36) NOT NULL,
	[catalog_group_parent_id] [nvarchar](36) NOT NULL,
	[catalog_group_name] [nvarchar](100) NOT NULL,
 CONSTRAINT [PK_bitrix_groups_links] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[bitrix_groups_products_links]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[bitrix_groups_products_links](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[catalog_tree_id] [nvarchar](36) NOT NULL,
	[catalog_group_id] [nvarchar](36) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
 CONSTRAINT [PK_bitrix_groups_products_links] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[business_regions]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[business_regions](
	[id] [nvarchar](36) NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[parent_id] [nvarchar](36) NOT NULL,
	[delete_mark] [smallint] NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[bx_1cprod]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[bx_1cprod](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[bx_id] [int] NOT NULL,
	[parent_bx_id] [int] NOT NULL,
	[f1c_id] [varchar](255) NULL,
	[parent_1c_id] [varchar](255) NULL,
	[artikul] [varchar](255) NULL,
	[price] [decimal](28, 10) NULL,
	[amount] [decimal](28, 10) NULL,
	[usr] [int] NULL,
	[name] [varchar](512) NOT NULL,
	[bprice] [decimal](28, 10) NULL,
 CONSTRAINT [PK_bx_1cprod] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[bx_1csect]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[bx_1csect](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[bx_id] [int] NOT NULL,
	[name] [varchar](255) NOT NULL,
	[parent_bx_id] [int] NOT NULL,
	[f1c_id] [varchar](255) NULL,
	[parent_1c_id] [varchar](255) NULL,
 CONSTRAINT [PK_bx_1csect] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[bx_bsect]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[bx_bsect](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[bx_id] [int] NOT NULL,
	[name] [varchar](255) NOT NULL,
	[parent_bx_id] [int] NOT NULL,
	[f1c_id] [varchar](255) NULL,
	[link_1csect_bx_id] [int] NULL,
	[link_1csect_1c_id] [varchar](255) NULL,
 CONSTRAINT [PK_bx_bsect] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[cb_logs]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[cb_logs](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[dt] [datetime] NOT NULL,
	[caption] [varchar](255) NULL,
	[log_type] [varchar](20) NULL,
	[user_id] [int] NULL,
	[log_text] [varchar](max) NULL,
 CONSTRAINT [PK_cb_logs] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[cb_privilegies]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[cb_privilegies](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](255) NOT NULL,
	[quota_coeff] [int] NOT NULL,
 CONSTRAINT [PK_cb_privilegies] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[cb_settings]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[cb_settings](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](50) NOT NULL,
	[int_val] [int] NOT NULL,
	[str_val] [varchar](255) NOT NULL,
	[dt_val] [datetime] NOT NULL,
 CONSTRAINT [PK_cb_settings] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[cb_user_groups]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[cb_user_groups](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](255) NOT NULL,
	[code] [varchar](20) NOT NULL,
 CONSTRAINT [PK_cb_user_groups] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[cb_users]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[cb_users](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](255) NOT NULL,
	[ekfapikey] [varchar](255) NULL,
	[pwd] [varchar](255) NULL,
	[login] [varchar](50) NOT NULL,
	[privilegy_id] [int] NOT NULL,
	[user_group_id] [int] NULL,
 CONSTRAINT [PK_cb_users] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[cb_users_activity]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[cb_users_activity](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[object_name] [varchar](50) NOT NULL,
	[activity_counter] [int] NOT NULL,
	[user_id] [int] NOT NULL,
	[date] [datetime] NOT NULL,
 CONSTRAINT [PK_users_activity] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[company_prices]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[company_prices](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[price_type_id] [nvarchar](36) NOT NULL,
	[price] [numeric](15, 2) NOT NULL,
	[date] [datetime] NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[company_products_amounts]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[company_products_amounts](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[warehouse_id] [nvarchar](36) NOT NULL,
	[qnt] [numeric](15, 3) NOT NULL,
	[date] [datetime] NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[company_sales]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[company_sales](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[date] [datetime] NOT NULL,
	[doc_id] [nvarchar](36) NOT NULL,
	[doc_type] [nvarchar](100) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[partner_id] [nvarchar](36) NOT NULL,
	[warehouse_id] [nvarchar](36) NOT NULL,
	[qnt] [numeric](15, 3) NOT NULL,
	[sum] [numeric](15, 2) NOT NULL,
	[cost_price] [numeric](15, 2) NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[company_seasons_matrix]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[company_seasons_matrix](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[season_type] [nvarchar](50) NOT NULL,
	[season_date] [date] NOT NULL,
	[date] [datetime] NOT NULL,
	[season_percent] [numeric](8, 2) NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[completing_of_orders_headers]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[completing_of_orders_headers](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[order_id] [nvarchar](36) NOT NULL,
	[total_ordered_qnt] [decimal](15, 3) NULL,
	[total_ordered_sum] [decimal](15, 2) NULL,
	[total_ordered_percent] [decimal](6, 2) NULL,
	[total_shipped_qnt] [decimal](15, 3) NULL,
	[total_shipped_sum] [decimal](15, 2) NULL,
	[total_shipped_percent] [decimal](6, 2) NULL,
	[total_ready_to_shipment_qnt] [decimal](15, 3) NULL,
	[total_ready_to_shipment_sum] [decimal](15, 2) NULL,
	[total_ready_to_shipment_percent] [decimal](6, 2) NULL,
	[total_in_reserve_qnt] [decimal](15, 3) NULL,
	[total_in_reserve_sum] [decimal](15, 2) NULL,
	[total_in_reserve_percent] [decimal](6, 2) NULL,
	[total_available_qnt] [decimal](15, 3) NULL,
	[total_available_sum] [decimal](15, 2) NULL,
	[total_available_percent] [decimal](6, 2) NULL,
	[total_description] [text] NULL,
 CONSTRAINT [PK_completing_of_orders_headers] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[completing_of_orders_tables]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[completing_of_orders_tables](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[order_id] [nvarchar](36) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[product_state] [nvarchar](50) NOT NULL,
	[analogs_available] [smallint] NOT NULL,
	[ordered_qnt] [decimal](15, 3) NULL,
	[ordered_sum] [decimal](15, 2) NULL,
	[ordered_percent] [decimal](5, 2) NULL,
	[shipped_qnt] [decimal](15, 3) NULL,
	[shipped_sum] [decimal](15, 2) NULL,
	[shipped_percent] [decimal](5, 2) NULL,
	[ready_to_shipment_qnt] [decimal](15, 3) NULL,
	[ready_to_shipment_sum] [decimal](15, 2) NULL,
	[ready_to_shipment_percent] [decimal](5, 2) NULL,
	[in_reserve_qnt] [decimal](15, 3) NULL,
	[in_reserve_sum] [decimal](15, 2) NULL,
	[in_reserve_percent] [decimal](5, 2) NULL,
	[available_qnt] [decimal](15, 3) NULL,
	[available_sum] [decimal](15, 2) NULL,
	[available_percent] [decimal](5, 2) NULL,
	[description] [text] NULL,
 CONSTRAINT [PK_completing_of_orders_tables] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[consignees]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[consignees](
	[id] [nvarchar](36) NOT NULL,
	[name] [nvarchar](250) NOT NULL,
	[partner_id] [nvarchar](36) NOT NULL,
	[address] [nvarchar](500) NULL,
	[delete_mark] [smallint] NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[currencies]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[currencies](
	[id] [nvarchar](36) NOT NULL,
	[name] [nvarchar](50) NULL,
	[code] [nvarchar](3) NOT NULL,
 CONSTRAINT [PK_currencies] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[files_types]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[files_types](
	[id] [nvarchar](36) NULL,
	[code] [nvarchar](25) NULL,
	[name] [nvarchar](100) NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[information_systems]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[information_systems](
	[id] [int] NOT NULL,
	[name] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_information_systems] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[is_names_fields]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[is_names_fields](
	[id] [nvarchar](36) NULL,
	[code] [nvarchar](25) NULL,
	[name] [nvarchar](50) NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[is_products_groups]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[is_products_groups](
	[id] [nvarchar](36) NULL,
	[is_id] [nvarchar](36) NULL,
	[collapse_vendor_code] [smallint] NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[is_products_groups_fields]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[is_products_groups_fields](
	[id] [nvarchar](36) NOT NULL,
	[is_field_id] [nvarchar](36) NULL,
	[is_description] [text] NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[is_products_groups_files]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[is_products_groups_files](
	[id] [nvarchar](36) NOT NULL,
	[file_description] [text] NULL,
	[file_name] [nvarchar](100) NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[is_products_groups_properties]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[is_products_groups_properties](
	[id] [nvarchar](36) NOT NULL,
	[properties_id] [nvarchar](36) NULL,
	[filter_name] [nvarchar](100) NULL,
	[filter] [smallint] NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[orders]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[orders](
	[id] [nvarchar](36) NOT NULL,
	[number_1c] [nvarchar](10) NULL,
	[partner_id] [nvarchar](36) NOT NULL,
	[commercial_case_id] [nvarchar](36) NOT NULL,
	[contract_id] [nvarchar](36) NOT NULL,
	[date] [datetime] NULL,
	[organization_id] [nvarchar](36) NOT NULL,
	[warehouse_id] [nvarchar](36) NOT NULL,
	[doc_sum] [decimal](15, 2) NOT NULL,
	[delete_mark] [smallint] NOT NULL,
	[order_source] [nvarchar](50) NULL,
	[state] [nvarchar](50) NULL,
	[comment] [nvarchar](500) NULL,
	[order_variant] [nvarchar](50) NULL,
	[user_id] [nvarchar](36) NULL,
	[consignee_id] [nvarchar](36) NULL,
 CONSTRAINT [PK_orders] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[orders_products]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[orders_products](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[line_number] [int] NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[qnt] [decimal](15, 3) NOT NULL,
	[price] [decimal](15, 2) NOT NULL,
	[order_id] [nvarchar](36) NOT NULL,
	[discount] [decimal](5, 2) NOT NULL,
	[qnt_coefficient] [decimal](15, 3) NOT NULL,
	[price_discount] [decimal](15, 2) NOT NULL,
	[sum] [decimal](15, 2) NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[partners]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[partners](
	[id] [nvarchar](36) NOT NULL,
	[short_name] [nvarchar](150) NOT NULL,
	[code_1c] [nvarchar](5) NOT NULL,
	[business_region_id] [nvarchar](36) NOT NULL,
	[client_state] [nvarchar](50) NOT NULL,
	[delete_mark] [smallint] NOT NULL,
	[head_partner_id] [nvarchar](36) NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[price_types]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[price_types](
	[id] [nvarchar](36) NOT NULL,
	[name] [nvarchar](50) NOT NULL,
	[currency_id] [nvarchar](36) NOT NULL,
	[with_NDS] [smallint] NOT NULL,
 CONSTRAINT [PK_price_types] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[prices]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[prices](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[price_type_id] [nvarchar](36) NOT NULL,
	[currency_id] [nvarchar](36) NOT NULL,
	[price] [decimal](15, 2) NOT NULL,
 CONSTRAINT [PK_prices] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[product_groups]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[product_groups](
	[id] [nvarchar](36) NOT NULL,
	[name] [nvarchar](150) NULL,
	[parent_id] [nvarchar](36) NULL,
 CONSTRAINT [PK_product_groups] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[product_properties_values]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[product_properties_values](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[product_property_code] [nvarchar](50) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[pvalue] [text] NULL,
 CONSTRAINT [PK_product_properties_values] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[products]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products](
	[id] [nvarchar](36) NOT NULL,
	[vendor_code] [nvarchar](200) NULL,
	[short_name] [nvarchar](150) NULL,
	[name] [nvarchar](512) NULL,
	[product_group_id] [nvarchar](36) NULL,
	[bx_group_external_code] [nvarchar](36) NULL,
	[state] [nvarchar](50) NULL,
	[manufacturer] [nvarchar](50) NULL,
	[delete_mark] [smallint] NOT NULL,
	[manufactured_by_country] [nvarchar](50) NULL,
	[product_category] [nvarchar](50) NULL,
	[season_type] [nvarchar](50) NULL,
	[barcode] [nvarchar](13) NULL,
	[updated_at] [datetime] NULL,
 CONSTRAINT [PK_products] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[products_amounts]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products_amounts](
	[product_id] [nvarchar](36) NOT NULL,
	[warehouse_id] [nvarchar](36) NOT NULL,
	[quantity] [numeric](15, 3) NOT NULL,
	[id] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [PK_products_amounts] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[products_attached_files]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products_attached_files](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[file_path] [nvarchar](500) NOT NULL,
	[file_name] [nvarchar](150) NULL,
	[file_type] [nvarchar](50) NOT NULL,
	[sort_order] [int] NOT NULL,
	[file_updated] [smallint] NOT NULL,
	[file_extension] [nvarchar](10) NOT NULL,
	[MD5_control_sum] [nvarchar](32) NOT NULL,
 CONSTRAINT [PK_products_attached_files] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[products_groups_recommendations]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products_groups_recommendations](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[recomended_group_id] [nvarchar](36) NOT NULL,
	[targret_group_id] [nvarchar](36) NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[products_hierarchy_levels]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products_hierarchy_levels](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[parent_id] [nvarchar](36) NOT NULL,
	[parent_hierarchy_level] [int] NOT NULL,
	[product_hierarchy_level] [int] NOT NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[products_permissions]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products_permissions](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[information_system] [int] NOT NULL,
	[used] [smallint] NOT NULL,
 CONSTRAINT [PK_products_permissions] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[products_properties]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products_properties](
	[code] [nvarchar](50) NOT NULL,
	[name] [nvarchar](150) NULL,
 CONSTRAINT [PK_products_properties] PRIMARY KEY CLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[products_properties_permissions]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products_properties_permissions](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[product_id] [nvarchar](36) NOT NULL,
	[properties_id] [nvarchar](36) NOT NULL,
	[used] [smallint] NOT NULL,
	[information_system] [int] NOT NULL,
 CONSTRAINT [PK_products_properties_permissions] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[products_receipt]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products_receipt](
	[product_id] [nvarchar](36) NOT NULL,
	[warehouse_id] [nvarchar](36) NOT NULL,
	[quantity] [numeric](15, 3) NOT NULL,
	[date] [datetime] NOT NULL,
	[state] [nvarchar](50) NULL,
	[id] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [PK_products_receipt] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[quarterly_bonuses]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[quarterly_bonuses](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[partner_id] [nvarchar](36) NULL,
	[quarter] [smallint] NULL,
	[quarter_plan] [numeric](15, 2) NULL,
	[quarter_fact] [numeric](15, 2) NULL,
	[percentage_of_plan] [numeric](15, 2) NULL,
	[amount_price_special] [numeric](15, 2) NULL,
	[amount_price_segment] [numeric](15, 2) NULL,
	[percent_1_deficit] [nvarchar](50) NULL,
	[percent_2_deficit] [nvarchar](50) NULL,
	[percent_3_deficit] [nvarchar](50) NULL,
	[percent_4_deficit] [nvarchar](50) NULL,
	[quarterly_bonus_percent] [smallint] NULL,
	[quarterly_bonus] [numeric](15, 2) NULL,
 CONSTRAINT [PK_quarterly_bonuses] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tables_operating_state]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tables_operating_state](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[table_name] [nvarchar](150) NOT NULL,
	[in_use_by_1C] [smallint] NOT NULL,
	[in_use_by_cb] [smallint] NOT NULL,
 CONSTRAINT [PK_tables_operating_state] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[warehouses]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[warehouses](
	[id] [nvarchar](36) NOT NULL,
	[short_name] [nvarchar](50) NULL,
	[location] [text] NULL,
	[ims2_active] [tinyint] NOT NULL,
	[ims2_name] [nvarchar](150) NULL,
	[name] [nvarchar](150) NULL,
 CONSTRAINT [PK_warehouses] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  View [dbo].[cb_ekfgroup_cattree]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfgroup_cattree]
AS
SELECT        id, catalog_tree_id, catalog_group_id, catalog_group_parent_id, catalog_group_name
FROM            dbo.bitrix_groups_links
WHERE        (catalog_tree_id = '4766ca2d-731d-4ff4-8f49-991bfc067e07')

GO
/****** Object:  View [dbo].[cb_ekfgroup_add_1csect_to_bx]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfgroup_add_1csect_to_bx]
AS
SELECT        ISNULL(del_ekfgr_bx_sect.id, 'NULL') as id, ISNULL(ctree.catalog_group_name,'') AS name, ISNULL(ctree.catalog_group_parent_id,'NULL') AS parent_id
FROM            (SELECT        catalog_group_id AS id
                          FROM            dbo.cb_ekfgroup_cattree
                          EXCEPT
                          SELECT        f1c_id AS id
                          FROM            dbo.bx_1csect) AS del_ekfgr_bx_sect LEFT JOIN
                         dbo.cb_ekfgroup_cattree AS ctree ON ctree.catalog_group_id = del_ekfgr_bx_sect.id

GO
/****** Object:  View [dbo].[cb_ekfgroup_upd_1csect_to_bx]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfgroup_upd_1csect_to_bx]
AS
SELECT        TOP (500) ISNULL(b1cs.f1c_id, N'NULL') AS sid, ISNULL(b1cs.name, N'') AS bxname, ISNULL(ekft.catalog_group_name, N'') AS f1cname, ISNULL(b1cs.parent_1c_id, 
                         N'') AS bxparent_id, ISNULL(ekft.catalog_group_parent_id, N'NULL') AS f1cparent_id
FROM            dbo.cb_ekfgroup_cattree AS ekft INNER JOIN
                         dbo.bx_1csect AS b1cs ON ekft.catalog_group_id = b1cs.f1c_id
WHERE        (b1cs.parent_1c_id <> ekft.catalog_group_parent_id) AND (LEN(ekft.catalog_group_parent_id) = 36 OR
                         LEN(b1cs.parent_1c_id) = 36) OR
                         (b1cs.name <> ekft.catalog_group_name)

GO
/****** Object:  View [dbo].[cb_ekfgroup_toload_view]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfgroup_toload_view]
AS
SELECT DISTINCT pr.id, pr.vendor_code
FROM            dbo.products_permissions AS pp INNER JOIN
                         dbo.products AS pr ON pr.id = pp.product_id
WHERE        (pp.information_system = 3) AND (pp.used = 1)

GO
/****** Object:  View [dbo].[cb_ekfroup_del_from_bx_view]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfroup_del_from_bx_view]
AS
SELECT TOP (1000) dt.* FROM (
SELECT        f1c_id AS id, artikul AS vendor_code
FROM            dbo.bx_1cprod
EXCEPT
SELECT        id, vendor_code
FROM            dbo.cb_ekfgroup_toload_view) as dt

GO
/****** Object:  View [dbo].[cb_ekfgroup_new_from_1c_view]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO




CREATE VIEW [dbo].[cb_ekfgroup_new_from_1c_view]
AS
SELECT id, vendor_code
FROM
    dbo.cb_ekfgroup_toload_view
EXCEPT
SELECT        f1c_id as id, artikul as vendor_code
FROM            dbo.bx_1cprod 




GO
/****** Object:  View [dbo].[cb_ekfgroup_pr_amounts]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfgroup_pr_amounts]
AS
SELECT        SUM(quantity) AS quant, product_id
FROM            dbo.products_amounts
WHERE        (warehouse_id = 'e5e08b47-90d9-11e4-ba37-005056b80040')
GROUP BY product_id

GO
/****** Object:  View [dbo].[cb_ekfgroup_prod_links]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfgroup_prod_links]
AS
SELECT DISTINCT product_id, catalog_group_id
FROM            dbo.bitrix_groups_products_links
WHERE        (catalog_tree_id = '4766ca2d-731d-4ff4-8f49-991bfc067e07')

GO
/****** Object:  View [dbo].[cb_prods_base_prices]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_prods_base_prices]
AS
SELECT        product_id, MAX(price) AS price
FROM            dbo.prices
WHERE        (price_type_id = '6dcb3f5a-f670-11d8-b667-000a48086d14')
GROUP BY product_id

GO
/****** Object:  View [dbo].[cb_prods_ishop_prices]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [dbo].[cb_prods_ishop_prices]
AS
SELECT        product_id, MAX(price) AS price
FROM            dbo.prices
WHERE        (price_type_id = '9b3b39e2-e835-11e4-b153-005056b80040')
GROUP BY product_id


GO
/****** Object:  View [dbo].[cb_ekfgroup_to_updated_bx_1c]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfgroup_to_updated_bx_1c]
AS
SELECT        TOP (200) id, artikul, quant, base_price, ishop_price, bamount, bisprice, bbsprice, name, f1c_id, bx_group_external_code, product_group_id, short_name, 
                         parent_1c_id, bx_name, updated_at, last_ekfgr_exch_dt, dbo.getPPropsJSONArray(f1c_id) AS json_data, 
                         CAST(CASE WHEN updated_at > last_ekfgr_exch_dt THEN 1 ELSE 0 END AS smallint) AS to_update
FROM            (SELECT        bpr.id, ISNULL(bpr.artikul, '') AS artikul, ISNULL(pa.quant, 0) AS quant, ISNULL(pbp.price, 0) AS base_price, ISNULL(pisp.price, 0) AS ishop_price, 
                                                    ISNULL(bpr.amount, 0) AS bamount, ISNULL(bpr.price, 0) AS bisprice, ISNULL(bpr.bprice, 0) AS bbsprice, ISNULL(p.name, '') AS name, ISNULL(p.id, 
                                                    'NULL') AS f1c_id, ISNULL(epl.catalog_group_id, 'NULL') AS bx_group_external_code, ISNULL(p.product_group_id, 'NULL') AS product_group_id, 
                                                    ISNULL(p.short_name, '') AS short_name, ISNULL(bpr.parent_1c_id, 'NULL') AS parent_1c_id, ISNULL(bpr.name, '') AS bx_name, ISNULL(p.updated_at, 
                                                    GETDATE()) AS updated_at, dbo.getLastEkfgrExchngDate() AS last_ekfgr_exch_dt
                          FROM            dbo.products AS p LEFT OUTER JOIN
                                                    dbo.cb_ekfgroup_pr_amounts AS pa ON p.id = pa.product_id LEFT OUTER JOIN
                                                    dbo.cb_prods_base_prices AS pbp ON p.id = pbp.product_id LEFT OUTER JOIN
                                                    dbo.cb_prods_ishop_prices AS pisp ON p.id = pisp.product_id LEFT OUTER JOIN
                                                    dbo.cb_ekfgroup_prod_links AS epl ON p.id = epl.product_id INNER JOIN
                                                    dbo.cb_ekfgroup_toload_view AS tld ON p.id = tld.id INNER JOIN
                                                    dbo.bx_1cprod AS bpr ON bpr.f1c_id = p.id AND bpr.artikul = p.vendor_code AND (ISNULL(pa.quant, 0) <> ISNULL(bpr.amount, 0) OR
                                                    ISNULL(pisp.price, 0) <> ISNULL(bpr.price, 0) OR
                                                    ISNULL(pbp.price, 0) <> ISNULL(bpr.bprice, 0) OR
                                                    epl.catalog_group_id <> bpr.parent_1c_id AND (LEN(epl.catalog_group_id) = 36 OR
                                                    LEN(bpr.parent_1c_id) = 36) OR
                                                    bpr.name <> p.name OR
                                                    p.updated_at > dbo.getLastEkfgrExchngDate())) AS upwp
WHERE        (LEN(artikul) > 0) AND (LEN(f1c_id) > 0)

GO
/****** Object:  View [dbo].[cb_ekfgroup_del_1csect_from_bx]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfgroup_del_1csect_from_bx]
AS
SELECT        TOP (1000) ISNULL(del_ekfgr_bx_sect.id, 'NULL') as id, ISNULL(bx1csct.name,'') as name, ISNULL(bx1csct.bx_id, 0) AS bx_id
FROM            (SELECT        f1c_id AS id
                          FROM            dbo.bx_1csect
                          EXCEPT
                          SELECT        catalog_group_id AS id
                          FROM            dbo.cb_ekfgroup_cattree) AS del_ekfgr_bx_sect LEFT JOIN
                         dbo.bx_1csect AS bx1csct ON bx1csct.f1c_id = del_ekfgr_bx_sect.id

GO
/****** Object:  View [dbo].[cd_1c_ekf_product_sect]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [dbo].[cd_1c_ekf_product_sect]
AS
SELECT        id
FROM            dbo.product_groups
WHERE        (dbo.sectionIsParented(id, '33048d6b-a7a0-4d22-9d84-4e712686ae2e') = 1)


GO
/****** Object:  View [dbo].[cd_1c_ekf_production]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*WHERE        (dbo.sectionIsParented(product_group_id, '33048d6b-a7a0-4d22-9d84-4e712686ae2e') = 1)*/
CREATE VIEW [dbo].[cd_1c_ekf_production]
AS
SELECT        p.id, p.vendor_code, p.short_name, p.name, p.product_group_id, p.bx_group_external_code, p.state, p.manufacturer, p.delete_mark, p.manufactured_by_country, 
                         p.product_category, p.season_type, p.barcode
FROM            dbo.products AS p INNER JOIN
                         dbo.cd_1c_ekf_product_sect AS c ON p.product_group_id = c.id

GO
/****** Object:  View [dbo].[cb_ekfgroup_new_pr_from_1c_wprops]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ekfgroup_new_pr_from_1c_wprops]
AS
SELECT        TOP (10) id, vendor_code, short_name, name, product_group_id, json_data, fp0, fp1, fp2, fp3, fp4, quant, base_price, ishop_price, bx_group_external_code
FROM            (SELECT        pr.id, ISNULL(pr.vendor_code, N'NULL') AS vendor_code, ISNULL(pr.short_name, N'') AS short_name, ISNULL(pr.name, N'') AS name, 
                                                    ISNULL(pr.product_group_id, N'NULL') AS product_group_id, dbo.getPPropsJSONArray(n1c.id) AS json_data, dbo.getProdImgFilePath(n1c.id, 0, 0) AS fp0, 
                                                    dbo.getProdImgFilePath(n1c.id, 1, 0) AS fp1, dbo.getProdImgFilePath(n1c.id, 2, 0) AS fp2, dbo.getProdImgFilePath(n1c.id, 3, 0) AS fp3, 
                                                    dbo.getProdImgFilePath(n1c.id, 4, 0) AS fp4, ISNULL(pa.quant, 0) AS quant, ISNULL(pbp.price, 0) AS base_price, ISNULL(pisp.price, 0) AS ishop_price, 
                                                    ISNULL(epl.catalog_group_id, N'NULL') AS bx_group_external_code
                          FROM            dbo.cb_ekfgroup_new_from_1c_view AS n1c LEFT OUTER JOIN
                                                    dbo.products AS pr ON n1c.id = pr.id LEFT OUTER JOIN
                                                    dbo.cb_ekfgroup_pr_amounts AS pa ON n1c.id = pa.product_id LEFT OUTER JOIN
                                                    dbo.cb_prods_base_prices AS pbp ON n1c.id = pbp.product_id LEFT OUTER JOIN
                                                    dbo.cb_prods_ishop_prices AS pisp ON n1c.id = pisp.product_id LEFT OUTER JOIN
                                                    dbo.cb_ekfgroup_prod_links AS epl ON n1c.id = epl.product_id) AS npwp
WHERE        (LEN(bx_group_external_code) = 36) AND (LEN(ISNULL(fp0, '')) > 0) AND (base_price > 0) AND (ishop_price > 0)

GO
/****** Object:  View [dbo].[cb_ims2_orders_completing_reports]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ims2_orders_completing_reports]
AS
SELECT     dbo.completing_of_orders_headers.total_ordered_qnt, dbo.completing_of_orders_headers.total_ordered_sum, 
                      dbo.completing_of_orders_headers.total_ordered_percent, dbo.completing_of_orders_headers.total_shipped_qnt, 
                      dbo.completing_of_orders_headers.total_shipped_sum, dbo.completing_of_orders_headers.total_shipped_percent, 
                      dbo.completing_of_orders_headers.total_ready_to_shipment_qnt, dbo.completing_of_orders_headers.total_ready_to_shipment_sum, 
                      dbo.completing_of_orders_headers.total_ready_to_shipment_percent, dbo.completing_of_orders_headers.total_in_reserve_qnt, 
                      dbo.completing_of_orders_headers.total_in_reserve_sum, dbo.completing_of_orders_headers.total_in_reserve_percent, 
                      dbo.completing_of_orders_headers.total_available_qnt, dbo.completing_of_orders_headers.total_available_sum, 
                      dbo.completing_of_orders_headers.total_available_percent, dbo.completing_of_orders_headers.total_description, dbo.completing_of_orders_tables.product_id, 
                      dbo.completing_of_orders_tables.product_state, dbo.completing_of_orders_tables.analogs_available, dbo.completing_of_orders_tables.ordered_qnt, 
                      dbo.completing_of_orders_tables.ordered_sum, dbo.completing_of_orders_tables.ordered_percent, dbo.completing_of_orders_tables.shipped_qnt, 
                      dbo.completing_of_orders_tables.shipped_sum, dbo.completing_of_orders_tables.shipped_percent, dbo.completing_of_orders_tables.ready_to_shipment_qnt, 
                      dbo.completing_of_orders_tables.ready_to_shipment_sum, dbo.completing_of_orders_tables.ready_to_shipment_percent, 
                      dbo.completing_of_orders_tables.in_reserve_qnt, dbo.completing_of_orders_tables.in_reserve_sum, dbo.completing_of_orders_tables.in_reserve_percent, 
                      dbo.completing_of_orders_tables.available_qnt, dbo.completing_of_orders_tables.available_sum, dbo.completing_of_orders_tables.available_percent, 
                      dbo.completing_of_orders_tables.description, dbo.products.short_name, dbo.products.vendor_code, dbo.completing_of_orders_tables.id, 
                      dbo.completing_of_orders_headers.order_id
FROM         dbo.completing_of_orders_tables INNER JOIN
                      dbo.completing_of_orders_headers ON dbo.completing_of_orders_tables.order_id = dbo.completing_of_orders_headers.order_id INNER JOIN
                      dbo.products ON dbo.completing_of_orders_tables.product_id = dbo.products.id

GO
/****** Object:  View [dbo].[cb_ims2_orders_completing_reports_header]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_ims2_orders_completing_reports_header]
AS
SELECT        total_ordered_qnt, total_ordered_sum, total_ordered_percent, total_shipped_qnt, total_shipped_sum, total_shipped_percent, total_ready_to_shipment_qnt, 
                         total_ready_to_shipment_sum, total_ready_to_shipment_percent, total_in_reserve_qnt, total_in_reserve_sum, total_in_reserve_percent, total_available_qnt, 
                         total_available_sum, total_available_percent, total_description, order_id, id
FROM            dbo.completing_of_orders_headers

GO
/****** Object:  View [dbo].[cb_products_props]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[cb_products_props]
AS
SELECT        dbo.product_properties_values.poduct_property_code, dbo.product_properties_values.product_id, dbo.product_properties_values.value
FROM            dbo.product_properties_values INNER JOIN
                         dbo.products ON dbo.product_properties_values.product_id = dbo.products.id

GO
ALTER TABLE [dbo].[bx_1cprod] ADD  CONSTRAINT [DF_bx_1cprod_bx_id]  DEFAULT ((-1)) FOR [bx_id]
GO
ALTER TABLE [dbo].[bx_1cprod] ADD  CONSTRAINT [DF_bx_1cprod_parent_bx_id]  DEFAULT ((-1)) FOR [parent_bx_id]
GO
ALTER TABLE [dbo].[bx_1cprod] ADD  CONSTRAINT [DF_bx_1cprod_f1c_id]  DEFAULT ('') FOR [f1c_id]
GO
ALTER TABLE [dbo].[bx_1cprod] ADD  CONSTRAINT [DF_bx_1cprod_name]  DEFAULT ('') FOR [name]
GO
ALTER TABLE [dbo].[bx_1csect] ADD  CONSTRAINT [DF_bx_1csect_bx_id]  DEFAULT ((-1)) FOR [bx_id]
GO
ALTER TABLE [dbo].[bx_1csect] ADD  CONSTRAINT [DF_bx_1csect_name]  DEFAULT ('') FOR [name]
GO
ALTER TABLE [dbo].[bx_1csect] ADD  CONSTRAINT [DF_bx_1csect_parent_bx_id]  DEFAULT ((-1)) FOR [parent_bx_id]
GO
ALTER TABLE [dbo].[bx_1csect] ADD  CONSTRAINT [DF_bx_1csect_f1c_id]  DEFAULT ('') FOR [f1c_id]
GO
ALTER TABLE [dbo].[bx_bsect] ADD  CONSTRAINT [DF_bx_bsect_bx_id]  DEFAULT ((-1)) FOR [bx_id]
GO
ALTER TABLE [dbo].[bx_bsect] ADD  CONSTRAINT [DF_bx_bsect_name]  DEFAULT ('') FOR [name]
GO
ALTER TABLE [dbo].[bx_bsect] ADD  CONSTRAINT [DF_bx_bsect_parent_bx_id]  DEFAULT ((-1)) FOR [parent_bx_id]
GO
ALTER TABLE [dbo].[bx_bsect] ADD  CONSTRAINT [DF_bx_bsect_f1c_id]  DEFAULT ('') FOR [f1c_id]
GO
ALTER TABLE [dbo].[cb_logs] ADD  CONSTRAINT [DF_cb_logs_dt]  DEFAULT (getdate()) FOR [dt]
GO
ALTER TABLE [dbo].[cb_privilegies] ADD  CONSTRAINT [DF_cb_privilegies_quota_coeff]  DEFAULT ((1)) FOR [quota_coeff]
GO
ALTER TABLE [dbo].[cb_settings] ADD  CONSTRAINT [DF_cb_settings_int_val]  DEFAULT ((0)) FOR [int_val]
GO
ALTER TABLE [dbo].[cb_settings] ADD  CONSTRAINT [DF_cb_settings_str_val]  DEFAULT ('') FOR [str_val]
GO
ALTER TABLE [dbo].[cb_settings] ADD  CONSTRAINT [DF_cb_settings_dt_val]  DEFAULT (getdate()) FOR [dt_val]
GO
ALTER TABLE [dbo].[cb_users_activity] ADD  CONSTRAINT [DF_users_activity_activity_counter]  DEFAULT ((0)) FOR [activity_counter]
GO
ALTER TABLE [dbo].[cb_users_activity] ADD  CONSTRAINT [DF_cb_users_activity_date]  DEFAULT (getdate()) FOR [date]
GO
ALTER TABLE [dbo].[is_products_groups] ADD  CONSTRAINT [DF_is_products_groups_collapse_vendor_code]  DEFAULT ((0)) FOR [collapse_vendor_code]
GO
ALTER TABLE [dbo].[is_products_groups_properties] ADD  CONSTRAINT [DF_is_products_groups_properties_filter]  DEFAULT ((0)) FOR [filter]
GO
ALTER TABLE [dbo].[orders_products] ADD  CONSTRAINT [DF_orders_products_discount]  DEFAULT ((0)) FOR [discount]
GO
ALTER TABLE [dbo].[orders_products] ADD  CONSTRAINT [DF_orders_products_quantity_coefficient]  DEFAULT ((1)) FOR [qnt_coefficient]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT ((0)) FOR [delete_mark]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT (sysdatetime()) FOR [updated_at]
GO
ALTER TABLE [dbo].[products_properties_permissions] ADD  CONSTRAINT [DF_products_properties_permissions_used]  DEFAULT ((0)) FOR [used]
GO
ALTER TABLE [dbo].[cb_logs]  WITH CHECK ADD  CONSTRAINT [FK_cb_logs_cb_users] FOREIGN KEY([user_id])
REFERENCES [dbo].[cb_users] ([id])
GO
ALTER TABLE [dbo].[cb_logs] CHECK CONSTRAINT [FK_cb_logs_cb_users]
GO
ALTER TABLE [dbo].[cb_users]  WITH CHECK ADD  CONSTRAINT [FK_cb_users_cb_privilegies] FOREIGN KEY([privilegy_id])
REFERENCES [dbo].[cb_privilegies] ([id])
GO
ALTER TABLE [dbo].[cb_users] CHECK CONSTRAINT [FK_cb_users_cb_privilegies]
GO
ALTER TABLE [dbo].[cb_users]  WITH CHECK ADD  CONSTRAINT [FK_cb_users_cb_user_groups] FOREIGN KEY([user_group_id])
REFERENCES [dbo].[cb_user_groups] ([id])
GO
ALTER TABLE [dbo].[cb_users] CHECK CONSTRAINT [FK_cb_users_cb_user_groups]
GO
ALTER TABLE [dbo].[cb_users_activity]  WITH CHECK ADD  CONSTRAINT [FK_users_activity_users_activity] FOREIGN KEY([user_id])
REFERENCES [dbo].[cb_users] ([id])
GO
ALTER TABLE [dbo].[cb_users_activity] CHECK CONSTRAINT [FK_users_activity_users_activity]
GO
ALTER TABLE [dbo].[products_amounts]  WITH CHECK ADD  CONSTRAINT [FK_products_amounts_warehouses] FOREIGN KEY([warehouse_id])
REFERENCES [dbo].[warehouses] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[products_amounts] CHECK CONSTRAINT [FK_products_amounts_warehouses]
GO
ALTER TABLE [dbo].[products_receipt]  WITH CHECK ADD  CONSTRAINT [FK_products_receipt_warehouses] FOREIGN KEY([warehouse_id])
REFERENCES [dbo].[warehouses] ([id])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[products_receipt] CHECK CONSTRAINT [FK_products_receipt_warehouses]
GO
/****** Object:  Trigger [dbo].[product_groups_modified]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[product_groups_modified] 
   ON  [dbo].[product_groups]
   AFTER  INSERT,DELETE,UPDATE
AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='delta'
    -- Insert statements for trigger here

END

GO
ALTER TABLE [dbo].[product_groups] ENABLE TRIGGER [product_groups_modified]
GO
/****** Object:  Trigger [dbo].[product_properties_values_modified]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[product_properties_values_modified] 
   ON  [dbo].[product_properties_values]
   AFTER  INSERT,DELETE,UPDATE
AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='delta'
    -- Insert statements for trigger here

END

GO
ALTER TABLE [dbo].[product_properties_values] ENABLE TRIGGER [product_properties_values_modified]
GO
/****** Object:  Trigger [dbo].[trg_dbo_products_af]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE TRIGGER [dbo].[trg_dbo_products_af]
ON [dbo].[products]
AFTER UPDATE 
AS
Begin
    UPDATE [dbo].[products]
    SET [updated_at] = SYSDATETIME()
    FROM Inserted i
    WHERE i.ID = [dbo].[products].id

    end


GO
ALTER TABLE [dbo].[products] ENABLE TRIGGER [trg_dbo_products_af]
GO
/****** Object:  Trigger [dbo].[products_amounts_modified]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[products_amounts_modified] 
   ON  [dbo].[products_amounts]
   AFTER  INSERT,DELETE,UPDATE
AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='delta'
    -- Insert statements for trigger here

END

GO
ALTER TABLE [dbo].[products_amounts] ENABLE TRIGGER [products_amounts_modified]
GO
/****** Object:  Trigger [dbo].[products_attached_files_modified]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[products_attached_files_modified] 
   ON  [dbo].[products_attached_files]
   AFTER  INSERT,DELETE,UPDATE
AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='delta'
    -- Insert statements for trigger here

END

GO
ALTER TABLE [dbo].[products_attached_files] ENABLE TRIGGER [products_attached_files_modified]
GO
/****** Object:  Trigger [dbo].[products_permissions_modified]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[products_permissions_modified] 
   ON  [dbo].[products_permissions]
   AFTER  INSERT,DELETE,UPDATE
AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='delta'
    -- Insert statements for trigger here

END

GO
ALTER TABLE [dbo].[products_permissions] ENABLE TRIGGER [products_permissions_modified]
GO
/****** Object:  Trigger [dbo].[products_properties_modified]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[products_properties_modified] 
   ON  [dbo].[products_properties]
   AFTER  INSERT,DELETE,UPDATE
AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='delta'
    -- Insert statements for trigger here

END

GO
ALTER TABLE [dbo].[products_properties] ENABLE TRIGGER [products_properties_modified]
GO
/****** Object:  Trigger [dbo].[products_receipt_modified]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE TRIGGER [dbo].[products_receipt_modified] 
   ON  [dbo].[products_receipt]
   AFTER  INSERT,DELETE,UPDATE
AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
	UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='delta'
    -- Insert statements for trigger here

END

GO
ALTER TABLE [dbo].[products_receipt] ENABLE TRIGGER [products_receipt_modified]
GO
/****** Object:  Trigger [dbo].[tos_update]    Script Date: 18.09.2016 12:07:44 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE TRIGGER [dbo].[tos_update] 
   ON  [dbo].[tables_operating_state] 
   AFTER UPDATE
AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	DECLARE @tname varchar(255), @old1c smallint, @new1c smallint;

	SELECT @tname=d.table_name, @old1c=d.in_use_by_1C from deleted d
	SELECT @new1c=d.in_use_by_1C from inserted d
	IF @tname='all' and @old1c=1 and @new1c=0 BEGIN
		UPDATE tables_operating_state SET in_use_by_cb=1 WHERE table_name='delta'
	END

END

GO
ALTER TABLE [dbo].[tables_operating_state] ENABLE TRIGGER [tos_update]
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_add_1csect_to_bx'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_add_1csect_to_bx'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "bitrix_groups_links"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 135
               Right = 257
            End
            DisplayFlags = 280
            TopColumn = 1
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_cattree'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_cattree'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_del_1csect_from_bx'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_del_1csect_from_bx'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "npwp"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 135
               Right = 253
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_new_pr_from_1c_wprops'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_new_pr_from_1c_wprops'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "products_amounts"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 135
               Right = 212
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 12
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_pr_amounts'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_pr_amounts'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "bitrix_groups_products_links"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 135
               Right = 218
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_prod_links'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_prod_links'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[9] 2[32] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "upwp"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 135
               Right = 253
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_to_updated_bx_1c'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_to_updated_bx_1c'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "pp"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 182
               Right = 232
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "pr"
            Begin Extent = 
               Top = 6
               Left = 270
               Bottom = 135
               Right = 453
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_toload_view'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_toload_view'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "ekft"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 135
               Right = 257
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "b1cs"
            Begin Extent = 
               Top = 33
               Left = 401
               Bottom = 162
               Right = 575
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_upd_1csect_to_bx'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfgroup_upd_1csect_to_bx'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfroup_del_from_bx_view'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ekfroup_del_from_bx_view'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[56] 4[13] 2[19] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "completing_of_orders_tables"
            Begin Extent = 
               Top = 21
               Left = 475
               Bottom = 339
               Right = 719
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "completing_of_orders_headers"
            Begin Extent = 
               Top = 20
               Left = 83
               Bottom = 344
               Right = 371
            End
            DisplayFlags = 280
            TopColumn = 1
         End
         Begin Table = "products"
            Begin Extent = 
               Top = 29
               Left = 875
               Bottom = 352
               Right = 1085
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ims2_orders_completing_reports'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ims2_orders_completing_reports'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "completing_of_orders_headers"
            Begin Extent = 
               Top = 0
               Left = 37
               Bottom = 335
               Right = 538
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 1155
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ims2_orders_completing_reports_header'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_ims2_orders_completing_reports_header'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "prices"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 135
               Right = 212
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 12
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_prods_base_prices'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_prods_base_prices'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "product_properties_values"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 177
               Right = 246
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "products"
            Begin Extent = 
               Top = 6
               Left = 284
               Bottom = 135
               Right = 467
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_products_props'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cb_products_props'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "p"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 135
               Right = 266
            End
            DisplayFlags = 280
            TopColumn = 9
         End
         Begin Table = "c"
            Begin Extent = 
               Top = 138
               Left = 38
               Bottom = 216
               Right = 212
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cd_1c_ekf_production'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'cd_1c_ekf_production'
GO
USE [master]
GO
ALTER DATABASE [MDS] SET  READ_WRITE 
GO
