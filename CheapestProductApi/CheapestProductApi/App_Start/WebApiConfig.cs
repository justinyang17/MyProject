using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.Http;

namespace CheapestProductApi
{
    public static class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            // Web API 設定和服務

            //移除xml
            var appXmlType = config.Formatters.XmlFormatter.SupportedMediaTypes.
                FirstOrDefault(t => t.MediaType == "application/xml");
            config.Formatters.XmlFormatter.SupportedMediaTypes.Remove(appXmlType);

            //Json縮排
            config.Formatters.JsonFormatter.SerializerSettings.Formatting = Newtonsoft.Json.Formatting.Indented;

            // Web API 路由
            config.MapHttpAttributeRoutes();

            
            config.Routes.MapHttpRoute(
                //name: "DefaultApi",
                name: "GetProduct",
                routeTemplate: "api/{controller}/{barcode}",
                defaults: new { barcode = RouteParameter.Optional }
            );

            config.Routes.MapHttpRoute(
                name: "GetPrice",
                routeTemplate: "api/{controller}/detail/barcode",
                defaults: new { barcode = RouteParameter.Optional }
            );

            config.Routes.MapHttpRoute(
                name: "AddPrice",
                routeTemplate: "api/{controller}/detail/add/",
                defaults: new { barcode = RouteParameter.Optional }
            );
        }
    }
}
