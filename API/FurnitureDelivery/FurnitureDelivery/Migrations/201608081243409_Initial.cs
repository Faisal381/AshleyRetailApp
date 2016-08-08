namespace FurnitureDelivery.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Initial : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.CustomerAddresses",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        CustomerProfileId = c.Int(),
                        Postcode = c.String(),
                        Name = c.String(),
                        Lat = c.Double(nullable: false),
                        Lon = c.Double(nullable: false),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.CustomerProfiles", t => t.CustomerProfileId)
                .Index(t => t.CustomerProfileId);
            
            CreateTable(
                "dbo.CustomerProfiles",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        PhoneNumber = c.String(nullable: false, maxLength: 12),
                        Email = c.String(),
                        Name = c.String(),
                        SecondName = c.String(),
                        ContactAddressId = c.Int(),
                    })
                .PrimaryKey(t => t.Id);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.CustomerAddresses", "CustomerProfileId", "dbo.CustomerProfiles");
            DropIndex("dbo.CustomerAddresses", new[] { "CustomerProfileId" });
            DropTable("dbo.CustomerProfiles");
            DropTable("dbo.CustomerAddresses");
        }
    }
}
