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
    /// Delivery logs
    /// Headers:
    /// Content-type: application/json
    /// </summary>
    public class DeliveryLogsController : ApiController
    {
        private FurnitureDeliveryContext db = new FurnitureDeliveryContext();

        /// <summary>
        /// GET: api/DeliveryLogs
        /// </summary>
        /// <returns></returns>
        public IQueryable<DeliveryLog> GetDeliveryLogs()
        {
            return db.DeliveryLogs;
        }

        /// <summary>
        /// GET: api/DeliveryLogs/5
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [ResponseType(typeof(DeliveryLog))]
        public async Task<IHttpActionResult> GetDeliveryLog(int id)
        {
            DeliveryLog deliveryLog = await db.DeliveryLogs.FindAsync(id);
            if (deliveryLog == null)
            {
                return NotFound();
            }

            return Ok(deliveryLog);
        }

        ///// <summary>
        ///// PUT: api/DeliveryLogs/5
        ///// </summary>
        ///// <param name="id"></param>
        ///// <param name="deliveryLog"></param>
        ///// <returns></returns>
        //[ResponseType(typeof(void))]
        //public async Task<IHttpActionResult> PutDeliveryLog(int id, DeliveryLog deliveryLog)
        //{
        //    if (!ModelState.IsValid)
        //    {
        //        return BadRequest(ModelState);
        //    }

        //    if (id != deliveryLog.Id)
        //    {
        //        return BadRequest();
        //    }

        //    db.Entry(deliveryLog).State = EntityState.Modified;

        //    try
        //    {
        //        await db.SaveChangesAsync();
        //    }
        //    catch (DbUpdateConcurrencyException)
        //    {
        //        if (!DeliveryLogExists(id))
        //        {
        //            return NotFound();
        //        }
        //        else
        //        {
        //            throw;
        //        }
        //    }

        //    return StatusCode(HttpStatusCode.NoContent);
        //}

        /// <summary>
        /// POST: api/DeliveryLogs
        /// </summary>
        /// <param name="deliveryLog"></param>
        /// <returns></returns>
        [ResponseType(typeof(DeliveryLog))]
        public async Task<IHttpActionResult> PostDeliveryLog(DeliveryLog deliveryLog)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.DeliveryLogs.Add(deliveryLog);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = deliveryLog.Id }, deliveryLog);
        }

        /// <summary>
        /// DELETE: api/DeliveryLogs/5s
        /// </summary>
        /// <param name="id"></param>
        /// <returns></returns>
        [ResponseType(typeof(DeliveryLog))]
        public async Task<IHttpActionResult> DeleteDeliveryLog(int id)
        {
            DeliveryLog deliveryLog = await db.DeliveryLogs.FindAsync(id);
            if (deliveryLog == null)
            {
                return NotFound();
            }

            db.DeliveryLogs.Remove(deliveryLog);
            await db.SaveChangesAsync();

            return Ok(deliveryLog);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool DeliveryLogExists(int id)
        {
            return db.DeliveryLogs.Count(e => e.Id == id) > 0;
        }
    }
}