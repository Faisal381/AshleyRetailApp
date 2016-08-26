namespace FurnitureDelivery.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class initial9 : DbMigration
    {
        public override void Up()
        {
            AlterColumn("dbo.Orders", "InvoiceNumber", c => c.String(nullable: false, maxLength: 8));
        }
        
        public override void Down()
        {
            AlterColumn("dbo.Orders", "InvoiceNumber", c => c.String(nullable: false));
        }
    }
}
