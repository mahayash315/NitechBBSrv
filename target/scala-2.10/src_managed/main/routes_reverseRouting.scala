// @SOURCE:C:/c0de/workspace/MeikoBBSrv/conf/routes
// @HASH:750542e70ebecbe7587ee421ad2d1a7e4ed7ccc4
// @DATE:Thu Mar 20 08:49:21 JST 2014

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.j._

import play.api.mvc._
import play.libs.F

import Router.queryString


// @LINE:18
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:6
package controllers {

// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
class ReverseBBAnalyzer {
    

// @LINE:12
def procKuromoji(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "bbanalyzer/kuromoji")
}
                                                

// @LINE:13
def analyzeBody(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "bbanalyzer/analyzeBody")
}
                                                

// @LINE:10
def index(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "bbanalyzer/")
}
                                                

// @LINE:11
def kuromoji(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "bbanalyzer/kuromoji")
}
                                                
    
}
                          

// @LINE:18
class ReverseAssets {
    

// @LINE:18
def at(file:String): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                                                
    
}
                          

// @LINE:9
class ReverseDefault {
    

// @LINE:9
def redirect(): Call = {
   Call("GET", _prefix + { _defaultPrefix } + "bbanalyzer")
}
                                                
    
}
                          

// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index(): Call = {
   Call("GET", _prefix)
}
                                                
    
}
                          
}
                  

// @LINE:14
package controllers.api {

// @LINE:14
class ReverseBBAnalyzer {
    

// @LINE:14
def analyzeBody(): Call = {
   Call("POST", _prefix + { _defaultPrefix } + "bbanalyzer/analyzeBody.json")
}
                                                
    
}
                          
}
                  


// @LINE:18
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:6
package controllers.javascript {

// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
class ReverseBBAnalyzer {
    

// @LINE:12
def procKuromoji : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.BBAnalyzer.procKuromoji",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "bbanalyzer/kuromoji"})
      }
   """
)
                        

// @LINE:13
def analyzeBody : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.BBAnalyzer.analyzeBody",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "bbanalyzer/analyzeBody"})
      }
   """
)
                        

// @LINE:10
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.BBAnalyzer.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "bbanalyzer/"})
      }
   """
)
                        

// @LINE:11
def kuromoji : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.BBAnalyzer.kuromoji",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "bbanalyzer/kuromoji"})
      }
   """
)
                        
    
}
              

// @LINE:18
class ReverseAssets {
    

// @LINE:18
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        
    
}
              

// @LINE:9
class ReverseDefault {
    

// @LINE:9
def redirect : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Default.redirect",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "bbanalyzer"})
      }
   """
)
                        
    
}
              

// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        
    
}
              
}
        

// @LINE:14
package controllers.api.javascript {

// @LINE:14
class ReverseBBAnalyzer {
    

// @LINE:14
def analyzeBody : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.api.BBAnalyzer.analyzeBody",
   """
      function() {
      return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "bbanalyzer/analyzeBody.json"})
      }
   """
)
                        
    
}
              
}
        


// @LINE:18
// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
// @LINE:9
// @LINE:6
package controllers.ref {


// @LINE:13
// @LINE:12
// @LINE:11
// @LINE:10
class ReverseBBAnalyzer {
    

// @LINE:12
def procKuromoji(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.BBAnalyzer.procKuromoji(), HandlerDef(this, "controllers.BBAnalyzer", "procKuromoji", Seq(), "POST", """""", _prefix + """bbanalyzer/kuromoji""")
)
                      

// @LINE:13
def analyzeBody(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.BBAnalyzer.analyzeBody(), HandlerDef(this, "controllers.BBAnalyzer", "analyzeBody", Seq(), "POST", """""", _prefix + """bbanalyzer/analyzeBody""")
)
                      

// @LINE:10
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.BBAnalyzer.index(), HandlerDef(this, "controllers.BBAnalyzer", "index", Seq(), "GET", """""", _prefix + """bbanalyzer/""")
)
                      

// @LINE:11
def kuromoji(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.BBAnalyzer.kuromoji(), HandlerDef(this, "controllers.BBAnalyzer", "kuromoji", Seq(), "GET", """""", _prefix + """bbanalyzer/kuromoji""")
)
                      
    
}
                          

// @LINE:18
class ReverseAssets {
    

// @LINE:18
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this, "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      
    
}
                          

// @LINE:9
class ReverseDefault {
    

// @LINE:9
def redirect(to:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Default.redirect(to), HandlerDef(this, "controllers.Default", "redirect", Seq(classOf[String]), "GET", """ BBAnalyzer""", _prefix + """bbanalyzer""")
)
                      
    
}
                          

// @LINE:6
class ReverseApplication {
    

// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this, "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      
    
}
                          
}
        

// @LINE:14
package controllers.api.ref {


// @LINE:14
class ReverseBBAnalyzer {
    

// @LINE:14
def analyzeBody(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.api.BBAnalyzer.analyzeBody(), HandlerDef(this, "controllers.api.BBAnalyzer", "analyzeBody", Seq(), "POST", """""", _prefix + """bbanalyzer/analyzeBody.json""")
)
                      
    
}
                          
}
        
    