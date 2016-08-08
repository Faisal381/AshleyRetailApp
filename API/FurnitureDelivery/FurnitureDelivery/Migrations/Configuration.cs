namespace FurnitureDelivery.Migrations
{
    using Models;
    using System;
    using System.Collections.Generic;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;

    internal sealed class Configuration : DbMigrationsConfiguration<FurnitureDelivery.Models.FurnitureDeliveryContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        List<CustomerAddress> list2 = new List<CustomerAddress>
            {
                new CustomerAddress { Name="Address 4", Lat =  51.1321233, Lon = 12.122123  },
                new CustomerAddress { Name="Address 5", Lat =  51.1328283, Lon = 12.123423  },
                new CustomerAddress { Name="Address 6", Lat =  51.1355233, Lon = 12.324445  },
            };


        List<CustomerAddress> list3 = new List<CustomerAddress>
            {
                new CustomerAddress { Name="Address 7", Lat =  51.1321233, Lon = 12.122123  },
                new CustomerAddress { Name="Address 8", Lat =  51.1321243, Lon = 12.123423  },
                new CustomerAddress { Name="Address 9", Lat =  51.1321277, Lon = 12.324445 },
            };

        List<CustomerAddress> list4 = new List<CustomerAddress>
            {
                new CustomerAddress { Name="Address 10", Lat =  51.1321233, Lon = 12.122123 },
                new CustomerAddress { Name="Address 11", Lat =  51.8908989, Lon = 12.890890 },
                new CustomerAddress { Name="Address 12", Lat =  51.8921233, Lon = 12.304445 },
            };


        List<CustomerAddress> list1 = new List<CustomerAddress>
            {
                new CustomerAddress { Name="Address 1", Lat =  51.1321233, Lon = 12.122123 },
                new CustomerAddress { Name="Address 2", Lat =  11.1321233, Lon = 12.122123  },
                new CustomerAddress { Name="Address 3", Lat =  23.1321233, Lon = 12.324445 },
            };


        protected override void Seed(FurnitureDelivery.Models.FurnitureDeliveryContext context)
        {

            var prof1 = new CustomerProfile { FullName = "Profile 1", Email = "p1@aim.co", PhoneNumber = "123456789", DeliveryAddresses = list1 };
            var prof2 = new CustomerProfile { FullName = "Profile 2", Email = "p2@aim.co", PhoneNumber = "223456789", DeliveryAddresses = list2 };
            var prof3 = new CustomerProfile { FullName = "Profile 3", Email = "p3@aim.co", PhoneNumber = "323456789", DeliveryAddresses = list3 };
            var prof4 = new CustomerProfile { FullName = "Profile 4", Email = "p4@aim.co", PhoneNumber = "423456789", DeliveryAddresses = list4 };


            //context.CustomerAddresses.AddOrUpdate(x => x.Id, list1.ToArray());
            //context.CustomerAddresses.AddOrUpdate(x => x.Id, list2.ToArray());
            //context.CustomerAddresses.AddOrUpdate(x => x.Id, list3.ToArray());
            //context.CustomerAddresses.AddOrUpdate(x => x.Id, list4.ToArray());


            //list1.ForEach(x => x.CustomerProfileId = prof1.Id);
            //list2.ForEach(x => x.CustomerProfileId = prof2.Id);
            //list3.ForEach(x => x.CustomerProfileId = prof3.Id);
            //list4.ForEach(x => x.CustomerProfileId = prof4.Id);


            context.CustomerProfiles.AddOrUpdate(x => x.Id, prof1, prof2, prof3, prof4 );


            Order o1 = new Order
            {
                InvoiceNumber = "123",
                DeliveryAddress = list1[0],
                CustomerProfile = prof1

            };

            Order o2 = new Order
            {
                InvoiceNumber = "222",
                DeliveryAddress = list1[0],
                CustomerProfile = prof1

            };

            Order o3 = new Order
            {
                InvoiceNumber = "555",
                DeliveryAddress = list1[0],
                CustomerProfile = prof1

            };

            context.Orders.AddOrUpdate(x => x.Id, o1, o2, o3 );


            DeliveryLog dl = new DeliveryLog
            {
                Timestamp = "0",
                Action = "Log",
                DeviceId = "123"

            };
            context.DeliveryLogs.AddOrUpdate(x => x.Id, dl);
            //todo add addresses

        }


    }
}
