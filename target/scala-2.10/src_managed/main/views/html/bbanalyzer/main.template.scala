
package views.html.bbanalyzer

import play.templates._
import play.templates.TemplateMagic._

import play.api.templates._
import play.api.templates.PlayMagic._
import models._
import controllers._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.api.i18n._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._
import views.html._
/**/
object main extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template2[String,Html,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(title: String)(content: Html):play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.32*/("""

<!DOCTYPE html>

<html>
    <head>
        <title>"""),_display_(Seq[Any](/*7.17*/title)),format.raw/*7.22*/("""</title>
        <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*8.54*/routes/*8.60*/.Assets.at("stylesheets/bootstrap/bootstrap.min.css"))),format.raw/*8.113*/("""">
        <link rel="stylesheet" media="screen" href=""""),_display_(Seq[Any](/*9.54*/routes/*9.60*/.Assets.at("stylesheets/bbanalyzer/main.css"))),format.raw/*9.105*/("""">
        <script src=""""),_display_(Seq[Any](/*10.23*/routes/*10.29*/.Assets.at("javascripts/jquery-1.9.0.min.js"))),format.raw/*10.74*/("""" type="text/javascript"></script>
    </head>
    <body>
        <header class="topbar">
            <h1 class="fill">
				"""),_display_(Seq[Any](/*15.6*/title)),format.raw/*15.11*/("""
            </h1>
        </header>
    
        <section id="main">
            """),_display_(Seq[Any](/*20.14*/content)),format.raw/*20.21*/("""
        </section>
    </body>
</html>
"""))}
    }
    
    def render(title:String,content:Html): play.api.templates.HtmlFormat.Appendable = apply(title)(content)
    
    def f:((String) => (Html) => play.api.templates.HtmlFormat.Appendable) = (title) => (content) => apply(title)(content)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Thu Mar 20 08:49:22 JST 2014
                    SOURCE: C:/c0de/workspace/MeikoBBSrv/app/views/bbanalyzer/main.scala.html
                    HASH: 5b11fcc2f4ee88d09ebd3d5a98f54655706180a9
                    MATRIX: 789->1|913->31|1001->84|1027->89|1124->151|1138->157|1213->210|1304->266|1318->272|1385->317|1446->342|1461->348|1528->393|1688->518|1715->523|1834->606|1863->613
                    LINES: 26->1|29->1|35->7|35->7|36->8|36->8|36->8|37->9|37->9|37->9|38->10|38->10|38->10|43->15|43->15|48->20|48->20
                    -- GENERATED --
                */
            