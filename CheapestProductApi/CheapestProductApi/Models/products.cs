//------------------------------------------------------------------------------
// <auto-generated>
//     這個程式碼是由範本產生。
//
//     對這個檔案進行手動變更可能導致您的應用程式產生未預期的行為。
//     如果重新產生程式碼，將會覆寫對這個檔案的手動變更。
// </auto-generated>
//------------------------------------------------------------------------------

namespace CheapestProductApi.Models
{
    using Newtonsoft.Json;
    using System;
    using System.Collections.Generic;
    
    public partial class products
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public products()
        {
            this.price = new HashSet<price>();
        }
    
        public string barcode { get; set; }
        public string goods_name { get; set; }
        public byte[] goods_img { get; set; }
        public string goods_description { get; set; }
        public string favorite { get; set; }
        public Nullable<int> goods_type { get; set; }

        [JsonIgnore]
        public virtual goodstype goodstype { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        [JsonIgnore]
        public virtual ICollection<price> price { get; set; }
    }
}
