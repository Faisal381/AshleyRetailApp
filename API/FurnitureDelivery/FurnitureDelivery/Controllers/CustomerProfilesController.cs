using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using FurnitureDelivery.Models;

namespace FurnitureDelivery.Controllers
{
    /// <summary>
    /// Customer profiles API
    /// Headers:
    /// Content-type: application/json
    /// </summary>
    public class CustomerProfilesController : ApiController
    {
        private FurnitureDeliveryContext db = new FurnitureDeliveryContext();

        /// <summary>
        /// Method returns profile connected to phone number or 
        /// new profile created using phone number
        /// </summary>
        /// <param name="phone">{String} Phone Number (spaces are ignored)</param>
        /// <returns>Customer Profile JSON object or error</returns>
        [HttpGet]
        [ResponseType(typeof(CustomerProfile))]
        public async Task<IHttpActionResult> GetCustomerProfile(string phone)
        {
            phone = phone.Replace(" ","");//ignore spaces

            var result = db.CustomerProfiles.Where(x => x.PhoneNumber == phone).Include(item => item.ContactAddresses).Select(q => q).ToList();

            if(result.Count() == 0) //no records - add record
            {
                CustomerProfile customerProfile = new CustomerProfile
                {
                    PhoneNumber = phone,
                };

                Validate(customerProfile);
                if (!ModelState.IsValid)
                    return BadRequest(ModelState);

                db.CustomerProfiles.Add(customerProfile);
                await db.SaveChangesAsync();

                //return customerProfile;
                return CreatedAtRoute("DefaultApi", new { phone = customerProfile.PhoneNumber }, customerProfile);
            }

            //return result.FirstOrDefault();
            var customerProf = result.FirstOrDefault();
            return CreatedAtRoute("DefaultApi", new { phone = customerProf.PhoneNumber }, customerProf);
        }


        /// <summary>
        /// Method (PUT) to update CustomerProfile record. 
        /// All profile properties put will rewrite. 
        /// We can also manipulate the addresses connected to user. If  address record doesn't have {id} or id is unknown then 
        /// method unbind old address and connect the new one
        /// </summary>
        /// <param name="phone">{String} Phone number (spaces are ignored)</param>
        /// <param name="customerProfile">{CustomerProfile JSON} Customer profile object with properties filled</param>
        /// <returns></returns>
        [ResponseType(typeof(CustomerProfile))]
        public async Task<IHttpActionResult> PutCustomerProfile(String phone, CustomerProfile customerProfile)
        {
            phone = phone.Replace(" ", "");//ignore spaces

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            await updateCustomerProfile(phone, customerProfile);

            Validate(customerProfile);
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            await db.SaveChangesAsync();


            var returnProfile = db.CustomerProfiles.Include(p => p.ContactAddresses).Where(x => x.PhoneNumber == phone).FirstOrDefault();
            return CreatedAtRoute("DefaultApi", new { phone = customerProfile.PhoneNumber }, returnProfile);
        }


        [NonAction]
        public async Task updateCustomerProfile(String phone, CustomerProfile customerProfile)
        {
            phone.Replace(" ", "");//ignore spaces
            var profile = db.CustomerProfiles.Include(p => p.ContactAddresses).Where(x => x.PhoneNumber == phone).FirstOrDefault();

            if (profile == null)//if no profile then create
            {
                customerProfile.PhoneNumber.Replace(" ", "");//ignore spaces
                db.CustomerProfiles.Add(customerProfile);
            }
            else //update object
            {
                customerProfile.Id = profile.Id;
                customerProfile.PhoneNumber.Replace(" ", "");//ignore spaces

                db.Entry(profile).CurrentValues.SetValues(customerProfile);

                await UpdateAddresses(customerProfile.Id, customerProfile.ContactAddresses);
            }

        }


        [NonAction]
        private async Task UpdateAddresses(int profileId, ICollection<CustomerAddress> addresses)
        {
            //delete references to not used addresses anymore
            var allAddresses = db.CustomerAddresses.Where(x => x.CustomerProfileId == profileId);
            
            foreach (var ad in allAddresses)
            {
                if(addresses.Where(x=>x.Id == ad.Id).Count() == 0)//adres not exist in new list, unbind from user
                {
                    ad.CustomerProfileId = null;
                    //db.CustomerAddresses.Attach(ad)
                    ad.CustomerProfileId = null;
                    //await db.SaveChangesAsync();//.Attach(ad);
                }
            }

            await db.SaveChangesAsync();

            int addressesCount = db.CustomerAddresses.Where(x => x.CustomerProfileId == profileId).Count();

            //deleted not used addresses
            //add new addresses
            addresses.ToList().ForEach(x => x.CustomerProfileId = profileId);

            foreach (var address in addresses)
            {
                var dbAddress = db.CustomerAddresses.Where(x => x.Id == address.Id && x.CustomerProfileId == profileId).FirstOrDefault();
                if(dbAddress == null || addressesCount < 3)//not exists with this id
                {
                    //address.CustomerProfileId = profileId;
                    db.CustomerAddresses.Add(address);
                    addressesCount++;
                }
            }
            await db.SaveChangesAsync();

        }

    }
}