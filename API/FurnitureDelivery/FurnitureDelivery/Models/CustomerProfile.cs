using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Runtime.Serialization;
using System.Web.Http.Description;

namespace FurnitureDelivery.Models
{
    /// <summary>
    /// User's profile in API
    /// </summary>
    public class CustomerProfile
    {
        /// <summary>
        /// 
        /// </summary>
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        [ApiExplorerSettings(IgnoreApi = true)]
        public int Id { get; set; }

        /// <summary>
        /// Phone number string
        /// </summary>
        [Required]
        [MinLength(7)]
        [MaxLength(12)]
        [Phone]
        public String PhoneNumber { get; set; }


        /// <summary>
        /// User's field (there is a validation attached)
        /// </summary>
        //[Required]
        [EmailAddress]
        public String Email { get; set; }

        /// <summary>
        /// User's Name
        /// </summary>
        public String FullName { get; set; }

        /// <summary>
        /// List of delivery addresses
        /// </summary>
        public virtual ICollection<CustomerAddress> DeliveryAddresses { get; set; }


        public CustomerProfile()
        {
            this.DeliveryAddresses = new List<CustomerAddress>();
        }

    }
}