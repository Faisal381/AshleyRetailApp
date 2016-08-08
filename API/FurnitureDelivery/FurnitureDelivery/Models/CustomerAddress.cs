using Newtonsoft.Json;
using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.Serialization;
using System.Web.Http.Description;

namespace FurnitureDelivery.Models
{
    /// <summary>
    /// Address instance
    /// </summary>
    public class CustomerAddress
    {
        /// <summary>
        /// 
        /// </summary>
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }

        /// <summary>
        /// 
        /// </summary>
        [ApiExplorerSettings(IgnoreApi = true)]
        public int? CustomerProfileId { get; set; }

        /// <summary>
        /// Profile connected to this address
        /// </summary>
        [ForeignKey("CustomerProfileId")]
        [ApiExplorerSettings(IgnoreApi = true)]
        public virtual CustomerProfile CustomerProfile { get; set; }

        /// <summary>
        /// The name of address
        /// </summary>
        public String Name { get; set; }

        /// <summary>
        /// Latitude
        /// </summary>
        public double Lat { get; set; }
        /// <summary>
        /// Longitude
        /// </summary>
        public double Lon { get; set; }

    }
}