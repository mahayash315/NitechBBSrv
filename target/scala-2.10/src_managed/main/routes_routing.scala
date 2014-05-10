// @SOURCE:C:/c0de/workspace/MeikoBBSrv/conf/routes
// @HASH:750542e70ebecbe7587ee421ad2d1a7e4ed7ccc4
// @DATE:Thu Mar 20 08:49:21 JST 2014


import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString

object Routes extends Router.Routes {

private var _prefix = "/"

def setPrefix(prefix: String) {
  _prefix = prefix
  List[(String,Routes)]().foreach {
    case (p, router) => router.setPrefix(prefix + (if(prefix.endsWith("/")) "" else "/") + p)
  }
}

def prefix = _prefix

lazy val defaultPrefix = { if(Routes.prefix.endsWith("/")) "" else "/" }


// @LINE:6
private[this] lazy val controllers_Application_index0 = Route("GET", PathPattern(List(StaticPart(Routes.prefix))))
        

// @LINE:9
private[this] lazy val controllers_Default_redirect1 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("bbanalyzer"))))
        

// @LINE:10
private[this] lazy val controllers_BBAnalyzer_index2 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("bbanalyzer/"))))
        

// @LINE:11
private[this] lazy val controllers_BBAnalyzer_kuromoji3 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("bbanalyzer/kuromoji"))))
        

// @LINE:12
private[this] lazy val controllers_BBAnalyzer_procKuromoji4 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("bbanalyzer/kuromoji"))))
        

// @LINE:13
private[this] lazy val controllers_BBAnalyzer_analyzeBody5 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("bbanalyzer/analyzeBody"))))
        

// @LINE:14
private[this] lazy val controllers_api_BBAnalyzer_analyzeBody6 = Route("POST", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("bbanalyzer/analyzeBody.json"))))
        

// @LINE:18
private[this] lazy val controllers_Assets_at7 = Route("GET", PathPattern(List(StaticPart(Routes.prefix),StaticPart(Routes.defaultPrefix),StaticPart("assets/"),DynamicPart("file", """.+""",false))))
        
def documentation = List(("""GET""", prefix,"""controllers.Application.index()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """bbanalyzer""","""controllers.Default.redirect(to:String = "/bbanalyzer/")"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """bbanalyzer/""","""controllers.BBAnalyzer.index()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """bbanalyzer/kuromoji""","""controllers.BBAnalyzer.kuromoji()"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """bbanalyzer/kuromoji""","""controllers.BBAnalyzer.procKuromoji()"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """bbanalyzer/analyzeBody""","""controllers.BBAnalyzer.analyzeBody()"""),("""POST""", prefix + (if(prefix.endsWith("/")) "" else "/") + """bbanalyzer/analyzeBody.json""","""controllers.api.BBAnalyzer.analyzeBody()"""),("""GET""", prefix + (if(prefix.endsWith("/")) "" else "/") + """assets/$file<.+>""","""controllers.Assets.at(path:String = "/public", file:String)""")).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
  case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
  case l => s ++ l.asInstanceOf[List[(String,String,String)]] 
}}
      

def routes:PartialFunction[RequestHeader,Handler] = {

// @LINE:6
case controllers_Application_index0(params) => {
   call { 
        invokeHandler(controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Nil,"GET", """ Home page""", Routes.prefix + """"""))
   }
}
        

// @LINE:9
case controllers_Default_redirect1(params) => {
   call(Param[String]("to", Right("/bbanalyzer/"))) { (to) =>
        invokeHandler(controllers.Default.redirect(to), HandlerDef(this, "controllers.Default", "redirect", Seq(classOf[String]),"GET", """ BBAnalyzer""", Routes.prefix + """bbanalyzer"""))
   }
}
        

// @LINE:10
case controllers_BBAnalyzer_index2(params) => {
   call { 
        invokeHandler(controllers.BBAnalyzer.index(), HandlerDef(this, "controllers.BBAnalyzer", "index", Nil,"GET", """""", Routes.prefix + """bbanalyzer/"""))
   }
}
        

// @LINE:11
case controllers_BBAnalyzer_kuromoji3(params) => {
   call { 
        invokeHandler(controllers.BBAnalyzer.kuromoji(), HandlerDef(this, "controllers.BBAnalyzer", "kuromoji", Nil,"GET", """""", Routes.prefix + """bbanalyzer/kuromoji"""))
   }
}
        

// @LINE:12
case controllers_BBAnalyzer_procKuromoji4(params) => {
   call { 
        invokeHandler(controllers.BBAnalyzer.procKuromoji(), HandlerDef(this, "controllers.BBAnalyzer", "procKuromoji", Nil,"POST", """""", Routes.prefix + """bbanalyzer/kuromoji"""))
   }
}
        

// @LINE:13
case controllers_BBAnalyzer_analyzeBody5(params) => {
   call { 
        invokeHandler(controllers.BBAnalyzer.analyzeBody(), HandlerDef(this, "controllers.BBAnalyzer", "analyzeBody", Nil,"POST", """""", Routes.prefix + """bbanalyzer/analyzeBody"""))
   }
}
        

// @LINE:14
case controllers_api_BBAnalyzer_analyzeBody6(params) => {
   call { 
        invokeHandler(controllers.api.BBAnalyzer.analyzeBody(), HandlerDef(this, "controllers.api.BBAnalyzer", "analyzeBody", Nil,"POST", """""", Routes.prefix + """bbanalyzer/analyzeBody.json"""))
   }
}
        

// @LINE:18
case controllers_Assets_at7(params) => {
   call(Param[String]("path", Right("/public")), params.fromPath[String]("file", None)) { (path, file) =>
        invokeHandler(controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]),"GET", """ Map static resources from the /public folder to the /assets URL path""", Routes.prefix + """assets/$file<.+>"""))
   }
}
        
}

}
     