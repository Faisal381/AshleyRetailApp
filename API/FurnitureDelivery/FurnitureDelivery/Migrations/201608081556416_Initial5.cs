namespace FurnitureDelivery.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Initial5 : DbMigration
    {
        public override void Up()
        {
            RenameColumn(table: "dbo.Orders", name: "CustomerAddressId", newName: "DeliveryAddressId");
            RenameIndex(table: "dbo.Orders", name: "IX_CustomerAddressId", newName: "IX_DeliveryAddressId");
            AddColumn("dbo.CustomerProfiles", "FullName", c => c.String());
            AddColumn("dbo.Orders", "DeviceId", c => c.String());
            DropColumn("dbo.CustomerAddresses", "Postcode");
            DropColumn("dbo.CustomerProfiles", "Name");
            DropColumn("dbo.CustomerProfiles", "SecondName");
        }
        
        public override void Down()
        {
            AddColumn("dbo.CustomerProfiles", "SecondName", c => c.String());
            AddColumn("dbo.CustomerProfiles", "Name", c => c.String());
            AddColumn("dbo.CustomerAddresses", "Postcode", c => c.String());
            DropColumn("dbo.Orders", "DeviceId");
            DropColumn("dbo.CustomerProfiles", "FullName");
            RenameIndex(table: "dbo.Orders", name: "IX_DeliveryAddressId", newName: "IX_CustomerAddressId");
            RenameColumn(table: "dbo.Orders", name: "DeliveryAddressId", newName: "CustomerAddressId");
        }
    }
}
