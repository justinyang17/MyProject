using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace CheapestProductApi.Models
{
    public class ProductVieModel
    {

        public partial class products
        {
            
            public string barcode { get; set; }
            public string goods_name { get; set; }
            public byte[] goods_img { get; set; }
            public string goods_description { get; set; }
            public string favorite { get; set; }
            public Nullable<int> goods_type { get; set; }

            //public virtual goodstype goodstype { get; set; }

            //public virtual ICollection<price> price { get; set; }
        }

        public ICollection<price> price { get; set; }
        public string goods_description { get; internal set; }
        public int? goods_type { get; internal set; }
    }
}