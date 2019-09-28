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
    public class CustomPizza
    {
        public String type { get; set; }
        public String name { get; set; }
        public int pizza_id { get; set; }
        public int base_id { get; set; }
        public int cheese_id { get; set; }
        public int sauce_id { get; set; }
        public int toppings_id { get; set; }
        public int veggies_id { get; set; }
        public String b_name { get; set; }
        public String c_name { get; set; }
        public String s_name { get; set; }
        public String t_name { get; set; }
        public String v_name { get; set; }
    }
}
