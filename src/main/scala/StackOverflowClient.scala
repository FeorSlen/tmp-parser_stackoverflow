import zio.*
import zio.http.*
import zio.json.*

abstract class SoApiError(msg: String) extends Exception(msg)
case class SoNotFound(tag: String, page: Int)   extends SoApiError(s"404 tag=$tag page=$page")
case class SoForbidden(tag: String, page: Int)  extends SoApiError(s"403 tag=$tag page=$page")

trait StackOverflowClient:
  def search(tag: String, page: Int): Task[SearchResponse]


object StackOverflowClientLive:
  val layer: URLayer[AppConfig & Client, StackOverflowClient] =
    ZLayer.fromFunction(StackOverflowClientLive(_, _))


case class StackOverflowClientLive(config: AppConfig, http: Client) extends StackOverflowClient:

  def search(tag: String, page: Int): Task[SearchResponse] =
    ZIO.scoped {
      for
        url  <- buildUrl(tag, page)
        resp <- http.request(Request.get(url).addHeader("Accept-Encoding", "identity"))
        body <- resp.body.asString
        data <- parseResponse(resp.status, body, tag, page)
      yield data
    }


  private def buildUrl(tag: String, page: Int): Task[URL] =
    val raw =
      "https://api.stackexchange.com/2.3/search" +
      s"?page=$page&pagesize=${config.pageSize}" +
      s"&order=${config.order}&sort=${config.sort}" +
      s"&tagged=$tag&site=stackoverflow&filter=default"
    ZIO.fromEither(URL.decode(raw)).mapError(e => Exception(s"Bad URL: $e"))


  private def parseResponse(status: Status, body: String, tag: String, page: Int): Task[SearchResponse] =
    status match
      case Status.Ok       => ZIO.fromEither(body.fromJson[SearchResponse]).mapError(Exception(_))
      case Status.NotFound => ZIO.fail(SoNotFound(tag, page))
      case Status.Forbidden => ZIO.fail(SoForbidden(tag, page))
      case s               => ZIO.fail(Exception(s"Unexpected HTTP ${s.code}"))
