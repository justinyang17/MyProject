﻿//------------------------------------------------------------------------------
// <auto-generated>
//     這個程式碼是由範本產生。
//
//     對這個檔案進行手動變更可能導致您的應用程式產生未預期的行為。
//     如果重新產生程式碼，將會覆寫對這個檔案的手動變更。
// </auto-generated>
//------------------------------------------------------------------------------

namespace CheapestProductApi.Models
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Infrastructure;
    
    public partial class CheapProductsEntities : DbContext
    {
        public CheapProductsEntities()
            : base("name=CheapProductsEntities")
        {
        }
    
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            throw new UnintentionalCodeFirstException();
        }
    
        public virtual DbSet<area> area { get; set; }
        public virtual DbSet<goodstype> goodstype { get; set; }
        public virtual DbSet<levels> levels { get; set; }
        public virtual DbSet<price> price { get; set; }
        public virtual DbSet<products> products { get; set; }
        public virtual DbSet<store> store { get; set; }
        public virtual DbSet<users> users { get; set; }
    }
}
