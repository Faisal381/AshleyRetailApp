using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Web.Http.Description;

namespace FurnitureDelivery.Models
{
    /// <summary>
    /// Deliver log class
    /// </summary>
    public class DeliveryLog
    {
        /// <summary>
        /// Key
        /// </summary>
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [ApiExplorerSettings(IgnoreApi = true)]
        public int Id { get; set; }
        
        /// <summary>
        /// Timestamp
        /// </summary>
        public String Timestamp { get; set; }

        /// <summary>
        /// Action
        /// </summary>
        public String Action { get; set; }

        /// <summary>
        /// Device Id
        /// </summary>
        public String DeviceId { get; set; }
    }
}