package controllers.task;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import models.actor.BBTaskActor;
import play.libs.Akka;
import play.libs.Time;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Props;

public class BBTaskActorBase {
	private static BBTaskActorBase instance = new BBTaskActorBase();
	private ActorSystem system = ActorSystem.create("bbActor");
	private ActorRef bbTaskActor = Akka.system().actorOf(Props.create(BBTaskActor.class));
	private Cancellable cancellable;
	
	public static BBTaskActorBase getInstance() {
		return instance;
	}

	private BBTaskActorBase() {
		
	}
	
	public void start() throws ParseException {
		// 定期実行の日付・時刻を設定する
		Time.CronExpression cron = new Time.CronExpression("0 0 4,11,18 * * ? *");
////		[ Cron Expression ]
////			see http://www.playframework.com/documentation/2.2.x/api/java/play/libs/Time.CronExpression.html
////		Field Name	 	Allowed Values	 	Allowed Special Characters
////		Seconds	 		0-59	 			, - * /
////		Minutes	 		0-59	 			, - * /
////		Hours	 		0-23	 			, - * /
////		Day-of-month	1-31	 			, - * ? / L W
////		Month	 		1-12 or JAN-DEC	 	, - * /
////		Day-of-Week	 	1-7 or SUN-SAT	 	, - * ? / L #
////		Year (Optional)	empty, 1970-2099	, - * /
//		
		Date nextValidTimeAfter = cron.getNextValidTimeAfter(new Date());
		FiniteDuration finiteDuraion = Duration.create(
				nextValidTimeAfter.getTime() - System.currentTimeMillis(),
				TimeUnit.MILLISECONDS);
		
		cancellable = system.scheduler().scheduleOnce(
				finiteDuraion,
				bbTaskActor,
				"Call",
				Akka.system().dispatcher(),
				null);
	}
	
	public void shutdown() {
		if (cancellable != null) {
			cancellable.cancel();
		}
	}
}
