namespace FurnitureDelivery.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Initial2 : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Orders",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        InvoiceNumber = c.String(nullable: false),
                        CustomerAddressId = c.Int(nullable: false),
                        CustomerProfileId = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.CustomerAddresses", t => t.CustomerAddressId)
                .ForeignKey("dbo.CustomerProfiles", t => t.CustomerProfileId)
                .Index(t => t.CustomerAddressId)
                .Index(t => t.CustomerProfileId);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Orders", "CustomerProfileId", "dbo.CustomerProfiles");
            DropForeignKey("dbo.Orders", "CustomerAddressId", "dbo.CustomerAddresses");
            DropIndex("dbo.Orders", new[] { "CustomerProfileId" });
            DropIndex("dbo.Orders", new[] { "CustomerAddressId" });
            DropTable("dbo.Orders");
        }
    }
}
