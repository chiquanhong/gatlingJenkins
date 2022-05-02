package simulations

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ComputerDatabase extends Simulation {

	val httpProtocol = http
		.baseUrl("https://computer-database.gatling.io")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())

	val scn = scenario("ComputerDatabase")
		.exec(http("get all computers")
			.get("/computers"))
		.pause(3)
		.exec(http("create new computer")
			.get("/computers/new"))
		.pause(18)
		.exec(http("get all computers again")
			.post("/computers")
			.formParam("name", "TestQuan")
			.formParam("introduced", "1982-12-21")
			.formParam("discontinued", "1990-01-01")
			.formParam("company", "1")
			)

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}