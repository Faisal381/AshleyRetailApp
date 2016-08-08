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
            Order order = await db.Orders.Where(x=>x.InvoiceNumber == invoiceNumber).FirstOrDefaultAsync();
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