package controllers.task;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import play.libs.Akka;
import play.libs.Time;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.actor.Props;

public class BBAnalyzerTaskActorBase {

	private static BBAnalyzerTaskActorBase instance = new BBAnalyzerTaskActorBase();
	private ActorSystem system = ActorSystem.create("bbAnalyzerActor");
	private ActorRef bbAnalyzerTaskActor = Akka.system().actorOf(new Props(BBAnalyzerTaskActor.class));
	private Cancellable cancellable;
	
	private BBAnalyzerTaskActorBase() {
		super();
	}
	
	public static BBAnalyzerTaskActorBase getInstance() {
		return instance;
	}
	
	public void start() throws ParseException {
		Time.CronExpression cron = new Time.CronExpression("1 * * * * ?");
		Date nextValidTimeAfter = cron.getNextValidTimeAfter(new Date());
		FiniteDuration finiteDuraion = Duration.create(
				nextValidTimeAfter.getTime() - System.currentTimeMillis(),
				TimeUnit.MILLISECONDS);
		
		cancellable = system.scheduler().scheduleOnce(
				finiteDuraion,
				bbAnalyzerTaskActor,
				"Call",
				Akka.system().dispatcher(),
				null);
	}
	
	public void shutdown() {
		cancellable.cancel();
	}
}
