﻿using System;
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
    public class Cheese
    {
        public int cheese_id { get; set; }
        public String name { get; set; }
        public float price { get; set; }
        public String img { get; set; }
    }
}
