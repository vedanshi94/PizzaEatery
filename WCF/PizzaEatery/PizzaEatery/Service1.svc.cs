using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Data.SqlClient;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web;
using System.Net;
using System.Net.Mail;
using System.Data;

namespace PizzaEatery
{

    // NOTE: If you change the class name "Service1" here, you must also update the reference to "Service1" in Web.config and in the associated .svc file.
    public class Service1 : IService1
    {
        SqlConnection con = new SqlConnection("Data Source =YAREE-PC\\SQLEXPRESS; Initial Catalog=Project; User ID='sa'; password='yaree'");

        public string GetData(int value)
        {
            return string.Format("You entered: {0}", value);
        }

        public CompositeType GetDataUsingDataContract(CompositeType composite)
        {
            if (composite.BoolValue)
            {
                composite.StringValue += "Suffix";
            }
            return composite;
        }
        public String Authentication(CustomerInfo ci)
       {
           int flag = 0;
          // SqlConnection con = new SqlConnection("Data Source =VEDANSHI; Initial Catalog=Project; User ID='vedanshi'; password='vedanshi'");
           con.Open();
           SqlCommand com = new SqlCommand("select UserName from UserDetail where UserName='"+ci.uname+"'",con);
           SqlDataReader dr = com.ExecuteReader();
           
           while (dr.Read())
           {
               flag = 1;     
           }
           dr.Close();
           if (flag == 0)
           {
               com = new SqlCommand("insert into UserDetail values ('"+ci.uname+"','"+ci.password+"','customer')", con);
               com.ExecuteNonQuery();

               com = new SqlCommand("Select UserId from UserDetail where UserName='" + ci.uname + "'", con);
               SqlDataReader dr1=com.ExecuteReader();
               dr1.Read();
               int uid = (int)dr1["UserId"];
               dr1.Close();
               com = new SqlCommand("insert into Cust_detail values (NULL,'"+ci.mobno+"','"+uid+"','"+ci.emailid+"',NULL,NULL)", con);
               com.ExecuteNonQuery();

               return "a";  
           }
           else
               return "b";
        }

        public Pizza GetOnePizzaInfo(int pizza_id)
        {
            Pizza pz = new Pizza();
            con.Open();
            SqlCommand com = new SqlCommand("Select Pizza_type from PizzaId where Pizza_id='" + pizza_id + "'", con);
            SqlDataReader dr = com.ExecuteReader();
            dr.Read();
            String type = dr["Pizza_type"].ToString();
            dr.Close();
            if (type.Equals("Standard"))
            {
                com = new SqlCommand("Select Pizza_name, PizzaImg from Standard_pizza where Pizza_id='" + pizza_id + "'", con);
                dr = com.ExecuteReader();
                dr.Read();
                pz.name = dr[0].ToString();
                pz.img = dr[1].ToString();
            }
            else
            {
                com = new SqlCommand("Select Pizza_name from Custom_pizza where Pizza_id='" + pizza_id + "'", con);
                dr = com.ExecuteReader();
                dr.Read();
                pz.name = dr[0].ToString();
                pz.img = "custom";
            }
            return pz;
        }

        public int PlaceOrder(OrderInfo oi, List<OrderDetail> list, String uname)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + uname + "'", con);
            SqlDataReader dr = com.ExecuteReader();
            dr.Read();
            int uid = (int)dr["UserId"];
            dr.Close();
            com = new SqlCommand("Select Cust_id from Cust_detail where UserId='" + uid + "'", con);
            dr = com.ExecuteReader();
            dr.Read();
            int cid = (int)dr["Cust_id"];
            dr.Close();
            //insert into PizzaId OUTPUT INSERTED.Pizza_id values(@Pizza_type)
            com = new SqlCommand("Insert into Order_history(Deliveryboy_id,Cust_id,total_amt,Date,Outlet_id,Status) OUTPUT INSERTED.Order_id  values(null,@Cust_id, @total_amt, @Date, @Outlet_id, @Status)", con);
            com.Parameters.Add(new SqlParameter("Cust_id", cid));
            com.Parameters.Add(new SqlParameter("total_amt", oi.total));
            com.Parameters.Add(new SqlParameter("Date", oi.date));
            com.Parameters.Add(new SqlParameter("Outlet_id", oi.outlet_id));
            com.Parameters.Add(new SqlParameter("Status", "Paid"));
            int or = (int)com.ExecuteScalar();

            dr.Close();
            int i = 0;
            while (i < list.Count)
            {
                com = new SqlCommand("Insert into Order_detail values (@Order_id,@Pizza_id, @Quantity)", con);
                com.Parameters.Add(new SqlParameter("Order_id", or));
                com.Parameters.Add(new SqlParameter("Pizza_id", list[i].pizza_id));
                com.Parameters.Add(new SqlParameter("Quantity", list[i].quant));
                com.ExecuteNonQuery();
                i++;
            }
            return or;
        }

        public CustomerInfo GetProf(String uname)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + uname + "'", con);
            SqlDataReader dr = com.ExecuteReader();
            dr.Read();
            int uid = (int)dr["UserId"];
            dr.Close();
            com = new SqlCommand("Select EmailId, ContactNo from Cust_detail where UserId='" + uid + "'", con);
            dr = com.ExecuteReader();
            dr.Read();
            CustomerInfo ci = new CustomerInfo();
            ci.emailid = (String)dr["EmailId"];
            ci.mobno = (String)dr["ContactNo"];
            dr.Close();
            ci.uname = uname;
            return ci;
        }

        public int UpdatePwd(String uname, String password, String oldpwd)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Update UserDetail SET Password='"+password+"' where UserName='"+uname+"' and Password='"+oldpwd+"'", con);
            int i=com.ExecuteNonQuery();
            return i;
        }

        public String Login(String uname, String password, String type)
        {
            con.Open();
            if (type.Equals("deliveryboy"))
            {
                SqlCommand com = new SqlCommand("select * from UserDetail where UserName='" + uname + "' and Password='" + password + "' and Role='deliveryboy'", con);
                SqlDataReader dr = com.ExecuteReader();

                if (dr.Read())
                {
                    return uname;
                }
            }
            else if (type.Equals("customer"))
            {
                SqlCommand com = new SqlCommand("select * from UserDetail where UserName='" + uname + "' and Password='" + password + "' and Role='customer'", con);
                SqlDataReader dr = com.ExecuteReader();

                if (dr.Read())
                {
                    return uname;
                }
            }
            else
            {
                SqlCommand com = new SqlCommand("select * from UserDetail where UserName='" + uname + "' and Password='" + password + "' and Role='admin'", con);
                SqlDataReader dr = com.ExecuteReader();

                if (dr.Read())
                {
                    return uname;
                }
            }
            return "no user found";
        }

        public String SubCom(Rating rt)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + rt.uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("Select Cust_id from Cust_detail where UserId='" + uid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            int cid = (int)dr1["Cust_id"];
            dr1.Close();
            com = new SqlCommand("Select Pizza_id from Rating_Comment where Cust_id='" + cid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            int pid = (int)dr1["Pizza_id"];
            dr1.Close();
            if (pid == rt.pizza_id)
            {
                com = new SqlCommand("Update Rating_Comment SET Date='" + rt.date + "', Pizza_Rating='" + rt.rating + "', Comment='" + rt.comment + "' where Cust_id='" + cid + "' and Pizza_id='" + rt.pizza_id + "'", con);
                int i = com.ExecuteNonQuery();
                return "Updated";
            }
            else
            {
                com = new SqlCommand("insert into Rating_Comment values (@Pizza_id, @Date, @Cust_id, @Pizza_Rating, @Comment)", con);
                com.Parameters.Add(new SqlParameter("Pizza_id", rt.pizza_id));
                com.Parameters.Add(new SqlParameter("Date", rt.date));
                com.Parameters.Add(new SqlParameter("Cust_id", cid));
                com.Parameters.Add(new SqlParameter("Pizza_Rating", rt.rating));
                com.Parameters.Add(new SqlParameter("Comment", rt.comment));
                int i = com.ExecuteNonQuery();
                return "Inserted";
            }
        }

        public int DelAddr(CustomerInfo ci)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + ci.uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();


            com = new SqlCommand("Update Cust_detail SET Cust_Address='" + ci.addr + "',Latitude='"+ci.latitude+"',Longitude='"+ci.longitude+"' where UserId='" + uid + "'", con);
            int i=com.ExecuteNonQuery();
            return i;
        }

        public List<OrderInfo> GetOrderInfo(String uname)
        {
            List<OrderInfo> list = new List<OrderInfo>();
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("Select Cust_id from Cust_detail where UserId='" + uid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            int cid = (int)dr1["Cust_id"];
            dr1.Close();
            com = new SqlCommand("Select Order_id , Status , Date, total_amt from Order_history where Cust_id='" + cid + "'", con);
            dr1 = com.ExecuteReader();

            while (dr1.Read())
            {
                OrderInfo oi = new OrderInfo();
                oi.order_id = (int)dr1[0];
                oi.status = dr1[1].ToString();
                oi.date = dr1[2].ToString();
                oi.total = (float)dr1[3];
                list.Add(oi);
            }
            dr1.Close();
            return list;
        }

        public List<OrderDetail> GetOrderDetail(int order_id)
        {
            List<OrderDetail> list = new List<OrderDetail>();
            con.Open();
            SqlCommand com = new SqlCommand("Select Pizza_id , Quantity from Order_detail where Order_id='" + order_id + "'", con);
            SqlDataReader dr = com.ExecuteReader();
            SqlCommand cmd1, cmd2;
            SqlDataReader dr1;
            List<int> b = new List<int>();
            List<int> c = new List<int>();
            List<int> v = new List<int>();
            List<int> t = new List<int>();
            List<int> s = new List<int>();
            while (dr.Read())
            {
                OrderDetail od = new OrderDetail();
                od.order_id = order_id;
                od.pizza_id = (int)dr[0];
                od.quant = (int)dr[1];
                list.Add(od);
            }
            dr.Close();
            int i = 0, j = 0;
            while (i < list.Count)
            {
                cmd1 = new SqlCommand("Select Pizza_type from PizzaId where Pizza_id='" + list[i].pizza_id + "'", con);
                dr1 = cmd1.ExecuteReader();
                dr1.Read();
                list[i].type = dr1[0].ToString();
                dr1.Close();
                if (list[i].type.Equals("Standard"))
                {
                    com = new SqlCommand("Select Pizza_name , Price , PizzaImg from Standard_pizza where Pizza_id='" + list[i].pizza_id + "'", con);
                    dr = com.ExecuteReader();
                    dr.Read();
                    list[i].name = dr[0].ToString();
                    list[i].price = (float)dr[1];
                    list[i].total = (float)list[i].quant * list[i].price;
                    list[i].image = dr[2].ToString();
                    dr.Close();
                }
                else
                {
                    com = new SqlCommand("Select * from Custom_pizza where Pizza_id='" + list[i].pizza_id + "'", con);
                    dr = com.ExecuteReader();
                    dr.Read();
                    list[i].name = dr[1].ToString();
                    list[i].image = "custom";
                    if (dr[2].ToString().Equals(""))
                        b.Add(0);
                    else
                        b.Add((int)dr[2]);
                    if (dr[3].ToString().Equals(""))
                        c.Add(0);
                    else
                        c.Add((int)dr[3]);
                    if (dr[4].ToString().Equals(""))
                        v.Add(0);
                    else
                        v.Add((int)dr[4]);
                    if (dr[5].ToString().Equals(""))
                        t.Add(0);
                    else
                        t.Add((int)dr[5]);
                    if (dr[6].ToString().Equals(""))
                        s.Add(0);
                    else
                        s.Add((int)dr[6]);
                    dr.Close();
                    float bpr = 0, cpr = 0, vpr = 0, tpr = 0, spr = 0;
                    SqlDataReader dr2;
                    if (b[j] != 0)
                    {
                        cmd2 = new SqlCommand("select Price from Base_price where Base_id=" + b[j], con);
                        dr2 = cmd2.ExecuteReader();
                        dr2.Read();
                        bpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        bpr = 0;

                    if (c[j] != 0)
                    {
                        cmd2 = new SqlCommand("select Price from Cheese_price where Cheese_id=" + c[j], con);
                        dr2 = cmd2.ExecuteReader();
                        dr2.Read();
                        cpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        cpr = 0;

                    if (v[j] != 0)
                    {
                        cmd2 = new SqlCommand("select Price from Veggies_price where Veggies_id=" + v[j], con);
                        dr2 = cmd2.ExecuteReader();
                        dr2.Read();
                        vpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        vpr = 0;

                    if (t[j] != 0)
                    {
                        cmd2 = new SqlCommand("select Price from Toppings_price where Toppings_id=" + t[j], con);
                        dr2 = cmd2.ExecuteReader();
                        dr2.Read();
                        tpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        tpr = 0;
                    if (s[j] != 0)
                    {
                        cmd2 = new SqlCommand("select Price from Sauce_price where Sauce_id=" + s[j], con);
                        dr2 = cmd2.ExecuteReader();
                        dr2.Read();
                        spr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        spr = 0;
                    list[i].price = cpr + bpr + vpr + tpr + spr;
                    list[i].total = (float)list[i].quant * list[i].price;
                    j++;
                }
                i++;
                dr1.Close();
            }
            return list;
        }

        public List<Pizza> GetPizzaInfo()
        {
            List<Pizza> list = new List<Pizza>();
            con.Open();
            SqlCommand cmd = new SqlCommand("select * from Standard_pizza", con);
            SqlDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                Pizza pz = new Pizza();
                pz.pizza_id = (int)dr[0];
                pz.type = "standard";
                pz.name = dr[1].ToString();
                String price = dr[2].ToString();
                pz.price = float.Parse(price);

                pz.img = dr[3].ToString();
                list.Add(pz);
            }
            dr.Close();
            SqlDataReader dr1 = null;
            int i = 0;
            float avg;
            while (i < list.Count)
            {
                cmd = new SqlCommand("select Pizza_Rating from Rating_Comment where Pizza_id='" + list[i].pizza_id + "'", con);
                dr1 = cmd.ExecuteReader();
                int j = 0;
                float sum = 0;
                while (dr1.Read())
                {
                    sum = sum + (int)dr1[0];
                    j++;
                }

                if (j != 0)
                    avg = sum / j;
                else
                    avg = 0;
                list[i].rating = avg;
                dr1.Close();
                i++;
            }

            return list;

        }

        public List<Pizza> GetToprated()
        {
            List<Pizza> list = new List<Pizza>();
            List<int> b = new List<int>();
            List<int> c = new List<int>();
            List<int> v = new List<int>();
            List<int> t = new List<int>();
            List<int> s = new List<int>();
            con.Open();
            SqlCommand cmd = new SqlCommand("select * from Standard_pizza", con);
            SqlDataReader dr = cmd.ExecuteReader();
            int j = 0;
            while (dr.Read())
            {
                Pizza pz = new Pizza();
                pz.type = "standard";
                pz.pizza_id = (int)dr[0];
                pz.name = dr[1].ToString();
                String price = dr[2].ToString();
                pz.price = float.Parse(price);
                pz.img = dr[3].ToString();
                list.Add(pz);
                j++;
            }
            dr.Close();

            SqlCommand cmd1 = new SqlCommand("select * from Custom_pizza", con);
            SqlDataReader dr1 = cmd1.ExecuteReader();

            while (dr1.Read())
            {
                Pizza pz = new Pizza();
                pz.type = "custom";
                pz.pizza_id = (int)dr1[0];
                pz.name = dr1[1].ToString();
                if (dr1[2].ToString().Equals(""))
                    b.Add(0);
                else
                    b.Add((int)dr1[2]);
                if (dr1[3].ToString().Equals(""))
                    c.Add(0);
                else
                    c.Add((int)dr1[3]);
                if (dr1[4].ToString().Equals(""))
                    v.Add(0);
                else
                    v.Add((int)dr1[4]);
                if (dr1[5].ToString().Equals(""))
                    t.Add(0);
                else
                    t.Add((int)dr1[5]);
                if (dr1[6].ToString().Equals(""))
                    s.Add(0);
                else
                    s.Add((int)dr1[6]);
                list.Add(pz);
            }
            dr1.Close();
            SqlDataReader dr2;
            int i = 0;
            
            float bpr = 0, cpr = 0, vpr = 0, tpr = 0, spr = 0;
            while (j < list.Count)
            {

                if (b[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Base_price where Base_id=" + b[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    bpr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    bpr = 0;

                if (c[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Cheese_price where Cheese_id=" + c[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    cpr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    cpr = 0;

                if (v[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Veggies_price where Veggies_id=" + v[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    vpr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    vpr = 0;

                if (t[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Toppings_price where Toppings_id=" + t[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    tpr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    tpr = 0;
                if (s[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Sauce_price where Sauce_id=" + s[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    spr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    spr = 0;

                float price = cpr + bpr + vpr + tpr + spr;
                list[j].price = price;
                list[j].img = "custom";
                i++;
                j++;
            }
            dr1.Close();
            dr1 = null;
            i = 0;
            float avg;
            while (i < list.Count)
            {
                cmd = new SqlCommand("select Pizza_Rating from Rating_Comment where Pizza_id='" + list[i].pizza_id + "'", con);
                dr1 = cmd.ExecuteReader();
                j = 0;
                float sum = 0;
                while (dr1.Read())
                {
                    sum = sum + (int)dr1[0];
                    j++;
                }

                if (j != 0)
                    avg = sum / j;
                else
                    avg = 0;
                list[i].rating = avg;
                dr1.Close();
                i++;
            }
            list.Sort(delegate(Pizza p1, Pizza p2) { return p1.rating.CompareTo(p2.rating); });
            list.Reverse();
            return list;
        }

        public List<Base> GetBaseInfo()
        {
            List<Base> list = new List<Base>();
            con.Open();
            SqlCommand cmd = new SqlCommand("select * from Base_price", con);
            SqlDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                Base bs = new Base();
                bs.base_id = (int)dr[0];
                bs.name = dr[1].ToString();
                bs.price = (float)dr[2];
                bs.img = dr[3].ToString();
                list.Add(bs);
            }
            return list;
        }

        public List<Toppings> GetToppingsInfo()
        {
            List<Toppings> list = new List<Toppings>();
            con.Open();
            SqlCommand cmd = new SqlCommand("select * from Toppings_price", con);
            SqlDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                Toppings tp = new Toppings();
                tp.toppings_id = (int)dr[0];
                tp.name = dr[1].ToString();
                tp.price = (float)dr[2];
                tp.img = dr[3].ToString();
                list.Add(tp);
            }
            return list;
        }

        public List<Veggies> GetVeggiesInfo()
        {
            List<Veggies> list = new List<Veggies>();
            con.Open();
            SqlCommand cmd = new SqlCommand("select * from Veggies_price", con);
            SqlDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                Veggies vg = new Veggies();
                vg.veggies_id = (int)dr[0];
                vg.name = dr[1].ToString();
                vg.price = (float)dr[2];
                vg.img = dr[3].ToString();
                list.Add(vg);
            }
            return list;
        }

        public List<Sauce> GetSauceInfo()
        {
            List<Sauce> list = new List<Sauce>();
            con.Open();
            SqlCommand cmd = new SqlCommand("select * from Sauce_price", con);
            SqlDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                Sauce sc = new Sauce();
                sc.sauce_id = (int)dr[0];
                sc.name = dr[1].ToString();
                sc.price = (float)dr[2];
                sc.img = dr[3].ToString();
                list.Add(sc);
            }
            return list;
        }

        public List<Cheese> GetCheeseInfo()
        {
            List<Cheese> list = new List<Cheese>();
            con.Open();
            SqlCommand cmd = new SqlCommand("select * from Cheese_price", con);
            SqlDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                Cheese ch = new Cheese();
                ch.cheese_id = (int)dr[0];
                ch.name = dr[1].ToString();
                ch.price = (float)dr[2];
                ch.img = dr[3].ToString();
                list.Add(ch);
            }
            return list;
        }

        public String AddtoCart(Cart ct)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + ct.uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("Select Cust_id from Cust_detail where UserId='" + uid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            int cid = (int)dr1["Cust_id"];
            dr1.Close();

            com=new SqlCommand ("select * from Cart where Cust_id='"+ cid +"' and Pizza_id='"+ ct.pizza_id +"'",con);
            dr1=com.ExecuteReader();
            if (dr1.Read())
            {
                dr1.Close();
                com = new SqlCommand("Update Cart SET Quantity='" + ct.quantity + "' where Cust_id='" + cid + "' and Pizza_id='" + ct.pizza_id + "'", con);
                int k = com.ExecuteNonQuery();
                return "Already added";
            }
            dr1.Close();

            com = new SqlCommand("insert into Cart values('" +cid+ "','"+ ct.pizza_id +"','"+ ct.quantity +"')", con);
            int i=com.ExecuteNonQuery();
            if (i == 1)
                return "Added";
            else
                return "error";
        }

        public List<Rating> GetCom(int pizza_id)
        {
            List<Rating> list = new List<Rating>();
            List<int> c_id=new List<int>();
            con.Open();
            SqlCommand cmd = new SqlCommand("select * from Rating_Comment where Pizza_id='"+pizza_id+"'", con);
            SqlDataReader dr = cmd.ExecuteReader();

            while (dr.Read())
            {
                Rating rt = new Rating();
                rt.date = dr[1].ToString();
                c_id.Add((int)dr[2]);
                rt.comment = dr[4].ToString();
                rt.rating = (int)dr[3];
                list.Add(rt);
            }
            dr.Close();

            int uId,i = 0;
            while (i < list.Count)
            {
                cmd = new SqlCommand("select UserId from Cust_detail where Cust_id='" + c_id[i] + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                uId = (int)dr[0];
                dr.Close();

                cmd = new SqlCommand("select UserName from UserDetail where UserId='" + uId + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                list[i].uname=dr[0].ToString();
                dr.Close();
                i++;
            }
            return list;
        }

        public String RemoveFromCart(Cart ct)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + ct.uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("Select Cust_id from Cust_detail where UserId='" + uid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            int cid = (int)dr1["Cust_id"];
            dr1.Close();

            com = new SqlCommand("delete from Cart where Cust_id='" + cid + "' and Pizza_id='" + ct.pizza_id + "'", con);
            int i = com.ExecuteNonQuery();

            if (i == 1)
                return "Removed";
            else
                return "error";
        }

        public List<Outlet> GetOutlets(float lat, float lng)
        {
            List<Outlet> list = new List<Outlet>();
            con.Open();
            SqlCommand com = new SqlCommand("Select * from Outlet", con);
            SqlDataReader dr = com.ExecuteReader();
            Outlet ol = new Outlet();
            while (dr.Read())
            {
                ol = new Outlet();
                ol.outlet_id = (int)dr[0];
                ol.adr = dr[3].ToString();
                ol.lat = (float)dr[1];
                ol.lng = (float)dr[2];
                CalDist cd = new CalDist();
                ol.dist = (float)cd.DistanceBetweenPlaces(lng, lat, ol.lng, ol.lat);
                list.Add(ol);
            }

            List<Outlet> sorted = list.OrderBy(om => om.dist).ToList();
            return sorted;
        }

        public int CustomPizza(CustomPizza cp)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select Pizza_id from Standard_pizza where Pizza_name='" + cp.name + "'", con);
            SqlDataReader dr = com.ExecuteReader();
            if (dr.Read())
                return 0;
            dr.Close();
            com = new SqlCommand("Select Pizza_id from Custom_pizza where Pizza_name='" + cp.name + "'", con);
            dr = com.ExecuteReader();

            if (dr.Read())
            {
                return 0;
            }

            else
            {
                int pizza_id;
                com = new SqlCommand("insert into PizzaId OUTPUT INSERTED.Pizza_id values(@Pizza_type)", con);
                dr.Close();
                com.Parameters.Add(new SqlParameter("Pizza_type", "Custom"));
                pizza_id = (int)com.ExecuteScalar();
                com = new SqlCommand("insert into Custom_pizza values(@Pizza_id , @Pizza_name , @Base_id , @Cheese_id , @Veggies_id , @Toppings_id , @Sauce_id)", con);
                com.Parameters.Add(new SqlParameter("Pizza_id", pizza_id));
                com.Parameters.Add(new SqlParameter("Pizza_name", cp.name));
                com.Parameters.Add(new SqlParameter("Base_id", cp.base_id));
                if (cp.cheese_id == 0)
                {
                    com.Parameters.Add(new SqlParameter("Cheese_id", DBNull.Value));
                }
                else
                {
                    com.Parameters.Add(new SqlParameter("Cheese_id", cp.cheese_id));
                }
                if (cp.veggies_id == 0)
                {
                    com.Parameters.Add(new SqlParameter("Veggies_id", DBNull.Value));
                }
                else
                {
                    com.Parameters.Add(new SqlParameter("Veggies_id", cp.veggies_id));
                }
                if (cp.toppings_id == 0)
                {
                    com.Parameters.Add(new SqlParameter("Toppings_id", DBNull.Value));
                }
                else
                {
                    com.Parameters.Add(new SqlParameter("Toppings_id", cp.toppings_id));
                }
                if (cp.sauce_id == 0)
                {
                    com.Parameters.Add(new SqlParameter("Sauce_id", DBNull.Value));
                }
                else
                {
                    com.Parameters.Add(new SqlParameter("Sauce_id", cp.sauce_id));
                }
                com.ExecuteNonQuery();
                return pizza_id;
            }

        }

        public List<Cart> GetCart(String uname)
        {
            List<int> b = new List<int>();
            List<int> c = new List<int>();
            List<int> v = new List<int>();
            List<int> t = new List<int>();
            List<int> s = new List<int>();
            List<Cart> list = new List<Cart>();
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + uname + "'", con);
            SqlDataReader dr = com.ExecuteReader();
            dr.Read();
            int uid = (int)dr["UserId"];
            dr.Close();
            com = new SqlCommand("Select Cust_id from Cust_detail where UserId='" + uid + "'", con);
            dr = com.ExecuteReader();
            dr.Read();
            int cid = (int)dr["Cust_id"];
            dr.Close();
            com = new SqlCommand("Select 1 from Cart where Cust_id='" + cid + "'", con);
            dr = com.ExecuteReader();
            if (!dr.Read())
            {
                dr.Close();
                return list;
            }
            else
            {
                dr.Close();
                com = new SqlCommand("Select * from Cart where Cust_id='" + cid + "'", con);
                dr = com.ExecuteReader();
                while (dr.Read())
                {
                    Cart c1 = new Cart();
                    c1.pizza_id = (int)dr[1];
                    c1.quantity = (int)dr[2];
                    c1.uname = uname;
                    list.Add(c1);
                }
                dr.Close();

                int i = 0;
                int j = 0;
                while (i < list.Count)
                {
                    com = new SqlCommand("Select Pizza_type from PizzaId where Pizza_id='" + list[i].pizza_id + "'", con);
                    dr = com.ExecuteReader();
                    dr.Read();
                    String pizza_type = dr[0].ToString();
                    dr.Close();
                    if (pizza_type.Equals("Standard"))
                    {
                        com = new SqlCommand("Select Pizza_name , Price , PizzaImg from Standard_pizza where Pizza_id='" + list[i].pizza_id + "'", con);
                        dr = com.ExecuteReader();
                        dr.Read();
                        list[i].name = dr[0].ToString();
                        list[i].price = (float)dr[1];
                        list[i].total = (float)list[i].quantity * list[i].price;
                        list[i].img = dr[2].ToString();
                        dr.Close();
                    }
                    else
                    {
                        com = new SqlCommand("Select * from Custom_pizza where Pizza_id='" + list[i].pizza_id + "'", con);
                        dr = com.ExecuteReader();
                        dr.Read();
                        list[i].name = dr[1].ToString();
                        list[i].img = "custom";
                        if (dr[2].ToString().Equals(""))
                            b.Add(0);
                        else
                            b.Add((int)dr[2]);
                        if (dr[3].ToString().Equals(""))
                            c.Add(0);
                        else
                            c.Add((int)dr[3]);
                        if (dr[4].ToString().Equals(""))
                            v.Add(0);
                        else
                            v.Add((int)dr[4]);
                        if (dr[5].ToString().Equals(""))
                            t.Add(0);
                        else
                            t.Add((int)dr[5]);
                        if (dr[6].ToString().Equals(""))
                            s.Add(0);
                        else
                            s.Add((int)dr[6]);
                        dr.Close();
                        float bpr = 0, cpr = 0, vpr = 0, tpr = 0, spr = 0;
                        SqlDataReader dr2;
                        if (b[j] != 0)
                        {
                            com = new SqlCommand("select Price from Base_price where Base_id=" + b[j], con);
                            dr2 = com.ExecuteReader();
                            dr2.Read();
                            bpr = (float)dr2["Price"];
                            dr2.Close();
                        }
                        else
                            bpr = 0;

                        if (c[j] != 0)
                        {
                            com = new SqlCommand("select Price from Cheese_price where Cheese_id=" + c[j], con);
                            dr2 = com.ExecuteReader();
                            dr2.Read();
                            cpr = (float)dr2["Price"];
                            dr2.Close();
                        }
                        else
                            cpr = 0;

                        if (v[j] != 0)
                        {
                            com = new SqlCommand("select Price from Veggies_price where Veggies_id=" + v[j], con);
                            dr2 = com.ExecuteReader();
                            dr2.Read();
                            vpr = (float)dr2["Price"];
                            dr2.Close();
                        }
                        else
                            vpr = 0;

                        if (t[j] != 0)
                        {
                            com = new SqlCommand("select Price from Toppings_price where Toppings_id=" + t[j], con);
                            dr2 = com.ExecuteReader();
                            dr2.Read();
                            tpr = (float)dr2["Price"];
                            dr2.Close();
                        }
                        else
                            tpr = 0;
                        if (s[j] != 0)
                        {
                            com = new SqlCommand("select Price from Sauce_price where Sauce_id=" + s[j], con);
                            dr2 = com.ExecuteReader();
                            dr2.Read();
                            spr = (float)dr2["Price"];
                            dr2.Close();
                        }
                        else
                            spr = 0;
                        list[i].price = cpr + bpr + vpr + tpr + spr;
                        list[i].total = (float)list[i].quantity * list[i].price;
                        j++;
                    }
                    i++;
                }
                return list;
            }
        }

        public int UpdateQuant(Cart ct)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + ct.uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("Select Cust_id from Cust_detail where UserId='" + uid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            int cid = (int)dr1["Cust_id"];
            dr1.Close();

            com = new SqlCommand("Update Cart SET Quantity='" + ct.quantity + "' where Cust_id='"+ cid +"' and Pizza_id='"+ ct.pizza_id +"'", con);
            int i = com.ExecuteNonQuery();
            return i;
        }

        public DBoyInfo GetDBoyLoc(String uname)
        {
            con.Open();
            DBoyInfo db = new DBoyInfo();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("Select Cust_id from Cust_detail where UserId='" + uid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            int cid = (int)dr1["Cust_id"];
            dr1.Close();

            com = new SqlCommand("select max(Order_id) as Order_id from Order_history where Cust_id='" + cid + "' and Status='Assigned'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            if (dr1[0].ToString().Equals(""))
            {
                db.latitude = 0;
                db.longitude = 0;
                return db; 
            }
            int oId = (int)dr1[0];
            dr1.Close();
            com = new SqlCommand("select Deliveryboy_id from Order_history where Order_id='" + oId + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            if (dr1[0].ToString().Equals(""))
            {
                db.latitude = 0;
                db.longitude = 0;
                return db;
            }
            int dId = (int)dr1[0];
            dr1.Close();

            com = new SqlCommand("select Latitude,Longitude from Track where DeliveryBoy_id='" + dId + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            db.latitude = (float)dr1[0];
            db.longitude=(float)dr1[1];
            dr1.Close();

            return db;
        }

        public CustomerInfo GetCustLoc(String uname)
        {
            con.Open();
            CustomerInfo ci = new CustomerInfo();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("select Cust_Address,Latitude,Longitude from Cust_detail where UserId='" + uid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            ci.addr = dr1[0].ToString();
            ci.latitude = (float)dr1[1];
            ci.longitude = (float)dr1[2];
            dr1.Close();

            return ci;
        }

        public List<OrderInfo> GetAssgndOrder(String uname)
        {
            List<OrderInfo> list = new List<OrderInfo>();
            List<int> cid = new List<int>();
            List<int> uId = new List<int>();
            
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("Select DeliveryBoy_id from Track where UserId='" + uid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            int dId = (int)dr1[0];
            dr1.Close();
            com = new SqlCommand("Select Order_id , Cust_id , Date from Order_history where Deliveryboy_id='" + dId + "' and Status='Assigned'", con);
            dr1 = com.ExecuteReader();

            while (dr1.Read())
            {
                OrderInfo oi = new OrderInfo();
                oi.order_id = (int)dr1[0];
                cid.Add((int)dr1[1]);
                oi.date = dr1[2].ToString();
                list.Add(oi);
            }
            dr1.Close();

            for (int i = 0; i < cid.Count; i++)
            {
                com = new SqlCommand("Select Cust_Address,UserId from Cust_detail where Cust_id='" + cid[i] + "'", con);
                dr1 = com.ExecuteReader();
                dr1.Read();
                uId.Add((int)dr1[1]);
                list[i].addr = dr1[0].ToString();
                dr1.Close();
            }

            for (int i = 0; i < uId.Count; i++) 
            {
                com = new SqlCommand("Select UserName from UserDetail where UserId='" + uId[i] + "'", con);
                dr1 = com.ExecuteReader();
                dr1.Read();
                list[i].uname = dr1[0].ToString();
                dr1.Close();
            }
                return list;
        }

        public int UpdateDBoyLoc(DBoyInfo di)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + di.uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("Update Track SET Latitude='" + di.latitude + "',Longitude='" + di.longitude + "' where UserId='" + uid + "'", con);
            
            int i = com.ExecuteNonQuery();
            return i;
        }

        public int ClearCart(String uname)
        {
            con.Open();
            SqlCommand com = new SqlCommand("Select UserId from UserDetail where UserName='" + uname + "'", con);
            SqlDataReader dr1 = com.ExecuteReader();
            dr1.Read();
            int uid = (int)dr1["UserId"];
            dr1.Close();
            com = new SqlCommand("Select Cust_id from Cust_detail where UserId='" + uid + "'", con);
            dr1 = com.ExecuteReader();
            dr1.Read();
            int cid = (int)dr1["Cust_id"];
            dr1.Close();

            com = new SqlCommand("delete from Cart where Cust_id='" + cid + "'", con);
            int i = com.ExecuteNonQuery();

            return i;
        }

        public List<OrderInfo> ViewOrderList()
        {
            con.Open();
            List<int> cid = new List<int>();
            List<int> uId = new List<int>();
            List<OrderInfo> list = new List<OrderInfo>();
            SqlCommand com = new SqlCommand("Select * from Order_history where Status='Paid'", con);
            SqlDataReader dr = com.ExecuteReader();
            while (dr.Read())
            {
                OrderInfo oi = new OrderInfo();
                oi.order_id = (int)dr[0];
                cid.Add((int)dr[2]);
                oi.status = dr[6].ToString();
                oi.date = dr[4].ToString();
                oi.total = (float)dr[3];
                list.Add(oi);
            }
            dr.Close();

            for (int i = 0; i < cid.Count; i++)
            {
                com = new SqlCommand("Select UserId from Cust_detail where Cust_id='" + cid[i] + "'", con);
                dr = com.ExecuteReader();
                dr.Read();
                uId.Add((int)dr[0]);
                dr.Close();
            }

            for (int i = 0; i < uId.Count; i++)
            {
                com = new SqlCommand("Select UserName from UserDetail where UserId='" + uId[i] + "'", con);
                dr = com.ExecuteReader();
                dr.Read();
                list[i].uname = dr[0].ToString();
                dr.Close();
            }
            return list;
        }

        public List<OrderInfo> ViewAssignedOrderList()
        {
            con.Open();
            List<int> did = new List<int>();
            List<int> uId = new List<int>();
            List<OrderInfo> list = new List<OrderInfo>();
            SqlCommand com = new SqlCommand("Select * from Order_history where Status='Assigned'", con);
            SqlDataReader dr = com.ExecuteReader();
            while (dr.Read())
            {
                OrderInfo oi = new OrderInfo();
                oi.order_id = (int)dr[0];
                did.Add((int)dr[1]);
                oi.status = dr[6].ToString();
                oi.date = dr[4].ToString();
                oi.total = (float)dr[3];
                list.Add(oi);
            }
            dr.Close();

            for (int i = 0; i < did.Count; i++)
            {
                com = new SqlCommand("Select UserId from Track where DeliveryBoy_id='" + did[i] + "'", con);
                dr = com.ExecuteReader();
                dr.Read();
                uId.Add((int)dr[0]);
                dr.Close();
            }

            for (int i = 0; i < uId.Count; i++)
            {
                com = new SqlCommand("Select UserName from UserDetail where UserId='" + uId[i] + "'", con);
                dr = com.ExecuteReader();
                dr.Read();
                list[i].uname = dr[0].ToString();
                dr.Close();
            }

            return list;
        }

        public int AssignOrder(OrderInfo oi)
        {
            con.Open();
            SqlCommand cmd = new SqlCommand("update Order_history SET Deliveryboy_id='" + oi.deliveryboy_id + "', Status=@status where Order_id='" + oi.order_id + "'", con);
            cmd.Parameters.Add(new SqlParameter("status", "Assigned"));
            int i = cmd.ExecuteNonQuery();

            cmd = new SqlCommand("select DeviceId from Track where DeliveryBoy_id='"+oi.deliveryboy_id+"'", con);
            SqlDataReader dr = cmd.ExecuteReader();
            dr.Read();
            if (dr[0].ToString() == "")
            {
                return 0;
            }
            string deviceId = dr[0].ToString();
            dr.Close();
            AndroidGCMPushNotification apnGCM = new AndroidGCMPushNotification();

            apnGCM.SendNotification(deviceId,"Order Id: "+oi.order_id.ToString());

            return i;
        }


        public int CngStatusOfOrder(OrderInfo oi)
        {
            con.Open();
            SqlCommand cmd = new SqlCommand("update Order_history SET Status=@status where Order_id='" + oi.order_id + "'", con);
            cmd.Parameters.Add(new SqlParameter("status", "Delivered"));
            int i = cmd.ExecuteNonQuery();
            return i;
        }

        public int UpdateStdPizza(Pizza pz)
        {
            List<int> q = new List<int>();
            List<int> order_id = new List<int>();
            con.Open();
            SqlCommand cmd = new SqlCommand("select Price from Standard_pizza where Pizza_id='" + pz.pizza_id + "'", con);
            SqlDataReader dr = cmd.ExecuteReader();
            dr.Read();
            float price = (float)dr[0];
            dr.Close();
            cmd = new SqlCommand("update Standard_pizza SET Pizza_name=@name , Price=@price where Pizza_id='" + pz.pizza_id + "'", con);
            cmd.Parameters.Add(new SqlParameter("name", pz.name));
            cmd.Parameters.Add(new SqlParameter("price", pz.price));
            int i = cmd.ExecuteNonQuery();
            cmd = new SqlCommand("Select Quantity , Order_id from Order_detail where Pizza_id='" + pz.pizza_id + "'", con);
            dr = cmd.ExecuteReader();
            while (dr.Read())
            {
                q.Add((int)dr[0]);
                order_id.Add((int)dr[1]);
            }
            dr.Close();
            for (int j = 0; j < order_id.Count; j++)
            {
                cmd = new SqlCommand("Select total_amt from Order_history where Order_id='" + order_id[j] + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                float total = (float)dr[0];
                dr.Close();
                if (pz.price != price)
                {
                    float temp = (float)q[j] * price;
                    total = total - temp;
                    temp = (float)q[j] * pz.price;
                    total = total + temp;
                    cmd = new SqlCommand("Update Order_history SET total_amt='" + total + "' where Order_id='" + order_id[j] + "'", con);
                    int k = cmd.ExecuteNonQuery();
                }
            }
            return i;
        }

        public int AddStdPizza(Pizza pz)
        {
            con.Open();
            SqlCommand cmd = new SqlCommand("insert into PizzaId OUTPUT INSERTED.Pizza_id values(@Pizza_type)", con);
            cmd.Parameters.Add(new SqlParameter("Pizza_type", "Standard"));
            int pizza_id = (int)cmd.ExecuteScalar();
            cmd = new SqlCommand("insert into Standard_pizza (Pizza_id,Pizza_name, Price, PizzaImg) values (@pizza_id,@name,@price,@img)", con);
            cmd.Parameters.Add(new SqlParameter("name", pz.name));
            cmd.Parameters.Add(new SqlParameter("price", pz.price));
            cmd.Parameters.Add(new SqlParameter("img", pz.img));
            cmd.Parameters.Add(new SqlParameter("pizza_id", pizza_id));
            int i = cmd.ExecuteNonQuery();
            return i;
        }

        public List<DBoyInfo> GetDeliveryboyList()
        {
            List<DBoyInfo> list = new List<DBoyInfo>();
            con.Open();
            SqlCommand cmd = new SqlCommand("Select UserName, Password from UserDetail where Role='deliveryboy'", con);
            SqlDataReader dr = cmd.ExecuteReader();
            while (dr.Read())
            {
                DBoyInfo di = new DBoyInfo();
                di.uname = dr[0].ToString();
                di.password = dr[1].ToString();
                list.Add(di);
            }
            return list;
        }

        public int GetDeliveryboyId(String uname)
        {
            con.Open();
            SqlCommand cmd = new SqlCommand("Select UserId from UserDetail where Role='deliveryboy' and UserName='" + uname + "'", con);
            SqlDataReader dr = cmd.ExecuteReader();
            dr.Read();
            cmd = new SqlCommand("Select DeliveryBoy_id from Track where UserId='" + (int)dr[0] + "'", con);
            dr.Close();
            dr = cmd.ExecuteReader();
            dr.Read();
            return (int)dr[0];
        }

        public int DeletePizza(int pizza_id)
        {
            con.Open();
            List<int> q = new List<int>();
            List<int> order_id = new List<int>();
            SqlCommand cmd = new SqlCommand("Select Price from Standard_pizza where Pizza_id='" + pizza_id + "'", con);
            SqlDataReader dr = cmd.ExecuteReader();
            dr.Read();
            float price = (float)dr[0];
            dr.Close();
            cmd = new SqlCommand("Select Quantity , Order_id from Order_detail where Pizza_id='" + pizza_id + "'", con);
            dr = cmd.ExecuteReader();
            while (dr.Read())
            {
                q.Add((int)dr[0]);
                order_id.Add((int)dr[1]);
            }
            dr.Close();
            for (int j = 0; j < order_id.Count; j++)
            {
                cmd = new SqlCommand("Select total_amt from Order_history where Order_id='" + order_id[j] + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                float total = (float)dr[0];
                dr.Close();
                float temp = (float)q[j] * price;
                total = total - temp;
                cmd = new SqlCommand("Update Order_history SET total_amt='" + total + "' where Order_id='" + order_id[j] + "'", con);
                int k = cmd.ExecuteNonQuery();
            }
            cmd = new SqlCommand("delete from Order_detail where Pizza_id='" + pizza_id + "'", con);
            int i = cmd.ExecuteNonQuery();
            cmd = new SqlCommand("delete from Rating_Comment where Pizza_id='" + pizza_id + "'", con);
            i = cmd.ExecuteNonQuery();
            cmd = new SqlCommand("delete from Cart where Pizza_id='" + pizza_id + "'", con);
            i = cmd.ExecuteNonQuery();
            cmd = new SqlCommand("delete from Standard_pizza where Pizza_id='" + pizza_id + "'", con);
            i = cmd.ExecuteNonQuery();
            cmd = new SqlCommand("delete from PizzaId where Pizza_id='" + pizza_id + "'", con);
            i = cmd.ExecuteNonQuery();
            return i;
        }

        public int UpdateIngr(Pizza pz)
        {
            int i = 0;
            con.Open();
            if (pz.type.Equals("Base"))
            {
                SqlCommand cmd1 = new SqlCommand("Select Pizza_id from Custom_pizza where Base_id='" + pz.pizza_id + "'", con);
                SqlDataReader dr = cmd1.ExecuteReader();
                List<int> pid = new List<int>();
                List<int> q = new List<int>();
                List<int> b = new List<int>();
                List<int> c = new List<int>();
                List<int> v = new List<int>();
                List<int> t = new List<int>();
                List<int> s = new List<int>();
                List<int> order_id = new List<int>();
                while (dr.Read())
                {
                    pid.Add((int)dr[0]);
                }
                dr.Close();
                int k = 0;
                while (k < pid.Count)
                {
                    cmd1 = new SqlCommand("Select * from Custom_pizza where Pizza_id='" + pid[k] + "'", con);
                    dr = cmd1.ExecuteReader();
                    dr.Read();
                    if (dr[2].ToString().Equals(""))
                        b.Add(0);
                    else
                        b.Add((int)dr[2]);
                    if (dr[3].ToString().Equals(""))
                        c.Add(0);
                    else
                        c.Add((int)dr[3]);
                    if (dr[4].ToString().Equals(""))
                        v.Add(0);
                    else
                        v.Add((int)dr[4]);
                    if (dr[5].ToString().Equals(""))
                        t.Add(0);
                    else
                        t.Add((int)dr[5]);
                    if (dr[6].ToString().Equals(""))
                        s.Add(0);
                    else
                        s.Add((int)dr[6]);
                    dr.Close();
                    float bpr = 0, cpr = 0, vpr = 0, tpr = 0, spr = 0;
                    SqlDataReader dr2;
                    if (b[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Base_price where Base_id=" + b[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        bpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        bpr = 0;

                    if (c[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Cheese_price where Cheese_id=" + c[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        cpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        cpr = 0;

                    if (v[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Veggies_price where Veggies_id=" + v[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        vpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        vpr = 0;

                    if (t[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Toppings_price where Toppings_id=" + t[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        tpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        tpr = 0;
                    if (s[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Sauce_price where Sauce_id=" + s[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        spr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        spr = 0;
                    float price = cpr + bpr + vpr + tpr + spr;
                    float nprice = cpr + pz.price + vpr + tpr + spr;
                    cmd1 = new SqlCommand("Select Quantity , Order_id from Order_detail where Pizza_id='" + pid[i] + "'", con);
                    dr = cmd1.ExecuteReader();
                    while (dr.Read())
                    {
                        q.Add((int)dr[0]);
                        order_id.Add((int)dr[1]);
                    }
                    dr.Close();
                    for (int j = 0; j < order_id.Count; j++)
                    {
                        cmd1 = new SqlCommand("Select total_amt from Order_history where Order_id='" + order_id[j] + "'", con);
                        dr = cmd1.ExecuteReader();
                        dr.Read();
                        float total = (float)dr[0];
                        dr.Close();
                        if (price != nprice)
                        {
                            float temp = (float)q[j] * price;
                            total = total - temp;
                            temp = (float)q[j] * nprice;
                            total = total + temp;
                            cmd1 = new SqlCommand("Update Order_history SET total_amt='" + total + "' where Order_id='" + order_id[j] + "'", con);
                            int o = cmd1.ExecuteNonQuery();
                        }
                    }
                    k++;
                }
                SqlCommand cmd2 = new SqlCommand("update Base_price SET Base_name=@name , Price=@price where Base_id='" + pz.pizza_id + "'", con);
                cmd2.Parameters.Add(new SqlParameter("name", pz.name));
                cmd2.Parameters.Add(new SqlParameter("price", pz.price));
                i = cmd2.ExecuteNonQuery();
            }
            else if (pz.type.Equals("Cheese"))
            {
                SqlCommand cmd1 = new SqlCommand("Select Pizza_id from Custom_pizza where Cheese_id='" + pz.pizza_id + "'", con);
                SqlDataReader dr = cmd1.ExecuteReader();
                List<int> pid = new List<int>();
                List<int> q = new List<int>();
                List<int> b = new List<int>();
                List<int> c = new List<int>();
                List<int> v = new List<int>();
                List<int> t = new List<int>();
                List<int> s = new List<int>();
                List<int> order_id = new List<int>();
                while (dr.Read())
                {
                    pid.Add((int)dr[0]);
                }
                dr.Close();
                int k = 0;
                while (k < pid.Count)
                {
                    cmd1 = new SqlCommand("Select * from Custom_pizza where Pizza_id='" + pid[k] + "'", con);
                    dr = cmd1.ExecuteReader();
                    dr.Read();
                    if (dr[2].ToString().Equals(""))
                        b.Add(0);
                    else
                        b.Add((int)dr[2]);
                    if (dr[3].ToString().Equals(""))
                        c.Add(0);
                    else
                        c.Add((int)dr[3]);
                    if (dr[4].ToString().Equals(""))
                        v.Add(0);
                    else
                        v.Add((int)dr[4]);
                    if (dr[5].ToString().Equals(""))
                        t.Add(0);
                    else
                        t.Add((int)dr[5]);
                    if (dr[6].ToString().Equals(""))
                        s.Add(0);
                    else
                        s.Add((int)dr[6]);
                    dr.Close();
                    float bpr = 0, cpr = 0, vpr = 0, tpr = 0, spr = 0;
                    SqlDataReader dr2;
                    if (b[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Base_price where Base_id=" + b[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        bpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        bpr = 0;

                    if (c[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Cheese_price where Cheese_id=" + c[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        cpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        cpr = 0;

                    if (v[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Veggies_price where Veggies_id=" + v[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        vpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        vpr = 0;

                    if (t[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Toppings_price where Toppings_id=" + t[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        tpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        tpr = 0;
                    if (s[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Sauce_price where Sauce_id=" + s[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        spr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        spr = 0;
                    float price = cpr + bpr + vpr + tpr + spr;
                    float nprice = bpr + pz.price + vpr + tpr + spr;
                    cmd1 = new SqlCommand("Select Quantity , Order_id from Order_detail where Pizza_id='" + pid[i] + "'", con);
                    dr = cmd1.ExecuteReader();
                    while (dr.Read())
                    {
                        q.Add((int)dr[0]);
                        order_id.Add((int)dr[1]);
                    }
                    dr.Close();
                    for (int j = 0; j < order_id.Count; j++)
                    {
                        cmd1 = new SqlCommand("Select total_amt from Order_history where Order_id='" + order_id[j] + "'", con);
                        dr = cmd1.ExecuteReader();
                        dr.Read();
                        float total = (float)dr[0];
                        dr.Close();
                        if (price != nprice)
                        {
                            float temp = (float)q[j] * price;
                            total = total - temp;
                            temp = (float)q[j] * nprice;
                            total = total + temp;
                            cmd1 = new SqlCommand("Update Order_history SET total_amt='" + total + "' where Order_id='" + order_id[j] + "'", con);
                            int o = cmd1.ExecuteNonQuery();
                        }
                    }
                    k++;
                }
                SqlCommand cmd = new SqlCommand("update Cheese_price SET Cheese_name=@name , Price=@price where Cheese_id='" + pz.pizza_id + "'", con);
                cmd.Parameters.Add(new SqlParameter("name", pz.name));
                cmd.Parameters.Add(new SqlParameter("price", pz.price));
                i = cmd.ExecuteNonQuery();
            }
            else if (pz.type.Equals("Veggies"))
            {
                SqlCommand cmd1 = new SqlCommand("Select Pizza_id from Custom_pizza where Veggies_id='" + pz.pizza_id + "'", con);
                SqlDataReader dr = cmd1.ExecuteReader();
                List<int> pid = new List<int>();
                List<int> q = new List<int>();
                List<int> b = new List<int>();
                List<int> c = new List<int>();
                List<int> v = new List<int>();
                List<int> t = new List<int>();
                List<int> s = new List<int>();
                List<int> order_id = new List<int>();
                while (dr.Read())
                {
                    pid.Add((int)dr[0]);
                }
                dr.Close();
                int k = 0;
                while (k < pid.Count)
                {
                    cmd1 = new SqlCommand("Select * from Custom_pizza where Pizza_id='" + pid[k] + "'", con);
                    dr = cmd1.ExecuteReader();
                    dr.Read();
                    if (dr[2].ToString().Equals(""))
                        b.Add(0);
                    else
                        b.Add((int)dr[2]);
                    if (dr[3].ToString().Equals(""))
                        c.Add(0);
                    else
                        c.Add((int)dr[3]);
                    if (dr[4].ToString().Equals(""))
                        v.Add(0);
                    else
                        v.Add((int)dr[4]);
                    if (dr[5].ToString().Equals(""))
                        t.Add(0);
                    else
                        t.Add((int)dr[5]);
                    if (dr[6].ToString().Equals(""))
                        s.Add(0);
                    else
                        s.Add((int)dr[6]);
                    dr.Close();
                    float bpr = 0, cpr = 0, vpr = 0, tpr = 0, spr = 0;
                    SqlDataReader dr2;
                    if (b[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Base_price where Base_id=" + b[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        bpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        bpr = 0;

                    if (c[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Cheese_price where Cheese_id=" + c[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        cpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        cpr = 0;

                    if (v[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Veggies_price where Veggies_id=" + v[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        vpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        vpr = 0;

                    if (t[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Toppings_price where Toppings_id=" + t[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        tpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        tpr = 0;
                    if (s[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Sauce_price where Sauce_id=" + s[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        spr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        spr = 0;
                    float price = cpr + bpr + vpr + tpr + spr;
                    float nprice = cpr + pz.price + bpr + tpr + spr;
                    cmd1 = new SqlCommand("Select Quantity , Order_id from Order_detail where Pizza_id='" + pid[i] + "'", con);
                    dr = cmd1.ExecuteReader();
                    while (dr.Read())
                    {
                        q.Add((int)dr[0]);
                        order_id.Add((int)dr[1]);
                    }
                    dr.Close();
                    for (int j = 0; j < order_id.Count; j++)
                    {
                        cmd1 = new SqlCommand("Select total_amt from Order_history where Order_id='" + order_id[j] + "'", con);
                        dr = cmd1.ExecuteReader();
                        dr.Read();
                        float total = (float)dr[0];
                        dr.Close();
                        if (price != nprice)
                        {
                            float temp = (float)q[j] * price;
                            total = total - temp;
                            temp = (float)q[j] * nprice;
                            total = total + temp;
                            cmd1 = new SqlCommand("Update Order_history SET total_amt='" + total + "' where Order_id='" + order_id[j] + "'", con);
                            int o = cmd1.ExecuteNonQuery();
                        }
                    }
                    k++;
                }
                SqlCommand cmd = new SqlCommand("update Veggies_price SET Veggies=@name , Price=@price where Veggies_id='" + pz.pizza_id + "'", con);
                cmd.Parameters.Add(new SqlParameter("name", pz.name));
                cmd.Parameters.Add(new SqlParameter("price", pz.price));
                i = cmd.ExecuteNonQuery();
            }
            else if (pz.type.Equals("Toppings"))
            {
                SqlCommand cmd1 = new SqlCommand("Select Pizza_id from Custom_pizza where Toppings_id='" + pz.pizza_id + "'", con);
                SqlDataReader dr = cmd1.ExecuteReader();
                List<int> pid = new List<int>();
                List<int> q = new List<int>();
                List<int> b = new List<int>();
                List<int> c = new List<int>();
                List<int> v = new List<int>();
                List<int> t = new List<int>();
                List<int> s = new List<int>();
                List<int> order_id = new List<int>();
                while (dr.Read())
                {
                    pid.Add((int)dr[0]);
                }
                dr.Close();
                int k = 0;
                while (k < pid.Count)
                {
                    cmd1 = new SqlCommand("Select * from Custom_pizza where Pizza_id='" + pid[k] + "'", con);
                    dr = cmd1.ExecuteReader();
                    dr.Read();
                    if (dr[2].ToString().Equals(""))
                        b.Add(0);
                    else
                        b.Add((int)dr[2]);
                    if (dr[3].ToString().Equals(""))
                        c.Add(0);
                    else
                        c.Add((int)dr[3]);
                    if (dr[4].ToString().Equals(""))
                        v.Add(0);
                    else
                        v.Add((int)dr[4]);
                    if (dr[5].ToString().Equals(""))
                        t.Add(0);
                    else
                        t.Add((int)dr[5]);
                    if (dr[6].ToString().Equals(""))
                        s.Add(0);
                    else
                        s.Add((int)dr[6]);
                    dr.Close();
                    float bpr = 0, cpr = 0, vpr = 0, tpr = 0, spr = 0;
                    SqlDataReader dr2;
                    if (b[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Base_price where Base_id=" + b[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        bpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        bpr = 0;

                    if (c[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Cheese_price where Cheese_id=" + c[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        cpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        cpr = 0;

                    if (v[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Veggies_price where Veggies_id=" + v[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        vpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        vpr = 0;

                    if (t[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Toppings_price where Toppings_id=" + t[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        tpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        tpr = 0;
                    if (s[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Sauce_price where Sauce_id=" + s[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        spr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        spr = 0;
                    float price = cpr + bpr + vpr + tpr + spr;
                    float nprice = cpr + pz.price + bpr + vpr + spr;
                    cmd1 = new SqlCommand("Select Quantity , Order_id from Order_detail where Pizza_id='" + pid[i] + "'", con);
                    dr = cmd1.ExecuteReader();
                    while (dr.Read())
                    {
                        q.Add((int)dr[0]);
                        order_id.Add((int)dr[1]);
                    }
                    dr.Close();
                    for (int j = 0; j < order_id.Count; j++)
                    {
                        cmd1 = new SqlCommand("Select total_amt from Order_history where Order_id='" + order_id[j] + "'", con);
                        dr = cmd1.ExecuteReader();
                        dr.Read();
                        float total = (float)dr[0];
                        dr.Close();
                        if (price != nprice)
                        {
                            float temp = (float)q[j] * price;
                            total = total - temp;
                            temp = (float)q[j] * nprice;
                            total = total + temp;
                            cmd1 = new SqlCommand("Update Order_history SET total_amt='" + total + "' where Order_id='" + order_id[j] + "'", con);
                            int o = cmd1.ExecuteNonQuery();
                        }
                    }
                    k++;
                }
                SqlCommand cmd = new SqlCommand("update Toppings_price SET Toppings=@name , Price=@price where Toppings_id='" + pz.pizza_id + "'", con);
                cmd.Parameters.Add(new SqlParameter("name", pz.name));
                cmd.Parameters.Add(new SqlParameter("price", pz.price));
                i = cmd.ExecuteNonQuery();
            }
            else
            {
                SqlCommand cmd1 = new SqlCommand("Select Pizza_id from Custom_pizza where Sauce_id='" + pz.pizza_id + "'", con);
                SqlDataReader dr = cmd1.ExecuteReader();
                List<int> pid = new List<int>();
                List<int> q = new List<int>();
                List<int> b = new List<int>();
                List<int> c = new List<int>();
                List<int> v = new List<int>();
                List<int> t = new List<int>();
                List<int> s = new List<int>();
                List<int> order_id = new List<int>();
                while (dr.Read())
                {
                    pid.Add((int)dr[0]);
                }
                dr.Close();
                int k = 0;
                while (k < pid.Count)
                {
                    cmd1 = new SqlCommand("Select * from Custom_pizza where Pizza_id='" + pid[k] + "'", con);
                    dr = cmd1.ExecuteReader();
                    dr.Read();
                    if (dr[2].ToString().Equals(""))
                        b.Add(0);
                    else
                        b.Add((int)dr[2]);
                    if (dr[3].ToString().Equals(""))
                        c.Add(0);
                    else
                        c.Add((int)dr[3]);
                    if (dr[4].ToString().Equals(""))
                        v.Add(0);
                    else
                        v.Add((int)dr[4]);
                    if (dr[5].ToString().Equals(""))
                        t.Add(0);
                    else
                        t.Add((int)dr[5]);
                    if (dr[6].ToString().Equals(""))
                        s.Add(0);
                    else
                        s.Add((int)dr[6]);
                    dr.Close();
                    float bpr = 0, cpr = 0, vpr = 0, tpr = 0, spr = 0;
                    SqlDataReader dr2;
                    if (b[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Base_price where Base_id=" + b[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        bpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        bpr = 0;

                    if (c[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Cheese_price where Cheese_id=" + c[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        cpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        cpr = 0;

                    if (v[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Veggies_price where Veggies_id=" + v[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        vpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        vpr = 0;

                    if (t[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Toppings_price where Toppings_id=" + t[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        tpr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        tpr = 0;
                    if (s[k] != 0)
                    {
                        cmd1 = new SqlCommand("select Price from Sauce_price where Sauce_id=" + s[k], con);
                        dr2 = cmd1.ExecuteReader();
                        dr2.Read();
                        spr = (float)dr2["Price"];
                        dr2.Close();
                    }
                    else
                        spr = 0;
                    float price = cpr + bpr + vpr + tpr + spr;
                    float nprice = cpr + pz.price + bpr + tpr + vpr;
                    cmd1 = new SqlCommand("Select Quantity , Order_id from Order_detail where Pizza_id='" + pid[i] + "'", con);
                    dr = cmd1.ExecuteReader();
                    while (dr.Read())
                    {
                        q.Add((int)dr[0]);
                        order_id.Add((int)dr[1]);
                    }
                    dr.Close();
                    for (int j = 0; j < order_id.Count; j++)
                    {
                        cmd1 = new SqlCommand("Select total_amt from Order_history where Order_id='" + order_id[j] + "'", con);
                        dr = cmd1.ExecuteReader();
                        dr.Read();
                        float total = (float)dr[0];
                        dr.Close();
                        if (price != nprice)
                        {
                            float temp = (float)q[j] * price;
                            total = total - temp;
                            temp = (float)q[j] * nprice;
                            total = total + temp;
                            cmd1 = new SqlCommand("Update Order_history SET total_amt='" + total + "' where Order_id='" + order_id[j] + "'", con);
                            int o = cmd1.ExecuteNonQuery();
                        }
                    }
                    k++;
                }
                SqlCommand cmd = new SqlCommand("update Sauce_price SET Sauce=@name , Price=@price where Sauce_id='" + pz.pizza_id + "'", con);
                cmd.Parameters.Add(new SqlParameter("name", pz.name));
                cmd.Parameters.Add(new SqlParameter("price", pz.price));
                i = cmd.ExecuteNonQuery();
            }

            return i;
        }

        public int AddIngr(Pizza pz)
        {
            int i = 0;
            con.Open();
            if (pz.type.Equals("Base"))
            {
                SqlCommand cmd = new SqlCommand("select Base_id from Base_price where Base_name='" + pz.name + "'", con);
                SqlDataReader dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    dr.Close();
                    return 2;
                }
                dr.Close();
                cmd = new SqlCommand("insert into Base_price (Base_name,Price,BaseImg) values(@name,@price,@img)", con);
                cmd.Parameters.Add(new SqlParameter("name", pz.name));
                cmd.Parameters.Add(new SqlParameter("price", pz.price));
                cmd.Parameters.Add(new SqlParameter("img", pz.img));
                i = cmd.ExecuteNonQuery();
            }
            else if (pz.type.Equals("Cheese"))
            {
                SqlCommand cmd = new SqlCommand("select Cheese_id from Cheese_price where Cheese_name='" + pz.name + "'", con);
                SqlDataReader dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    dr.Close();
                    return 2;
                }
                dr.Close();
                cmd = new SqlCommand("insert into Cheese_price (Cheese_name,Price,CheeseImg) values(@name,@price,@img)", con);
                cmd.Parameters.Add(new SqlParameter("name", pz.name));
                cmd.Parameters.Add(new SqlParameter("price", pz.price));
                cmd.Parameters.Add(new SqlParameter("img", pz.img));
                i = cmd.ExecuteNonQuery();
            }
            else if (pz.type.Equals("Veggies"))
            {
                SqlCommand cmd = new SqlCommand("select Veggies_id from Veggies_price where Veggies='" + pz.name + "'", con);
                SqlDataReader dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    dr.Close();
                    return 2;
                }
                dr.Close();
                cmd = new SqlCommand("insert into Veggies_price (Veggies,Price,VeggiesImg) values(@name,@price,@img)", con);
                cmd.Parameters.Add(new SqlParameter("name", pz.name));
                cmd.Parameters.Add(new SqlParameter("price", pz.price));
                cmd.Parameters.Add(new SqlParameter("img", pz.img));
                i = cmd.ExecuteNonQuery();
            }
            else if (pz.type.Equals("Toppings"))
            {
                SqlCommand cmd = new SqlCommand("select Toppings_id from Toppings_price where Toppings='" + pz.name + "'", con);
                SqlDataReader dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    dr.Close();
                    return 2;
                }
                dr.Close();
                cmd = new SqlCommand("insert into Toppings_price (Toppings,Price,ToppingsImg) values(@name,@price,@img)", con);
                cmd.Parameters.Add(new SqlParameter("name", pz.name));
                cmd.Parameters.Add(new SqlParameter("price", pz.price));
                cmd.Parameters.Add(new SqlParameter("img", pz.img));
                i = cmd.ExecuteNonQuery();
            }
            else
            {
                SqlCommand cmd = new SqlCommand("select Sauce_id from Sauce_price where Sauce='" + pz.name + "'", con);
                SqlDataReader dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    dr.Close();
                    return 2;
                }
                dr.Close();
                cmd = new SqlCommand("insert into Sauce_price (Sauce,Price,SauceImg) values(@name,@price,@img)", con);
                cmd.Parameters.Add(new SqlParameter("name", pz.name));
                cmd.Parameters.Add(new SqlParameter("price", pz.price));
                cmd.Parameters.Add(new SqlParameter("img", pz.img));
                i = cmd.ExecuteNonQuery();
            }

            return i;
        }

        public int DeleteIngr(int ingr_id, String type)
        {
            List<Pizza> list = new List<Pizza>();
            List<int> b = new List<int>();
            List<int> c = new List<int>();
            List<int> v = new List<int>();
            List<int> t = new List<int>();
            List<int> s = new List<int>();
            List<int> q = new List<int>();
            List<int> order_id = new List<int>();
            int i = 0;
            con.Open();
            SqlCommand cmd, cmd1;
            SqlDataReader dr, dr1;
            if (type.Equals("Base"))
            {
                cmd = new SqlCommand("Select * from Custom_pizza where Base_id='" + ingr_id + "'", con);
            }

            else if (type.Equals("Cheese"))
            {
                cmd = new SqlCommand("Select * from Custom_pizza where Cheese_id='" + ingr_id + "'", con);
            }
            else if (type.Equals("Veggies"))
            {
                cmd = new SqlCommand("Select * from Custom_pizza where Veggies_id='" + ingr_id + "'", con);
            }
            else if (type.Equals("Toppings"))
            {

                cmd = new SqlCommand("Select * from Custom_pizza where Toppings_id='" + ingr_id + "'", con);

            }
            else
            {
                cmd = new SqlCommand("Select *from Custom_pizza where Sauce_id='" + ingr_id + "'", con);
            }
            dr1 = cmd.ExecuteReader();
            while (dr1.Read())
            {
                Pizza pz = new Pizza();
                pz.type = "custom";
                pz.pizza_id = (int)dr1[0];
                pz.name = dr1[1].ToString();
                if (dr1[2].ToString().Equals(""))
                    b.Add(0);
                else
                    b.Add((int)dr1[2]);
                if (dr1[3].ToString().Equals(""))
                    c.Add(0);
                else
                    c.Add((int)dr1[3]);
                if (dr1[4].ToString().Equals(""))
                    v.Add(0);
                else
                    v.Add((int)dr1[4]);
                if (dr1[5].ToString().Equals(""))
                    t.Add(0);
                else
                    t.Add((int)dr1[5]);
                if (dr1[6].ToString().Equals(""))
                    s.Add(0);
                else
                    s.Add((int)dr1[6]);
                list.Add(pz);
            }
            dr1.Close();
            SqlDataReader dr2;
            i = 0;
            float bpr = 0, cpr = 0, vpr = 0, tpr = 0, spr = 0;
            while (i < list.Count)
            {

                if (b[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Base_price where Base_id=" + b[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    bpr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    bpr = 0;

                if (c[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Cheese_price where Cheese_id=" + c[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    cpr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    cpr = 0;

                if (v[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Veggies_price where Veggies_id=" + v[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    vpr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    vpr = 0;

                if (t[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Toppings_price where Toppings_id=" + t[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    tpr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    tpr = 0;
                if (s[i] != 0)
                {
                    cmd1 = new SqlCommand("select Price from Sauce_price where Sauce_id=" + s[i], con);
                    dr2 = cmd1.ExecuteReader();
                    dr2.Read();
                    spr = (float)dr2["Price"];
                    dr2.Close();
                }
                else
                    spr = 0;

                float price = cpr + bpr + vpr + tpr + spr;
                list[i].price = price;
                i++;
            }
            for (int k = 0; k < list.Count; k++)
            {
                cmd = new SqlCommand("Select Quantity , Order_id from Order_detail where Pizza_id='" + list[k].pizza_id + "'", con);
                dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    q.Add((int)dr[0]);
                    order_id.Add((int)dr[1]);
                }
                dr.Close();
                for (int j = 0; j < order_id.Count; j++)
                {
                    cmd = new SqlCommand("Select total_amt from Order_history where Order_id='" + order_id[j] + "'", con);
                    dr = cmd.ExecuteReader();
                    dr.Read();
                    float total = (float)dr[0];
                    dr.Close();
                    float temp = (float)q[j] * list[k].price;
                    total = total - temp;
                    cmd = new SqlCommand("Update Order_history SET total_amt='" + total + "' where Order_id='" + order_id[j] + "'", con);
                    int z = cmd.ExecuteNonQuery();
                }
                q = new List<int>();
                order_id = new List<int>();
                cmd = new SqlCommand("delete from Order_detail where Pizza_id='" + list[k].pizza_id + "'", con);
                i = cmd.ExecuteNonQuery();
                cmd = new SqlCommand("delete from Rating_Comment where Pizza_id='" + list[k].pizza_id + "'", con);
                i = cmd.ExecuteNonQuery();
                cmd = new SqlCommand("delete from Cart where Pizza_id='" + list[k].pizza_id + "'", con);
                i = cmd.ExecuteNonQuery();
                cmd = new SqlCommand("delete from Custom_pizza where Pizza_id='" + list[k].pizza_id + "'", con);
                i = cmd.ExecuteNonQuery();
                cmd = new SqlCommand("delete from PizzaId where Pizza_id='" + list[k].pizza_id + "'", con);
                i = cmd.ExecuteNonQuery();
            }
            if (type.Equals("Base"))
            {
                cmd = new SqlCommand("delete from Base_price where Base_id='" + ingr_id + "'", con);
                i = cmd.ExecuteNonQuery();
            }
            else if (type.Equals("Cheese"))
            {
                cmd = new SqlCommand("delete from Cheese_price where Cheese_id='" + ingr_id + "'", con);
                i = cmd.ExecuteNonQuery();
            }
            else if (type.Equals("Veggies"))
            {
                cmd = new SqlCommand("delete from Veggies_price where Veggies_id='" + ingr_id + "'", con);
                i = cmd.ExecuteNonQuery();
            }
            else if (type.Equals("Toppings"))
            {
                cmd = new SqlCommand("delete from Toppings_price where Toppings_id='" + ingr_id + "'", con);
                i = cmd.ExecuteNonQuery();
            }
            else
            {
                cmd = new SqlCommand("delete from Sauce_price where Sauce_id='" + ingr_id + "'", con);
                i = cmd.ExecuteNonQuery();
            }

            return i;
        }

        public int AddDel(DBoyInfo di)
        {
            con.Open();
            SqlCommand cmd = new SqlCommand("insert into UserDetail OUTPUT INSERTED.UserId (UserName , Password , Role) values (@name, @pwd , @role)", con);
            cmd.Parameters.Add(new SqlParameter("name", di.uname));
            cmd.Parameters.Add(new SqlParameter("pwd", di.password));
            cmd.Parameters.Add(new SqlParameter("role", "deliveryboy"));
            int id = (int)cmd.ExecuteScalar();
            cmd = new SqlCommand("insert into Track (UserId) values (@id)", con);
            cmd.Parameters.Add(new SqlParameter("id", id));
            int i = cmd.ExecuteNonQuery();
            return i;
        }

        public int RemoveDel(DBoyInfo di)
        {
            int i;
            con.Open();
            SqlCommand cmd = new SqlCommand("select UserId from UserDetail where UserName='" + di.uname + "'", con);
            SqlDataReader dr = cmd.ExecuteReader();
            dr.Read();
            int uid = (int)dr[0];
            cmd = new SqlCommand("select DeliveryBoy_id from Track where UserId='" + uid + "'", con);
            dr.Close();
            dr = cmd.ExecuteReader();
            dr.Read();
            int id = (int)dr[0];
            cmd = new SqlCommand("Select Order_id from Order_history where Deliveryboy_id='" + id + "' and Status='Assigned'", con);
            dr.Close();
            dr = cmd.ExecuteReader();
            if (dr.Read())
            {
                dr.Close();
                return 2;
            }
            else
            {
                dr.Close();
                cmd = new SqlCommand("Update Order_history SET Deliveryboy_id=NULL where Deliveryboy_id='" + id + "' and Status='Delivered'", con);
                i = cmd.ExecuteNonQuery();
                cmd = new SqlCommand("delete from Track where DeliveryBoy_id='" + id + "'", con);
                i = cmd.ExecuteNonQuery();
                cmd = new SqlCommand("delete from UserDetail where UserId='" + uid + "'", con);
                i = cmd.ExecuteNonQuery();
            }
            return i;
        }

        public CustomPizza GetIngr(int pizza_id)
        {
            con.Open();
            int b, c, v, t, s;
            CustomPizza cp = new CustomPizza();
            SqlCommand cmd = new SqlCommand("select * from Custom_pizza where Pizza_id='" + pizza_id + "'", con);
            SqlDataReader dr = cmd.ExecuteReader();
            dr.Read();

            if (dr[2].ToString().Equals(""))
            {
                b = 0;
                cp.b_name = "null";
            }
            else
            {
                b = (int)dr[2];
            }

            if (dr[3].ToString().Equals(""))
            {
                c = 0;
                cp.c_name = "null";
            }
            else
            {
                c = (int)dr[3];
            }

            if (dr[4].ToString().Equals(""))
            {
                v = 0;
                cp.v_name = "null";
            }
            else
            {
                v = (int)dr[4];
            }

            if (dr[5].ToString().Equals(""))
            {
                t = 0;
                cp.t_name = "null";
            }
            else
            {
                t = (int)dr[5];
            }

            if (dr[6].ToString().Equals(""))
            {
                s = 0;
                cp.s_name = "null";
            }
            else
            {
                s = (int)dr[6];
            }
            dr.Close();
            if (b != 0)
            {
                cmd = new SqlCommand("select Base_name from Base_price where Base_id='" + b + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                cp.b_name = dr[0].ToString();
                dr.Close();
            }
            if (c != 0)
            {
                cmd = new SqlCommand("select Cheese_name from Cheese_price where Cheese_id='" + c + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                cp.c_name = dr[0].ToString();
                dr.Close();
            }
            if (v != 0)
            {
                cmd = new SqlCommand("select Veggies from Veggies_price where Veggies_id='" + v + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                cp.v_name = dr[0].ToString();
                dr.Close();
            }
            if (t != 0)
            {
                cmd = new SqlCommand("select Toppings from Toppings_price where Toppings_id='" + t + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                cp.t_name = dr[0].ToString();
                dr.Close();
            }
            if (s != 0)
            {
                cmd = new SqlCommand("select Sauce from Sauce_price where Sauce_id='" + s + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                cp.s_name = dr[0].ToString();
                dr.Close();
            }
            return cp;
        }

        public int RegDevice(String uname, String deviceId) 
        {
            con.Open();
            SqlCommand cmd = new SqlCommand("Select UserId from UserDetail where Role='deliveryboy' and UserName='" + uname + "'", con);
            SqlDataReader dr = cmd.ExecuteReader();
            dr.Read();
            int uid=(int)dr[0];
            dr.Close();

            cmd = new SqlCommand("update track set DeviceId='"+ deviceId +"' where UserId='"+ uid +"'", con);
            int i=cmd.ExecuteNonQuery();
            return i;
        }

        public int SentMail(String uname)
        {
            Random r = new Random();
            int i = r.Next(0, 9);
            String s = Convert.ToString(i);

            int i1 = r.Next(0, 9);
            String s1 = Convert.ToString(i1);

            int i2 = r.Next(0, 9);
            String s2 = Convert.ToString(i2);

            int i3 = r.Next(0, 9);
            String s3 = Convert.ToString(i3);

            string pwd = s + s1 + s2 + s3;
            try
            {
                con.Open();
                SqlCommand cmd = new SqlCommand("select UserId from UserDetail where UserName='" + uname + "'", con);
                SqlDataReader dr = cmd.ExecuteReader();
                dr.Read();
                int uid = (int)dr[0];
                dr.Close();
                cmd = new SqlCommand("select EmailId from Cust_detail where UserId='" + uid + "'", con);
                dr = cmd.ExecuteReader();
                dr.Read();
                String emailid = dr[0].ToString();
                string fromAddress = "pizzaeatery.merchant@gmail.com";
                string fromPassword = "pizzamerchant";
                string subject = "New Password";
                string body = "Your new password is:" + pwd;
                dr.Close();
                SmtpClient smtp = new SmtpClient();
                {
                    smtp.Host = "smtp.gmail.com";
                    smtp.Port = 587;
                    smtp.EnableSsl = true;
                    smtp.DeliveryMethod = SmtpDeliveryMethod.Network;
                    smtp.Credentials = new NetworkCredential(fromAddress, fromPassword);
                    smtp.Timeout = 20000;
                }
                smtp.Send(fromAddress, emailid, subject, body);
                cmd = new SqlCommand("update UserDetail SET Password ='" + pwd + "' where UserId='" + uid + "'", con);
                i = cmd.ExecuteNonQuery();
            }

            catch (Exception e)
            {
                return 2;
            }
            return i;
        }

    }
}
