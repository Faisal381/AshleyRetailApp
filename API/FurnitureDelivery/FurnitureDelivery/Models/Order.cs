﻿using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.Serialization;
using System.Web.Http.Description;

namespace FurnitureDelivery.Models
{
    /// <summary>
    /// Order in API
    /// </summary>
    public class Order
    {
        /// <summary>
        /// Key
        /// </summary>
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [ApiExplorerSettings(IgnoreApi = true)]
        [JsonIgnore]
        public int Id { get; set; }

        /// <summary>
        /// Invoice number, also known as Order number
        /// </summary>
        [Required]
        [MinLength(4)]
        [MaxLength(8)]
        [RegularExpression("([A-Z a-z]{2})[0-9]{2,6}")]
        public String InvoiceNumber { get; set; }

        /// <summary>
        /// Invoice number, also known as Order number
        /// </summary>
        public String DeviceId { get; set; }

        /// <summary>
        /// 
        /// </summary>
        [JsonIgnore]
        public int DeliveryAddressId { get; set; }
        
        /// <summary>
        /// Delivery address
        /// </summary>
        [ForeignKey("DeliveryAddressId")]
        public virtual CustomerAddress DeliveryAddress{ get; set; }

        /// <summary>
        /// 
        /// </summary>
        [JsonIgnore]
        public int CustomerProfileId { get; set; }

        /// <summary>
        /// Customer who is order owner (who orders)
        /// </summary>
        [ForeignKey("CustomerProfileId")]
        [ApiExplorerSettings(IgnoreApi = true)]
        public virtual CustomerProfile CustomerProfile { get; set; }

        /// <summary>
        /// Which address is delivery address
        /// </summary>
        [NotMapped]
        public int? DeliveryAddressNumber { get; set; }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        //[JsonIgnore]
        public Order Clone()
        {
            return (Order)this.MemberwiseClone();
        }
    }
}