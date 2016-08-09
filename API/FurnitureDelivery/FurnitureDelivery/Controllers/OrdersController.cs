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
    /// Orders controller to handle all order queries
    /// Headers:
    /// Content-type: application/json
    /// </summary>
    public class OrdersController : ApiController
    {
        private FurnitureDeliveryContext db = new FurnitureDeliveryContext();

        /// <summary>
        /// Returns order with invoice number
        /// </summary>
        /// <param name="invoiceNumber">Invoice number string</param>
        /// <returns></returns>
        [ResponseType(typeof(Order))]
        public async Task<IHttpActionResult> GetOrder(string invoiceNumber)
        {
            Order order = await db.Orders.Where(x => x.InvoiceNumber == invoiceNumber).FirstOrDefaultAsync();
            if (order == null)
            {
                return NotFound();
            }

            return Ok(order);
        }



        /// <summary>
        /// Saves order to database. All fields are requred!
        /// </summary>
        /// <param name="order"></param>
        /// <returns></returns>
        [ResponseType(typeof(Order))]
        [HttpPost]
        [ActionName("PostOrder")]
        public async Task<IHttpActionResult> PostOrder(Order order)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Orders.Add(order);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = order.Id }, order);
        }


        /// <summary>
        /// Saves (or updates) order to database depending if invoice number exists in database.
        /// Also user's profile in order is updated
        /// All fields are requred!
        /// </summary>
        /// <param name="order"></param>
        /// <returns></returns>
        [HttpPost]
        [ResponseType(typeof(Order))]
        [ActionName("PostOrderWithProfileUpdate")]
        public async Task<IHttpActionResult> PostOrderWithProfileUpdate(Order order)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var existingOrder = db.Orders.Where(e => e.InvoiceNumber == order.InvoiceNumber).FirstOrDefault();

            if (existingOrder == null)
            {
                db.Orders.Add(order);
            }
            else
            {
                db.Entry(existingOrder).CurrentValues.SetValues(order);
            }

            await db.SaveChangesAsync();

            //updaste user profile
            CustomerProfilesController profileCOntroller = new CustomerProfilesController();
            await profileCOntroller.updateCustomerProfile(order.CustomerProfile.PhoneNumber, order.CustomerProfile);

            return CreatedAtRoute("DefaultApi", new { id = order.Id }, order);
        }


        private bool OrderExists(string invoiceNumber)
        {
            return db.Orders.Count(e => e.InvoiceNumber == invoiceNumber) > 0;
        }


        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}