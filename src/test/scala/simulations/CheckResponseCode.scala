package simulations

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class CheckResponseCode extends Simulation {

  val httpProtocol = http
    .baseUrl("https://computer-database.gatling.io")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())

  val scn = scenario("ComputerDatabase")
    .exec(http("get all computers")
      .get("/computers").check(status.is(200)))
    .exec(http("get to creation page")
      .get("/computers/new").check(status.in(200 to 210)))
    .exec(http("create new computer")
      .post("/computers")
      .check(status.not(404), status.not(500))
      .check(bodyString.saveAs("responseBody"))
      .formParam("name", "TestQuan" + Math.random())
      .formParam("introduced", "1982-12-21")
      .formParam("discontinued", "1990-01-01")
      .formParam("company", "1")
    )


  setUp(scn.inject(atOnceUsers(5), rampUsers(10) during (10))).protocols(httpProtocol)
}