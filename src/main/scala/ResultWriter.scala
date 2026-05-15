import zio.*
import zio.json.*
import java.nio.file.*


trait ResultWriter:
  def write(tag: String, page: Int, response: SearchResponse): Task[Unit]


object ResultWriterLive:
  val layer: URLayer[AppConfig, ResultWriter] =
    ZLayer.fromFunction(ResultWriterLive(_))


case class ResultWriterLive(config: AppConfig) extends ResultWriter:


  def write(tag: String, page: Int, response: SearchResponse): Task[Unit] =
    ZIO.attempt {
      val dir = Paths.get(config.outputPath, tag)
      Files.createDirectories(dir)
      Files.writeString(dir.resolve(s"page_$page.json"), response.toJson)
    }
