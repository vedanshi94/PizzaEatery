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
    public class OrderInfo
    {
        public int order_id { get; set; }
        public float total { get; set; }
        public String status { get; set; }
        public String date { get; set; }
        public int outlet_id { get; set; }
        public String uname { get; set; }
        public String addr { get; set; }
        public int deliveryboy_id { get; set; }
    }
}
