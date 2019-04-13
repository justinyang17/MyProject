using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using System.Web.Http.Description;
using CheapestProductApi.Models;

namespace CheapestProductApi.Controllers
{
    
    public class ProductController : ApiController
    {

        CheapProductsEntities db = new CheapProductsEntities();
        // GET: api/Product
        public IEnumerable<products> GetProduct(String barcode)
        {
            //List<products> result;
            //result = db.products.ToList();
            return db.products.Where(x=>x.barcode.Equals(barcode)).ToList();
        }

        // GET: api/Product/5
        [Route("api/product/detail/{barcode}")]
        public IEnumerable<object> GetPrice(string barcode)
        {
            //product & price join 語法
            //var data = (from p in db.products
            //            join o in db.price on p.barcode equals o.barcode
            //            select new { p.barcode, p.goods_name, o.price1,o.store_id,o.update_date,o.area_id }
            //            ).Where(p=>p.barcode.Equals(barcode));


            //取得各分區最新一筆報價
            var data =(from p in db.price
                       where p.barcode.Equals(barcode)
            group p by new { p.store_id, p.area_id }
            into Group
            select Group.OrderByDescending(x => x.update_date)
            .FirstOrDefault());

            //.Where (p => p.barcode.Equals(barcode))
            //orderby p.update_date descending
            // { p.barcode,p.price1, p.update_date }).Where(p => p.barcode.Equals(barcode)).Take(3);

            return data;
            //return db.price.Where(x => x.barcode.Equals(barcode)).Take(3).ToList();


        }

        // POST: api/Product
        //public IHttpActionResult PostPrice([FromBody]price price)
        [Route("api/product/detail/add/")]
        [HttpPost]
        public price PostPrice([FromBody]price price)

        {
            try
            {
                price = db.price.Add(price);
                db.SaveChanges();
                
            }catch (Exception e)
            {
                Console.Write(e);
            }

            return price;
        }

        // PUT: api/Product/5
        public void Put(int id, [FromBody]string value)
        {


        }

        // DELETE: api/Product/5
        public void Delete(int id)
        {
        }
    }
}
