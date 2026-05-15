import zio.*
import zio.http.*
import scala.io.Source

case class AppConfig(
  pages:       Int,
  pageSize:    Int,
  sort:        String,
  order:       String,
  outputPath:  String,
  maxParallel: Int,
  tagsFile:    String,
)

object AppConfig:
  val layer: ULayer[AppConfig] = ZLayer.fromZIO(
    (for
      pages       <- System.envOrElse("SO_PAGES",        "5").map(_.toInt)
      pageSize    <- System.envOrElse("SO_PAGE_SIZE",    "10").map(_.toInt)
      sort        <- System.envOrElse("SO_SORT",         "activity")
      order       <- System.envOrElse("SO_ORDER",        "desc")
      outputPath  <- System.envOrElse("SO_OUTPUT_PATH",  "/data/results")
      maxParallel <- System.envOrElse("SO_MAX_PARALLEL", "3").map(_.toInt)
      tagsFile    <- System.envOrElse("SO_TAGS_FILE",    "tags.txt")
    yield AppConfig(pages, pageSize, sort, order, outputPath, maxParallel, tagsFile)).orDie
  )


object Main extends ZIOAppDefault:

  def run =
    (for
      config <- ZIO.service[AppConfig]
      tags   <- ZIO.attempt(
                  Source.fromFile(config.tagsFile)
                    .getLines()
                    .map(_.trim)
                    .filter(_.nonEmpty)
                    .toList
                ).tapError(e => ZIO.succeed(println(s"Cannot read tags file '${config.tagsFile}': ${e.getMessage}")))
      _      <- ZIO.succeed(println(s"Loaded ${tags.length} tags: ${tags.mkString(", ")}"))
      _      <- ZIO.serviceWithZIO[ParserOrchestrator](_.run(tags))
    yield ())
      .provide(
        AppConfig.layer,
        Client.default,
        StackOverflowClientLive.layer,
        ResultWriterLive.layer,
        ParserOrchestratorLive.layer,
      )
