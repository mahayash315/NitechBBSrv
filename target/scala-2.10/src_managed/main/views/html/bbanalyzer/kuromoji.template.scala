
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
object kuromoji extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template2[play.data.Form[models.request.bbanalyzer.BBAnalyzerRequest],models.response.bbanalyzer.BBAnalyzerResult,play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/(form : play.data.Form[models.request.bbanalyzer.BBAnalyzerRequest], result : models.response.bbanalyzer.BBAnalyzerResult):play.api.templates.HtmlFormat.Appendable = {
        _display_ {import helper.twitterBootstrap._


Seq[Any](format.raw/*1.124*/("""

"""),format.raw/*4.1*/("""
"""),_display_(Seq[Any](/*5.2*/main("Testing Kuromoji - BBAnalyzer")/*5.39*/ {_display_(Seq[Any](format.raw/*5.41*/("""

    """),_display_(Seq[Any](/*7.6*/helper/*7.12*/.form(action = routes.BBAnalyzer.procKuromoji())/*7.60*/ {_display_(Seq[Any](format.raw/*7.62*/("""
    	"""),_display_(Seq[Any](/*8.7*/helper/*8.13*/.textarea(form("body"), '_label -> "本文", '_class -> "largeTextArea"))),format.raw/*8.81*/("""
    	
    	<div id="actions" style="margin-left: 8em;">
    		<input type="submit" value="Submit" class="btn primary"> or
			<a href=""""),_display_(Seq[Any](/*12.14*/routes/*12.20*/.BBAnalyzer.kuromoji())),format.raw/*12.42*/("""" class="btn">Clear</a>
    	</div>
    """)))})),format.raw/*14.6*/("""

	"""),_display_(Seq[Any](/*16.3*/if(result != null)/*16.21*/ {_display_(Seq[Any](format.raw/*16.23*/("""
		<div class="well">
			
			"""),_display_(Seq[Any](/*19.5*/for(i <- 0 until result.getSurfaces().size()) yield /*19.50*/ {_display_(Seq[Any](format.raw/*19.52*/("""
				<a href="javascript:void(0);" class="myshow">"""),_display_(Seq[Any](/*20.51*/(result.getSurfaces().get(i)))),format.raw/*20.80*/("""<span>"""),_display_(Seq[Any](/*20.87*/(result.getFeatures().get(i)))),format.raw/*20.116*/("""</span></a>
			""")))})),format.raw/*21.5*/("""
			
		</div>
	""")))})),format.raw/*24.3*/("""
""")))})),format.raw/*25.2*/("""
"""))}
    }
    
    def render(form:play.data.Form[models.request.bbanalyzer.BBAnalyzerRequest],result:models.response.bbanalyzer.BBAnalyzerResult): play.api.templates.HtmlFormat.Appendable = apply(form,result)
    
    def f:((play.data.Form[models.request.bbanalyzer.BBAnalyzerRequest],models.response.bbanalyzer.BBAnalyzerResult) => play.api.templates.HtmlFormat.Appendable) = (form,result) => apply(form,result)
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Thu Mar 20 08:49:22 JST 2014
                    SOURCE: C:/c0de/workspace/MeikoBBSrv/app/views/bbanalyzer/kuromoji.scala.html
                    HASH: e017e19eeba7e95d6581c3226541af538543dd33
                    MATRIX: 885->1|1135->123|1163->159|1199->161|1244->198|1283->200|1324->207|1338->213|1394->261|1433->263|1474->270|1488->276|1577->344|1749->480|1764->486|1808->508|1880->549|1919->553|1946->571|1986->573|2051->603|2112->648|2152->650|2239->701|2290->730|2333->737|2385->766|2432->782|2479->798|2512->800
                    LINES: 26->1|30->1|32->4|33->5|33->5|33->5|35->7|35->7|35->7|35->7|36->8|36->8|36->8|40->12|40->12|40->12|42->14|44->16|44->16|44->16|47->19|47->19|47->19|48->20|48->20|48->20|48->20|49->21|52->24|53->25
                    -- GENERATED --
                */
            