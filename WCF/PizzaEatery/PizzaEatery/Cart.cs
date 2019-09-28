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
    public class Cart
    {
        public String uname { get; set; }
        public int pizza_id { get; set; }
        public int quantity { get; set; }
        public String name { get; set; }
        public float price { get; set; }
        public String img { get; set; }
        public float total { get; set; }

    }
}
