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
    public class CustomerInfo
    {
        public String uname{get; set;}
        public String password{get; set;}
        public String mobno{get; set;}
        public String emailid{get; set;}
        public String addr{get; set;}
        public float latitude { get; set;}
        public float longitude { get; set; }
    }
}
