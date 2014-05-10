
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
object index extends BaseScalaTemplate[play.api.templates.HtmlFormat.Appendable,Format[play.api.templates.HtmlFormat.Appendable]](play.api.templates.HtmlFormat) with play.api.templates.Template0[play.api.templates.HtmlFormat.Appendable] {

    /**/
    def apply/*1.2*/():play.api.templates.HtmlFormat.Appendable = {
        _display_ {

Seq[Any](format.raw/*1.4*/("""

"""),_display_(Seq[Any](/*3.2*/main("BBAnalyzer")/*3.20*/ {_display_(Seq[Any](format.raw/*3.22*/("""

    <div id="description">
		<h2>HTML での利用</h2>
		<div id="action" style="padding: 20px;">
			<input type="button" class="btn primary" href="javascript:void(0);" onClick="location.href='"""),_display_(Seq[Any](/*8.97*/routes/*8.103*/.BBAnalyzer.kuromoji())),format.raw/*8.125*/("""'"" value="Kuromoji テスト" />
		</div>
	</div>

""")))})),format.raw/*12.2*/("""
"""))}
    }
    
    def render(): play.api.templates.HtmlFormat.Appendable = apply()
    
    def f:(() => play.api.templates.HtmlFormat.Appendable) = () => apply()
    
    def ref: this.type = this

}
                /*
                    -- GENERATED --
                    DATE: Thu Mar 20 08:49:22 JST 2014
                    SOURCE: C:/c0de/workspace/MeikoBBSrv/app/views/bbanalyzer/index.scala.html
                    HASH: f532b8a2e0ce11951f117c2f616e9e96a48b85d3
                    MATRIX: 778->1|873->3|910->6|936->24|975->26|1199->215|1214->221|1258->243|1336->290
                    LINES: 26->1|29->1|31->3|31->3|31->3|36->8|36->8|36->8|40->12
                    -- GENERATED --
                */
            