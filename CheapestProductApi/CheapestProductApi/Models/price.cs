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
    
    public partial class price
    {
        public int id { get; set; }
        public string barcode { get; set; }
        public Nullable<double> price1 { get; set; }
        public Nullable<System.DateTime> update_date { get; set; }
        public Nullable<int> store_id { get; set; }
        public Nullable<int> area_id { get; set; }
        public string verify { get; set; }
        public string user_id { get; set; }
        public string remark { get; set; }

        [JsonIgnore]
        public virtual area area { get; set; }
        [JsonIgnore]
        public virtual products products { get; set; }
        [JsonIgnore]
        public virtual store store { get; set; }
    }
}
