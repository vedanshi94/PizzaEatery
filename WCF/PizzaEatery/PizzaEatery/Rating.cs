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
    public class Rating
    {
        public int pizza_id { get; set; }
        public String date { get; set; }
        public int rating { get; set; }
        public String comment { get; set; }
        public String uname { get; set; }
    }
}
