
using System.Web.Mvc;

namespace FurnitureDelivery.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            ViewBag.Title = "Home Page";

            return Redirect("Help");
        }
    }
}
