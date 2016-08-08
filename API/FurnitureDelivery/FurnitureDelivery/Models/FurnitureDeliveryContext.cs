using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.ModelConfiguration.Conventions;
using System.Linq;
using System.Web;

namespace FurnitureDelivery.Models
{
    public class FurnitureDeliveryContext : DbContext
    {
        // You can add custom code to this file. Changes will not be overwritten.
        // 
        // If you want Entity Framework to drop and regenerate your database
        // automatically whenever you change your model schema, please use data migrations.
        // For more information refer to the documentation:
        // http://msdn.microsoft.com/en-us/data/jj591621.aspx
    
        public FurnitureDeliveryContext() : base("name=FurnitureDeliveryContext")
        {
           this.Configuration.LazyLoadingEnabled = true;
        }

        public System.Data.Entity.DbSet<FurnitureDelivery.Models.CustomerProfile> CustomerProfiles { get; set; }

        //public System.Data.Entity.DbSet<FurnitureDelivery.Models.CustomerCity> CustomerCities { get; set; }

        public System.Data.Entity.DbSet<FurnitureDelivery.Models.CustomerAddress> CustomerAddresses { get; set; }


        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Conventions.Remove<OneToManyCascadeDeleteConvention>();

            //one-to-many 
            //modelBuilder.Entity<CustomerAddress>()
            //            .HasOptional<CustomerProfile>(s => s.CustomerProfile) // Student entity requires Standard 
            //            .WithMany(s => s.DeliveryAddresses); // Standard entity includes many Students entities


            //modelBuilder.Entity<CustomerProfile>()
            //   .HasOptional(s => s.ContactAddress) // Mark Address property optional in Student entity
            //   .WithRequired(ad => ad.CustomerProfileContact); // mark Student property as required in StudentAddress entity. Cannot save StudentAddress without Student



            //modelBuilder.Entity<CustomerAddress>()
            //   .HasOptional(s => s.CustomerProfileContact) // Mark Address property optional in Student entity
            //   .WithRequired(ad => ad.ContactAddress); // mark Student property as required in StudentAddress entity. Cannot save StudentAddress without Student

            //         modelBuilder.Entity<CustomerAddress>()
            //.HasOptional(s => s.CustomerProfileContact) // Mark Address property optional in Student entity
            //.WithRequired(ad => ad.ContactAddress); // mark Student property as required in StudentAddress entity. Cannot save StudentAddress without Student

            //modelBuilder.Entity<CustomerAddress>()
            //.HasRequired<CustomerProfile>(s => s.CustomerProfile) // Student entity requires Standard 
            //.WithMany(s => s.DeliveryAddresses); // Standard entity includes many Students entities


            //modelBuilder.Entity<CustomerProfile>()
            //    .HasMany<CustomerAddress>(s => s.DeliveryAddresses)
            //    .WithOptional(c => c.CustomerProfile);

        }

        public System.Data.Entity.DbSet<FurnitureDelivery.Models.Order> Orders { get; set; }

        public System.Data.Entity.DbSet<FurnitureDelivery.Models.DeliveryLog> DeliveryLogs { get; set; }
    }
}
