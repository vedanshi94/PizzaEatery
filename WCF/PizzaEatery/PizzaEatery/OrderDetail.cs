using System;
using System.Data;
using System.Configuration;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.HtmlControls;
using System.Web.UI.WebControls;
using System.Web.UI.WebControls.WebParts;
using System.Xml.Linq;

namespace PizzaEatery
{
    public class OrderDetail
    {
        public String name { get; set; }
        public float price { get; set; }
        public int quant { get; set; }
        public int pizza_id { get; set; }
        public float total { get; set; }
        public String image { get; set; }
        public int order_id { get; set; }
        public String type { get; set; }
    }
}
