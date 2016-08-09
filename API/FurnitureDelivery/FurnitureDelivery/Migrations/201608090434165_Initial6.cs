namespace FurnitureDelivery.Migrations
{
    using System.Data.Entity.Migrations;
    
    public partial class Initial6 : DbMigration
    {
        public override void Up()
        {
            DropColumn("dbo.CustomerProfiles", "ContactAddressId");
        }
        
        public override void Down()
        {
            AddColumn("dbo.CustomerProfiles", "ContactAddressId", c => c.Int());
        }
    }
}
