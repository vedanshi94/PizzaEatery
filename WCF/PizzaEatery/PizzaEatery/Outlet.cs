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
    public class Outlet
    {
        public int outlet_id { get; set; }
        public float lat { get; set; }
        public float lng { get; set; }
        public String adr { get; set; }
        public float dist { get; set; }
    }
}
